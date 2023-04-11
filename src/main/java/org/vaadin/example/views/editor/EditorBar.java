package org.vaadin.example.views.editor;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.vaadin.example.components.CustomGrid;
import org.vaadin.example.components.CustomPicUploadWithCaptionAndScaling;
import org.vaadin.example.entity.InhaltEntity;
import org.vaadin.example.entity.KapitelEntity;
import org.vaadin.example.entity.PflichtenheftEntity;
import org.vaadin.example.model.ComponentModel;
import org.vaadin.example.service.SpecificationsService;

import java.util.*;
import java.util.stream.Collectors;

public class EditorBar extends HorizontalLayout {

    private final SpecificationsService service;
    private ComboBox<String> cb;
    private ArrayList<ComponentModel> components;

    private PflichtenheftEntity pflichtenheftEntity;

    private int currentChapter;

    public EditorBar(SpecificationsService service, PflichtenheftEntity pflichtenheft) {
        this.service = service;
        components = new ArrayList<>();
        this.pflichtenheftEntity = pflichtenheft;
        updateComponentView();
    }

    private void updateComponentView() {
        this.getStyle().set("background-color", "#f5f5f5");
        removeAll();
        VerticalLayout tempLayout = new VerticalLayout();
        tempLayout.add(getToolbar());
        for (ComponentModel c : components) {
            tempLayout.add(c.getComponent());
        }
        add(tempLayout);
    }

    private void moveComponentUp(Component c) {
        ComponentModel swapModel = components.stream().filter(
                cmp -> cmp.getComponent().equals(c)).findFirst().get();

        int index = components.indexOf(swapModel);
        //int index = components.indexOf(c);
        if (index == 0) {
            Notification.show("Komponente kann nicht nach oben verschoben werden!");
            return;
        }
        Collections.swap(components, index, index - 1);
        updateComponentView();
    }

    private void moveComponentDown(Component c) {
        ComponentModel swapModel = components.stream().filter(
                cmp -> cmp.getComponent().equals(c)).findFirst().get();

        int index = components.indexOf(swapModel);
        if (index == components.size() - 1) {
            Notification.show("Komponente kann nicht nach unten verschoben werden!");
            return;
        }
        Collections.swap(components, index, index + 1);
        updateComponentView();
    }

    private void removeComponent(Component c) {
        ComponentModel swapModel = components.stream().filter(
                cmp -> cmp.getComponent().equals(c)).findFirst().get();

        int index = components.indexOf(swapModel);
        components.remove(swapModel);
        updateComponentView();
    }

    public void resetComponents() {
        this.components = new ArrayList<>();
        updateComponentView();
    }

    /**
     * Die Methode soll zu einem bestimmten Kaptitel, die Inhaltskomponenten laden und
     * diese im Editor anzeigen.
     */
    private void loadChapterComponents() {
        for (KapitelEntity kapitelEntity : pflichtenheftEntity.getKapitel()) {
            if (kapitelEntity.getKapitelVordefiniertOid() == currentChapter){
                Set<InhaltEntity> inhalte = kapitelEntity.getInhalte();

                for (InhaltEntity inhaltEntity: inhalte.stream().sorted(Comparator.comparing(InhaltEntity::getAnordnungIndex)).collect(Collectors.toList())) {
                    if (inhaltEntity.getBildInhalt() == null){
                        components.add( new ComponentModel( inhaltEntity.getInhaltOid(), addComponent("Textfeld", inhaltEntity.getTextInhalt(), null )));
                    }else{
                        components.add( new ComponentModel(inhaltEntity.getInhaltOid(), addComponent("Abbildung", inhaltEntity.getTextInhalt(), inhaltEntity.getBildInhalt() )));
                    }
                }
                updateComponentView();
                return;
            }
        }
    }

    /**
     * Die öffentliche Methode, soll aus der PflichtenheftEditor Klasse bedient werden, und bei Umselektierung
     * eines Kapitels, diese hier in der laufenden Instanz ändern.
     */
    public void setChapter(int kapitel){
        this.currentChapter = kapitel;
        this.resetComponents();
        this.loadChapterComponents();
    }

    /**
     * Die Methode ermöglicht es, bei der Erstellung einer Komponente (Textfeld, Abbildung, ...), dass zugehörige Buttons
     * zur Verschiebung der Komponente nach oben oder unten zu erstellen, oder zur Löschung erstellt werden und neben der
     * Komponente angezeigt werden
     * @return Toolbar zu einer bestimmten Komponente
     */
    private VerticalLayout getComponentToolbar(Component tempLayout){
        VerticalLayout buttonLayout = new VerticalLayout();
        Button upButton = new Button(new Icon(VaadinIcon.ARROW_UP));
        upButton.addClickListener(click1 -> {
            moveComponentUp(tempLayout);
        });
        Button downButton = new Button(new Icon(VaadinIcon.ARROW_DOWN));
        downButton.addClickListener(click1 -> {
            moveComponentDown(tempLayout);
        });
        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(click1 -> {
            removeComponent(tempLayout);
        });
        buttonLayout.add(upButton, downButton, deleteButton);
        return buttonLayout;
    }

