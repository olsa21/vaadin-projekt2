package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.entity.TabellenzeileEntity;

public interface TabellenzellenRepository extends JpaRepository<TabellenzeileEntity, Integer> {
}
