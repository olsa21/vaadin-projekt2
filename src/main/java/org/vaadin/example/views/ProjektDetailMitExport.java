package org.vaadin.example.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.components.ExportComponent;
import org.vaadin.example.components.ProjectForm;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.entity.PflichtenheftEntity;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;

import javax.annotation.security.PermitAll;
import java.time.LocalDate;

@PermitAll
@Route("ProjektDetailMitExport")
public class ProjektDetailMitExport extends VerticalLayout {
    private final SpecificationsService service;
    private ProjectForm projectForm = new ProjectForm();
    private PflichtenheftEntity pflichtenheftEntity;

    private ExportComponent export = new ExportComponent();

    public ProjektDetailMitExport(SpecificationsService service, int projektOid) {
        this.service = service;

        //Laden und setzen der Werte
        pflichtenheftEntity = service.readPflichtenheft(projektOid);
        projectForm.titel.setValue(pflichtenheftEntity.getTitel());
        projectForm.beschreibung.setValue(pflichtenheftEntity.getBeschreibung());
        projectForm.frist.setValue(LocalDate.parse(pflichtenheftEntity.getFrist()));
        projectForm.repo.setValue(pflichtenheftEntity.getRepositoryLink());

        projectForm.titel.setAutofocus(true);
        //add(NavigationBar.getInstance());
        add("Projekt bearbeiten");
        add(
                projectForm,
                export
        );

        projectForm.speichern.addClickListener(event -> {
            if (!projectForm.titel.getValue().isBlank() && !projectForm.beschreibung.getValue().isBlank()) {
                pflichtenheftEntity.setTitel(projectForm.titel.getValue());
                pflichtenheftEntity.setBeschreibung(projectForm.beschreibung.getValue());
                pflichtenheftEntity.setFrist(projectForm.frist.getValue().toString());
                pflichtenheftEntity.setRepositoryLink(projectForm.repo.getValue());
                service.addPflichtenheft(pflichtenheftEntity);
            } else {
                if (projectForm.titel.getValue().isBlank())
                    projectForm.titel.setInvalid(true);
                if (projectForm.beschreibung.getValue().isBlank())
                    projectForm.beschreibung.setInvalid(true);
            }
            Notification.show("Projekt bearbeitet");
        });
        export.exportButton.addClickListener(event -> {
            Notification.show("Exportiere als " + export.exportCombo.getValue());
        });

    }
}
