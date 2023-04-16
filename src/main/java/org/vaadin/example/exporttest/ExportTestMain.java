package org.vaadin.example.exporttest;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ExportTestMain {
    private static String path = System.getProperty("user.dir") + "\\src\\main\\java\\org\\vaadin\\example\\exporttest\\";

    public static void main(String[] args) throws Exception {
        System.out.println("Hello world!");

        //writeASmallStory("template.docx", "generated.docx");
        writePflichtenheft("styles.docx", "generated.docx");
        //createWord();
        //createOdt();
        //convertWord("generated.docx", "test.pdf");
        //convertWordv2();
        convertAspa("generated.docx", "test.pdf");
        //template();

    }

    private static void convertAspa(String docxSrc, String pdfDest) throws Exception {
        // Load the Word document
        Document doc = new Document(path + docxSrc);

        // Update fields
        doc.updateFields();

        // Save the Word document as a PDF
        doc.save(path + pdfDest, SaveFormat.PDF);
    }

    private static void writeASmallStory(String docxTemplate, String docxDest) throws IOException {
        XWPFDocument document = new XWPFDocument();
        XWPFDocument template = new XWPFDocument(new FileInputStream(path + docxTemplate));
        XWPFStyles newStyles = document.createStyles();
        Map<String, XWPFStyle> styles = new HashMap<>();
        //"berschrift" weil Template mit einer deutschen Word Version erstellt wurde
        //normalerweise müsste es "Heading1" heißen
        styles.put("Heading1", template.getStyles().getStyle("berschrift1"));
        styles.put("Heading2", template.getStyles().getStyle("berschrift2"));
        styles.put("Heading3", template.getStyles().getStyle("berschrift3"));
        styles.put("Title", template.getStyles().getStyle("Titel"));

        XWPFStyles s = template.getStyles();

        for (XWPFStyle style : styles.values()) {
            newStyles.addStyle(style);
        }

        //generateHeaderAndFooter(document);
        generateBodyParagraphs(document, styles);
        //generateTable(document);

        saveDocument(document, docxDest);
    }

    private static void saveDocument(XWPFDocument doc, String docxDest) throws IOException {
        File file = new File(path + docxDest);
        file.delete();
        file.createNewFile();
        doc.write(new FileOutputStream(file));
//        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
//        doc.write(baos);
        doc.close();
    }

    public static void generateBodyParagraphs(XWPFDocument document, Map<String, XWPFStyle> style) throws IOException {
        XWPFParagraph pBody = document.createParagraph();
        XWPFRun run1 = pBody.createRun();
        pBody.setStyle(style.get("Heading1").getStyleId());
        run1.setStyle(style.get("Heading1").getStyleId());
        run1.setText("This is the first headerr");

        XWPFParagraph pBody2 = document.createParagraph();
        XWPFRun run2 = pBody2.createRun();
        pBody2.setStyle(style.get("Heading2").getStyleId());
        run2.setStyle(style.get("Heading2").getStyleId());
        run2.setText("This is the second headerr");

        XWPFParagraph pBody3 = document.createParagraph();
        XWPFRun run3 = pBody3.createRun();
        pBody3.setStyle(style.get("Heading3").getStyleId());
        run3.setStyle(style.get("Heading3").getStyleId());
        run3.setText("This is the third headerr");

        XWPFParagraph pBody4 = document.createParagraph();
        XWPFRun run4 = pBody4.createRun();
        pBody4.setStyle(style.get("Title").getStyleId());
        run4.setStyle(style.get("Title").getStyleId());
        run4.setText("This is the Titel");

        // Empty paragraph
        document.createParagraph();

        //Hilft evtl bei der Konvertierung zu PDF
        //CTSectPr sect = document.getDocument().getBody().getSectPr();
        //sect.addNewTitlePg();

    }

    private static void createWordTutorial() {

    }



    private static void createOdt() {
        //create simple odt file
        try {
            XWPFDocument doc = new XWPFDocument();
            //doc.createStyles();
            XWPFParagraph p1 = doc.createParagraph();
            p1.setAlignment(ParagraphAlignment.CENTER);
            // Set Text to Bold and font size to 22 for first paragraph
            XWPFRun r1 = p1.createRun();
            //r1.setText("Spring Boot + Apache POI Example");


            FileOutputStream out = new FileOutputStream("test.odt");
            doc.write(out);
            out.close();
            doc.close();

            System.out.println("Word created successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createWord() {
        try {
            XWPFDocument doc = new XWPFDocument();
            //doc.createStyles();
            XWPFParagraph p1 = doc.createParagraph();
            p1.setAlignment(ParagraphAlignment.CENTER);
            // Set Text to Bold and font size to 22 for first paragraph
            XWPFRun r1 = p1.createRun();
            r1.setText("Spring Boot + Apache POI Example");


            FileOutputStream out = new FileOutputStream(path + "test.docx");
            doc.write(out);
            out.close();
            doc.close();

            System.out.println("Word created successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    // Pflichtenheft Teil; Wird verändert in das Projekt übernommen     -------------------------------------------------------
    private static void writePflichtenheft(String docxTemplate, String docxDest) throws IOException, InvalidFormatException {
        // Dokument wird erstellt und Styles werden geladen
        XWPFDocument document = new XWPFDocument();
        XWPFDocument template = new XWPFDocument(new FileInputStream(path + docxTemplate));
        XWPFStyles newStyles = document.createStyles();
        Map<String, XWPFStyle> styles = new HashMap<>();
        //"berschrift" weil Template mit einer deutschen Word Version erstellt wurde
        //normalerweise müsste es "Heading1" heißen
        styles.put("Heading1", template.getStyles().getStyle("berschrift1"));
        styles.put("Heading2", template.getStyles().getStyle("berschrift2"));
        styles.put("Heading3", template.getStyles().getStyle("berschrift3"));
        styles.put("Title", template.getStyles().getStyle("Titel"));

        XWPFStyles s = template.getStyles();

        for (XWPFStyle style : styles.values()) {
            newStyles.addStyle(style);
        }

        //Hier wird Inhalt eingefügt
        //TODO Style als Klassenattribut definieren
        insertDeckblatt(document, styles);
        newpage(document);
        insertInhaltsverzeichnis(document, styles);
        newpage(document);
        insertUeberschrift(document, styles, 1, "Ueberschrift 1");
        insertUeberschrift(document, styles, 2, "Ueberschrift 1.1");
        insertUeberschrift(document, styles, 2, "Ueberschrift 1.2");
        insertUeberschrift(document, styles, 3, "Ueberschrift 1.2.1");
        insertUeberschrift(document, styles, 3, "Ueberschrift 1.2.2");
        newpage(document);
        insertUeberschrift(document, styles, 1, "Ueberschrift 2");
        insertUeberschrift(document, styles, 1, "Ueberschrift 3");
        insertText(document);
        insertTable(document);

        //load file save as byte array
        byte [] bytes = Files.readAllBytes(Paths.get(path + "testimage.png"));

        insertImage(document, bytes);
        insertImage(document, bytes);
        insertAbbVerz(document, styles);

        //new para


        //Speichern
        saveDocument(document, docxDest);
    }

    private static void insertImage(XWPFDocument doc, byte[] image) throws IOException, InvalidFormatException {
        // Ein neuer Absatz erstellen
        XWPFParagraph paragraph = doc.createParagraph();

        //Bild einfügen
        XWPFRun run = paragraph.createRun();
        int pictureType = XWPFDocument.PICTURE_TYPE_PNG; // Der Typ des Bilds
        run.addPicture(new ByteArrayInputStream(image), pictureType, "image.png", Units.toEMU(200), Units.toEMU(200)); // Das Bild einfügen

        paragraph = doc.createParagraph(); //caption for figure
        run = paragraph.createRun();
        run.setText("Abbildung ");
        CTSimpleField seq = paragraph.getCTP().addNewFldSimple();
        seq.setInstr("SEQ figure \\* ARABIC"); //This field is important for creating the table of figures then.
        run = paragraph.createRun();
        run.setText(": Description of sample picture 1");
    }

    private static void insertTable(XWPFDocument doc) {
        XWPFTable table = doc.createTable();
        table.setTableAlignment(TableRowAlign.CENTER);

        ArrayList<ArrayList<String>> data = new ArrayList<>();
        data.add(new ArrayList<>(Arrays.asList("Java, Scala", "PHP, Flask", "Ruby, Rails")));
        data.add(new ArrayList<>(Arrays.asList("C, C ++", "Python, Kotlin", "Android, React")));
        data.add(new ArrayList<>(Arrays.asList("C, C ++", "Python, Kotlin", "Android, React")));
        data.add(new ArrayList<>(Arrays.asList("C, C ++", "Python, Kotlin", "Android, React")));
        data.add(new ArrayList<>(Arrays.asList("C, C ++", "Python, Kotlin", "Android, React")));

        //Header
        XWPFTableRow rowX = table.getRow(0);
        rowX.getCell(0).setText(data.get(0).get(0));
        rowX.addNewTableCell().setText(data.get(0).get(1));
        rowX.addNewTableCell().setText(data.get(0).get(2));

        for(ArrayList<String> row : data.subList(1, data.size())) {
            XWPFTableRow roww = table.createRow();
            for(int i = 0; i < row.size(); i++) {
                roww.getCell(i).setText(row.get(i));
            }
        }
        //Content
    }

    private static void insertInhaltsverzeichnis(XWPFDocument document, Map<String, XWPFStyle> styles) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText("Inhaltsverzeichnis");
        //set font size
        run.setFontSize(20);

        paragraph = document.createParagraph();
        CTP ctP = paragraph.getCTP();
        CTSimpleField toc = ctP.addNewFldSimple();
        toc.setInstr("TOC \\h");
        toc.setDirty(STOnOff.TRUE);
    }

    private static void newpage(XWPFDocument doc) {
        doc.createParagraph().setPageBreak(true);
    }
    private static void insertDeckblatt(XWPFDocument document, Map<String, XWPFStyle> styles) {
        document.createParagraph().setPageBreak(true);

        // Absatz für das Deckblatt erstellen
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        paragraph.setVerticalAlignment(TextAlignment.CENTER); // Text vertikal zentrieren

        // Titel des Deckblatts
        XWPFRun runTitle = paragraph.createRun();
        runTitle.setText("Deckblatt");
        paragraph.setStyle(styles.get("Title").getStyleId());
        runTitle.setStyle(styles.get("Title").getStyleId());

        // Autor des Deckblatts
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        paragraph.setVerticalAlignment(TextAlignment.CENTER);
        XWPFRun runAuthor = paragraph.createRun();
        runAuthor.setText("Autor: Max Mustermann");
        runAuthor.setFontSize(14);
        runAuthor.addBreak();

        // Datum des Deckblatts
        XWPFRun runDate = paragraph.createRun();
        runDate.setText("Datum: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        runDate.setFontSize(14);
        runDate.addBreak();

        // Optional: Mail Adresse
    }

    private static void insertUeberschrift(XWPFDocument document, Map<String, XWPFStyle> styles, int level, String text) {
        XWPFParagraph pBody = document.createParagraph();
        XWPFRun run1 = pBody.createRun();

        pBody.setStyle(styles.get("Heading" + level).getStyleId());
        run1.setStyle(styles.get("Heading" + level).getStyleId());
        run1.setText(text);

    }

    private static void insertText(XWPFDocument document) {
        XWPFParagraph p = document.createParagraph();
        XWPFRun run1 = p.createRun();

        /* Ohne STYLE
        p.setStyle(style.get("Heading1").getStyleId());
        run1.setStyle(style.get("Heading1").getStyleId());
         */
        run1.setText("TEXT");
    }

    private static void insertAbbVerz(XWPFDocument document, Map<String, XWPFStyle> styles) {
        insertUeberschrift(document, styles, 1, "Abbildungsverzeichnis");
        //Create table of figures field. Word will updating that field while opening the file.
        XWPFParagraph paragraph = document.createParagraph();
        CTSimpleField toc = paragraph.getCTP().addNewFldSimple();
        toc.setInstr("TOC \\c \"figure\" \\* MERGEFORMAT");
        toc.setDirty(STOnOff.TRUE); //set dirty to forcing update
    }
}