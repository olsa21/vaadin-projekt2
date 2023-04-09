package org.vaadin.example.model;

import com.vaadin.flow.component.Component;

public class ComponentModel {
    private Integer componentOid;

    private Component component;

    public ComponentModel(){ }

    public ComponentModel(Integer componentOid, Component component){
        // componentOid darf explizit null sein, um anzuzeigen, dass es sich nicht um eine bestehende Komponente handelt
        if (component == null){
            throw new IllegalArgumentException("Component must not be null");
        }
        this.componentOid = componentOid;
        this.component = component;
    }

    public Integer getComponentOid() {
        return componentOid;
    }

    public void setComponentOid(Integer componentOid) {
        this.componentOid = componentOid;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
