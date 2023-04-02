package org.vaadin.example.service;


import org.springframework.stereotype.Service;
import org.vaadin.example.entity.*;
import org.vaadin.example.repository.AbteilungzuweisungRepository;
import org.vaadin.example.repository.MitarbeiterRepository;
import org.vaadin.example.repository.PflichtenheftRepository;
import org.vaadin.example.repository.ProjektZuweisungRepository;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpecificationsService {

    private final MitarbeiterRepository benutzerRepository;
    private final AbteilungzuweisungRepository abteilungzuweisungRepository;

    private final ProjektZuweisungRepository projektZuweisungRepository;
    private final PflichtenheftRepository pflichtenheftRepository;


    public SpecificationsService(MitarbeiterRepository benutzerRepository, AbteilungzuweisungRepository abteilungzuweisungRepository, ProjektZuweisungRepository projektZuweisungRepository, PflichtenheftRepository pflichtenheftRepository){
        this.benutzerRepository = benutzerRepository;
        this.abteilungzuweisungRepository = abteilungzuweisungRepository;
        this.projektZuweisungRepository = projektZuweisungRepository;
        this.pflichtenheftRepository = pflichtenheftRepository;
    }

    public List<MitarbeiterEntity> findAllUser(String filterText){
        if (filterText == null || filterText.isEmpty()){
            return benutzerRepository.findAll();
        }else{
            return benutzerRepository.search(filterText);
        }
    }

    public MitarbeiterEntity findSpecificUser(String username){
        return benutzerRepository.readUserWhere(username, "%");
    }

    public void addMitarbeiter(MitarbeiterEntity benutzer){
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

    @Transactional
    public void deleteAbteilungszuweisung(int benutzerOID){
        abteilungzuweisungRepository.deleteZuweisung(benutzerOID);
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

    public List<AbteilungEntity> getAbteilungList() {
        try {
            return benutzerRepository.readAbteilungen();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<AbteilungEntity> getAbteilungListWhere(int mitarbeiterOID) {
        //Die Schnittmenge aller Abteilungen und der Abteilungen des Benutzers wird zurückgegeben
        List<AbteilungEntity> abteilungen = getAbteilungList();

        List<AbteilungEntity> rueckgabe = new ArrayList<>();

        List<AbteilungszuweisungEntity> abfrage = abteilungzuweisungRepository.readAbteilungenWhere(mitarbeiterOID);

        for (AbteilungszuweisungEntity abteilungszuweisungEntity : abfrage) {
            for (AbteilungEntity abteilungEntity : abteilungen) {
                if (abteilungEntity.getAbteilungOid() == abteilungszuweisungEntity.getAbteilungOid()){
                    rueckgabe.add(abteilungEntity);
                }
            }
        }

        return rueckgabe;
    }

    @Transactional
    public void saveAbteilungZuweisungen(int mitarbeiterOid, List<AbteilungEntity> ausgewaehlteAbteilungen) {
        abteilungzuweisungRepository.deleteZuweisung(mitarbeiterOid);
        ausgewaehlteAbteilungen.forEach(item -> {
            abteilungzuweisungRepository.saveRaw(mitarbeiterOid, item.getAbteilungOid());
        });
    }

    @Transactional
    public void addPflichtenheft(int mitarbeiterOid, String titel, String beschreibung, LocalDate frist, String repoLink, int oeffentlich) {
        PflichtenheftEntity pflichtenheftEntity = new PflichtenheftEntity();
        pflichtenheftEntity.setTitel(titel);
        pflichtenheftEntity.setBeschreibung(beschreibung);
        pflichtenheftEntity.setFrist(frist.toString());
        pflichtenheftEntity.setRepositoryLink(repoLink);
        pflichtenheftEntity.setOeffentlich((byte) oeffentlich);

        PflichtenheftEntity entity = pflichtenheftRepository.save(pflichtenheftEntity);

        addProjektZuweisung(mitarbeiterOid, entity.getProjektOid());
    }

    @Transactional
    public void addProjektZuweisung(int mitarbeiterOid, int projektOid) {
        projektZuweisungRepository.saveRaw(mitarbeiterOid, projektOid);
    }
}
