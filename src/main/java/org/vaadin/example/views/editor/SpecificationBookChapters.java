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
     * @param service
     * @return
     */
    public static SpecificationBookChapters getInstance(SpecificationsService service){
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
        TreeMap<ChapterModel, ArrayList<ChapterModel>> treeMap = new TreeMap<>();

        Map<Integer, KapitelvordefiniertEntity> entityMap = new HashMap<>();
        for (KapitelvordefiniertEntity entity : kapitelvordefiniert) {
            entityMap.put(entity.getKapitelVordefiniertOid(), entity);
        }

        for (KapitelvordefiniertEntity entity : kapitelvordefiniert) {
            //String parentName = "";
            ChapterModel parent = new ChapterModel(-1,"");
            if (entity.getParent() != null) {
                KapitelvordefiniertEntity parentEntity = entityMap.get(entity.getParent());
                if (parentEntity != null) {
                    //parentName = parentEntity.getName();
                    parent = new ChapterModel(parentEntity.getKapitelVordefiniertOid(), parentEntity.getName());
                }
            }

            //if (!treeMap.containsKey(parentName)) {
            if (!treeMap.containsKey(parent)) {
                //treeMap.put(parentName, new ArrayList<>());
                treeMap.put(parent, new ArrayList<>());
            }
            //List<String> children = treeMap.get(parentName);
            List<ChapterModel> children = treeMap.get(parent);
            //children.add(entity.getName());
            children.add(new ChapterModel(entity.getKapitelVordefiniertOid(), entity.getName()));
        }
        return treeMap;
    }
}
