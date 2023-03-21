package org.vaadin.example.entity;

public class Abteilung {
    private int id;
    private String name;

    public Abteilung(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
