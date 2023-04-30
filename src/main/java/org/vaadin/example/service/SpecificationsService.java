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


/**
 * Die Klasse ist dafür zuständig, dass Operationen, welche auf die Datenbank zugreifen zentral über diese Klasse
 * erfolgen. Hierbei werden die Repositories verwendet, welche die Datenbankzugriffe ermöglichen.
 */
@Service
public class SpecificationsService {

    private final MitarbeiterRepository benutzerRepository;
    private final AbteilungzuweisungRepository abteilungzuweisungRepository;
    private final ProjektZuweisungRepository projektZuweisungRepository;
    private final PflichtenheftRepository pflichtenheftRepository;
    private final KapitelvordefiniertRepository kapitelvordefiniertRepository;
    private final KapitelRepository kapitelRepository;
    private final InhaltRepository inhaltRepository;
    private final TabellenRepository tabellenRepository;
    private final TabellenzellenRepository tabellenzellenRepository;
    private final TextInhaltRepository textInhaltRepository;
    private final AbbildungInhaltRepository abbildungInhaltRepository;

    /**
     * Konstruktor der Klasse, welcher die Repositories initialisiert
     */
    public SpecificationsService(MitarbeiterRepository benutzerRepository, AbteilungzuweisungRepository abteilungzuweisungRepository, ProjektZuweisungRepository projektZuweisungRepository, PflichtenheftRepository pflichtenheftRepository, KapitelvordefiniertRepository kapitelvordefiniertRepository, KapitelRepository kapitelRepository, InhaltRepository inhaltRepository, TabellenRepository tabellenRepository, TabellenzellenRepository tabellenzellenRepository, TextInhaltRepository textInhaltRepository, AbbildungInhaltRepository abbildungInhaltRepository){
        this.benutzerRepository = benutzerRepository;
        this.abteilungzuweisungRepository = abteilungzuweisungRepository;
        this.projektZuweisungRepository = projektZuweisungRepository;
        this.pflichtenheftRepository = pflichtenheftRepository;
        this.kapitelvordefiniertRepository = kapitelvordefiniertRepository;
        this.kapitelRepository = kapitelRepository;
        this.inhaltRepository = inhaltRepository;
        this.tabellenRepository = tabellenRepository;
        this.tabellenzellenRepository = tabellenzellenRepository;
        this.textInhaltRepository = textInhaltRepository;
        this.abbildungInhaltRepository = abbildungInhaltRepository;
    }

    /**
     * Methode, welche alle Benutzer zurückgibt, welche in der Datenbank gespeichert sind.
     * @param filterText der Filtertext, welcher auf die Benutzer angewendet werden soll
     * @return die Liste der relevanten Benutzer
     */
    public List<MitarbeiterEntity> findAllUser(String filterText){
        if (filterText == null || filterText.isEmpty()){
            return benutzerRepository.findAll();
        }else{
            return benutzerRepository.search(filterText);
        }
    }

    /**
     * Methode, welche einen Benutzer anhand seines Benutzernamens zurückgibt.
     * @param username der Benutzername des Benutzers, welcher zurückgegeben werden soll
     * @return der Benutzer, welcher den Benutzernamen besitzt
     */
    public MitarbeiterEntity findSpecificUser(String username){
        return benutzerRepository.readUserWhere(username, "%");
    }

    /**
     * Methode, welche prüft, ob eine Mail bereits in der DB eingetragen ist.
     * @param mail die Mail, welche geprüft werden soll
     * @return true, wenn die Mail bereits in der DB eingetragen ist, ansonsten false
     */
    public boolean findSpecificUserByMail(String mail){
        return benutzerRepository.readUserWhereMail(mail) != null;
    }

    /**
     * Methode, welche einen Benutzer anhand seiner OID zurückgibt.
     * @param id die OID des Benutzers, welcher zurückgegeben werden soll
     * @return der Benutzer, welcher die OID besitzt
     */
    public MitarbeiterEntity findSpecificUserById(Integer id){
        if (id == null || id < 0){
            throw new IllegalArgumentException("Die OID muss einen gültigen nicht negativen Wert beinhalten!");
        }
        return benutzerRepository.findByOid(id);
    }

