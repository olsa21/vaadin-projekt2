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
}
