package org.vaadin.example.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;
import org.vaadin.example.NavigationBar;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "/ProjektErstellen", layout = MainLayout.class)
public class ProjektErstellenView extends VerticalLayout {
    private TextField titel = new TextField();
    private TextArea beschreibung = new TextArea();
    private DatePicker frist = new DatePicker();
    private TextField repo = new TextField();
    private Button speichern = new Button("Speichern");
    public ProjektErstellenView() {
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
    }
}