    /**
     * Methode, welche einen Benutzer anhand seines Benutzernamens und Passworts authentifiziert.
     * @param username der Benutzername des Benutzers, welcher authentifiziert werden soll
     * @param password das Passwort des Benutzers, welcher authentifiziert werden soll
     * @return true, wenn der Benutzer authentifiziert werden konnte, ansonsten false
     */
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

    /**
     * Methode, welche einen Benutzer hinzufügt.
     * @param benutzer der Benutzer, welcher hinzugefügt werden soll
     */
    public void addMitarbeiter(MitarbeiterEntity benutzer){
        if (benutzer == null){
            System.err.println("Benutzer muss gültig sein!");
        }
        benutzerRepository.save(benutzer);
    }

    /**
     * Methode, welche eine Liste von Pflichtenhefter-Objekten zurückgibt, welche zu einem bestimmten Benutzer gehören.
     * @param mitarbeiterOID die OID des Benutzers, zu welchem die Pflichtenhefter zurückgegeben werden sollen
     * @return die Liste der Pflichtenhefter, welche zu dem Benutzer gehören
     */
    public ArrayList<PflichtenheftEntity> getPflichtenheftListWhere(int mitarbeiterOID){
        return pflichtenheftRepository.readPflichtenheftWhere(mitarbeiterOID);
    }

    /**
     * Methode, welche ein Kapitel-Objekt speichert
     * @param kapitel das Kapitel-Objekt, welches gespeichert werden soll
     */
    public void saveKapitel(KapitelEntity kapitel){
        if (kapitel == null){
            System.err.println("Kapitel darf nicht null sein!");
        }
        kapitelRepository.save(kapitel);
    }

    /**
     * Methode, welche ein Benutzer löscht.
     * @param benutzer der Benutzer, welcher gelöscht werden soll
     */
    public void deleteMitarbeiter(MitarbeiterEntity benutzer){
        benutzerRepository.delete(benutzer);
    }

    /**
     * Methode, welche eine Abteilungszuweisung löscht.
     * @param benutzerOID die OID des Benutzers, zu welchem die Abteilungszuweisung gelöscht werden soll
     */
    @Transactional
    public void deleteAbteilungszuweisung(int benutzerOID){
        abteilungzuweisungRepository.deleteZuweisung(benutzerOID);
    }

