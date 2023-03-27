package org.vaadin.example.views.test;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.service.SpecificationsService;

import java.util.Collections;



@PageTitle("Kontaktliste")
@Route(value = "/testversuch")
public class ListView extends VerticalLayout {

    private final SpecificationsService service;
    MitarbeiterEntity currentUser;
    Grid<MitarbeiterEntity> grid = new Grid<>(MitarbeiterEntity.class);
    TextField filterText = new TextField();

    ContactForm form;
    public ListView(SpecificationsService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();

        configureGrid();
        configureForm();
        add(
                getToolbar(),
                getContent()
        );
        updateList();
    }

    private void updateList() {
        grid.setItems(service.findAllUser(null));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid,form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new ContactForm(service, Collections.emptyList(), Collections.emptyList());
        form.setWidth("25em");

    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        Button addContactBtn = new Button("Add User");
        addContactBtn.addClickListener(click->{
            Notification.show("Hinzufügen vom Benutzer");
            this.currentUser = new MitarbeiterEntity();
            //this.form.updateForm(this.currentUser);
        });
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactBtn);
        return toolbar;
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("benutzername", "mail", "passwort");
        grid.addColumn(user -> user.getMitarbeiterOid()).setHeader("OID");
        grid.addColumn(user -> user.getVorname()).setHeader("Vorname");
        grid.addColumn(user -> user.getNachname()).setHeader("Nachname");
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
    }



}
