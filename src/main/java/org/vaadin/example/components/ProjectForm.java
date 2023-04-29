package org.vaadin.example.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Die Klasse ist dafür zuständig, dass ein Formular für die Erstellung eines Projektes erstellt wird.
 */
public class ProjectForm extends FormLayout {

    public TextField titel = new TextField();
    public TextArea beschreibung = new TextArea();
    public DatePicker frist = new DatePicker();
    public TextField repo = new TextField();
    public Button speichern = new Button("Speichern");

    /**
     * Standard-Konstruktor, welcher die Komponenten initialisiert und hinzufügt.
     */
    public ProjectForm() {
        setResponsiveSteps(
            new FormLayout.ResponsiveStep("0",1)
        );
        addFormItem(titel, "Titel");
        addFormItem(beschreibung, "Beschreibung");
        addFormItem(frist, "Frist");
        addFormItem(repo, "Repository");
        addFormItem(speichern, "");
    }
}
