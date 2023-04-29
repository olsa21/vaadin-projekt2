package org.vaadin.example.model;

import java.util.Objects;

/**
 * Die Klasse dient der Abbildung eines Kapitels in der Datenbank. Die Kapitelstruktur wird zu Beginn aus der Datenbank
 * geladen. Damit die Verweise korrekt sind, wird bei Erstellung der Kapitelstruktur für baumartige View-Strukturen
 * ein Kapitel-Modell verwendet, damit das Kapitel korrekt angezeigt wird und man gleichzeitig einen korrekten Verweis hat.
 */
public class ChapterModel implements Comparable<ChapterModel> {
    private int chapterOid;
    private String chapterName;

    /**
     * Konstruktor, welcher das Kapitel-Modell mit den übergebenen Werten initialisiert.
     * @param chapterOid die OID des Kapitels
     * @param chapterName der Name des Kapitels
     */
    public ChapterModel(int chapterOid, String chapterName) {
        //if (chapterName == null || chapterName.isEmpty()){
          //  throw new IllegalArgumentException("Der Kapitelname muss einen gültigen nicht leerer Wert beinhalten!");
        //}
        this.chapterOid = chapterOid;
        this.chapterName = chapterName;
    }

    /**
     * Getter-Methode, welche die OID des Kapitels zurückgibt.
     * @return die OID des Kapitels
     */
    public int getChapterOid() {
        return chapterOid;
    }

    /**
     * Setter-Methode, welche die OID des Kapitels setzt.
     * @param chapterOid die OID des Kapitels
     */
    public void setChapterOid(int chapterOid) {
        this.chapterOid = chapterOid;
    }

    /**
     * Getter-Methode, welche den Namen des Kapitels zurückgibt.
     * @return der Name des Kapitels
     */
    public String getChapterName() {
        return chapterName;
    }

    /**
     * Setter-Methode, welche den Namen des Kapitels setzt.
     * @param chapterName der Name des Kapitels
     */
    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChapterModel that = (ChapterModel) o;
        return chapterOid == that.chapterOid &&
                Objects.equals(chapterName, that.chapterName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chapterOid, chapterName);
    }

    @Override
    public int compareTo(ChapterModel o) {
        return this.getChapterName().compareTo(o.chapterName);
    }
}
