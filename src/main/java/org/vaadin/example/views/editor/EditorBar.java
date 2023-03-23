package org.vaadin.example.views.editor;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import java.util.ArrayList;
import java.util.Collections;

public class EditorBar extends HorizontalLayout {

    private ComboBox<String> cb;
    ArrayList<Component> components;

    private void updateComponentView(){

        this.getStyle().set("background-color", "#f5f5f5");
        removeAll();
        VerticalLayout tempLayout = new VerticalLayout();
        tempLayout.add(getToolbar());
        for(Component c : components){
            tempLayout.add(c);
        }
        add(tempLayout);
    }

    private void moveComponentUp(Component c){
        int index = components.indexOf(c);
        if (index == 0){
            Notification.show("Komponente kann nicht nach oben verschoben werden!");
            return;
        }
        Collections.swap(components, index, index-1);
        updateComponentView();
    }

    private void moveComponentDown(Component c){
        int index = components.indexOf(c);
        if (index == components.size()-1){
            Notification.show("Komponente kann nicht nach unten verschoben werden!");
            return;
        }
        Collections.swap(components, index, index+1);
        updateComponentView();
    }

    public void resetComponents(){
        this.components = new ArrayList<>();
        updateComponentView();
    }

    private Component getToolbar(){
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addClassName("toolbar");
        Button addBtn = new Button("Hinzufügen");
        this.cb = new ComboBox<>();
        cb.setItems("Textfeld", "Abbildung", "Tabelle");
        cb.setValue("Textfeld");
        addBtn.addClickListener(click->{
            HorizontalLayout tempLayout = new HorizontalLayout();
            tempLayout.setWidth("100%");
            VerticalLayout buttonLayout = new VerticalLayout();
            Button upButton = new Button( new Icon(VaadinIcon.ARROW_UP));
            upButton.addClickListener(click1 -> {
                moveComponentUp(tempLayout);
            });
            Button downButton = new Button( new Icon(VaadinIcon.ARROW_DOWN));
            downButton.addClickListener(click1 -> {
                moveComponentDown(tempLayout);
            });
            buttonLayout.add(upButton, downButton);
            if (cb.getValue() == null){
                Notification.show("Bitte wählen Sie eine Komponente aus!");
                return;
            }else if (cb.getValue().equals("Textfeld")){
                Notification.show("Textfeld hinzugefügt!");
                TextArea textArea = new TextArea();


                textArea.setWidth("160%");
                tempLayout.add(textArea);

                tempLayout.add(buttonLayout);
                this.components.add(tempLayout);
            }else if (cb.getValue().equals("Abbildung")){
                Notification.show("Abbildung hinzugefügt!");
                tempLayout.add(new Icon(VaadinIcon.ABACUS));
                tempLayout.add(buttonLayout);
                this.components.add(tempLayout);
            }else if (cb.getValue().equals("Tabelle")){
                Notification.show("Tabelle hinzugefügt!");
                //this.components.add(new VerticalLayout(new MyTableView()));
            }
            updateComponentView();
        });
        toolbar.add(addBtn, cb);
        return toolbar;
    }

    EditorBar(){
        components = new ArrayList<>();
        updateComponentView();
    }

}
