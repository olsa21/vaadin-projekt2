package org.vaadin.example.components;

import java.util.ArrayList;

public class GridRow {
    private ArrayList<String> content = new ArrayList<>();

    public GridRow(ArrayList<String> content) {
        this.content = content;
    }

    public ArrayList<String> getContent() {
        return content;
    }
}
