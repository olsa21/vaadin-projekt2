package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.entity.AbbildungsinhaltEntity;

public interface AbbildungInhaltRepository extends JpaRepository<AbbildungsinhaltEntity, Long> {
}
