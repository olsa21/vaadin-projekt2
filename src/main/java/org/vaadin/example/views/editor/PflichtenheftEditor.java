package org.vaadin.example.views.editor;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;
import org.vaadin.example.model.Mitarbeiter;
import org.vaadin.example.model.Pflichtenheft;
import org.vaadin.example.views.ProjektDetailMitExport;
import org.vaadin.example.views.editor.EditorBar;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.TreeMap;

@PermitAll
@PageTitle("Projektübersicht")
@Route(value = "/project-editor", layout = MainLayout.class)
public class PflichtenheftEditor extends HorizontalLayout
{
    private TreeGrid<String> chapterOverview;

    //---------------------------------------------Testbeispieldaten für horizontalen Prototyp
    private ArrayList<Pflichtenheft> pflichtenhefter;
    //---------------------------------------------Ende
    EditorBar editBar;
    ProjektDetailMitExport projektDetailMitExport;

    public PflichtenheftEditor(){
        // add(NavigationBar.getInstance());
        this.pflichtenhefter = new ArrayList<>();
        editBar = new EditorBar();
        projektDetailMitExport = new ProjektDetailMitExport();
        addClassName("editor-view");
        setSizeFull();
        setWidthFull();
        TreeGrid<String> testGrid = new TreeGrid<>();
        ArrayList<String> root = SpecificationBookChapters.getInstance().getChapter(null);
        testGrid.setItems(root, SpecificationBookChapters.getInstance()::getChapter);
        testGrid.addHierarchyColumn(String::toString).setSortable(false);



        testGrid.setHeightFull();
        //setFlexGrow(0.4, testGrid);
        Button projectButton = new Button("Pflichtenheft: Verkabelung von Systemen");
        projectButton.addClickListener(event -> {
            Notification.show("Pflichtenheft: Verkabelung von Systemen");

            remove(editBar);
            add(projektDetailMitExport);

        });
        HorizontalLayout projectTree = new HorizontalLayout( new VerticalLayout(projectButton, testGrid));
        setFlexGrow(0.3, projectTree);
        add(projectTree);



        //EditorBar editorBar = new EditorBar();
        //setFlexGrow(0.7, editorBar);

        //ProjektDetailMitExport projektDetailMitExport = new ProjektDetailMitExport();
        //setFlexGrow(0.7, projektDetailMitExport);

        //add(editorBar);

        testGrid.addItemClickListener(event -> {
            Notification.show("ITEM CLICKED");
            remove(projektDetailMitExport);
            add(editBar);

        });

        //beispielDatensaetze();

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












}






