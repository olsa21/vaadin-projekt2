package org.vaadin.example.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class AbteilungszuweisungEntityPK implements Serializable {
    @Column(name = "mitarbeiterOID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mitarbeiterOid;
    @Column(name = "abteilungOID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int abteilungOid;

    public int getMitarbeiterOid() {
        return mitarbeiterOid;
    }

    public void setMitarbeiterOid(int mitarbeiterOid) {
        this.mitarbeiterOid = mitarbeiterOid;
    }

    public int getAbteilungOid() {
        return abteilungOid;
    }

    public void setAbteilungOid(int abteilungOid) {
        this.abteilungOid = abteilungOid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbteilungszuweisungEntityPK that = (AbteilungszuweisungEntityPK) o;

        if (mitarbeiterOid != that.mitarbeiterOid) return false;
        if (abteilungOid != that.abteilungOid) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mitarbeiterOid;
        result = 31 * result + abteilungOid;
        return result;
    }
}
