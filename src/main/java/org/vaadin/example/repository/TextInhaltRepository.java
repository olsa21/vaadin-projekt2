package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.entity.TextinhaltEntity;

public interface TextInhaltRepository extends JpaRepository<TextinhaltEntity, Long> {
}
