package org.vaadin.example.views.editor;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.example.model.Mitarbeiter;
import org.vaadin.example.model.Pflichtenheft;
import org.vaadin.example.views.editor.EditorBar;

import java.util.ArrayList;
import java.util.TreeMap;

@PageTitle("Projektübersicht")
@Route(value = "/project-editor")
public class PflichtenheftEditor extends HorizontalLayout
{


    private TreeGrid<String> chapterOverview;

    //---------------------------------------------Testbeispieldaten für horizontalen Prototyp
    private ArrayList<Pflichtenheft> pflichtenhefter;
    //---------------------------------------------Ende

    public ArrayList<String> getChapter(String chapter){
        TreeMap<String, ArrayList<String>> treeMap = new TreeMap<>();
        ArrayList<String> rootDir = new ArrayList<>();
        ArrayList<String> ausgangsSituation = new ArrayList<>();
        ArrayList<String> dekomposition = new ArrayList<>();
        ArrayList<String> funktionaleAnforderungen = new ArrayList<>();
        ArrayList<String> abnahmeKriterien = new ArrayList<>();

        rootDir.add("Einleitung");
        rootDir.add("Ausgangssituation und Zielsetzung");
        rootDir.add("Dekomposition des Gesamtsystems");
        rootDir.add("Funktionale Anforderungen");
        rootDir.add("Nicht-Funktionale Anforderungen");
        rootDir.add("Sicherheitsanfoderungen");
        rootDir.add("Lieferumfang");
        rootDir.add("Abnahmekriterien und Vorgehen zur Ausgangsprüfung");

        ausgangsSituation.add("Gegenwärtige Defizite");
        ausgangsSituation.add("Argumente für Durchführung");
        ausgangsSituation.add("Stakeholder");
        ausgangsSituation.add("Ziele");

        dekomposition.add("Präzisierung des Kontextes des Softwareproduktes");

        funktionaleAnforderungen.add("Liste mit funktionalen Anforderungen");
        funktionaleAnforderungen.add("Anwendungsfälle");
        funktionaleAnforderungen.add("Geschäftsprozess zur Projektbeantragung");

        abnahmeKriterien.add("Testfälle für das Softwareprodukt");

        treeMap.put("", rootDir);
        treeMap.put("Ausgangssituation und Zielsetzung", ausgangsSituation);
        treeMap.put("Dekomposition des Gesamtsystems", dekomposition);
        treeMap.put("Funktionale Anforderungen", funktionaleAnforderungen);
        treeMap.put("Abnahmekriterien und Vorgehen zur Ausgangsprüfung", abnahmeKriterien);
        if (chapter == null)
            return rootDir;
        if(treeMap.get(chapter) == null)
            return new ArrayList<>();
        return treeMap.get(chapter);
    }

    public PflichtenheftEditor(){
       // add(NavigationBar.getInstance());
        this.pflichtenhefter = new ArrayList<>();
        addClassName("editor-view");
        setSizeFull();
        setWidthFull();
        TreeGrid<String> testGrid = new TreeGrid<>();
        ArrayList<String> root = this.getChapter(null);
        testGrid.setItems(root, this::getChapter);
        testGrid.addHierarchyColumn(String::toString).setHeader("Kapitel");
        testGrid.setHeightFull();
        setFlexGrow(0.4, testGrid);
        add(testGrid);

        EditorBar editorBar = new EditorBar();
        setFlexGrow(0.6, editorBar);

        add(editorBar);

        testGrid.addItemClickListener(event -> {
            editorBar.resetComponents();
        });

        //beispielDatensaetze();

    }

    private Component getChapterOverview() {

        chapterOverview = new TreeGrid<>();
        chapterOverview.addColumn(String::toString).setHeader("Kapitel");
        chapterOverview.addColumn(String::toString).setHeader("Modifziert");

        return chapterOverview;
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






