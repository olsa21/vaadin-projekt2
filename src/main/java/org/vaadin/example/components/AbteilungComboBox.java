package org.vaadin.example.components;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.vaadin.example.entity.AbteilungEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Eine benutzerdefinierte MultiSelectComboBox, die eine Liste von Abteilungen darstellt und es dem Benutzer ermöglicht,
 * mehrere Abteilungen auszuwählen.
 */
public class AbteilungComboBox extends HorizontalLayout {
    private MultiSelectComboBox comboBox = new MultiSelectComboBox();
    private List<AbteilungEntity> abteilungen;
    private List<AbteilungEntity> selektierteAbteilungen;

    /**
     * Konstruktor; initialisiert die MultiSelectComboBox mit den gegebenen Werten.
     * @param abteilungen Liste von Abteilungen
     * @param selektierteAbteilungen Liste von bereits selektierten Abteilungen
     */
    public AbteilungComboBox(List<AbteilungEntity> abteilungen, List<AbteilungEntity> selektierteAbteilungen) {
        this.abteilungen = abteilungen;
        this.selektierteAbteilungen = selektierteAbteilungen;

        comboBox.setItems(abteilungen.stream().map(AbteilungEntity::getName));

        comboBox.setValue(selektierteAbteilungen.stream().map(AbteilungEntity::getName).collect(Collectors.toList()));

        add(comboBox);
    }

    /**
     * Gibt eine Liste von ausgewählten Abteilungen zurück
     * @return Liste von ausgewählten Abteilungen
     */
    public List<AbteilungEntity> ausgewaehlteAbteilungen() {
        return abteilungen.stream()
                .filter(abteilung -> comboBox.getValue().contains(abteilung.getName()))
                .collect(Collectors.toList());
    }
}
