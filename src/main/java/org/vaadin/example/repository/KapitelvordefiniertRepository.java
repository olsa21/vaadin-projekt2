package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.entity.KapitelvordefiniertEntity;
import org.vaadin.example.entity.MitarbeiterEntity;

public interface KapitelvordefiniertRepository extends JpaRepository<KapitelvordefiniertEntity, Integer> {

}
