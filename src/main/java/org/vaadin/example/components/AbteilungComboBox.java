package org.vaadin.example.components;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.example.entity.AbteilungEntity;

import java.util.ArrayList;
import java.util.List;

public class AbteilungComboBox extends VerticalLayout {
    private MultiSelectComboBox comboBox = new MultiSelectComboBox();

    private List<AbteilungEntity> abteilungen = new ArrayList<>();
    private List<AbteilungEntity> selektierteAbteilungen = new ArrayList<>();

    public AbteilungComboBox(List<AbteilungEntity> abteilungen, List<AbteilungEntity> selektierteAbteilungen) {
        this.abteilungen = abteilungen;
        this.selektierteAbteilungen = selektierteAbteilungen;

        comboBox.setItems(abteilungen.stream().map(AbteilungEntity::getName));

        List<String> selected = new ArrayList<>();

        for (AbteilungEntity abteilung : selektierteAbteilungen) {
            selected.add(abteilung.getName());
        }

        comboBox.setValue(selected);

        add(comboBox);
    }

    public List<AbteilungEntity> ausgewaehlteAbteilungen() {
        List<AbteilungEntity> ausgewaehlteAbteilungen = new ArrayList<>();

        //Quadratische Laufzeit ...
        comboBox.getValue().forEach(abteilung -> {
            for (AbteilungEntity abteilungEntity : abteilungen) {
                if (abteilungEntity.getName().equals(abteilung)) {
                    ausgewaehlteAbteilungen.add(abteilungEntity);
                }
            }
        });

        return ausgewaehlteAbteilungen;
    }
}
