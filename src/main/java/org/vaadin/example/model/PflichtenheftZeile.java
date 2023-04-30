package org.vaadin.example.model;

import org.vaadin.example.entity.PflichtenheftEntity;

/**
 * Klasse stellt eine Zeile der Tabelle dar
 */
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
