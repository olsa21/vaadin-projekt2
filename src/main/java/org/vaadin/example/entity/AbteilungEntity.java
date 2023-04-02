package org.vaadin.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "abteilung", schema = "pflichtenhefter", catalog = "")
public class AbteilungEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "abteilungOID")
    private int abteilungOid;
    @Basic
    @Column(name = "name")
    private String name;

    public int getAbteilungOid() {
        return abteilungOid;
    }

    public void setAbteilungOid(int abteilungOid) {
        this.abteilungOid = abteilungOid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbteilungEntity that = (AbteilungEntity) o;

        if (abteilungOid != that.abteilungOid) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = abteilungOid;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
