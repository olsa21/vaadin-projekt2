package org.vaadin.example.service;


import org.springframework.stereotype.Service;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.repository.MitarbeiterRepository;

import java.util.List;

@Service
public class SpecificationsService {

    private final MitarbeiterRepository benutzerRepository;

    public SpecificationsService(MitarbeiterRepository benutzerRepository){
        this.benutzerRepository = benutzerRepository;
    }

    public List<MitarbeiterEntity> findAllUser(String filterText){
        if (filterText == null || filterText.isEmpty()){
            return benutzerRepository.findAll();
        }else{
            return benutzerRepository.search(filterText);
        }
    }

    public void addBenutzer(MitarbeiterEntity benutzer){
        if (benutzer == null){
            System.err.println("Benutzer darf nicht null sein!");
        }
        benutzerRepository.save(benutzer);
    }

    public long countBenutzer(){
        return benutzerRepository.count();
    }

    public void deleteBenutzer(MitarbeiterEntity benutzer){
        benutzerRepository.delete(benutzer);
    }

    public void saveBenutzer(MitarbeiterEntity benutzer){
        if (benutzer == null){
            System.err.println("Benutzer muss gültig sein!");
        }
        benutzerRepository.save(benutzer);
    }

}
