package org.vaadin.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "pflichtenheft", schema = "pflichtenhefter", catalog = "")
public class PflichtenheftEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "projektOID")
    private int projektOid;
    @Basic
    @Column(name = "titel")
    private String titel;
    @Basic
    @Column(name = "beschreibung")
    private String beschreibung;
    @Basic
    @Column(name = "oeffentlich")
    private Byte oeffentlich;
    @Basic
    @Column(name = "frist")
    private String frist;
    @Basic
    @Column(name = "repositoryLink")
    private String repositoryLink;

    public int getProjektOid() {
        return projektOid;
    }

    public void setProjektOid(int projektOid) {
        this.projektOid = projektOid;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Byte getOeffentlich() {
        return oeffentlich;
    }

    public void setOeffentlich(Byte oeffentlich) {
        this.oeffentlich = oeffentlich;
    }

    public String getFrist() {
        return frist;
    }

    public void setFrist(String frist) {
        this.frist = frist;
    }

    public String getRepositoryLink() {
        return repositoryLink;
    }

    public void setRepositoryLink(String repositoryLink) {
        this.repositoryLink = repositoryLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PflichtenheftEntity that = (PflichtenheftEntity) o;

        if (projektOid != that.projektOid) return false;
        if (titel != null ? !titel.equals(that.titel) : that.titel != null) return false;
        if (beschreibung != null ? !beschreibung.equals(that.beschreibung) : that.beschreibung != null) return false;
        if (oeffentlich != null ? !oeffentlich.equals(that.oeffentlich) : that.oeffentlich != null) return false;
        if (frist != null ? !frist.equals(that.frist) : that.frist != null) return false;
        if (repositoryLink != null ? !repositoryLink.equals(that.repositoryLink) : that.repositoryLink != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = projektOid;
        result = 31 * result + (titel != null ? titel.hashCode() : 0);
        result = 31 * result + (beschreibung != null ? beschreibung.hashCode() : 0);
        result = 31 * result + (oeffentlich != null ? oeffentlich.hashCode() : 0);
        result = 31 * result + (frist != null ? frist.hashCode() : 0);
        result = 31 * result + (repositoryLink != null ? repositoryLink.hashCode() : 0);
        return result;
    }
}
