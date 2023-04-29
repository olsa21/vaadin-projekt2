package org.vaadin.example.model;

import com.vaadin.flow.component.Component;

/**
 * Die Klasse ist dafür zuständig, dass eine Komponente mit einer OID verknüpft werden kann.
 */
public class ComponentModel {
    private Integer componentOid;
    private Component component;

    /**
     * Konstruktor, welche die Komponente und die OID setzt.
     * @param componentOid die OID der Komponente
     * @param component die Komponente
     */
    public ComponentModel(Integer componentOid, Component component){
        // componentOid darf explizit null sein, um anzuzeigen, dass es sich nicht um eine bestehende Komponente handelt
        if (component == null){
            throw new IllegalArgumentException("Die Komponente muss eine gültige Referenz besitzen!");
        }
        this.componentOid = componentOid;
        this.component = component;
    }

    /**
     * Getter-Methode, welche die OID der Komponente zurückgibt.
     * @return die OID der Komponente
     */
    public Integer getComponentOid() {
        return componentOid;
    }

    /**
     * Setter-Methode, welche die OID der Komponente setzt.
     * @param componentOid die OID der Komponente
     */
    public void setComponentOid(Integer componentOid) {
        this.componentOid = componentOid;
    }

    /**
     * Getter-Methode, welche die Komponente zurückgibt.
     * @return die Komponente
     */
    public Component getComponent() {
        return component;
    }

    /**
     * Setter-Methode, welche die Komponente setzt.
     * @param component die Komponente
     */
    public void setComponent(Component component) {
        this.component = component;
    }
}
