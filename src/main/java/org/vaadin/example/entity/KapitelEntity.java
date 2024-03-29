package org.vaadin.example.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "kapitel", schema = "pflichtenhefter", catalog = "")
public class KapitelEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "kapitelOID")
    private int kapitelOid;
    //@Basic
    //@Column(name = "projektOID")
    //private int projektOid;
    @ManyToOne
    @JoinColumn(name = "kapitelVordefiniertOID", referencedColumnName = "kapitelVordefiniertOID")
    private KapitelvordefiniertEntity kapitelVordefiniert;

    @OneToMany(mappedBy = "kapitelOid", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<InhaltEntity> inhalte = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="projektOID", nullable = false)
    private PflichtenheftEntity projekt;

    public Set<InhaltEntity> getInhalte() {
        return inhalte;
    }

    public void setInhalte(Set<InhaltEntity> inhalte) {
        this.inhalte = inhalte;
    }

    public PflichtenheftEntity getProjekt() {
        return projekt;
    }

    public void setProjekt(PflichtenheftEntity projekt) {
        this.projekt = projekt;
    }

    /*@OneToMany(mappedBy = "kapitel")
    private Set<InhaltEntity> inhalte = new HashSet<>();

    public Set<InhaltEntity> getInhalte() {
        return inhalte;
    }

    public void setInhalte(Set<InhaltEntity> inhalte) {
        this.inhalte = inhalte;
    }*/

    public int getKapitelOid() {
        return kapitelOid;
    }

    public void setKapitelOid(int kapitelOid) {
        this.kapitelOid = kapitelOid;
    }

    /**public int getProjektOid() {
     return projektOid;
     }

     public void setProjektOid(int projektOid) {
     this.projektOid = projektOid;
     }*/

    public KapitelvordefiniertEntity getKapitelVordefiniert() {
        return kapitelVordefiniert;
    }

    public void setKapitelVordefiniert(KapitelvordefiniertEntity kapitelVordefiniert) {
        this.kapitelVordefiniert = kapitelVordefiniert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KapitelEntity that = (KapitelEntity) o;

        if (kapitelOid != that.kapitelOid) return false;
        if (kapitelVordefiniert != that.kapitelVordefiniert) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = kapitelOid;
        //result = 31 * result + projektOid;
        result = 31 * result + kapitelVordefiniert.hashCode();
        return result;
    }
}
