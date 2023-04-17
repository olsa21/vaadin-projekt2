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
import java.util.Set;


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
        boolean isMember = isMember();
        titel.setText(pflichtenheft.getTitel());
        beschreibung.setText(pflichtenheft.getBeschreibung());
        frist.setText(pflichtenheft.getFrist());

        //verantwortlicher.setText(createVerantwortlicherString());
        verantwortlicher.setText(pflichtenheft.getVerantwortlicher().getVorname() + " " + pflichtenheft.getVerantwortlicher().getNachname());
        mitglieder.setText(createMitgliederString());
        repo.setText(pflichtenheft.getRepositoryLink());

        setButtonVisibility(isMember);

        editBtn.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("document.querySelector('vaadin-dialog-overlay').close()");
            UI.getCurrent().navigate("project-editor/" + pflichtenheft.getProjektOid());
        });
        beitretenBtn.addClickListener(e -> {
            //TODO Check ob immer noch offen
            service.addProjektZuweisung(SecurityService.getLoggedInUsername(), pflichtenheft.getProjektOid());
            UI.getCurrent().getPage().executeJs("document.querySelector('vaadin-dialog-overlay').close()");
            UI.getCurrent().navigate("project-editor/" + pflichtenheft.getProjektOid());
        });
        freischaltenBtn.addClickListener(e -> {
            service.setOeffentlich(pflichtenheft, 1);
            setButtonVisibility(isMember);
        });
        schliessenBtn.addClickListener(e -> {
            service.setOeffentlich(pflichtenheft, 0);
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

    private boolean isMember() {
        //SecurityService.getLoggedInUsername(), pflichtenheft
        for(MitarbeiterEntity mitarbeiterEntity : pflichtenheft.getMitarbeiter()) {
            if(mitarbeiterEntity.getBenutzername().equals(SecurityService.getLoggedInUsername())) {
                return true;
            }
        }
        return false;
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
            freischaltenBtn.setVisible(false);
            schliessenBtn.setVisible(false);
            editBtn.setVisible(false);
        }
    }

    private String createMitgliederString() {
        Set<MitarbeiterEntity> mitgliederList = pflichtenheft.getMitarbeiter();
        String mitgliederString = "";
        for (MitarbeiterEntity mitglied : mitgliederList) {
            mitgliederString += mitglied.getVorname() + " " + mitglied.getNachname() + ", ";
        }
        return mitgliederString;
    }

    private String createVerantwortlicherString(){
        for(MitarbeiterEntity mitarbeiterEntity : pflichtenheft.getMitarbeiter()) {
            if(mitarbeiterEntity.getMitarbeiterOid() == pflichtenheft.getVerantwortlicher().getMitarbeiterOid()){
                return mitarbeiterEntity.getVorname() + " " + mitarbeiterEntity.getNachname();
            }
        }
        return null;
    }
}
