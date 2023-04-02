package org.vaadin.example.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class ProjektzuweisungEntityPK implements Serializable {
    @Column(name = "mitarbeiterOID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mitarbeiterOid;
    @Column(name = "projektOID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projektOid;

    public int getMitarbeiterOid() {
        return mitarbeiterOid;
    }

    public void setMitarbeiterOid(int mitarbeiterOid) {
        this.mitarbeiterOid = mitarbeiterOid;
    }

    public int getProjektOid() {
        return projektOid;
    }

    public void setProjektOid(int projektOid) {
        this.projektOid = projektOid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjektzuweisungEntityPK that = (ProjektzuweisungEntityPK) o;

        if (mitarbeiterOid != that.mitarbeiterOid) return false;
        if (projektOid != that.projektOid) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mitarbeiterOid;
        result = 31 * result + projektOid;
        return result;
    }
}
