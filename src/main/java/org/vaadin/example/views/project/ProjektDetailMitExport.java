package org.vaadin.example.views.project;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import org.vaadin.example.components.ExportComponent;
import org.vaadin.example.components.ProjectForm;
import org.vaadin.example.entity.PflichtenheftEntity;
import org.vaadin.example.listener.ProjektDetailsBroadcaster;
import org.vaadin.example.model.PojektDetailsBcValue;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;

import javax.annotation.security.PermitAll;
import java.time.LocalDate;

/**
 * View zum Bearbeiten von Projektdaten mit Exportfunktion
 */
@PermitAll
@Route("ProjektDetailMitExport")
public class ProjektDetailMitExport extends VerticalLayout {
    private final SpecificationsService service;
    private ProjectForm projectForm = new ProjectForm();
    private PflichtenheftEntity pflichtenheftEntity;

    private ExportComponent export = new ExportComponent();

    private Registration prjDetailsRegistration;

    private String currentUser;


    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UI ui = attachEvent.getUI();
        System.out.println("ProjektDetailMitExport onAttach");
        prjDetailsRegistration = ProjektDetailsBroadcaster.register(newMessage -> {
            int projektOid = newMessage.getProjektOID();
            String username = newMessage.getUsername();

            if(pflichtenheftEntity.getProjektOid() == projektOid && !username.equals(currentUser)){
                //Aktualisierung der Daten
                pflichtenheftEntity = service.readPflichtenheft(projektOid);
                ui.access(() -> {setData();});
            }
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        System.out.println("ProjektDetailMitExport onDetach");
        prjDetailsRegistration.remove();
    }

    public ProjektDetailMitExport(SpecificationsService service, int projektOid) {
        this.service = service;
        this.currentUser = SecurityService.getLoggedInUsername();

        //Laden und setzen der Werte
        pflichtenheftEntity = service.readPflichtenheft(projektOid);
        setData();
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
                service.updatePflichtenheft(pflichtenheftEntity);
                ProjektDetailsBroadcaster.broadcast(new PojektDetailsBcValue(pflichtenheftEntity.getProjektOid(), SecurityService.getLoggedInUsername()));

            } else {
                if (projectForm.titel.getValue().isBlank())
                    projectForm.titel.setInvalid(true);
                if (projectForm.beschreibung.getValue().isBlank())
                    projectForm.beschreibung.setInvalid(true);
            }
            Notification.show("Projekt bearbeitet");
        });
        export.exportButton.addClickListener(event -> {
            if (export.exportCombo.getValue().equals("PDF")) {
                UI.getCurrent().getPage().open("/pdf/" + projektOid, "_blank");
            } else {
                UI.getCurrent().getPage().open("/docx/" + projektOid, "_blank");
            }
        });

    }

    private void setData(){
        projectForm.titel.setValue(pflichtenheftEntity.getTitel());
        projectForm.beschreibung.setValue(pflichtenheftEntity.getBeschreibung());
        projectForm.frist.setValue(LocalDate.parse(pflichtenheftEntity.getFrist()));
        projectForm.repo.setValue(pflichtenheftEntity.getRepositoryLink());

        projectForm.titel.setAutofocus(true);
    }

    public void reload() {
        pflichtenheftEntity = service.readPflichtenheft(pflichtenheftEntity.getProjektOid());
        setData();
    }
}
