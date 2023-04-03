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

import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "/ProjektErstellen", layout = MainLayout.class)
public class ProjektErstellenView extends VerticalLayout {
    private final SpecificationsService service;
    private TextField titel = new TextField();
    private TextArea beschreibung = new TextArea();
    private DatePicker frist = new DatePicker();
    private TextField repo = new TextField();
    private Button speichern = new Button("Speichern");
    public ProjektErstellenView(SpecificationsService service) {
        this.service = service;

        MitarbeiterEntity mitarbeiter = service.findSpecificUser(SecurityService.getLoggedInUsername());

        titel.setAutofocus(true);
        //add(NavigationBar.getInstance());
        add("Projekt erstellen");
        add(
                new HorizontalLayout(new Text("Titel"), titel),
                new HorizontalLayout(new Text("Beschreibung"), beschreibung),
                new HorizontalLayout(new Text("Frist"), frist),
                new HorizontalLayout(new Text("Repository"), repo),
                speichern
        );
        getChildren().forEach(item -> {
            if (item instanceof HorizontalLayout) {
                ((HorizontalLayout) item).setAlignItems(Alignment.BASELINE);
            }
        });

        speichern.addClickListener(event -> {
            if(!titel.getValue().isBlank() && !beschreibung.getValue().isBlank())
                service.addPflichtenheft(mitarbeiter.getMitarbeiterOid(), titel.getValue(), beschreibung.getValue(), frist.getValue(), repo.getValue(), 0);
            else{
                if (titel.getValue().isBlank())
                    titel.setInvalid(true);
                if(beschreibung.getValue().isBlank())
                    beschreibung.setInvalid(true);
            }
            Notification.show("Projekt angelegt");
        });
    }
}
