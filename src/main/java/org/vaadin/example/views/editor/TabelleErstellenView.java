package org.vaadin.example.views.editor;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;

import javax.management.Notification;
import java.util.ArrayList;
import java.util.function.Consumer;

public class TabelleErstellenView extends FormLayout {
    private Consumer<ArrayList<String>> onSave;
    private ComboBox<Integer> spalten = new ComboBox<>();
    private ArrayList<TextField> spaltenNamen = new ArrayList<>();
    private Button speichern = new Button("Tabelle erstellen");

    public TabelleErstellenView(Consumer<ArrayList<String>> onSave) {
        this.onSave = onSave;
        spalten.setItems(2, 3, 4, 5);
        spalten.setValue(2);

        setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );

        addFormItem(spalten, "Anzahl der Spalten");
        for (int i = 0; i < 5; i++) {
            TextField textField = new TextField();
            spaltenNamen.add(textField);
            addFormItem(textField, "Spaltenname " + (i + 1));
        }
        add(speichern);

        enableTextfelder(2);

        spalten.addValueChangeListener(e -> {
            enableTextfelder(e.getValue());
        });
        speichern.addClickListener(e -> {
            //prüfen ob alle aktiven Textfelder ausgefüllt sind


            ArrayList<String> spaltenNamenList = new ArrayList<>();
            for (TextField tf : spaltenNamen) {
                if(tf.isEnabled() && tf.getValue().isBlank()) {
                    tf.setInvalid(true);
                    return;
                }
                if(!tf.getValue().isBlank()){
                    spaltenNamenList.add(tf.getValue());
                }
            }
            onSave.accept(spaltenNamenList);
        });
    }

    private void enableTextfelder(Integer neueAnzahl) {
        for (int i = 0; i < 5; i++) {
            if (i < neueAnzahl) {
                spaltenNamen.get(i).setEnabled(true);
            } else {
                spaltenNamen.get(i).clear();
                spaltenNamen.get(i).setEnabled(false);
            }
        }
    }
}
