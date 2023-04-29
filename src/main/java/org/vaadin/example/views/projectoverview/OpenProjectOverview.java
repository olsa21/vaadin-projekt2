package org.vaadin.example.views.projectoverview;


import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import org.vaadin.example.views.MainLayout;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.entity.PflichtenheftEntity;
import org.vaadin.example.listener.PflichtenheftBroadcaster;
import org.vaadin.example.model.PflichtenheftZeile;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;
import org.vaadin.example.views.project.ProjektDetailView;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse ist dafür zuständig, dass eine passende View für die Projektübersicht angezeigt wird.
 */
@PermitAll
@PageTitle("Projektübersicht")
@Route(value = "/open-project-overview", layout = MainLayout.class)
public class OpenProjectOverview extends VerticalLayout {

    Grid<PflichtenheftZeile> grid = new Grid<>();
    TextField filterText = new TextField();
    private final SpecificationsService service;
    List<PflichtenheftZeile> pflichtenheftZeilen = new ArrayList<>();
    Registration broadcasterRegistration;

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        System.out.println("detach layout");
        broadcasterRegistration.remove();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UI ui = attachEvent.getUI();
        broadcasterRegistration = PflichtenheftBroadcaster.register(newMessage -> {
            System.out.println("New message received: " + newMessage);
            getData();
            ui.access(() -> updateList());
        });
    }

    /**
     * Konstruktor der Klasse, welche die benötigten Services und Komponenten initialisiert.
     * @param service
     */
    public OpenProjectOverview(SpecificationsService service) {
        this.service = service;
        getData();
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        add(getToolbar(), getContent());
        updateList();
    }

    /**
     * Methode, welche die anzuzeigenden Daten (Pflichtenhefte) aus der Datenbank holt.
     */
    private void getData() {
        pflichtenheftZeilen.clear();
        List<PflichtenheftEntity> pflichtenheftEntities2 = service.findOpenProjects();
        pflichtenheftEntities2.forEach(pflichtenheftEntity -> {
            pflichtenheftZeilen.add(new PflichtenheftZeile(pflichtenheftEntity));
        });
    }

    /**
     * Methode, welche die Liste der Pflichtenhefte aktualisiert.
     */
    private void updateList() {
        if (filterText.isEmpty()) {
            grid.setItems(this.pflichtenheftZeilen);
        } else {
            grid.setItems(this.pflichtenheftZeilen.stream().filter(spec -> spec.getPflichtenheftEntity().getTitel().toLowerCase().startsWith(filterText.getValue().toLowerCase())));
        }
    }

    /**
     * Methode, welche die Komponenten für die View zusammenstellt.
     */
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(2, grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    /**
     * Methode, welche die Komponenten für die Toolbar zusammenstellt.
     * @return Toolbar
     */
    private Component getToolbar() {
        filterText.setPlaceholder("Filter by Titel");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        HorizontalLayout toolbar = new HorizontalLayout(filterText);
        return toolbar;
    }

    /**
     * Methode, welche die Grid-Komponente konfiguriert.
     */
    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.addColumn(spec -> spec.getPflichtenheftEntity().getTitel()).setHeader("Titel");
        grid.addColumn(spec -> spec.anzahlMitarbeiter()).setHeader("Mitarbeiteranzahl");
        grid.addColumn(spec -> spec.getPflichtenheftEntity().getVerantwortlicher().getVorname() + " " + spec.getPflichtenheftEntity().getVerantwortlicher().getNachname()).setHeader("Verantwortlicher");
        grid.addComponentColumn(spec -> {
            Button btn1 = new Button("Details", new Icon(VaadinIcon.INFO_CIRCLE_O));
            btn1.addClickListener(e -> {
                Dialog dialog = new Dialog();
                dialog.add(new ProjektDetailView(service, spec.getPflichtenheftEntity()));
                dialog.setHeight("90%");
                dialog.setWidth("80%");
                dialog.open();
            });
            Button btn2 = new Button("Beitreten", new Icon(VaadinIcon.KEY_O));
            if (isMember(spec.getPflichtenheftEntity()))
                btn2.setEnabled(false);
            btn2.addClickListener(e -> {
                service.addProjektZuweisung(SecurityService.getLoggedInUsername(), spec.getPflichtenheftEntity().getProjektOid());
                UI.getCurrent().navigate("project-editor/" + spec.getPflichtenheftEntity().getProjektOid());
            });
            HorizontalLayout lay = new HorizontalLayout();
            lay.add(btn1, btn2);
            return lay;
        }).setHeader("Optionen");
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.getColumns().forEach(column -> column.setSortable(true));
    }

    /**
     * Methode, welche prüft, ob der aktuelle Benutzer bereits Mitglied des Projekts ist.
     * @param pflichtenheftEntity Pflichtenheft
     * @return true, wenn der Benutzer bereits Mitglied ist, sonst false
     */
    private boolean isMember(PflichtenheftEntity pflichtenheftEntity) {
        //SecurityService.getLoggedInUsername(), pflichtenheft
        String username = SecurityService.getLoggedInUsername();
        for (MitarbeiterEntity mitarbeiterEntity : pflichtenheftEntity.getMitarbeiter()) {
            if (mitarbeiterEntity.getBenutzername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}






