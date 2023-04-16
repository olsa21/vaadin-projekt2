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
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.entity.PflichtenheftEntity;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

@Controller
public class ExportController {
    private PflichtenheftEntity pflichtenheftEntity;
    private final SpecificationsService service;


    public ExportController(SpecificationsService service) {
        this.service = service;
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<InputStreamResource> pdf(@PathVariable String id) throws Exception {
        pflichtenheftEntity = service.readPflichtenheft(Integer.parseInt(id));

        if(pflichtenheftEntity == null ||!isMember(pflichtenheftEntity)) {
            //GOTO ""
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        //Felder aktualisieren, damit die Verzeichnisse in der PDF angezeigt wird
        Document doc = new Document(WordGenerator.generateWord(pflichtenheftEntity));
        doc.updateFields();
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        doc.save(pdfOutputStream, SaveFormat.PDF);
        ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        String headerkey = "Content-Disposition";
        //String headervalue = "attachment; filename=mydoc.pdf";//Downloadet direkt
        String headervalue = "inline; filename=" + createFilename("pdf");//Browser öffnet PDF ohne Download
        headers.add(headerkey, headervalue);
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(pdfInputStream));
    }

    @GetMapping(value = "/docx/{id}", produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public ResponseEntity<InputStreamResource> word(@PathVariable String id) throws Exception {
        pflichtenheftEntity = service.readPflichtenheft(Integer.parseInt(id));

        if(pflichtenheftEntity == null || !isMember(pflichtenheftEntity)) {
            //GOTO ""
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        //Damit der Nutzer in Word keine Abfrage über das Aktualieren bekommt; optional
        Document doc = new Document(WordGenerator.generateWord(pflichtenheftEntity));
        doc.updateFields();
        ByteArrayOutputStream wordOutputStream = new ByteArrayOutputStream();
        doc.save(wordOutputStream, SaveFormat.DOCX);
        ByteArrayInputStream wordInputStream = new ByteArrayInputStream(wordOutputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=" + createFilename("docx");
        headers.add(headerkey, headervalue);
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(wordInputStream));
    }

    private PflichtenheftEntity getPflichtenheftEntity(String id) {
        PflichtenheftEntity pflichtenheftEntity = service.readPflichtenheft(Integer.parseInt(id));

        if (!isMember(pflichtenheftEntity)) {
            //GOTO ""
            return null;
        }
        return pflichtenheftEntity;
    }

    private boolean isMember(PflichtenheftEntity pflichtenheftEntity) {
        //SecurityService.getLoggedInUsername(), pflichtenheft
        for (MitarbeiterEntity mitarbeiterEntity : pflichtenheftEntity.getMitarbeiter()) {
            if (mitarbeiterEntity.getBenutzername().equals(SecurityService.getLoggedInUsername())) {
                return true;
            }
        }
        return false;
    }

    private String createFilename(String extension) {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        return "Pflichtenheft_" + pflichtenheftEntity.getTitel() + "_" + dateFormat.format(new Date()) + "." + extension;
    }
}
