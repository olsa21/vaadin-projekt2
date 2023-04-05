package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vaadin.example.entity.AbteilungEntity;
import org.vaadin.example.entity.MitarbeiterEntity;

import java.util.List;

public interface MitarbeiterRepository extends JpaRepository<MitarbeiterEntity, Integer> {

    @Query("select c from MitarbeiterEntity c")
    public List<MitarbeiterEntity> search(String filterText) ;

    @Query("select c from MitarbeiterEntity c where c.benutzername = ?1 and c.passwort like ?2")
    public MitarbeiterEntity readUserWhere(String username, String passwordHash);

    //TODO In separates Repository auslagern
    @Query("select c from AbteilungEntity c")
    public List<AbteilungEntity> readAbteilungen();

    @Query("select c from MitarbeiterEntity c where c.mitarbeiterOid = ?1")
    public MitarbeiterEntity findByOid(Integer id);

}