    /**
     * Die Methode erstellt eine Komponente (Text, Abbildung, Tabelle) mit der dazugehörigen Toolbar.
     * @return Component
     */
    private HorizontalLayout addComponent(String component, String textInhalt, byte [] bytes){
        HorizontalLayout tempLayout = new HorizontalLayout();
        tempLayout.setWidth("100%");

        VerticalLayout buttonLayout = getComponentToolbar(tempLayout);

        if (component == null) {
            Notification.show("Bitte wählen Sie eine Komponente aus!");
            throw new IllegalArgumentException("Bitte wählen Sie eine Komponente aus!");
        }

        switch (component){
            case "Textfeld":
                TextArea textArea = new TextArea();
                textArea.setValue(textInhalt);
                textArea.setWidth("160%");
                tempLayout.add(textArea);
                tempLayout.add(buttonLayout);

                break;
            case "Abbildung":
                //tempLayout.add(new CustomPicUpload());TODO wurde durch neue abgeleitete Klasse verwendet
                CustomPicUploadWithCaptionAndScaling pic = new CustomPicUploadWithCaptionAndScaling();
                if (bytes != null) {
                    pic.setBytes(bytes);
                    pic.setCaptionText(textInhalt);
                }
                //tempLayout.add(new CustomPicUploadWithCaptionAndScaling());
                tempLayout.add( pic );
                tempLayout.add(buttonLayout);
                break;
            case "Tabelle":
                Notification.show("Tabelle hinzugefügt!");
                //TODO AUSLAGERN
                Notification.show("Tabelle hinzugefügt!");

                //Zeige Dialog mit
                ArrayList<String> columnNames = new ArrayList<>();
                Dialog dialog = new Dialog();
                dialog.add(new TabelleErstellenView(spaltenNamenList -> {
                    columnNames.addAll(spaltenNamenList);
                    System.out.println("Spaltennamen: " + spaltenNamenList);
                    UI.getCurrent().getPage().executeJs("document.querySelector('vaadin-dialog-overlay').close()");

                    CustomGrid grid = new CustomGrid(columnNames);
                    tempLayout.add(grid);
                    tempLayout.add(buttonLayout);
                }));
                dialog.setHeight("90%");
                dialog.setWidth("80%");
                dialog.open();
                break;
        }
        return tempLayout;
    }

    /**
     * Ermöglicht es eine Komponente hinzuzufügen und stellt die Toolbar dar, um das Erstellen der Komponente
     * zu ermöglichen
     * @return Toolbar bestehend aus einem Hinzufügen-Button, ComboBox und einem Speichern-Button
     */
    private Component getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addClassName("toolbar");
        Button addBtn = new Button("Hinzufügen");
        this.cb = new ComboBox<>();
        cb.setItems("Textfeld", "Abbildung", "Tabelle");
        cb.setValue("Textfeld");

        addBtn.addClickListener(click -> {
            HorizontalLayout tempLayout = this.addComponent(cb.getValue(), "", null);


            this.components.add( new ComponentModel(null, tempLayout) );
            updateComponentView();
        });

        toolbar.add(addBtn, cb);
        Button speichernBtn = new Button("Speichern");

        speichernBtn.addClickListener(click -> {
            int anordnung = 0;
            KapitelEntity kapitel = null;

            if (pflichtenheftEntity.getKapitel().stream().filter(k -> k.getKapitelVordefiniertOid() == currentChapter).findFirst().isPresent()){
                kapitel = pflichtenheftEntity.getKapitel().stream().filter(k -> k.getKapitelVordefiniertOid() == currentChapter).
                        findFirst().
                        get();
            }else{
                kapitel = new KapitelEntity();
                kapitel.setKapitelVordefiniertOid(currentChapter);
                kapitel.setProjekt(pflichtenheftEntity);
                pflichtenheftEntity.getKapitel().add(kapitel);
                service.saveKapitel(kapitel);
            }

            Set<InhaltEntity> deleteList = new HashSet<>(kapitel.getInhalte());

            for (ComponentModel component : components) {
                InhaltEntity inhalt = null;
                if (component.getComponentOid() != null) {
                    // ÄNDERUNG
                    inhalt = kapitel.getInhalte().stream().filter(i -> i.getInhaltOid() == component.getComponentOid()).findFirst().get();
                    deleteList.remove(inhalt);
                } else {
                    // NEU ERSTELLUNG
                    inhalt = new InhaltEntity();
                    kapitel.getInhalte().add(inhalt);
                }

                inhalt.setKapitelOid(kapitel.getKapitelOid());

                if (component.getComponent().getChildren().findFirst().get() instanceof TextArea){
                    inhalt.setTextInhalt(((TextArea) component.getComponent().getChildren().collect(Collectors.toList()).get(0)).getValue());
                }else if(component.getComponent().getChildren().findFirst().get() instanceof CustomPicUploadWithCaptionAndScaling){
                    if (inhalt.getBildInhalt() == null)
                        inhalt.setBildInhalt(((CustomPicUploadWithCaptionAndScaling) component.getComponent().getChildren().collect(Collectors.toList()).get(0)).getBytes());
                    inhalt.setTextInhalt(((CustomPicUploadWithCaptionAndScaling) component.getComponent().getChildren().collect(Collectors.toList()).get(0)).getCaptionText());
                }
                inhalt.setAnordnungIndex(anordnung);
                service.saveInhalt(inhalt);
                ++anordnung;
            }
            for(InhaltEntity inhalt : deleteList) {
                kapitel.getInhalte().remove(inhalt);
                service.deleteInhalt(inhalt);
            }

        });

        toolbar.add(speichernBtn);
        return toolbar;
    }

}
