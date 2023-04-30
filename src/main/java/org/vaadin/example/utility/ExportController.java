package org.vaadin.example.utility;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.vaadin.example.entity.PflichtenheftEntity;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

/**
 * Controller für den Export.
 * Die Methoden werden über die URL aufgerufen
 */
@Controller
public class ExportController {
    private PflichtenheftEntity pflichtenheftEntity;
    private final SpecificationsService service;

    /**
     * Konstruktor
     * @param service
     */
    public ExportController(SpecificationsService service) {
        this.service = service;
    }

    /**
     * Erstellt eine PDF-Datei und gibt diese zurück
     * @param id ID des zu exportierenden Pflichtenheftes
     * @return
     */
    @GetMapping("/pdf/{id}")
    public ResponseEntity<InputStreamResource> pdf(@PathVariable String id) {
        try {
            pflichtenheftEntity = service.readPflichtenheft(Integer.parseInt(id));

            if(!SecurityService.userIsMemberOf(pflichtenheftEntity)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            //Felder aktualisieren, damit die Verzeichnisse in der PDF angezeigt wird
            Document doc = new Document(WordGenerator.generateWord(pflichtenheftEntity));
            doc.updateFields();
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            doc.save(pdfOutputStream, SaveFormat.PDF);
            ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());

            //HTTP Antwort erstellen und zurückgeben
            HttpHeaders headers = new HttpHeaders();
            String headerkey = "Content-Disposition";
            String headervalue = "inline; filename=" + createFilename("pdf");//Browser öffnet PDF über temporären Speicher
            headers.add(headerkey, headervalue);
            headers.setContentType(MediaType.APPLICATION_PDF);
            return ResponseEntity.ok().headers(headers).body(new InputStreamResource(pdfInputStream));
        } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Erstellt eine Word-Datei und gibt diese zurück
     * @param id ID des zu exportierenden Pflichtenheftes
     * @return Word-Datei
     */
    @GetMapping(value = "/docx/{id}", produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public ResponseEntity<InputStreamResource> word(@PathVariable String id) {
        try {
            pflichtenheftEntity = service.readPflichtenheft(Integer.parseInt(id));

            if(!SecurityService.userIsMemberOf(pflichtenheftEntity)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            //Damit der Nutzer in Word keine Abfrage über das Aktualieren bekommt; optional
            Document doc = new Document(WordGenerator.generateWord(pflichtenheftEntity));
            doc.updateFields();
            ByteArrayOutputStream wordOutputStream = new ByteArrayOutputStream();
            doc.save(wordOutputStream, SaveFormat.DOCX);
            ByteArrayInputStream wordInputStream = new ByteArrayInputStream(wordOutputStream.toByteArray());

            //HTTP Antwort erstellen und zurückgeben
            HttpHeaders headers = new HttpHeaders();
            String headerkey = "Content-Disposition";
            String headervalue = "attachment; filename=" + createFilename("docx");
            headers.add(headerkey, headervalue);
            return ResponseEntity.ok().headers(headers).body(new InputStreamResource(wordInputStream));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Erstellt den Dateinamen mit Datum und Uhrzeit
     * @param extension Dateiendung
     * @return Dateiname als String
     */
    private String createFilename(String extension) {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        return "Pflichtenheft_" + pflichtenheftEntity.getTitel() + "_" + dateFormat.format(new Date()) + "." + extension;
    }
}
