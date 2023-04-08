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
import org.vaadin.example.components.CustomPicUpload;
import org.vaadin.example.components.CustomPicUploadWithCaptionAndScaling;
import org.vaadin.example.entity.InhaltEntity;
import org.vaadin.example.entity.KapitelEntity;
import org.vaadin.example.components.CustomGrid;


import java.util.*;
import java.util.stream.Collectors;

public class EditorBar extends HorizontalLayout {

    private ComboBox<String> cb;
    ArrayList<Component> components;

    private void updateComponentView() {
        this.getStyle().set("background-color", "#f5f5f5");
        removeAll();
        VerticalLayout tempLayout = new VerticalLayout();
        tempLayout.add(getToolbar());
        for (Component c : components) {
            tempLayout.add(c);
        }
        add(tempLayout);
    }

    private void moveComponentUp(Component c) {
        int index = components.indexOf(c);
        if (index == 0) {
            Notification.show("Komponente kann nicht nach oben verschoben werden!");
            return;
        }
        Collections.swap(components, index, index - 1);
        updateComponentView();
    }

    private void moveComponentDown(Component c) {
        int index = components.indexOf(c);
        if (index == components.size() - 1) {
            Notification.show("Komponente kann nicht nach unten verschoben werden!");
            return;
        }
        Collections.swap(components, index, index + 1);
        updateComponentView();
    }

    private void removeComponent(Component c) {
        components.remove(c);
        updateComponentView();
    }

    public void resetComponents() {
        this.components = new ArrayList<>();
        updateComponentView();
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
            HorizontalLayout tempLayout = new HorizontalLayout();
            tempLayout.setWidth("100%");

            VerticalLayout buttonLayout = getComponentToolbar(tempLayout);

            if (cb.getValue() == null) {
                Notification.show("Bitte wählen Sie eine Komponente aus!");
                return;
            }

            switch (cb.getValue()){
                case "Textfeld":
                    TextArea textArea = new TextArea();
                    textArea.setWidth("160%");
                    tempLayout.add(textArea);
                    tempLayout.add(buttonLayout);

                    Notification.show("Textfeld hinzugefügt!");
                    break;
                case "Abbildung":
                    //tempLayout.add(new CustomPicUpload());TODO wurde durch neue abgeleitete Klasse verwendet
                    tempLayout.add(new CustomPicUploadWithCaptionAndScaling());
                    tempLayout.add(buttonLayout);
                    Notification.show("Abbildung hinzugefügt!");

                    break;
                case "Tabelle":
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

                    //wait for dialog to close


                    //CustomGrid grid = new CustomGrid(columnNames);
                    //CustomGrid grid = new CustomGrid();
                    //tempLayout.add(grid);
                    //tempLayout.add(buttonLayout);
                    break;
            }
            this.components.add(tempLayout);
            updateComponentView();
        });

        toolbar.add(addBtn, cb);
        Button speichernBtn = new Button("Speichern");

        speichernBtn.addClickListener(click -> {
            Notification.show("Speichern");
            Set<InhaltEntity> inhalte = new HashSet<>();
            KapitelEntity kapitel = new KapitelEntity();

            int anordnung = 0;
            for(Component c : components) {//Layout
                anordnung++;
                List<Component> children = c.getChildren().collect(Collectors.toList());
                for(Component c2 : children) {//HorizontalLayout
                    if(c2 instanceof TextArea) {
                        InhaltEntity inhalt = new InhaltEntity();
                        inhalt.setAnordnungIndex(anordnung);
                        //inhalt.setKapitel();
                        inhalt.setTextInhalt(((TextArea) c2).getValue());
                        inhalte.add(inhalt);
                    }else if(c2 instanceof CustomPicUpload) {
                        InhaltEntity inhalt = new InhaltEntity();
                        inhalt.setAnordnungIndex(anordnung);
                        //inhalt.setKapitel();
                        inhalt.setBildInhalt(((CustomPicUpload) c2).getBytes());
                        inhalte.add(inhalt);
                    //}else if(c2 instanceof CustomGrid) {FIXME
                        //TODO
                    }

                }
            }
            kapitel.setInhalte(inhalte);
            //Speichern
        });

        toolbar.add(speichernBtn);
        return toolbar;
    }

    public EditorBar() {
        components = new ArrayList<>();
        updateComponentView();
    }

}
