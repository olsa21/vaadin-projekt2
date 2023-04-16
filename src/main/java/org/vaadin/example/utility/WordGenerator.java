package org.vaadin.example.utility;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.vaadin.example.entity.InhaltEntity;
import org.vaadin.example.entity.KapitelEntity;
import org.vaadin.example.entity.PflichtenheftEntity;
import org.vaadin.example.model.KapitelModel;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class WordGenerator {

    public static ByteArrayInputStream generateWord(PflichtenheftEntity pflichtenheftEntity) throws IOException {
        // Dokument wird erstellt und Styles werden geladen
        XWPFDocument document = new XWPFDocument();
        XWPFDocument template = new XWPFDocument(ResourceLoader.readFile("styles.docx"));
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
        insertContent(document, pflichtenheftEntity, styles);
        newpage(document);
        insertAbbVerz(document, styles);

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        document.write(b);
        return new ByteArrayInputStream(b.toByteArray());

    }

    private static void insertContent(XWPFDocument doc, PflichtenheftEntity pflichtenheftEntity, Map<String, XWPFStyle> styles) {
        //TreeMap<String, ArrayList<String>> treeMap = createTreemap(pflichtenheftEntity.getKapitel());
        //Erste Ebene
        ArrayList<KapitelEntity> kapitelList = new ArrayList<>(pflichtenheftEntity.getKapitel());
        //
        List<KapitelModel> s = SortKapitel.sortKapitelList(kapitelList);

        for (KapitelModel k : s) {
            //k.getKapitelVordefiniert().getName()
            insertUeberschrift(doc, styles, k.getLevel(), k.getKapitelEntity().getKapitelVordefiniert().getName());

            //Sortierung nach Anordnungsindex
            Map<Integer, InhaltEntity> inhalte = new HashMap();
            for (InhaltEntity i : k.getKapitelEntity().getInhalte()) {
                inhalte.put(i.getAnordnungIndex(), i);
            }

            for (InhaltEntity i : inhalte.values()) {
                //Differenzieren zwischen Text und Bild
                if (i.getTextInhalt() != null) {
                    //i.getTextInhalt()
                    XWPFParagraph p1 = doc.createParagraph();
                    XWPFRun r1 = p1.createRun();
                    r1.setText(i.getTextInhalt());
                } else if (i.getBildInhalt() != null) {
                    try {
                        insertImage(doc, i.getBildInhalt(), "Bildbeschriftung");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidFormatException e) {
                        throw new RuntimeException(e);
                    }
                    //TODO Bildunterschrift einfuegen
                    //document.add(new Paragraph("getacro.gif"));
                    //Image gif= Image.getInstance(i.getBildInhalt());
                    //set width and height
                    //gif.scaleAbsolute(200, 200);
                    //document.add(gif);
                } else {
                    //TODO TABELLE
                    //insertTable(doc, );
                }

            }
        }
    }

    private static void insertAbbVerz(XWPFDocument document, Map<String, XWPFStyle> styles) {
        insertUeberschrift(document, styles, 1, "Abbildungsverzeichnis");
        //Create table of figures field. Word will updating that field while opening the file.
        XWPFParagraph paragraph = document.createParagraph();
        CTSimpleField toc = paragraph.getCTP().addNewFldSimple();
        toc.setInstr("TOC \\c \"figure\" \\* MERGEFORMAT");
        toc.setDirty(STOnOff.TRUE); //set dirty to forcing update
    }

    private static void insertImage(XWPFDocument doc, byte[] image, String bildbeschriftung) throws IOException, InvalidFormatException {
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
        run.setText(": " + bildbeschriftung);
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

        for (ArrayList<String> row : data.subList(1, data.size())) {
            XWPFTableRow roww = table.createRow();
            for (int i = 0; i < row.size(); i++) {
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
}

class SortKapitel {

    public static List<KapitelModel> sortKapitelList(List<KapitelEntity> kapitelList) {
        List<KapitelModel> sortedList = new ArrayList<>();
        List<KapitelEntity> topLevelKapitel = kapitelList.stream()
                .filter(kapitel -> kapitel.getKapitelVordefiniert().getParent() == null || kapitel.getKapitelVordefiniert().getParent() == 0)
                .sorted(Comparator.comparingInt(k -> k.getKapitelVordefiniert().getKapitelVordefiniertOid()))
                .collect(Collectors.toList());

        for (KapitelEntity kapitel : topLevelKapitel) {
            sortKapitelHierarchy(kapitel, kapitelList, sortedList, 1);
        }

        return sortedList;
    }

    private static void sortKapitelHierarchy(KapitelEntity parentKapitel, List<KapitelEntity> kapitelList, List<KapitelModel> sortedList, int level) {
        sortedList.add(new KapitelModel(parentKapitel, level));
        List<KapitelEntity> childKapitel = kapitelList.stream()
                .filter(kapitel -> parentKapitel.getKapitelVordefiniert().getKapitelVordefiniertOid() == (kapitel.getKapitelVordefiniert().getParent() == null ? 0 : kapitel.getKapitelVordefiniert().getParent()))
                .sorted(Comparator.comparingInt(k -> k.getKapitelVordefiniert().getKapitelVordefiniertOid()))
                .collect(Collectors.toList());

        for (KapitelEntity kapitel : childKapitel) {
            sortKapitelHierarchy(kapitel, kapitelList, sortedList, level + 1);
        }
    }
}