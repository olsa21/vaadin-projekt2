package org.vaadin.example.views.projectoverview;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
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
import org.vaadin.example.NavigationBar;
import org.vaadin.example.model.Mitarbeiter;
import org.vaadin.example.model.Pflichtenheft;

import java.util.ArrayList;

@PageTitle("Projektübersicht")
@Route(value = "/open-project-overview", layout = MainLayout.class)
public class OpenProjectOverview extends VerticalLayout
{

    Grid<Pflichtenheft> grid = new Grid<>(Pflichtenheft.class);
    TextField filterText = new TextField();

    //---------------------------------------------Testbeispieldaten für horizontalen Prototyp
    private ArrayList<Pflichtenheft> pflichtenhefter;
    //---------------------------------------------Ende

    public OpenProjectOverview(){
        //add(NavigationBar.getInstance());

        this.pflichtenhefter = new ArrayList<>();
        addClassName("list-view");
        setSizeFull();

        configureGrid();

        add(
            getToolbar(),
            getContent()
        );
        beispielDatensaetze();
        updateList();
    }

    private void beispielDatensaetze() {
        Mitarbeiter m1 = new Mitarbeiter("Cihan","W.","Softwareentwicklung");
        Mitarbeiter m2 = new Mitarbeiter("Oliver", "S.", "Softwareentwicklung");
        Mitarbeiter m3 = new Mitarbeiter("Rainer", "Drenor", "Systemintegration");
        Mitarbeiter m4 = new Mitarbeiter("Harald", "Krause", "Chefetage");
        Pflichtenheft p1 = new Pflichtenheft("Pflichtenheftgenerator","..","2023-10-10", m3);
        Pflichtenheft p2 = new Pflichtenheft("Verkabelung von Systemen","..","2025-01-01", m2);
        Pflichtenheft p3 = new Pflichtenheft("Testprojekt","..","2024-10-10", m1);
        p1.addMitarbeiter(m1);p1.addMitarbeiter(m2);p1.addMitarbeiter(m3);p1.addMitarbeiter(m4);
        p2.addMitarbeiter(m1);p2.addMitarbeiter(m4);
        p3.addMitarbeiter(m1);
        this.pflichtenhefter.clear();
        this.pflichtenhefter.add(p1);
        this.pflichtenhefter.add(p2);
        this.pflichtenhefter.add(p3);

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
        grid.addColumn(spec -> spec.getMitarbeiterListSize()).setHeader("Mitarbeiteranzahl");
        grid.addColumn(spec -> spec.getVerantwortlicher().getFullName()).setHeader("Verantwortlicher");
        grid.addComponentColumn(spec->{
            Button btn1 = new Button("Details", new Icon(VaadinIcon.INFO_CIRCLE_O));
            Button btn2 = new Button("Beitreten", new Icon(VaadinIcon.KEY_O));
            HorizontalLayout lay = new HorizontalLayout();
            lay.add(btn1, btn2);
            return lay;
        }).setHeader("Optionen");
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        grid.getColumns().forEach(column -> column.setSortable(true));
    }



}






