package org.vaadin.example.views.projectoverview;


import com.vaadin.flow.component.Component;
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
import org.vaadin.example.views.MainLayout;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.entity.PflichtenheftEntity;
import org.vaadin.example.model.PflichtenheftZeile;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;
import org.vaadin.example.views.project.ProjektDetailView;

import javax.annotation.security.PermitAll;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse ist dafür zuständig, dass eine passende View für die Projektübersicht angezeigt wird.
 */
@PermitAll
@PageTitle("Projektübersicht")
@Route(value = "", layout = MainLayout.class)
public class ProjectOverview extends VerticalLayout
{
    Grid<PflichtenheftZeile> grid = new Grid<>();
    private final SpecificationsService service;
    TextField filterText = new TextField();
    private ArrayList<PflichtenheftZeile> pflichtenhefter;

    /**
     * Konstruktor, welcher die View für die Projektübersicht erstellt.
     * @param service Service, welcher die Datenbankanbindung ermöglicht.
     */
    public ProjectOverview(SpecificationsService service){
        this.service = service;
        this.pflichtenhefter = new ArrayList<>();
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        add(getToolbar(), getContent());
        loadSpecificProjects();
        updateList();
    }

    /**
     * Methode, welche die Projekte lädt, welche dem angemeldeten Mitarbeiter zugeordnet sind.
     */
    private void loadSpecificProjects(){
        MitarbeiterEntity mitarbeiter = service.findSpecificUser(SecurityService.getLoggedInUsername());
        List<PflichtenheftEntity> list = service.getPflichtenheftListWhere(mitarbeiter.getMitarbeiterOid());
        for(PflichtenheftEntity pflichtenheftEntity : list){
            pflichtenhefter.add(new PflichtenheftZeile(pflichtenheftEntity));
        }
    }

    /**
     * Methode, welche die Liste der Projekte aktualisiert.
     */
    private void updateList() {
        if (filterText.isEmpty()) {
            grid.setItems(this.pflichtenhefter);
        }else{
            grid.setItems(this.pflichtenhefter.stream().filter(spec -> spec.getPflichtenheftEntity().getTitel().toLowerCase().startsWith(filterText.getValue().toLowerCase())));
        }
    }

    /**
     * Methode, welche die View für die Projektübersicht erstellt.
     * @return Gibt die View für die Projektübersicht zurück.
     */
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(2, grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    /**
     * Methode, welche die Toolbar für die Projektübersicht erstellt.
     * @return Gibt die Toolbar für die Projektübersicht zurück.
     */
    private Component getToolbar() {
        filterText.setPlaceholder("Filter by Titel");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e->updateList());
        HorizontalLayout toolbar = new HorizontalLayout(filterText);
        return toolbar;
    }

    /**
     * Methode, welche die Grid für die Projektübersicht konfiguriert.
     */
    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.addColumn(spec -> spec.getPflichtenheftEntity().getTitel()).setHeader("Titel");
        grid.addColumn(spec -> spec.anzahlMitarbeiter()).setHeader("Mitarbeiteranzahl");

        SimpleDateFormat dfInput = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat dfOutput = new SimpleDateFormat("dd. MMMM yyyy");
        grid.addColumn(spec -> {
            try {
                return dfOutput.format( dfInput.parse(spec.getPflichtenheftEntity().getFrist()) );
            } catch (Exception e) {
                return "Ungültiges Datum!";
            }
        }
        ).setHeader("Release Datum");
        grid.addComponentColumn(spec->{
            Icon icon = new Icon(VaadinIcon.INFO_CIRCLE_O);
            Button btnDetails = new Button("Details", icon);
            btnDetails.addClickListener(e->{
                Dialog dialog = new Dialog();
                dialog.add(new ProjektDetailView(service, spec.getPflichtenheftEntity()));

                dialog.setHeight("90%");
                dialog.setWidth("80%");
                dialog.open();
            });
            return btnDetails;
        }).setHeader("Optionen");
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.getColumns().forEach(column -> column.setSortable(true));
    }
}
