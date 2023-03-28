package org.vaadin.example.service;


import org.springframework.stereotype.Service;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.repository.MitarbeiterRepository;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public boolean credentialsCorrect(String username, String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            String sha256 = DatatypeConverter.printHexBinary(digest).toLowerCase();

            MitarbeiterEntity benutzer = benutzerRepository.readUserWhere(username, sha256);
            return benutzer.getBenutzername().equals(username) && benutzer.getPasswort().equals(sha256);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Fehler beim Hashen des Passworts!");
        } catch (NullPointerException e){
            System.err.println("Benutzername oder Passwort falsch!");
        }
        return false;
    }
}
