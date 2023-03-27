package org.vaadin.example.views.test;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.service.SpecificationsService;

import java.util.List;

public class ContactForm extends FormLayout {
    TextField username = new TextField("Benutzername");
    TextField password = new TextField("Passwort");
    TextField firstName = new TextField("Vorname");
    TextField lastName = new TextField("Nachname");
    EmailField eMail = new EmailField("Mail");
    TextField department = new TextField("Abteilung");


    MitarbeiterEntity currentBenutzer;
    Button saveButton = new Button("Save");
    Button deleteButton = new Button("Delete");
    Button cancelButton = new Button("Cancel");
    private final SpecificationsService service;

    public ContactForm(SpecificationsService service, List<?> companies, List<?> statuses){
        this.service = service;
        addClassName("contact-form");

        add(firstName, lastName, username, password, eMail, department, createButtonsLayout());
        saveButton.addClickListener(click->{
            Notification.show("ROFL");

            currentBenutzer.setBenutzername(username.getValue());
            currentBenutzer.setMail(eMail.getValue());
            currentBenutzer.setPasswort(password.getValue());
            currentBenutzer.setVorname(firstName.getValue());
            currentBenutzer.setNachname(lastName.getValue());
            //currentBenutzer.(department.getValue());

            service.saveBenutzer(currentBenutzer);

            Notification.show("Benutzer angelegt!");
        });
    }

    private Component createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);
        return new HorizontalLayout(saveButton, deleteButton, cancelButton);
    }

    public void updateForm(MitarbeiterEntity benutzer){
        this.currentBenutzer = benutzer;
    }

}
