package org.vaadin.example.views.editor;

import org.vaadin.example.entity.KapitelvordefiniertEntity;
import org.vaadin.example.model.ChapterModel;
import org.vaadin.example.service.SpecificationsService;

import java.util.*;

/**
 * Die Klasse ist dafür zuständig, dass die Kapitelstruktur des Pflichtenheftes geladen wird.
 * Hierbei wird nach dem Singleton-Pattern gearbeitet, damit die Kapitelstruktur nur einmal geladen wird.
 */
public class SpecificationBookChapters {
    private static SpecificationBookChapters instance;
    private TreeMap<ChapterModel, ArrayList<ChapterModel>> treeMap;
    final SpecificationsService service;

    /**
     * Gibt die Instanz der Klasse zurück. (Singleton-Pattern)
     * @param service Service, welcher für die Kommunikation mit der Datenbank zuständig ist.
     * @return Instanz der Klasse.
     */
    public static SpecificationBookChapters getInstance(SpecificationsService service){
        if (service == null)
            throw new IllegalArgumentException("Service darf nicht null sein!");
        if (instance == null)
            instance = new SpecificationBookChapters(service);
        return instance;
    }

    /**
     * Methode, welche die die Kapitelstruktur zurückgibt.
     * @param chapter Kapitel, welches die Kinder zurückgeben soll.
     * @return Liste mit den Kindern des Kapitels.
     */
    public ArrayList<ChapterModel> getChapter(ChapterModel chapter){
        if (chapter == null)
            return treeMap.get(new ChapterModel(-1,""));
        if(treeMap.get(chapter) == null)
            return new ArrayList<>();
        return treeMap.get(chapter);
    }

    /**
     * Privater Konstruktor, welcher die Kapitelstruktur aus der Datenbank lädt.
     */
    private SpecificationBookChapters(SpecificationsService service){
        this.service = service;
        List<KapitelvordefiniertEntity> kapitelvordefiniert = service.readAllKapitelvordefiniert();
        treeMap = createTreemap(kapitelvordefiniert);
    }

    /**
     * Erstellt die Kapitelstruktur aus der Datenbank.
     * @param kapitelvordefiniert Liste mit den Kapiteln aus der Datenbank.
     * @return Baumstruktur mit den Kapiteln.
     */
    public TreeMap<ChapterModel, ArrayList<ChapterModel>> createTreemap(List<KapitelvordefiniertEntity> kapitelvordefiniert) {
        if (kapitelvordefiniert == null)
            throw new IllegalArgumentException("Kapitelvordefiniert darf nicht null sein!");
        TreeMap<ChapterModel, ArrayList<ChapterModel>> treeMap = new TreeMap<>();

        //Alle vordefinierten Kapitel werden der Map eingefügt
        //Oid -> KapitelEntity
        Map<Integer, KapitelvordefiniertEntity> entityMap = new HashMap<>();
        for (KapitelvordefiniertEntity entity : kapitelvordefiniert) {
            entityMap.put(entity.getKapitelVordefiniertOid(), entity);
        }

        for (KapitelvordefiniertEntity entity : kapitelvordefiniert) {
            ChapterModel parent = new ChapterModel(-1,"");
            if (entity.getParent() != null) {
                KapitelvordefiniertEntity parentEntity = entityMap.get(entity.getParent());
                if (parentEntity != null) {
                    parent = new ChapterModel(parentEntity.getKapitelVordefiniertOid(), parentEntity.getName());
                }
            }

            // Das Parent-Kapitel wird der TreeMap hinzugefügt, falls nicht enthalten
            if (!treeMap.containsKey(parent)) {
                treeMap.put(parent, new ArrayList<>());
            }
            // Das aktuelle Kapitel wird dem Parent-Kapitel als Kind hinzugefügt
            List<ChapterModel> children = treeMap.get(parent);
            children.add(new ChapterModel(entity.getKapitelVordefiniertOid(), entity.getName()));
        }
        return treeMap;
    }
}
