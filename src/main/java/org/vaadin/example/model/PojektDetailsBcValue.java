package org.vaadin.example.model;

public class PojektDetailsBcValue {
    private int projektOID;
    private String username;

    public PojektDetailsBcValue(int projektOID, String username) {
        this.projektOID = projektOID;
        this.username = username;
    }

    public int getProjektOID() {
        return projektOID;
    }

    public String getUsername() {
        return username;
    }
}
