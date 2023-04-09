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

    public ChapterModel(int chapterOid, String chapterName) {
        //if (chapterName == null || chapterName.isEmpty()){
          //  throw new IllegalArgumentException("Der Kapitelname muss einen gültigen nicht leerer Wert beinhalten!");
        //}
        this.chapterOid = chapterOid;
        this.chapterName = chapterName;
    }
    public ChapterModel(){}

    public int getChapterOid() {
        return chapterOid;
    }

    public void setChapterOid(int chapterOid) {
        this.chapterOid = chapterOid;
    }

    public String getChapterName() {
        return chapterName;
    }

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
