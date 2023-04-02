package org.vaadin.example.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;
import org.vaadin.example.components.AbteilungComboBox;
import org.vaadin.example.components.CustomPicUpload;
import org.vaadin.example.entity.AbteilungEntity;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;

import javax.annotation.security.PermitAll;
import java.util.List;

@PermitAll
@Route(value = "/profil-edit", layout = MainLayout.class)
public class ProfilEditView extends VerticalLayout {
    private TextField benutzername = new TextField();
    private TextField passwort1 = new TextField();
    private TextField passwort2 = new TextField();
    private TextField mail = new TextField();
    private TextField vorname = new TextField();
    private TextField nachname = new TextField();
    private AbteilungComboBox abteilung;
    private CustomPicUpload profilbild = new CustomPicUpload();
    private Button save = new Button("Speichern");
    private MitarbeiterEntity mitarbeiter;
    private final SpecificationsService service;
    public ProfilEditView(SpecificationsService service) {
        this.service = service;
        mitarbeiter = service.findSpecificUser(SecurityService.getLoggedInUsername());
        List<AbteilungEntity> alleAbteilungenList = service.getAbteilungList();
        List<AbteilungEntity> selektierteAbteilungen = service.getAbteilungListWhere(mitarbeiter.getMitarbeiterOid());
        abteilung = new AbteilungComboBox(alleAbteilungenList, selektierteAbteilungen);

        benutzername.setValue(mitarbeiter.getBenutzername());

        mail.setValue(mitarbeiter.getMail());
        vorname.setValue(mitarbeiter.getVorname());
        nachname.setValue(mitarbeiter.getNachname());

        //erstmal
        benutzername.setEnabled(false);

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
                save
        );
        getChildren().forEach(item -> {
            if (item instanceof HorizontalLayout) {
                ((HorizontalLayout) item).setAlignItems(Alignment.BASELINE);
            }
        });

        save.addClickListener(event -> {
            //Mitarbeiter Tabelle bearbeiten
            mitarbeiter.setBenutzername(benutzername.getValue());
            mitarbeiter.setMail(mail.getValue());
            mitarbeiter.setVorname(vorname.getValue());
            mitarbeiter.setNachname(nachname.getValue());

            mitarbeiter.setProfilbild(profilbild.getBytes());

            if (!passwort1.getValue().isEmpty() && passwort1.getValue().equals(passwort2.getValue()))
                mitarbeiter.setPasswort(SecurityService.hash(passwort1.getValue()));

            service.saveBenutzer(mitarbeiter);

            service.saveAbteilungZuweisungen(mitarbeiter.getMitarbeiterOid(), abteilung.ausgewaehlteAbteilungen());

            Notification.show("Profil erfolgreich bearbeitet");
        });
    }
}
