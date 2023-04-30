package org.vaadin.example.model;

public class EditorBcValue {
    private int projektOID;
    private String username;
    private int currentchapter;
    private Integer componentOID;

    public EditorBcValue(int projektOID, String username, int currentchapter, Integer componentOID) {
        this.projektOID = projektOID;
        this.username = username;
        this.currentchapter = currentchapter;
        this.componentOID = componentOID;
    }

    public int getProjektOID() {
        return projektOID;
    }

    public String getUsername() {
        return username;
    }

    public int getCurrentchapter() {
        return currentchapter;
    }

    public Integer getComponentOID() {
        return componentOID;
    }
}
