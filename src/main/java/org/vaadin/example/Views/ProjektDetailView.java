package org.vaadin.example.Views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.example.NavigationBar;

@Route("ProjektDetail")
public class ProjektDetailView extends VerticalLayout {
    private Text titel = new Text("");
    private Text beschreibung = new Text("");
    private Text frist = new Text("");
    private Text verantwortlicher = new Text("");
    private Text mitglieder = new Text("");
    private Text repo = new Text("");

    public ProjektDetailView() {
        titel.setText("Titel");
        beschreibung.setText("Beschreibung");
        frist.setText("01.01.2020");
        verantwortlicher.setText("Max Mustermann");
        mitglieder.setText("Max Mustermann, Thomas Müller, Maria Schmidt, Anna Wagner, Paul Bauer, Laura Schulz, Andreas Mayer, Sarah Meier, Felix Becker, Hannah Schwarz, Janine Richter");
        repo.setText("https://github.com/ .... ");


        add(NavigationBar.getInstance());
        add("Projekt Details");
        add(
                new HorizontalLayout(new Text("Titel "), titel),
                new HorizontalLayout(new Text("Beschreibung" ), beschreibung),
                new HorizontalLayout(new Text("Frist "), frist),
                new HorizontalLayout(new Text("Verantwortlicher "), verantwortlicher),
                new HorizontalLayout(new Text("Mitglieder "), mitglieder),
                new HorizontalLayout(new Text("Repository "), repo),
                new Button("Projekt Beitreten")
        );

        getChildren().forEach(item -> {
            if (item instanceof HorizontalLayout) {
                ((HorizontalLayout) item).setAlignItems(Alignment.BASELINE);
            }
        });
    }
}
