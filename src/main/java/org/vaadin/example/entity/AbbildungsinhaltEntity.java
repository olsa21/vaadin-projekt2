package org.vaadin.example.entity;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "abbildungsinhalt", schema = "pflichtenhefter", catalog = "")
public class AbbildungsinhaltEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "abbildungOID")
    private int abbildungOid;
    @Basic
    @Column(name = "bildInhalt")
    private byte[] bildInhalt;
    @Basic
    @Column(name = "bildUnterschrift")
    private String bildUnterschrift;

    public int getAbbildungOid() {
        return abbildungOid;
    }

    public void setAbbildungOid(int abbildungOid) {
        this.abbildungOid = abbildungOid;
    }

    public byte[] getBildInhalt() {
        return bildInhalt;
    }

    public void setBildInhalt(byte[] bildInhalt) {
        this.bildInhalt = bildInhalt;
    }

    public String getBildUnterschrift() {
        return bildUnterschrift;
    }

    public void setBildUnterschrift(String bildUnterschrift) {
        this.bildUnterschrift = bildUnterschrift;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbbildungsinhaltEntity that = (AbbildungsinhaltEntity) o;

        if (abbildungOid != that.abbildungOid) return false;
        if (!Arrays.equals(bildInhalt, that.bildInhalt)) return false;
        if (bildUnterschrift != null ? !bildUnterschrift.equals(that.bildUnterschrift) : that.bildUnterschrift != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = abbildungOid;
        result = 31 * result + Arrays.hashCode(bildInhalt);
        result = 31 * result + (bildUnterschrift != null ? bildUnterschrift.hashCode() : 0);
        return result;
    }
}
