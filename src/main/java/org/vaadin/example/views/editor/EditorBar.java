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
import org.vaadin.example.components.GridRow;
import org.vaadin.example.entity.*;
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
                    if (inhaltEntity.getTabelle() != null){
                        Notification.show("Tabelle GEFUNDEN!!");
                        components.add( new ComponentModel( inhaltEntity.getInhaltOid(), addComponent("Tabelle", inhaltEntity.getTextInhalt(), null, inhaltEntity.getTabelle())) );
                    }else if (inhaltEntity.getBildInhalt() == null){
                        components.add( new ComponentModel( inhaltEntity.getInhaltOid(), addComponent("Textfeld", inhaltEntity.getTextInhalt(), null,null )));
                    }else{
                        components.add( new ComponentModel(inhaltEntity.getInhaltOid(), addComponent("Abbildung", inhaltEntity.getTextInhalt(), inhaltEntity.getBildInhalt(),null )));
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
    private HorizontalLayout addComponent(String component, String textInhalt, byte [] bytes, TabellenEntity tabelle){
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
                CustomPicUploadWithCaptionAndScaling pic = new CustomPicUploadWithCaptionAndScaling();
                if (bytes != null) {
                    pic.setBytes(bytes);
                    pic.setCaptionText(textInhalt);
                }
                tempLayout.add( pic );
                tempLayout.add(buttonLayout);
                break;
            case "Tabelle":
                ArrayList<String> columnNames = new ArrayList<>();

                if (tabelle != null){
                    // Tabelle wird aus der DB geladen und angezeigt
                    ArrayList<String> captions = new ArrayList<>();
                    captions.addAll(Arrays.asList(tabelle.getSpaltenCaption1(), tabelle.getSpaltenCaption2(),
                            tabelle.getSpaltenCaption3(), tabelle.getSpaltenCaption4(), tabelle.getSpaltenCaption5()));
                    for (String caption: captions) {
                        if (caption != null)
                            columnNames.add(caption);
                    }

                    CustomGrid grid = new CustomGrid(columnNames);
                    ArrayList <GridRow> rows = new ArrayList<>();

                    tabelle.getZellen().stream().sorted(Comparator.comparing(TabellenzeileEntity::getAnordnungsIndex)).collect(Collectors.toList()).forEach(zelle -> {
                        ArrayList<String> row = new ArrayList<>();
                        row.addAll(Arrays.asList(zelle.getZellenWert1(), zelle.getZellenWert2(), zelle.getZellenWert3(), zelle.getZellenWert4(), zelle.getZellenWert5()));
                        rows.add(new GridRow(row));
                        //grid.addItem(zelle);
                    });
                    grid.setData(rows);

                    tempLayout.add(grid);
                    tempLayout.add(buttonLayout);
                }else{
                    // leere Tabelle wird erzeugt
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
                }
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
            HorizontalLayout tempLayout = this.addComponent(cb.getValue(), "", null, null);


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
                System.err.println(kapitel);
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
                }else if(component.getComponent().getChildren().findFirst().get() instanceof CustomGrid){
        System.err.println("Tabelle: " + inhalt.getTabelle());
                    if (inhalt.getTabelle() == null) {
                        TabellenEntity newTable = new TabellenEntity();
                        service.saveTable(newTable);
                        inhalt.setTabelle(newTable);
                    }

    System.err.println("Tabelle: " + inhalt.getTabelle());
                    ArrayList<String> captions = ((CustomGrid) component.getComponent().getChildren().collect(Collectors.toList()).get(0)).getColumnNames();
                    inhalt.getTabelle().setSpaltenCaption1(captions.size() >=1? captions.get(0):null);
                    inhalt.getTabelle().setSpaltenCaption2(captions.size() >=2? captions.get(1):null);
                    inhalt.getTabelle().setSpaltenCaption3(captions.size() >=3? captions.get(2):null);
                    inhalt.getTabelle().setSpaltenCaption4(captions.size() >=4? captions.get(3):null);
                    inhalt.getTabelle().setSpaltenCaption5(captions.size() >=5? captions.get(4):null);
    System.err.println("1");
                    service.saveTable(inhalt.getTabelle());

    System.err.println("2");


                    //create like deletelist
                    Set<TabellenzeileEntity> deleteList2 = new HashSet<>(inhalt.getTabelle().getZellen());

                    int index = 0;
                    for (GridRow row : ((CustomGrid) component.getComponent().getChildren().collect(Collectors.toList()).get(0)).getData()) {
                        int finalIndex2 = index;
                        deleteList2.removeIf(z -> z.getAnordnungsIndex() == finalIndex2);
                        TabellenzeileEntity tabellenzeile = null;
                        int finalIndex = index;
                        if (inhalt.getTabelle().getZellen().stream().filter(z -> z.getAnordnungsIndex() == finalIndex).findFirst().isPresent()){
                            int finalIndex1 = index;
                            tabellenzeile = inhalt.getTabelle().getZellen().stream().filter(z -> z.getAnordnungsIndex() == finalIndex1).findFirst().get();
                        }else{
                            tabellenzeile = new TabellenzeileEntity();
                            tabellenzeile.setTabellenOid(inhalt.getTabelle().getTabellenOid());
                            inhalt.getTabelle().getZellen().add(tabellenzeile);
                        }

                        tabellenzeile.setAnordnungsIndex(index);

                        ArrayList<String> rows = row.getContent();
                        tabellenzeile.setZellenWert1(rows.size() >=1? rows.get(0):"");
                        tabellenzeile.setZellenWert2(rows.size() >=2? rows.get(1):"");
                        tabellenzeile.setZellenWert3(rows.size() >=3? rows.get(2):"");
                        tabellenzeile.setZellenWert4(rows.size() >=4? rows.get(3):"");
                        tabellenzeile.setZellenWert5(rows.size() >=5? rows.get(4):"");

                        service.saveTabellenzeile(tabellenzeile);
                        service.saveTable(inhalt.getTabelle());

/*
                        service.saveTabellenzeile(tabellenzeile);
                        inhalt.getTabelle().getZellen().add(tabellenzeile);
                        //tabellenzeile.setZellenWert1(row.getValues().get(0));
                        //tabellenzeile.setZellenWert2(row.getValues().get(1));
                        //tabellenzeile.setZellenWert3(row.getValues().get(2));
                        //tabellenzeile.setZellenWert4(row.getValues().get(3));
                        //tabellenzeile.setZellenWert5(row.getValues().get(4));
                        ++index;
                        //service.saveTabellenzeile(tabellenzeile);

                        service.saveTable(inhalt.getTabelle());*/
                        index++;
                    }

                    for ( TabellenzeileEntity tabellenzeileEntity: deleteList2) {

                        inhalt.getTabelle().getZellen().remove(tabellenzeileEntity);
                        service.deleteTabellenzeile(tabellenzeileEntity);


                    }



                }
                inhalt.setAnordnungIndex(anordnung);
                service.saveInhalt(inhalt);
                ++anordnung;
            }
            for(InhaltEntity inhalt : deleteList) {
                kapitel.getInhalte().remove(inhalt);
                if (inhalt.getTabelle() != null)
                    service.deleteTable(inhalt.getTabelle());
                service.deleteInhalt(inhalt);
                //service.saveKapitel(kapitel);
            }

        });

        toolbar.add(speichernBtn);
        return toolbar;
    }

}
