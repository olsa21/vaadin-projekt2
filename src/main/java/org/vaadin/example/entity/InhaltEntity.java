package org.vaadin.example.entity;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "inhalt", schema = "pflichtenhefter", catalog = "")
public class InhaltEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "inhaltOID")
    private int inhaltOid;
    @Basic
     @Column(name = "kapitelOID")
     private int kapitelOid;
    @Basic
    @Column(name = "anordnungIndex")
    private Integer anordnungIndex;
    @Basic
    @Column(name = "textInhalt")
    private String textInhalt;
    @Basic
    @Column(name = "bildInhalt")
    private byte[] bildInhalt;

    /*@ManyToOne
    @JoinColumn(name="kapitelOID", nullable = false)
    private KapitelEntity kapitel;*/

    /*public KapitelEntity getKapitel() {
        return kapitel;
    }

    public void setKapitel(KapitelEntity kapitel) {
        this.kapitel = kapitel;
    }*/

    public int getKapitelOid() {
        return kapitelOid;
    }

    public void setKapitelOid(int kapitelOid) {
        this.kapitelOid = kapitelOid;
    }

    public int getInhaltOid() {
        return inhaltOid;
    }

    public void setInhaltOid(int inhaltOid) {
        this.inhaltOid = inhaltOid;
    }

    public Integer getAnordnungIndex() {
        return anordnungIndex;
    }

    public void setAnordnungIndex(Integer anordnungIndex) {
        this.anordnungIndex = anordnungIndex;
    }

    public String getTextInhalt() {
        return textInhalt;
    }

    public void setTextInhalt(String textInhalt) {
        this.textInhalt = textInhalt;
    }

    public byte[] getBildInhalt() {
        return bildInhalt;
    }

    public void setBildInhalt(byte[] bildInhalt) {
        this.bildInhalt = bildInhalt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InhaltEntity that = (InhaltEntity) o;

        if (inhaltOid != that.inhaltOid) return false;
        //if (kapitelOid != that.kapitelOid) return false;
        if (anordnungIndex != null ? !anordnungIndex.equals(that.anordnungIndex) : that.anordnungIndex != null)
            return false;
        if (textInhalt != null ? !textInhalt.equals(that.textInhalt) : that.textInhalt != null) return false;
        if (!Arrays.equals(bildInhalt, that.bildInhalt)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = inhaltOid;
        //result = 31 * result + kapitelOid;
        result = 31 * result + (anordnungIndex != null ? anordnungIndex.hashCode() : 0);
        result = 31 * result + (textInhalt != null ? textInhalt.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(bildInhalt);
        return result;
    }
}
