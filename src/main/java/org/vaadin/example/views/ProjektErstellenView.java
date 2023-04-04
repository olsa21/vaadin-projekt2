package org.vaadin.example.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;

import org.vaadin.example.components.ProjectForm;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "/ProjektErstellen", layout = MainLayout.class)
public class ProjektErstellenView extends VerticalLayout {
    private final SpecificationsService service;
    private ProjectForm projectForm = new ProjectForm();
    public ProjektErstellenView(SpecificationsService service) {
        this.service = service;

        MitarbeiterEntity mitarbeiter = service.findSpecificUser(SecurityService.getLoggedInUsername());

        projectForm.titel.setAutofocus(true);
        //add(NavigationBar.getInstance());
        add("Projekt erstellen");
        add(projectForm);


        projectForm.speichern.addClickListener(event -> {
            if(!projectForm.titel.getValue().isBlank() && !projectForm.beschreibung.getValue().isBlank())
                service.addPflichtenheft(mitarbeiter.getMitarbeiterOid(), projectForm.titel.getValue(), projectForm.beschreibung.getValue(), projectForm.frist.getValue(), projectForm.repo.getValue(), 0);
            else{
                if (projectForm.titel.getValue().isBlank())
                    projectForm.titel.setInvalid(true);
                if(projectForm.beschreibung.getValue().isBlank())
                    projectForm.beschreibung.setInvalid(true);
            }
            projectForm.titel.clear();
            projectForm.beschreibung.clear();
            projectForm.frist.clear();
            projectForm.repo.clear();
            Notification.show("Projekt angelegt");
        });
    }
}
