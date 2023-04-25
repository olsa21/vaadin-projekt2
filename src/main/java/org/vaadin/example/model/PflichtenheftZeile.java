package org.vaadin.example.model;

import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.entity.PflichtenheftEntity;

public class PflichtenheftZeile {
    private PflichtenheftEntity pflichtenheftEntity;

    public PflichtenheftZeile(PflichtenheftEntity pflichtenheftEntity) {
        this.pflichtenheftEntity = pflichtenheftEntity;
    }

    public PflichtenheftEntity getPflichtenheftEntity() {
        return pflichtenheftEntity;
    }

    public int anzahlMitarbeiter(){
        return pflichtenheftEntity.getMitarbeiter().size();
    }
}
