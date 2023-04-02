package org.vaadin.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "abteilungszuweisung", schema = "pflichtenhefter", catalog = "")
@IdClass(AbteilungszuweisungEntityPK.class)
public class AbteilungszuweisungEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "mitarbeiterOID")
    private int mitarbeiterOid;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "abteilungOID")
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

        AbteilungszuweisungEntity that = (AbteilungszuweisungEntity) o;

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
