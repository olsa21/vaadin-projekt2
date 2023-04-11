package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.vaadin.example.entity.InhaltEntity;


public interface InhaltRepository extends JpaRepository<InhaltEntity, Integer> {

    @Modifying
    @Query("delete from InhaltEntity c where c.inhaltOid = ?1")
    public void deleteInhalt(int inhaltOid);

}
