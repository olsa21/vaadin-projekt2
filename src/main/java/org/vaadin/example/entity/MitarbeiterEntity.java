package org.vaadin.example.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mitarbeiter", schema = "pflichtenhefter", catalog = "")
public class MitarbeiterEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "mitarbeiterOID")
    private int mitarbeiterOid;
    @Basic
    @Column(name = "benutzername")
    private String benutzername;
    @Basic
    @Column(name = "mail")
    private String mail;
    @Basic
    @Column(name = "passwort")
    private String passwort;
    @Basic
    @Column(name = "vorname")
    private String vorname;
    @Basic
    @Column(name = "nachname")
    private String nachname;
    @Basic
    @Column(name = "profilbild")
    private byte[] profilbild;

    @ManyToMany(mappedBy = "mitarbeiter", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<PflichtenheftEntity> pflichtenhefte = new HashSet<>();

    public Set<PflichtenheftEntity> getPflichtenhefte(){
        return pflichtenhefte;
    }

    public void setPflichtenhefte(Set<PflichtenheftEntity> pflichtenhefte){
        this.pflichtenhefte = pflichtenhefte;
    }

    public void addPflichtenheft(PflichtenheftEntity pflichtenheft){
        pflichtenhefte.add(pflichtenheft);
    }

    public int getMitarbeiterOid() {
        return mitarbeiterOid;
    }

    public void setMitarbeiterOid(int mitarbeiterOid) {
        this.mitarbeiterOid = mitarbeiterOid;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public byte[] getProfilbild() {
        return profilbild;
    }

    public void setProfilbild(byte[] profilbild) {
        this.profilbild = profilbild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MitarbeiterEntity that = (MitarbeiterEntity) o;

        if (mitarbeiterOid != that.mitarbeiterOid) return false;
        if (benutzername != null ? !benutzername.equals(that.benutzername) : that.benutzername != null) return false;
        if (mail != null ? !mail.equals(that.mail) : that.mail != null) return false;
        if (passwort != null ? !passwort.equals(that.passwort) : that.passwort != null) return false;
        if (vorname != null ? !vorname.equals(that.vorname) : that.vorname != null) return false;
        if (nachname != null ? !nachname.equals(that.nachname) : that.nachname != null) return false;
        if (!Arrays.equals(profilbild, that.profilbild)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mitarbeiterOid;
        result = 31 * result + (benutzername != null ? benutzername.hashCode() : 0);
        result = 31 * result + (mail != null ? mail.hashCode() : 0);
        result = 31 * result + (passwort != null ? passwort.hashCode() : 0);
        result = 31 * result + (vorname != null ? vorname.hashCode() : 0);
        result = 31 * result + (nachname != null ? nachname.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(profilbild);
        return result;
    }
}
