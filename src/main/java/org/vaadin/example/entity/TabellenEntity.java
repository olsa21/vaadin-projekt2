package org.vaadin.example.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tabellen", schema = "pflichtenhefter", catalog = "")
public class TabellenEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "tabellenOID")
    private int tabellenOid;
    @Basic
    @Column(name = "spaltenCaption1")
    private String spaltenCaption1;
    @Basic
    @Column(name = "spaltenCaption2")
    private String spaltenCaption2;
    @Basic
    @Column(name = "spaltenCaption3")
    private String spaltenCaption3;
    @Basic
    @Column(name = "spaltenCaption4")
    private String spaltenCaption4;
    @Basic
    @Column(name = "spaltenCaption5")
    private String spaltenCaption5;

    @Basic
    @Column(name = "tabellenUnterschrift")
    private String tabellenUnterschrift;

    public String getTabellenUnterschrift() {
        return tabellenUnterschrift;
    }

    public void setTabellenUnterschrift(String tabellenUnterschrift) {
        this.tabellenUnterschrift = tabellenUnterschrift;
    }

    @OneToMany(mappedBy = "tabellenOid", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TabellenzeileEntity> zellen = new HashSet<>();

    public Set<TabellenzeileEntity> getZellen() {
        return zellen;
    }

    public void setZellen(Set<TabellenzeileEntity> zellen) {
        this.zellen = zellen;
    }

    public int getTabellenOid() {
        return tabellenOid;
    }

    public void setTabellenOid(int tabellenOid) {
        this.tabellenOid = tabellenOid;
    }

    public String getSpaltenCaption1() {
        return spaltenCaption1;
    }

    public void setSpaltenCaption1(String spaltenCaption1) {
        this.spaltenCaption1 = spaltenCaption1;
    }

    public String getSpaltenCaption2() {
        return spaltenCaption2;
    }

    public void setSpaltenCaption2(String spaltenCaption2) {
        this.spaltenCaption2 = spaltenCaption2;
    }

    public String getSpaltenCaption3() {
        return spaltenCaption3;
    }

    public void setSpaltenCaption3(String spaltenCaption3) {
        this.spaltenCaption3 = spaltenCaption3;
    }

    public String getSpaltenCaption4() {
        return spaltenCaption4;
    }

    public void setSpaltenCaption4(String spaltenCaption4) {
        this.spaltenCaption4 = spaltenCaption4;
    }

    public String getSpaltenCaption5() {
        return spaltenCaption5;
    }

    public void setSpaltenCaption5(String spaltenCaption5) {
        this.spaltenCaption5 = spaltenCaption5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TabellenEntity that = (TabellenEntity) o;

        if (tabellenOid != that.tabellenOid) return false;
        if (spaltenCaption1 != null ? !spaltenCaption1.equals(that.spaltenCaption1) : that.spaltenCaption1 != null)
            return false;
        if (spaltenCaption2 != null ? !spaltenCaption2.equals(that.spaltenCaption2) : that.spaltenCaption2 != null)
            return false;
        if (spaltenCaption3 != null ? !spaltenCaption3.equals(that.spaltenCaption3) : that.spaltenCaption3 != null)
            return false;
        if (spaltenCaption4 != null ? !spaltenCaption4.equals(that.spaltenCaption4) : that.spaltenCaption4 != null)
            return false;
        if (spaltenCaption5 != null ? !spaltenCaption5.equals(that.spaltenCaption5) : that.spaltenCaption5 != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tabellenOid;
        result = 31 * result + (spaltenCaption1 != null ? spaltenCaption1.hashCode() : 0);
        result = 31 * result + (spaltenCaption2 != null ? spaltenCaption2.hashCode() : 0);
        result = 31 * result + (spaltenCaption3 != null ? spaltenCaption3.hashCode() : 0);
        result = 31 * result + (spaltenCaption4 != null ? spaltenCaption4.hashCode() : 0);
        result = 31 * result + (spaltenCaption5 != null ? spaltenCaption5.hashCode() : 0);
        return result;
    }
}
