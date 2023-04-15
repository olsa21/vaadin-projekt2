package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.entity.TabellenEntity;

public interface TabellenRepository extends JpaRepository<TabellenEntity, Integer> {
}
