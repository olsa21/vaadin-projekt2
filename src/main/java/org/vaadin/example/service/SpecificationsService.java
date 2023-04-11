package org.vaadin.example.service;


import org.springframework.stereotype.Service;
import org.vaadin.example.entity.*;
import org.vaadin.example.listener.PflichtenheftBroadcaster;
import org.vaadin.example.repository.*;
import org.vaadin.example.utility.PasswordEncoder;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class SpecificationsService {

    private final MitarbeiterRepository benutzerRepository;
    private final AbteilungzuweisungRepository abteilungzuweisungRepository;
    private final ProjektZuweisungRepository projektZuweisungRepository;
    private final PflichtenheftRepository pflichtenheftRepository;
    private final KapitelvordefiniertRepository kapitelvordefiniertRepository;

    private final KapitelRepository kapitelRepository;
    private final InhaltRepository inhaltRepository;


    public SpecificationsService(MitarbeiterRepository benutzerRepository, AbteilungzuweisungRepository abteilungzuweisungRepository, ProjektZuweisungRepository projektZuweisungRepository, PflichtenheftRepository pflichtenheftRepository, KapitelvordefiniertRepository kapitelvordefiniertRepository, KapitelRepository kapitelRepository, InhaltRepository inhaltRepository){
        this.benutzerRepository = benutzerRepository;
        this.abteilungzuweisungRepository = abteilungzuweisungRepository;
        this.projektZuweisungRepository = projektZuweisungRepository;
        this.pflichtenheftRepository = pflichtenheftRepository;
        this.kapitelvordefiniertRepository = kapitelvordefiniertRepository;
        this.kapitelRepository = kapitelRepository;
        this.inhaltRepository = inhaltRepository;
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

    public MitarbeiterEntity findSpecificUserById(Integer id){
        if (id == null || id < 0){
            throw new IllegalArgumentException("Die OID muss einen gültigen nicht negativen Wert beinhalten!");
        }
        return benutzerRepository.findByOid(id);
    }

    public boolean authenticateUser(String username, String password){
        if (username == null || username.isEmpty()){
            System.err.println("Benutzername darf nicht leer sein!");
            return false;
        }
        if (password == null || password.isEmpty()){
            System.err.println("Passwort darf nicht leer sein!");
            return false;
        }

        try {
            String hashedSHA256Password = PasswordEncoder.hashPassword(password);
            MitarbeiterEntity benutzer = benutzerRepository.readUserWhere(username, hashedSHA256Password);
            return benutzer != null;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void addMitarbeiter(MitarbeiterEntity benutzer){
        if (benutzer == null){
            System.err.println("Benutzer darf nicht null sein!");
        }
        benutzerRepository.save(benutzer);
    }

    public ArrayList<PflichtenheftEntity> getPflichtenheftListWhere(int mitarbeiterOID){
        return pflichtenheftRepository.readPflichtenheftWhere(mitarbeiterOID);
    }

    public void saveKapitel(KapitelEntity kapitel){
        if (kapitel == null){
            System.err.println("Kapitel darf nicht null sein!");
        }
        kapitelRepository.save(kapitel);
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

    public void addPflichtenheft(PflichtenheftEntity pflichtenheftEntity) {
        pflichtenheftRepository.save(pflichtenheftEntity);
    }

    public void addPflichtenheft(MitarbeiterEntity mitarbeiter, String titel, String beschreibung, LocalDate frist, String repoLink, int oeffentlich) {
        PflichtenheftEntity pflichtenheftEntity = new PflichtenheftEntity();
        pflichtenheftEntity.setTitel(titel);
        pflichtenheftEntity.setBeschreibung(beschreibung);
        pflichtenheftEntity.setFrist(frist.toString());
        pflichtenheftEntity.setRepositoryLink(repoLink);
        pflichtenheftEntity.setOeffentlich((byte) oeffentlich);
        pflichtenheftEntity.setVerantwortlicher(mitarbeiter.getMitarbeiterOid());

        PflichtenheftEntity test =pflichtenheftRepository.save(pflichtenheftEntity);
        test.addMitarbeiter(mitarbeiter);
        pflichtenheftRepository.save(test);
    }

    //In Zukunft entfernen
    @Transactional
    public void addProjektZuweisung(String username, int projektOid) {
        int mitarbeiterOid = findSpecificUser(username).getMitarbeiterOid();
        addProjektZuweisung(mitarbeiterOid, projektOid);
    }
    @Transactional
    public void addProjektZuweisung(int mitarbeiterOid, int projektOid) {
        projektZuweisungRepository.saveRaw(mitarbeiterOid, projektOid);
    }

    public boolean isMember(String username, PflichtenheftEntity pflichtenheft) {
        return projektZuweisungRepository.readProjektZuweisungWhere(username, pflichtenheft.getProjektOid()) != null;
    }

    public List<MitarbeiterEntity> readMitglieder(PflichtenheftEntity pflichtenheft) {
        List<MitarbeiterEntity> mitglieder = projektZuweisungRepository.readMitglieder(pflichtenheft.getProjektOid());
        return mitglieder;
    }

    public MitarbeiterEntity readVerantwortlicher(PflichtenheftEntity pflichtenheft) {
        return pflichtenheftRepository.readVerantwortlicher(pflichtenheft.getProjektOid());
    }

    public void savePflichtenheft(PflichtenheftEntity pflichtenheft) {
        pflichtenheftRepository.save(pflichtenheft);
    }

    public void saveInhalt(InhaltEntity inhalt) {
        inhaltRepository.save(inhalt);
    }

    public void deleteInhalt(InhaltEntity inhalt) {
        inhaltRepository.delete(inhalt);
    }

    public PflichtenheftEntity readPflichtenheft(int projektOid) {
        Optional<PflichtenheftEntity> result = pflichtenheftRepository.findById(projektOid);
        return result.orElse(null);
    }

    public List<PflichtenheftEntity> findOpenProjects() {
        return pflichtenheftRepository.findOpenProjects();
    }

    //Methode wird nur verwendet um Broadcasts bei Änderungden der Sichtbarkeit zu senden
    public void setOeffentlich(PflichtenheftEntity pflichtenheft, int oeffentlich) {
        System.out.println("Öffentlichkeit wird geändert: =="  + oeffentlich);
        pflichtenheft.setOeffentlich((byte) oeffentlich);
        pflichtenheftRepository.save(pflichtenheft);
        PflichtenheftBroadcaster.broadcast("");
    }

    public List<KapitelvordefiniertEntity> readAllKapitelvordefiniert() {
        return kapitelvordefiniertRepository.findAll();
    }
}
