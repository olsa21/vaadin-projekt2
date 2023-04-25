package org.vaadin.example.components;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.ArrayList;
import java.util.Collections;

public class CustomGrid extends VerticalLayout {
    private Grid<GridRow> grid = new Grid<>();
    private ArrayList<String> columnNames = new ArrayList<>();
    Button neueZeileBtn = new Button(VaadinIcon.PLUS_CIRCLE.create());
    ArrayList<GridRow> data = new ArrayList<>();
    private Button print = new Button("Print");
    private TextField caption;

    public CustomGrid(ArrayList<String> columnNames, ComponentEventListener<SucceededEvent> listener) {
        this.columnNames = columnNames;
        caption = new TextField("Tabellenbezeichnung");
        for(int i = 0; i < columnNames.size(); i++) {
            int finalI = i;
            grid.addComponentColumn(item -> {
                if (item != data.get(data.size() - 1)) {
                    TextArea textField = new TextArea();
                    textField.setValue(String.valueOf(item.getContent().get(finalI)));
                    textField.setValueChangeMode(ValueChangeMode.LAZY);
                    textField.addValueChangeListener(e -> {
                        item.getContent().set(finalI, e.getValue());

                        listener.onComponentEvent(null);
                    });
                    return textField;
                }else {
                    return null;
                }
            }).setHeader(columnNames.get(i));
        }

        grid.addComponentColumn(item -> {
            if (item == data.get(data.size() - 1)) {
                return neueZeileBtn;
            }else{
                Button button = new Button(new Icon(VaadinIcon.TRASH));
                button.addClickListener(e -> {
                    System.out.println("Entferne Eintrag mir Index " + data.indexOf(item));
                    data.remove(item);
                    grid.getDataProvider().refreshAll();
                    listener.onComponentEvent(null);
                });
                return button;
            }
        }).setHeader("Optionen");

        //Leere Zeile einfügen
        data.add(new GridRow(new ArrayList<>(Collections.nCopies(columnNames.size(), ""))));
        grid.setItems(data);

        /*print.addClickListener(e -> {
            for(GridRow row : data) {
                System.out.println(row.getContent());
            }
        });*/

        neueZeileBtn.addClickListener(e -> {
            data.add(new GridRow(new ArrayList<>(Collections.nCopies(columnNames.size(), ""))));
            grid.setItems(data);
            listener.onComponentEvent(null);
        });

        /*caption.setValueChangeMode(ValueChangeMode.LAZY);
        caption.addValueChangeListener(event -> {
            if (event.getValue() != null && !event.getValue().isEmpty()){
                listener.onComponentEvent(null);
            }
        });*/

        add(grid);
        add(caption);
        //add(print);
        //add(neueZeileBtn);

    }

    public void addCaptionChangeListener(ComponentEventListener<SucceededEvent> listener) {
        caption.setValueChangeMode(ValueChangeMode.LAZY);
        caption.addValueChangeListener(evt -> {
            listener.onComponentEvent(null);
        });
    }

    //Rückgabe: Liste von Listen; siehe Print
    public ArrayList<GridRow> getData() {
        return data;
    }

    //Getter for caption
    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    public String getCaptionText() {
        return caption.getValue();
    }

    public void setCaptionText(String text) {
        caption.setValue(text);
    }

    public void setData(ArrayList<GridRow> data) {
        this.data = data;
        grid.setItems(data);
    }
}

