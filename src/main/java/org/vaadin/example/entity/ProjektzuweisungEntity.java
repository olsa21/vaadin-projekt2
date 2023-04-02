package org.vaadin.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "projektzuweisung", schema = "pflichtenhefter", catalog = "")
@IdClass(ProjektzuweisungEntityPK.class)
public class ProjektzuweisungEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "mitarbeiterOID")
    private int mitarbeiterOid;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "projektOID")
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

        ProjektzuweisungEntity that = (ProjektzuweisungEntity) o;

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
