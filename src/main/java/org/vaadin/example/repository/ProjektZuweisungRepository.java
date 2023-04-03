package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.vaadin.example.entity.AbteilungEntity;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.entity.ProjektzuweisungEntity;

import java.util.List;

public interface ProjektZuweisungRepository extends JpaRepository<ProjektzuweisungEntity, Integer> {

    @Modifying
    @Query(value = "insert into projektzuweisung (mitarbeiterOID, projektOID) values (?1, ?2)", nativeQuery = true)
    void saveRaw(int mitarbeiterOid, int projektOid);

    @Query("select p from ProjektzuweisungEntity p where p.mitarbeiterOid = (select m.mitarbeiterOid from MitarbeiterEntity m where m.benutzername = ?1) and p.projektOid = ?2")
    ProjektzuweisungEntity readProjektZuweisungWhere(String username, int projektOid);

    @Query("select m from MitarbeiterEntity m where m.mitarbeiterOid IN (select p.mitarbeiterOid from ProjektzuweisungEntity p where p.projektOid = ?1)")
    List<MitarbeiterEntity> readMitglieder(int projektOid);

    @Query("select p.mitarbeiterOid from ProjektzuweisungEntity p where p.projektOid = ?1")
    List<Integer> readMitgliederOids(int projektOid);

    @Modifying
    @Query(value = "insert into pro", nativeQuery = true)
    void addMember(String username, int projektOid);
}
