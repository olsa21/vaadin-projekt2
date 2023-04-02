package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vaadin.example.entity.AbteilungEntity;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.entity.PflichtenheftEntity;

import java.util.List;

public interface PflichtenheftRepository extends JpaRepository<PflichtenheftEntity, Integer> {

}
