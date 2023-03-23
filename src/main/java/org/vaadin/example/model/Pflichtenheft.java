package org.vaadin.example.model;

import java.sql.Array;
import java.util.ArrayList;

public class Pflichtenheft {

    private String titel;
    private String beschreibung;
    private String releaseDatum;
    private ArrayList<Mitarbeiter> mitarbeiterList;
    private Mitarbeiter verantwortlicher;

    public int getMitarbeiterListSize(){
        return this.mitarbeiterList.size();
    }
    public String getTitel(){
        return this.titel;
    }
    public String getBeschreibung(){
        return this.beschreibung;
    }
    public String getReleaseDatum(){
        return this.releaseDatum;
    }

    public Mitarbeiter getVerantwortlicher(){
        return this.verantwortlicher;
    }

    public Pflichtenheft(String titel, String beschreibung, String releaseDatum, Mitarbeiter verantwortlicher){
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.releaseDatum = releaseDatum;
        this.verantwortlicher = verantwortlicher;
        this.mitarbeiterList = new ArrayList<>();
    }

    public void addMitarbeiter(Mitarbeiter mitarbeiter){
        if (mitarbeiter == null){
            throw new IllegalArgumentException("Mitarbeiter muss gültige Instanz sein!");
        }
        this.mitarbeiterList.add(mitarbeiter);
    }


}
