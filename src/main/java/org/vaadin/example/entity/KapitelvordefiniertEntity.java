package org.vaadin.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "kapitelvordefiniert", schema = "pflichtenhefter", catalog = "")
public class KapitelvordefiniertEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "kapitelVordefiniertOID")
    private int kapitelVordefiniertOid;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "parent")
    private Integer parent;

    public int getKapitelVordefiniertOid() {
        return kapitelVordefiniertOid;
    }

    public void setKapitelVordefiniertOid(int kapitelVordefiniertOid) {
        this.kapitelVordefiniertOid = kapitelVordefiniertOid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KapitelvordefiniertEntity that = (KapitelvordefiniertEntity) o;

        if (kapitelVordefiniertOid != that.kapitelVordefiniertOid) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = kapitelVordefiniertOid;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }
}
