package org.vaadin.example.model;

public class Mitarbeiter {
    private String vorname;
    private String nachname;
    private String abteilung;

    public Mitarbeiter(String vorname, String nachname, String abteilung){
        this.vorname = vorname;
        this.nachname = nachname;
        this.abteilung = abteilung;
    }

    public String getFullName(){
        return (vorname+" "+nachname);
    }
}
