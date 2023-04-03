package org.vaadin.example.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.entity.PflichtenheftEntity;
import org.vaadin.example.service.SpecificationsService;

import javax.annotation.security.PermitAll;
import java.util.List;

@PermitAll
@Route(value = "/projekt-details", layout = MainLayout.class)
public class ProjektDetailView extends VerticalLayout {
    private final SpecificationsService service;
    private Text titel = new Text("");
    private Text beschreibung = new Text("");
    private Text frist = new Text("");
    private Text verantwortlicher = new Text("");
    private Text mitglieder = new Text("");
    private Text repo = new Text("");

    private PflichtenheftEntity pflichtenheft;

    private Button beitretenBtn = new Button("Projekt Beitreten");
    private Button editBtn = new Button("Edit");
    private Button freischaltenBtn = new Button("Freischalten");
    private Button schliessenBtn = new Button("Schliessen");

    public ProjektDetailView(SpecificationsService service, PflichtenheftEntity pflichtenheft) {
        this.service = service;
        this.pflichtenheft = pflichtenheft;
        boolean isMember = service.isMember(SecurityService.getLoggedInUsername(), pflichtenheft);
        titel.setText(pflichtenheft.getTitel());
        beschreibung.setText(pflichtenheft.getBeschreibung());
        frist.setText(pflichtenheft.getFrist());

        MitarbeiterEntity verantwortlichePerson = service.readVerantwortlicher(pflichtenheft);
        verantwortlicher.setText(verantwortlichePerson.getVorname() + " " + verantwortlichePerson.getNachname());

        mitglieder.setText(readMitgliederString());
        repo.setText(pflichtenheft.getRepositoryLink());

        setButtonVisibility(isMember);

        editBtn.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("document.querySelector('vaadin-dialog-overlay').close()");
            //TODO ID oder so mitgeben
            UI.getCurrent().navigate("project-editor");
        });
        beitretenBtn.addClickListener(e -> {
            service.addProjektZuweisung(SecurityService.getLoggedInUsername(), pflichtenheft.getProjektOid());
            UI.getCurrent().getPage().executeJs("document.querySelector('vaadin-dialog-overlay').close()");
            //TODO ID oder so mitgeben
            UI.getCurrent().navigate("project-editor");
        });
        freischaltenBtn.addClickListener(e -> {
            pflichtenheft.setOeffentlich((byte) 1);
            service.savePflichtenheft(pflichtenheft);
            setButtonVisibility(isMember);
        });
        schliessenBtn.addClickListener(e -> {
            pflichtenheft.setOeffentlich((byte) 0);
            service.savePflichtenheft(pflichtenheft);
            setButtonVisibility(isMember);
        });

        //add(NavigationBar.getInstance());
        add("Projekt Details");
        add(
                new HorizontalLayout(new Text("Titel "), titel),
                new HorizontalLayout(new Text("Beschreibung"), beschreibung),
                new HorizontalLayout(new Text("Frist "), frist),
                new HorizontalLayout(new Text("Verantwortlicher "), verantwortlicher),
                new HorizontalLayout(new Text("Mitglieder "), mitglieder),
                new HorizontalLayout(new Text("Repository "), repo),
                new HorizontalLayout(beitretenBtn, editBtn, freischaltenBtn, schliessenBtn)
        );

        getChildren().forEach(item -> {
            if (item instanceof HorizontalLayout) {
                ((HorizontalLayout) item).setAlignItems(Alignment.BASELINE);
            }
        });
    }

    private void setButtonVisibility(boolean isMember) {
        if (isMember) {
            beitretenBtn.setVisible(false);
            if (pflichtenheft.getOeffentlich() == 0) {
                freischaltenBtn.setVisible(true);
                schliessenBtn.setVisible(false);
            } else {
                freischaltenBtn.setVisible(false);
                schliessenBtn.setVisible(true);
            }
        } else {
            editBtn.setVisible(false);
        }
    }

    private String readMitgliederString() {
        List<MitarbeiterEntity> mitgliederList = service.readMitglieder(pflichtenheft);
        String mitgliederString = "";
        for (MitarbeiterEntity mitglied : mitgliederList) {
            mitgliederString += mitglied.getVorname() + " " + mitglied.getNachname() + ", ";
        }
        return mitgliederString;
    }
}
