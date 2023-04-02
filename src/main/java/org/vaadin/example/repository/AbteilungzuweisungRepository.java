package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.vaadin.example.entity.AbteilungszuweisungEntity;

import java.util.List;

public interface AbteilungzuweisungRepository extends JpaRepository<AbteilungszuweisungEntity, Integer> {
    @Query("select c from AbteilungszuweisungEntity c where c.mitarbeiterOid = ?1")
    public List<AbteilungszuweisungEntity> readAbteilungenWhere(int username);

    @Modifying
    @Query("delete from AbteilungszuweisungEntity c where c.mitarbeiterOid = ?1")
    public void deleteZuweisung(int mitarbeiterOid);

    @Modifying
    @Query(value = "insert into abteilungszuweisung (mitarbeiterOid, abteilungOid) values (?1, ?2)", nativeQuery = true)
    public void saveRaw(int mitarbeiterOid, int abteilungOid);

    //@Query("insert into AbteilungszuweisungEntity (abteilungOid, mitarbeiterOid) values (?1, (select b.mitarbeiterOid from MitarbeiterEntity b where b.benutzername = ?2))")
    //public void addZuweisung(String benutzername, String abteilungId);
}
