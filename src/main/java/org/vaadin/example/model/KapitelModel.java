package org.vaadin.example.model;

import org.vaadin.example.entity.KapitelEntity;

/**
 * Klasse um ein KapitelEntity einem Level zuzuordnen
 */
public class KapitelModel {
    private KapitelEntity kapitelEntity;
    private int level;

    /**
     * Konstruktor - setzen der Werte
     * @param kapitelEntity
     * @param level
     */
    public KapitelModel(KapitelEntity kapitelEntity, int level) {
        this.kapitelEntity = kapitelEntity;
        this.level = level;
    }

    public KapitelEntity getKapitelEntity() {
        return kapitelEntity;
    }

    public int getLevel() {
        return level;
    }

    public void setKapitelEntity(KapitelEntity kapitelEntity) {
        this.kapitelEntity = kapitelEntity;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
