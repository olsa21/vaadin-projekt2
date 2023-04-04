package org.vaadin.example.views.projectoverview;


import com.vaadin.flow.component.Component;
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
import org.vaadin.example.MainLayout;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.entity.PflichtenheftEntity;
import org.vaadin.example.model.PflichtenheftZeile;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;
import org.vaadin.example.views.ProjektDetailView;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;

@PermitAll
@PageTitle("Projektübersicht")
@Route(value = "/open-project-overview", layout = MainLayout.class)
public class OpenProjectOverview extends VerticalLayout {

    Grid<PflichtenheftZeile> grid = new Grid<>();
    TextField filterText = new TextField();

    //---------------------------------------------Ende

    private final SpecificationsService service;
    List<PflichtenheftZeile> pflichtenheftZeilen = new ArrayList<>();

    public OpenProjectOverview(SpecificationsService service) {
        this.service = service;
        //add(NavigationBar.getInstance());
        List<PflichtenheftEntity> pflichtenheftEntities2 = service.findOpenProjects();
        pflichtenheftEntities2.forEach(pflichtenheftEntity -> {
            pflichtenheftZeilen.add(new PflichtenheftZeile(pflichtenheftEntity));
        });

        addClassName("list-view");
        setSizeFull();

        configureGrid();

        add(
                getToolbar(),
                getContent()
        );
        beispielDatensaetze();
        //Laden der Daten
        updateList();
    }

    private void beispielDatensaetze() {

        //for (PflichtenheftEntity e : pflichtenheftEntities) {
        //    pflichtenhefter.add(new Pflichtenheft(e.getTitel(), e.getBeschreibung(), e.getFrist(), new Mitarbeiter("", "", "")));
        //}
    }

    private void updateList() {
        if (filterText.isEmpty()) {
            grid.setItems(this.pflichtenheftZeilen);
        } else {
            grid.setItems(this.pflichtenheftZeilen.stream().filter(spec -> spec.getPflichtenheftEntity().getTitel().toLowerCase().startsWith(filterText.getValue().toLowerCase())));
        }
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(2, grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }


    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        HorizontalLayout toolbar = new HorizontalLayout(filterText);
        return toolbar;
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.addColumn(spec -> spec.getPflichtenheftEntity().getTitel()).setHeader("titel");
        grid.addColumn(spec -> spec.anzahlMitarbeiter()).setHeader("Mitarbeiteranzahl");
        grid.addColumn(spec -> spec.verantwortlicher().getVorname() + " " + spec.verantwortlicher().getNachname()).setHeader("Verantwortlicher");
        grid.addComponentColumn(spec -> {
            Button btn1 = new Button("Details", new Icon(VaadinIcon.INFO_CIRCLE_O));
            btn1.addClickListener(e -> {
                //UI.getCurrent().navigate("projekt-details");
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






