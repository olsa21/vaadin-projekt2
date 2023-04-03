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
import org.vaadin.example.MainLayout;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.entity.PflichtenheftEntity;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;
import org.vaadin.example.views.ProjektDetailView;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;

@PermitAll
@PageTitle("Projektübersicht")
//ursprünglicher value="/project-overview", jedoch zu Testzwecken geändert TODO
@Route(value = "", layout = MainLayout.class)
public class ProjectOverview extends VerticalLayout
{
    Grid<PflichtenheftEntity> grid = new Grid<>(PflichtenheftEntity.class);
    private final SpecificationsService service;
    TextField filterText = new TextField();


    //---------------------------------------------Testbeispieldaten für horizontalen Prototyp
    private ArrayList<PflichtenheftEntity> pflichtenhefter;
    //---------------------------------------------Ende

    public ProjectOverview(SpecificationsService service){
        this.service = service;

        this.pflichtenhefter = new ArrayList<>();
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        add(
            getToolbar(),
            getContent()
        );
        loadSpecificProjects();
        updateList();
    }

    private void loadSpecificProjects(){
        MitarbeiterEntity mitarbeiter = service.findSpecificUser(SecurityService.getLoggedInUsername());
        this.pflichtenhefter = service.getPflichtenheftListWhere(mitarbeiter.getMitarbeiterOid());
    }

    private void updateList() {
        if (filterText.isEmpty()) {
            grid.setItems(this.pflichtenhefter);
        }else{
            grid.setItems(this.pflichtenhefter.stream().filter(spec -> spec.getTitel().toLowerCase().startsWith(filterText.getValue().toLowerCase())));
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
        filterText.addValueChangeListener(e->updateList());
        HorizontalLayout toolbar = new HorizontalLayout(filterText);
        return toolbar;
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("titel");
        //grid.addColumn(spec -> spec.getMitarbeiterListSize()).setHeader("Mitarbeiteranzahl");
        grid.addColumn(spec -> "500").setHeader("Mitarbeiteranzahl");//FIXME Daten korrekt eintragen!
        grid.addColumn(spec -> spec.getFrist()).setHeader("Release Datum");
        grid.addComponentColumn(spec->{
            Icon icon = new Icon(VaadinIcon.INFO_CIRCLE_O);
            Button btnDetails = new Button("Details", icon);
            btnDetails.addClickListener(e->{
                //UI.getCurrent().navigate("projekt-details");
                //So dann auch in OpenProjectOverview
                Dialog dialog = new Dialog();
                dialog.add(new ProjektDetailView(service, spec));
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
