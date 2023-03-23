package org.vaadin.example.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;
import org.vaadin.example.NavigationBar;
import org.vaadin.example.entity.Abteilung;

import java.util.ArrayList;

@Route("ProfilEdit")
public class ProfilEditView extends VerticalLayout {
    private TextField benutzername = new TextField();
    private TextField passwort1 = new TextField();
    private TextField passwort2 = new TextField();
    private TextField mail = new TextField();
    private TextField vorname = new TextField();
    private TextField nachname = new TextField();
    private MultiSelectComboBox<String> abteilung = new MultiSelectComboBox<>();
    private Upload profilbild = new Upload();
    public ProfilEditView() {
        //Statische Liste erstmal
        ArrayList<Abteilung> abteilungList = new ArrayList<>();
        abteilungList.add(new Abteilung(1, "IT"));
        abteilungList.add(new Abteilung(2, "Marketing"));
        abteilungList.add(new Abteilung(3, "Vertrieb"));
        abteilungList.add(new Abteilung(4, "Logistik"));
        abteilungList.add(new Abteilung(5, "Personal"));
        abteilungList.add(new Abteilung(6, "Buchhaltung"));
        abteilungList.add(new Abteilung(7, "Controlling"));



        profilbild.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        profilbild.setMaxFileSize(16777216);
        profilbild.setDropAllowed(true);
        profilbild.setUploadButton(new Button("Profilbild hochladen"));


        //Setzen der vorhanden Werte aus DB
        benutzername.setValue("Benutzername");
        mail.setValue("max.mustermann@gigasoft.de");
        vorname.setValue("Max");
        nachname.setValue("Mustermann");

        abteilung.setItems(abteilungList.stream().map(Abteilung::getName));
        abteilung.setValue(abteilungList.get(0).getName());

        add(NavigationBar.getInstance());
        add("Profil bearbeiten");
        add(
                new HorizontalLayout(new Text("Benutzername"), benutzername),
                new HorizontalLayout(new Text("Passwort"), passwort1),
                new HorizontalLayout(new Text("Passwort wiederholen"), passwort2),
                new HorizontalLayout(new Text("E-Mail"), mail),
                new HorizontalLayout(new Text("Vorname"), vorname),
                new HorizontalLayout(new Text("Nachname"), nachname),
                new HorizontalLayout(new Text("Abteilung"), abteilung),
                new HorizontalLayout(new Text("Profilbild"), profilbild),
                new Button("Speichern")

        );
        getChildren().forEach(item -> {
            if (item instanceof HorizontalLayout) {
                ((HorizontalLayout) item).setAlignItems(Alignment.BASELINE);
            }
        });
    }
}
