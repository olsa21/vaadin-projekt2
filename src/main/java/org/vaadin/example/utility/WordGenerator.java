package org.vaadin.example.utility;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.vaadin.example.entity.*;
import org.vaadin.example.model.KapitelModel;

import java.io.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WordGenerator {

    public static ByteArrayInputStream generateWord(PflichtenheftEntity pflichtenheftEntity) throws IOException {
        //  ---------------- Konfiguration ---------------- //
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

        //Seitenzahlen
        XWPFHeaderFooterPolicy headerFooterPolicy = document.getHeaderFooterPolicy();
        if (headerFooterPolicy == null) {
            headerFooterPolicy = document.createHeaderFooterPolicy();
        }

        XWPFFooter footer = headerFooterPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);
        XWPFParagraph pageNumberParagraph = footer.createParagraph();
        pageNumberParagraph.setAlignment(ParagraphAlignment.RIGHT);

        XWPFRun pageNumberRun = pageNumberParagraph.createRun();
        pageNumberRun.getCTR().addNewFldChar().setFldCharType(STFldCharType.BEGIN);
        pageNumberRun.getCTR().addNewInstrText().setStringValue("PAGE");
        pageNumberRun.getCTR().addNewFldChar().setFldCharType(STFldCharType.END);

        //  ---------------- Konfiguration ENDE ---------------- //

        //Hier wird Inhalt eingefügt
        //TODO Style als Klassenattribut definieren
        insertDeckblatt(document, styles);
        newpage(document);
        insertInhaltsverzeichnis(document, styles);
        newpage(document);
        insertContent(document, pflichtenheftEntity, styles);
        newpage(document);
        insertAbbVerz(document, styles);
        insertTblVerz(document, styles);

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        document.write(b);
        return new ByteArrayInputStream(b.toByteArray());

    }

    private static void insertContent(XWPFDocument doc, PflichtenheftEntity pflichtenheftEntity, Map<String, XWPFStyle> styles) {
        ArrayList<KapitelEntity> kapitelList = new ArrayList<>(pflichtenheftEntity.getKapitel());
        List<KapitelModel> s = sortKapitelList(kapitelList);

        for (KapitelModel k : s) {
            insertUeberschrift(doc, styles, k.getLevel(), k.getKapitelEntity().getKapitelVordefiniert().getName());

            //Sortierung nach Anordnungsindex
            ArrayList<InhaltEntity> inhalte = new ArrayList<>(k.getKapitelEntity().getInhalte());
            inhalte.sort(Comparator.comparing(InhaltEntity::getAnordnungIndex));

            for (InhaltEntity i : inhalte) {
                //Differenzieren zwischen Text/Bild/Tabelle
                //if (!i.getTextInhalt().isBlank() && i.getBildInhalt() == null && i.getTabelle() == null) {
                if (i.getTextinhalt() != null) {
                    //i.getTextInhalt() kann ein leerer String sein
                    insertText(doc, i.getTextinhalt().getTextInhalt());
                } else if (i.getAbbildungsinhalt() != null) {
                    insertImage(doc, i.getAbbildungsinhalt().getBildInhalt(), i.getAbbildungsinhalt().getBildUnterschrift());
                } else if (i.getTabelle() != null) {
                    insertTable(doc, i.getTabelle(), i.getTabelle().getTabellenUnterschrift());
                }
            }
        }
    }

    private static void insertText(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun run;

        Pattern pattern = Pattern.compile("(\\\\[a-z])\\{([^}]*)\\}");
        Matcher matcher = pattern.matcher(text);

        int position = 0;
        while (matcher.find()) {
            String steuerzeichen = matcher.group(1);
            String body = matcher.group(2);

            // Füge den Text vor dem Steuerzeichen hinzu
            if (position < matcher.start()) {
                run = p.createRun();
                run.setText(text.substring(position, matcher.start()));
            }

            // Füge den formatierten Text hinzu
            run = p.createRun();
            System.out.println(steuerzeichen);
            switch (steuerzeichen) {
                case "\\b":
                    run.setBold(true);
                    break;
                case "\\i":
                    run.setItalic(true);
                    break;
                case "\\u":
                    run.setUnderline(UnderlinePatterns.SINGLE);
                    break;
                case "\\n":
                    run.addBreak();
                    break;
                //Hier ggf weitere Formatierungen einfügen
                default:
                    System.out.println("Ungültiges Steuerzeichen: " + steuerzeichen);
            }
            run.setText(body);

            position = matcher.end();
        }

        // Füge den restlichen Text nach dem letzten Steuerzeichen hinzu
        if (position < text.length()) {
            run = p.createRun();
            run.setText(text.substring(position));
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

    private static void insertTblVerz(XWPFDocument document, Map<String, XWPFStyle> styles) {
        insertUeberschrift(document, styles, 1, "Tabellenverzeichnis");
        //Create table of figures field. Word will updating that field while opening the file.
        XWPFParagraph paragraph = document.createParagraph();
        CTSimpleField toc = paragraph.getCTP().addNewFldSimple();
        toc.setInstr("TOC \\c \"table\" \\* MERGEFORMAT");
        toc.setDirty(STOnOff.TRUE); //set dirty to forcing update
    }

    private static void insertImage(XWPFDocument doc, byte[] image, String bildbeschriftung) {
        try {
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
            run.setText(": " + ((bildbeschriftung.isBlank()) ? "Kein Titel" : bildbeschriftung));
        } catch (InvalidFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void insertTable(XWPFDocument doc, TabellenEntity tabelle, String tabellenbeschriftung) {
        XWPFTable table = doc.createTable();
        table.setTableAlignment(TableRowAlign.CENTER);

        //Spaltennamen setzen
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        data.add(new ArrayList<>(Arrays.asList(tabelle.getSpaltenCaption1(), tabelle.getSpaltenCaption1(), tabelle.getSpaltenCaption3(), tabelle.getSpaltenCaption4(), tabelle.getSpaltenCaption5())));

        //Alle Zellen Inhalte setzen
        ArrayList<TabellenzeileEntity> zeilen = new ArrayList<>(tabelle.getZellen());
        zeilen.sort(Comparator.comparing(TabellenzeileEntity::getAnordnungsIndex));
        for (TabellenzeileEntity zeile : zeilen) {
            data.add(new ArrayList<>(Arrays.asList(zeile.getZellenWert1(), zeile.getZellenWert2(), zeile.getZellenWert3(), zeile.getZellenWert4(), zeile.getZellenWert5())));
        }

        //Leere Inhalte entfernen, für korrekte Spaltenanzahl
        data.get(0).removeIf(Objects::isNull);
        for (int i = 1; i < data.size(); i++) {
            data.get(i).removeIf(String::isEmpty);
        }

        //Tabelle erstellen
        //Header
        XWPFTableRow rowX = table.getRow(0);
        rowX.getCell(0).setText(data.get(0).get(0));
        for (int i = 1; i < data.get(0).size(); i++) {
            rowX.addNewTableCell().setText(data.get(0).get(i));
        }

        for (ArrayList<String> row : data.subList(1, data.size())) {
            if (!isRowEmpty(row)) {
                XWPFTableRow roww = table.createRow();
                for (int i = 0; i < row.size(); i++) {
                    roww.getCell(i).setText(row.get(i));
                }
            }
        }

        XWPFParagraph paragraph = doc.createParagraph(); //caption for figure
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setText("Tabelle ");
        CTSimpleField seq = paragraph.getCTP().addNewFldSimple();
        seq.setInstr("SEQ table \\* ARABIC");
        run = paragraph.createRun();
        run.setText(": " + ((tabellenbeschriftung.isBlank()) ? "Kein Titel" : tabellenbeschriftung));
    }

    private static boolean isRowEmpty(ArrayList<String> row) {
        for (String cell : row) {
            if (!cell.isEmpty()) {
                return false;
            }
        }
        return true;
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

    private static List<KapitelModel> sortKapitelList(List<KapitelEntity> kapitelList) {
        List<KapitelModel> sortedList = new ArrayList<>();
        //Kapitel ohne Kinder werden rausgefiltert und nach kapitelVordefiniertOID sortiert
        List<KapitelEntity> topLevelKapitel = kapitelList.stream()
                .filter(kapitel -> kapitel.getKapitelVordefiniert().getParent() == null)
                .sorted(Comparator.comparingInt(k -> k.getKapitelVordefiniert().getKapitelVordefiniertOid()))
                .collect(Collectors.toList());

        for (KapitelEntity kapitel : topLevelKapitel) {
            sortKapitelHierarchy(kapitel, kapitelList, sortedList, 1);
        }

        return sortedList;
    }

    private static void sortKapitelHierarchy(KapitelEntity parent, List<KapitelEntity> kapitelList, List<KapitelModel> sortedList, int level) {
        sortedList.add(new KapitelModel(parent, level));
        //Untergeordnete Kapitel des mitgegebenen parent Filtern
        List<KapitelEntity> childKapitel = kapitelList.stream()
                .filter(kapitel -> parent.getKapitelVordefiniert().getKapitelVordefiniertOid() == (kapitel.getKapitelVordefiniert().getParent() == null ? 0 : kapitel.getKapitelVordefiniert().getParent()))
                .sorted(Comparator.comparingInt(k -> k.getKapitelVordefiniert().getKapitelVordefiniertOid()))
                .collect(Collectors.toList());

        for (KapitelEntity kapitel : childKapitel) {
            sortKapitelHierarchy(kapitel, kapitelList, sortedList, level + 1);
        }
    }
}