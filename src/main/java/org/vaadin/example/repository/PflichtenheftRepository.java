package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vaadin.example.entity.AbteilungEntity;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.entity.PflichtenheftEntity;

import java.util.ArrayList;
import java.util.List;

public interface PflichtenheftRepository extends JpaRepository<PflichtenheftEntity, Integer> {
    @Query("select c from " +
            "PflichtenheftEntity c inner join ProjektzuweisungEntity p on p.projektOid = c.projektOid inner join MitarbeiterEntity m on m.mitarbeiterOid = p.mitarbeiterOid where m.mitarbeiterOid = ?1")
    ArrayList<PflichtenheftEntity> readPflichtenheftWhere(int mitarbeiterOID);
    @Query("select m from MitarbeiterEntity m where m.mitarbeiterOid = (select p.verantwortlicher from PflichtenheftEntity p where p.projektOid = ?1)")
    MitarbeiterEntity readVerantwortlicher(int projektOid);

    @Query("select c from PflichtenheftEntity c where c.oeffentlich = 1")
    List<PflichtenheftEntity> findOpenProjects();
}
