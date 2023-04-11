package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.vaadin.example.entity.InhaltEntity;
import org.vaadin.example.entity.KapitelEntity;

public interface KapitelRepository extends JpaRepository<KapitelEntity, Integer> {


}


