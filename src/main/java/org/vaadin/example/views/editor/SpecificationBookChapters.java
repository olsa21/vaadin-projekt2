package org.vaadin.example.views.editor;

//Pflichtenheften Struktur
//Pflichtenheft
//      Einleitung
//      Ausgangssituation und Zielsetzung
//          Gegenwärtige Defizite
//          Argumente für Durchführung
//          Stakeholder
//          Ziele
//      Dekomposition des Gesamtsystems
//          Präzisierung des Kontextes des Softwareproduktes
//      Funktionale Anforderungen
//          Liste mit funktionalen Anforderungen
//          Anwendungsfälle
//          Geschäftsprozess zur Projektbeantragung
//      Nicht-Funktionale Anforderungen
//      Sicherheitsanforderungen
//      Lieferumfang
//      Abnahmekriterien und Vorgehen zur Ausgangsprüfung
//          Testfälle für das Softwareprodukt

import java.util.ArrayList;
import java.util.TreeMap;

public class SpecificationBookChapters {
    private static SpecificationBookChapters instance;

    private TreeMap<String, ArrayList<String>> treeMap;
    public static SpecificationBookChapters getInstance(){
        if (instance == null)
            instance = new SpecificationBookChapters();
        return instance;
    }

    public ArrayList<String> getChapter(String chapter){
        if (chapter == null)
            return treeMap.get("");
        if(treeMap.get(chapter) == null)
            return new ArrayList<>();
        return treeMap.get(chapter);
    }

    private SpecificationBookChapters(){
        treeMap = new TreeMap<>();
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
    }


}
