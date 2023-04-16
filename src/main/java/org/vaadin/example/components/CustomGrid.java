package org.vaadin.example.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.Collections;

public class CustomGrid extends VerticalLayout {
    private Grid<GridRow> grid = new Grid<>();
    private ArrayList<String> columnNames = new ArrayList<>();
    Button neueZeileBtn = new Button(VaadinIcon.PLUS_CIRCLE.create());
    ArrayList<GridRow> data = new ArrayList<>();
    private Button print = new Button("Print");
    private TextField caption;

    public CustomGrid(ArrayList<String> columnNames) {
        this.columnNames = columnNames;
        caption = new TextField("Tabellenbezeichnung");
        for(int i = 0; i < columnNames.size(); i++) {
            int finalI = i;
            grid.addComponentColumn(item -> {
                if (item != data.get(data.size() - 1)) {
                    TextField textField = new TextField();
                    textField.setValue(String.valueOf(item.getContent().get(finalI)));
                    textField.addValueChangeListener(e -> {
                        item.getContent().set(finalI, e.getValue());
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
        });

        add(grid);
        add(caption);
        //add(print);
        //add(neueZeileBtn);
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