    /**
     * Methode, welche eine Liste von Abteilungen zurückgibt.
     * @return die Liste der Abteilungen
     */
    public List<AbteilungEntity> getAbteilungList() {
        try {
            return benutzerRepository.readAbteilungen();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Methode, welche eine Liste von Abteilungen zurückgibt, welche zu einem bestimmten Benutzer gehören.
     * @param mitarbeiterOID die OID des Benutzers, zu welchem die Abteilungen zurückgegeben werden sollen
     * @return die Liste der Abteilungen, welche zu dem Benutzer gehören
     */
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

    /**
     * Methode, welche eine Abteilungszuweisung speichert.
     * @param mitarbeiterOid die OID des Benutzers
     * @param ausgewaehlteAbteilungen Liste von ausgewählten Abteilungen, die gespeichert werden sollen
     */
    @Transactional
    public void saveAbteilungZuweisungen(int mitarbeiterOid, List<AbteilungEntity> ausgewaehlteAbteilungen) {
        abteilungzuweisungRepository.deleteZuweisung(mitarbeiterOid);
        ausgewaehlteAbteilungen.forEach(item -> {
            abteilungzuweisungRepository.saveRaw(mitarbeiterOid, item.getAbteilungOid());
        });
    }

    /**
     * Methode aktualisiert ein Pflichtenheftobjekt in der Datenbank.
     * @param pflichtenheftEntity das Pflichtenheft-Objekt
     */
    public void updatePflichtenheft(PflichtenheftEntity pflichtenheftEntity) {
        pflichtenheftRepository.save(pflichtenheftEntity);
    }

    /**
     * Die Methode erstellt ein Pflichtenheft, mit den dazugehörigen Attributen separat.
     * @param mitarbeiter der verantwortliche Mitarbeiter
     * @param titel Titel des Pflichtenheftes
     * @param beschreibung Beschreibung des Pflichtenheftes
     * @param frist Abgabefrist des Pflichtenheftes
     * @param repoLink Repository-Link des Pflichtenheftes
     * @param oeffentlich Eigenschaft, ob das Pflichtenheft öffentlich ist oder nicht
     */
    public void createPflichtenheft(MitarbeiterEntity mitarbeiter, String titel, String beschreibung, LocalDate frist, String repoLink, int oeffentlich) {
        PflichtenheftEntity pflichtenheftEntity = new PflichtenheftEntity();
        pflichtenheftEntity.setTitel(titel);
        pflichtenheftEntity.setBeschreibung(beschreibung);
        pflichtenheftEntity.setFrist(frist.toString());
        pflichtenheftEntity.setRepositoryLink(repoLink);
        pflichtenheftEntity.setOeffentlich((byte) oeffentlich);
        pflichtenheftEntity.setVerantwortlicher(mitarbeiter);

        PflichtenheftEntity result = pflichtenheftRepository.save(pflichtenheftEntity);

        for(KapitelvordefiniertEntity kv : kapitelvordefiniertRepository.findAll()){
            KapitelEntity kapitelEntity = new KapitelEntity();
            kapitelEntity.setProjekt(result);
            kapitelEntity.setKapitelVordefiniert(kv);
            kapitelRepository.save(kapitelEntity);
        }

        result.addMitarbeiter(mitarbeiter);
        pflichtenheftRepository.save(result);
    }

    // TODO In Zukunft entfernen
    @Transactional
    public void addProjektZuweisung(String username, int projektOid) {
        int mitarbeiterOid = findSpecificUser(username).getMitarbeiterOid();
        addProjektZuweisung(mitarbeiterOid, projektOid);
    }

    /**
     * Methode, welche eine Projektzuweisung speichert.
     * @param mitarbeiterOid die OID des Benutzers
     * @param projektOid die OID des Projekts
     */
    @Transactional
    public void addProjektZuweisung(int mitarbeiterOid, int projektOid) {
        projektZuweisungRepository.saveRaw(mitarbeiterOid, projektOid);
    }

    /**
     * Methode, welche überprüft, ob ein Benutzername zu einem bestimmten Projekt gehört.
     * @param username der Benutzername
     * @param pflichtenheft das Pflichtenheft
     * @return true, wenn der Benutzername zu dem Projekt gehört, sonst false
     */
    public boolean isMember(String username, PflichtenheftEntity pflichtenheft) {
        return projektZuweisungRepository.readProjektZuweisungWhere(username, pflichtenheft.getProjektOid()) != null;
    }

    /**
     * Methode, welche die Mitglieder eines Projekts zurückgibt.
     * @param pflichtenheft das Pflichtenheft
     * @return Liste von Mitarbeitern
     */
    public List<MitarbeiterEntity> readMitglieder(PflichtenheftEntity pflichtenheft) {
        List<MitarbeiterEntity> mitglieder = projektZuweisungRepository.readMitglieder(pflichtenheft.getProjektOid());
        return mitglieder;
    }

    /**
     * Methode, welche den Verantwortlichen eines Projekts zurückgibt.
     * @param pflichtenheft das Pflichtenheft
     * @return verantwortlichen Mitarbeiter
     */
    public MitarbeiterEntity readVerantwortlicher(PflichtenheftEntity pflichtenheft) {
        return pflichtenheftRepository.readVerantwortlicher(pflichtenheft.getProjektOid());
    }

    /**
     * Methode, welche einen Inhalt aus einem Pflichtenheft aktualisiert.
     * @param inhalt der Inhalt
     * @return aktualisierter Inhalt
     */
    public InhaltEntity saveInhalt(InhaltEntity inhalt) {
        return inhaltRepository.save(inhalt);
    }

    /**
     * Methode, welche eine Tabelle aus einem Pflichtenheft aktualisiert.
     * @param table die Tabelle
     */
    public void saveTable(TabellenEntity table) {
        tabellenRepository.save(table);
    }

    /**
     * Methode, welche ein Inhalt aus einem Pflichtenheft löscht.
     * @param inhalt der zu löschende Inhalt
     */
    public void deleteInhalt(InhaltEntity inhalt) {
        inhaltRepository.delete(inhalt);
    }

    /**
     * Methode, welche ein Pflichtenheft sucht und zurückgibt.
     * @param projektOid die OID des Pflichtenhefts
     * @return das Pflichtenheft
     */
    public PflichtenheftEntity readPflichtenheft(int projektOid) {
        Optional<PflichtenheftEntity> result = pflichtenheftRepository.findById(projektOid);
        return result.orElse(null);
    }

    /**
     * Methode, welche eine Liste von offenen Projekten zurückgibt.
     * @return Liste von offenen Projekten
     */
    public List<PflichtenheftEntity> findOpenProjects() {
        return pflichtenheftRepository.findOpenProjects();
    }

    /**
     * Methode, welche die Sichtbarkeit eines Pflichtenhefts ändert.
     * @param pflichtenheft das Pflichtenheft
     * @param oeffentlich die Sichtbarkeit
     */
    public void setOeffentlich(PflichtenheftEntity pflichtenheft, int oeffentlich) {
        System.out.println("Öffentlichkeit wird geändert: =="  + oeffentlich);
        pflichtenheft.setOeffentlich((byte) oeffentlich);
        pflichtenheftRepository.save(pflichtenheft);
        PflichtenheftBroadcaster.broadcast("");
    }

    /**
     * Methode, welche alle vordefinierten Kapitel zurückgibt.
     * @return Liste von vordefinierten Kapiteln
     */
    public List<KapitelvordefiniertEntity> readAllKapitelvordefiniert() {
        return kapitelvordefiniertRepository.findAll();
    }

    /**
     * Methode, welche eine Tabelle aus einem Pflichtenheft löscht.
     * @param tabelle die zu löschende Tabelle
     */
    public void deleteTable(TabellenEntity tabelle) {
        tabellenRepository.delete(tabelle);
    }

    /**
     * Methode, welche eine Tabellenzeile aus einem Pflichtenheft speichert.
     * @param tabellenzeile die Tabellenzeile
     */
    public void saveTabellenzeile(TabellenzeileEntity tabellenzeile) {
        tabellenzellenRepository.save(tabellenzeile);
    }

    /**
     * Methode, welche eine Tabellenzeile aus einem Pflichtenheft löscht.
     * @param tabellenzeileEntity die zu löschende Tabellenzeile
     */
    public void deleteTabellenzeile(TabellenzeileEntity tabellenzeileEntity) {
        tabellenzellenRepository.delete(tabellenzeileEntity);
    }

    /**
     * Methode, welche einen Textinhalt aus einem Pflichtenheft speichert.
     * @param textinhalt der zu speichernde Textinhalt
     */
    public void saveTextinhalt(TextinhaltEntity textinhalt) {
        textInhaltRepository.save(textinhalt);
    }

    /**
     * Methode, welche einen Textinhalt aus einem Pflichtenheft löscht.
     * @param textinhalt der zu löschende Textinhalt
     */
    public void deleteTextinhalt(TextinhaltEntity textinhalt) {
        textInhaltRepository.delete(textinhalt);
    }

    /**
     * Methode, welche eine Abbildung aus einem Pflichtenheft speichert.
     * @param abbildungsinhalt die zu speichernde Abbildung
     */
    public void saveAbbildungsinhalt(AbbildungsinhaltEntity abbildungsinhalt) {
        abbildungInhaltRepository.save(abbildungsinhalt);
    }

    /**
     * Methode, welche eine Abbildung aus einem Pflichtenheft löscht.
     * @param abbildungsinhalt die zu löschende Abbildung
     */
    public void deleteAbbildungsinhalt(AbbildungsinhaltEntity abbildungsinhalt) {
        abbildungInhaltRepository.delete(abbildungsinhalt);
    }

    /**
     * Methode, welche einen bestimmtes Inhalt-Objekt aus der Datenbank zurückgibt.
     * @param inhaltOid die OID des Inhalts
     * @return das Inhalt-Objekt
     */
    public InhaltEntity readInhalt(int inhaltOid) {
        Optional<InhaltEntity> result = inhaltRepository.findById(inhaltOid);
        return result.orElse(null);
    }
}
