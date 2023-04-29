package org.vaadin.example.views.profile;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.components.AbteilungComboBox;
import org.vaadin.example.components.CustomPasswordField;
import org.vaadin.example.components.CustomPicUpload;
import org.vaadin.example.entity.AbteilungEntity;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;
import org.vaadin.example.utility.PasswordEncoder;
import org.vaadin.example.views.MainLayout;

import javax.annotation.security.PermitAll;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@PermitAll
@Route(value = "/profil-edit", layout = MainLayout.class)
public class ProfilEditView extends VerticalLayout {
    private TextField benutzername = new TextField();
    private PasswordField passwort1 = new CustomPasswordField();
    private PasswordField passwort2 = new PasswordField();
    private TextField mail = new TextField();
    private TextField vorname = new TextField();
    private TextField nachname = new TextField();
    private AbteilungComboBox abteilung;
    private CustomPicUpload profilbild = new CustomPicUpload();
    //private CustomPicUploadWithCaptionAndScaling profilbild = new CustomPicUploadWithCaptionAndScaling();
    private Button save = new Button("Speichern");
    private Button clearProfilbild = new Button("Profilbild löschen");
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

        if (mitarbeiter.getProfilbild() != null) {
            profilbild.setImage(mitarbeiter.getProfilbild());
        }

        add("Profil bearbeiten");

        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );
        form.addFormItem(benutzername, "Benutzernamen");
        form.addFormItem(passwort1, "Passwort");
        form.addFormItem(passwort2, "Passwort wiederholen");
        form.addFormItem(mail, "E-Mail");
        form.addFormItem(vorname, "Vorname");
        form.addFormItem(nachname, "Nachname");
        form.addFormItem(abteilung, "Abteilung");
        form.addFormItem(profilbild, "Profilbild");

        add(
                form
        );

        form.addFormItem(new HorizontalLayout(save, clearProfilbild), "");

        getChildren().forEach(item -> {
            if (item instanceof HorizontalLayout) {
                ((HorizontalLayout) item).setAlignItems(Alignment.BASELINE);
            }
        });

        save.addClickListener(event -> {
            try {
                //Mitarbeiter Tabelle bearbeiten
                mitarbeiter.setBenutzername(benutzername.getValue());
                mitarbeiter.setMail(mail.getValue());
                mitarbeiter.setVorname(vorname.getValue());
                mitarbeiter.setNachname(nachname.getValue());

                mitarbeiter.setProfilbild(profilbild.getImgBytesDownscaled());


                MainLayout mainLayout = (MainLayout) getUI().get().getChildren().filter(item -> item instanceof MainLayout).findFirst().get();
                mainLayout.setAvatarPicture(profilbild.getImgBytesDownscaled());

                if (!passwort1.getValue().isEmpty()) {
                    if (passwort1.isInvalid()) {
                        return;
                    }
                    if (passwort1.getValue().equals(passwort2.getValue())) {
                        mitarbeiter.setPasswort(PasswordEncoder.hashPassword((passwort1.getValue())));

                    } else {
                        passwort2.setInvalid(true);
                        return;
                    }
                }


                service.addMitarbeiter(mitarbeiter);

                service.saveAbteilungZuweisungen(mitarbeiter.getMitarbeiterOid(), abteilung.ausgewaehlteAbteilungen());

                Notification.show("Profil erfolgreich bearbeitet");
            } catch (NoSuchAlgorithmException | IOException e) {
                System.err.println(e.getMessage());
            }
        });

        clearProfilbild.addClickListener(event -> {
            profilbild.clear();
        });
    }
}
