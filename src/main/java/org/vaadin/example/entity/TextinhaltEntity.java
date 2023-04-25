package org.vaadin.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "textinhalt", schema = "pflichtenhefter", catalog = "")
public class TextinhaltEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "textOID")
    private int textOid;
    @Basic
    @Column(name = "textInhalt")
    private String textInhalt;

    public int getTextOid() {
        return textOid;
    }

    public void setTextOid(int textOid) {
        this.textOid = textOid;
    }

    public String getTextInhalt() {
        return textInhalt;
    }

    public void setTextInhalt(String textInhalt) {
        this.textInhalt = textInhalt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextinhaltEntity that = (TextinhaltEntity) o;

        if (textOid != that.textOid) return false;
        if (textInhalt != null ? !textInhalt.equals(that.textInhalt) : that.textInhalt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = textOid;
        result = 31 * result + (textInhalt != null ? textInhalt.hashCode() : 0);
        return result;
    }
}
