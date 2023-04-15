package org.vaadin.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "tabellenzeile", schema = "pflichtenhefter", catalog = "")
public class TabellenzeileEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "zeileOID")
    private int zeileOid;
    @Basic
    @Column(name = "zellenWert1")
    private String zellenWert1;
    @Basic
    @Column(name = "zellenWert2")
    private String zellenWert2;
    @Basic
    @Column(name = "zellenWert3")
    private String zellenWert3;
    @Basic
    @Column(name = "zellenWert4")
    private String zellenWert4;
    @Basic
    @Column(name = "zellenWert5")
    private String zellenWert5;
    @Basic
    @Column(name = "tabellenOID")
    private int tabellenOid;

    @Basic
    @Column(name = "anordnungsIndex")
    private int anordnungsIndex;

    public int getAnordnungsIndex() {
        return anordnungsIndex;
    }

    public void setAnordnungsIndex(int anordnungsIndex) {
        this.anordnungsIndex = anordnungsIndex;
    }

    public int getZeileOid() {
        return zeileOid;
    }

    public void setZeileOid(int zeileOid) {
        this.zeileOid = zeileOid;
    }

    public String getZellenWert1() {
        return zellenWert1;
    }

    public void setZellenWert1(String zellenWert1) {
        this.zellenWert1 = zellenWert1;
    }

    public String getZellenWert2() {
        return zellenWert2;
    }

    public void setZellenWert2(String zellenWert2) {
        this.zellenWert2 = zellenWert2;
    }

    public String getZellenWert3() {
        return zellenWert3;
    }

    public void setZellenWert3(String zellenWert3) {
        this.zellenWert3 = zellenWert3;
    }

    public String getZellenWert4() {
        return zellenWert4;
    }

    public void setZellenWert4(String zellenWert4) {
        this.zellenWert4 = zellenWert4;
    }

    public String getZellenWert5() {
        return zellenWert5;
    }

    public void setZellenWert5(String zellenWert5) {
        this.zellenWert5 = zellenWert5;
    }

    public int getTabellenOid() {
        return tabellenOid;
    }

    public void setTabellenOid(int tabellenOid) {
        this.tabellenOid = tabellenOid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TabellenzeileEntity that = (TabellenzeileEntity) o;

        if (zeileOid != that.zeileOid) return false;
        if (tabellenOid != that.tabellenOid) return false;
        if (zellenWert1 != null ? !zellenWert1.equals(that.zellenWert1) : that.zellenWert1 != null) return false;
        if (zellenWert2 != null ? !zellenWert2.equals(that.zellenWert2) : that.zellenWert2 != null) return false;
        if (zellenWert3 != null ? !zellenWert3.equals(that.zellenWert3) : that.zellenWert3 != null) return false;
        if (zellenWert4 != null ? !zellenWert4.equals(that.zellenWert4) : that.zellenWert4 != null) return false;
        if (zellenWert5 != null ? !zellenWert5.equals(that.zellenWert5) : that.zellenWert5 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = zeileOid;
        result = 31 * result + (zellenWert1 != null ? zellenWert1.hashCode() : 0);
        result = 31 * result + (zellenWert2 != null ? zellenWert2.hashCode() : 0);
        result = 31 * result + (zellenWert3 != null ? zellenWert3.hashCode() : 0);
        result = 31 * result + (zellenWert4 != null ? zellenWert4.hashCode() : 0);
        result = 31 * result + (zellenWert5 != null ? zellenWert5.hashCode() : 0);
        result = 31 * result + tabellenOid;
        return result;
    }
}
