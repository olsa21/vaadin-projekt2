package org.vaadin.example.views.editor;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import org.aspectj.weaver.ast.Not;
import org.vaadin.example.components.CustomGrid;
import org.vaadin.example.components.CustomPicUploadWithCaption;
import org.vaadin.example.components.GridRow;
import org.vaadin.example.entity.*;
import org.vaadin.example.listener.EditorBroadcaster;
import org.vaadin.example.listener.PflichtenheftBroadcaster;
import org.vaadin.example.model.ComponentModel;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class EditorBar extends HorizontalLayout {

    private final SpecificationsService service;
    private ComboBox<String> cb;
    private ArrayList<ComponentModel> components;

    private PflichtenheftEntity pflichtenheftEntity;

    private Button speichernBtn;

    private int currentChapter;

    private String currentUsername;

    Registration broadcasterRegistration;


    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        System.out.println("detach layout");
        broadcasterRegistration.remove();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UI ui = attachEvent.getUI();
        broadcasterRegistration = EditorBroadcaster.register(newMessage -> {
            String[] values = newMessage;

            int projektOid = Integer.parseInt(values[0]);
            int currentchapterMitgabe = Integer.parseInt(values[1]);
            String user = values[2];
            Integer componentOID = null;
            if (values[3] != null) {
                componentOID = Integer.parseInt(values[3]);
            }
            System.err.println("HALLI HALLO");
            System.err.println(componentOID);

            System.err.println(currentUsername + " hat eine Nachricht erhalten: " + values[0] + " " + values[1] + "bzgl. folgendem Component: " + (componentOID != null ? componentOID : "null"));

            //String currentUser = SecurityService.getLoggedInUsername();
            String currentUser = this.currentUsername;
            if(currentChapter == currentchapterMitgabe && pflichtenheftEntity.getProjektOid() == projektOid &&
                !user.equals(currentUser)) { //NUTZER ID
                System.out.println("NEU LADEN");
                Integer finalComponentOID = componentOID;


                for (  ComponentModel c : components){
                    Component cm = c.getComponent().getChildren().findFirst().get().getChildren().findFirst().get();
                    if (cm.getClass() == TextArea.class){
                        TextArea temp = (TextArea) cm;
                        System.err.println("FOKUS?: " +temp  );
                    }
                    System.err.println(c.getComponent().getChildren().findFirst().get().getChildren().findFirst().get().getClass().toString());
                }

                if (finalComponentOID != null){

                    int i = 0;
                    for (  ComponentModel c : components){
                        Component cm = c.getComponent().getChildren().findFirst().get().getChildren().findFirst().get();
                        if (c.getComponentOid() == finalComponentOID && cm.getClass() == TextArea.class){
                            // aus components-Liste die richtige Stelle finden, die aktualisiert worden ist
                            // den INhalt an der Stelle ersetzen mit dem neuen Inhalt
                            //System.err.println(components.indexOf(components.stream().filter(cmp->cmp.getComponentOid()==finalComponentOID).findFirst()));
                            System.err.println("UNTEN!");
                            System.err.println("i: " + i);
                            System.err.println(components.indexOf(c));

                            components.remove(i);
                            InhaltEntity testTemp = service.readInhalt(finalComponentOID);

                            HorizontalLayout tempLayout = addComponent("Textfeld", testTemp.getTextinhalt().getTextInhalt(), null,null,testTemp.getInhaltOid());
                            this.components.add(i, new ComponentModel( testTemp.getInhaltOid(), tempLayout) );
                            //TODO Einfach los programmiert
                            //TODO die Tage mal anschauen ob der Ansatz überhaupt funktionieren kann
                            //TODO ansonsten einfach lassen yolo

                            //TODO ANSATZ NICHT MÖGLICH WEIL MAN ENTWEDER PER SETVALUE DEN LISTENER ANTREIBT SPRICH ENDLOSSCHLEIFE

                            //TODO IRGENWIE MUSS DAS ÜBER FOCUSLISTENER GEMERKT WERDEN, WAS ICH ANGEKLICKT HATTE
                            //
                        }
                        i++;
                    }

                    /*TextArea temp123 = (TextArea) components.stream().filter(cmp->cmp.getComponentOid()==inhaltOid).findFirst().get().getComponent().getChildren().findFirst().get()
                            .getChildren().findFirst().get();  //component.getComponent().getChildren().findFirst().get().getChildren().findFirst().get();
                    boolean focused = temp123.isAutofocus();
                    System.err.println(temp123.getClass());
                    temp123.setValue(service.readInhalt(inhaltOid).getTextinhalt().getTextInhalt());
*/



                    ui.access(() -> {
                        //reloadSpecificContent(finalComponentOID);
                        //Notification.show(finalComponentOID.toString());
                        //updateComponentView();
                        updateComponentViewOptimized(finalComponentOID);
                        //pflichtenheftEntity = service.readPflichtenheft(pflichtenheftEntity.getProjektOid());
                        //reloadContent();
        //                Notification.show("Erhalt!");
                    });
                }else{
                    ui.access(() -> {
                        pflichtenheftEntity = service.readPflichtenheft(pflichtenheftEntity.getProjektOid());
                        reloadContent();
                    });
                }
                /*ui.access(() -> {
                    // TODO: fehlerhafte Stelle!!!
                    if (finalComponentOID != null) {
                        //ComponentModel componentModel = components.stream().filter(c -> c.getComponentOID() == componentOID).findFirst().get();
                        //componentModel.reloadContent();
                        reloadSpecificContent(finalComponentOID);
                        updateComponentView();
                        //Notification.show("EINS LADEN");
                    } else {
                        //updateComponentView();
                        pflichtenheftEntity = service.readPflichtenheft(pflichtenheftEntity.getProjektOid());
                        //Notification.show("ALLES NEU LADEN");
                        reloadContent();
                    }
                    //pflichtenheftEntity = service.readPflichtenheft(pflichtenheftEntity.getProjektOid());
                    //System.err.println("!!!");
                    //reloadContent();
                });*/
            }else if(currentChapter != currentchapterMitgabe && pflichtenheftEntity.getProjektOid() == projektOid && !user.equals(currentUser)){
                //"Es wurde ein Kapitel geändert (von jmd anderen) zu dem der Nutzer gehört aber dessen Kapitel nicht geöffnet ist"
                //=> Änderung muss in internen pflichtenheftEntity übernommen werden

                //Eine Möglichkeit wäre:
                pflichtenheftEntity = service.readPflichtenheft(pflichtenheftEntity.getProjektOid());
                //Kann es zu Kollisionen kommen ? Nur wenn Nutzer A eine Änderung macht und Nutzer B gerade eine Änderung macht und nicht mehr weiterschreibt
                //=> Eher unwahrscheinlich, selbst wenn, fehlen höchsten paar Wörter
                //Optimaler wäre sowas wie:
                //PflichtenheftEntity pRemote = service.readPflichtenheft(pflichtenheftEntity.getProjektOid())
                //pflichtenheftEntity.getKapitel().get(...).set(Geänderte Kapitel)
            }
        });
    }

    private void updateComponentViewOptimized(Integer finalComponentOID) {
        pflichtenheftEntity = service.readPflichtenheft(pflichtenheftEntity.getProjektOid());
        //ermitteln welche Komponente einen Fokus hatte
        //ComponentModel componentModel = components.stream().filter(c -> c.getComponentOid() == finalComponentOID).findFirst().get();

        //for ( ComponentModel c : components) {
        //    System.err.println(c.getComponent().getChildren().findFirst().get().getChildren().findFirst().get().getChildren().findFirst().get().getClass().toString());
        //}


        this.resetComponents();
        this.loadChapterComponents();
    }

    public EditorBar(SpecificationsService service, PflichtenheftEntity pflichtenheft) {
        this.currentUsername = SecurityService.getLoggedInUsername();
        this.service = service;
        components = new ArrayList<>();

        this.pflichtenheftEntity = pflichtenheft;
        updateComponentView();
    }

    /**
     * Die Methode aktualisiert alle zugehörigen Komponenten (Text, Abbildungen, Tabellen) im Editor
     */
    private void updateComponentView() {
        removeAll();
        Div tempDiv = new Div();
        tempDiv.setSizeFull();
        tempDiv.getStyle().set("overflow-y", "auto");
        VerticalLayout tempLayout = new VerticalLayout();
        //tempLayout should be scrollable

        tempLayout.add(getToolbar());
        for (ComponentModel c : components) {
            tempLayout.add(c.getComponent());
        }
        tempDiv.add(tempLayout);
        //add(tempLayout);
        add(tempDiv);

    }

    private void moveComponentUp(Component c) {
        ComponentModel swapModel = components.stream().filter(
                cmp -> cmp.getComponent().equals(c)).findFirst().get();

        int index = components.indexOf(swapModel);
        if (index == 0) {
            Notification.show("Komponente kann nicht nach oben verschoben werden!");
            return;
        }
        Collections.swap(components, index, index - 1);
        updateComponentView();
        setChangesForBroadcast(null);
    }

    private void moveComponentDown(Component c) {
        ComponentModel swapModel = components.stream().filter(
                cmp -> cmp.getComponent().equals(c)).findFirst().get();

        int index = components.indexOf(swapModel);
        if (index == components.size() - 1) {
            Notification.show("Komponente kann nicht nach unten verschoben werden!");
            return;
        }
        Collections.swap(components, index, index + 1);
        updateComponentView();
        setChangesForBroadcast(null);
    }

    private void removeComponent(Component c) {
        ComponentModel swapModel = components.stream().filter(
                cmp -> cmp.getComponent().equals(c)).findFirst().get();

        components.remove(swapModel);
        updateComponentView();
        setChangesForBroadcast(null);
    }

    public void resetComponents() {
        this.components = new ArrayList<>();
        updateComponentView();
    }

    /**
     * Die Methode soll zu einem bestimmten Kaptitel, die Inhaltskomponenten laden und im Editor anzeigen.
     */
    private void loadChapterComponents() {
        for (KapitelEntity kapitelEntity : pflichtenheftEntity.getKapitel()) {
            if (kapitelEntity.getKapitelVordefiniert().getKapitelVordefiniertOid() == currentChapter){
                Set<InhaltEntity> inhalte = kapitelEntity.getInhalte();

                for (InhaltEntity inhaltEntity: inhalte.stream().sorted(Comparator.comparing(InhaltEntity::getAnordnungIndex)).collect(Collectors.toList())) {
                    if(inhaltEntity.getTextinhalt() != null){
                        components.add( new ComponentModel( inhaltEntity.getInhaltOid(),
                            addComponent("Textfeld", inhaltEntity.getTextinhalt().getTextInhalt(), null,null,inhaltEntity.getInhaltOid() )));
                    }else if (inhaltEntity.getTabelle() != null){
                        components.add( new ComponentModel( inhaltEntity.getInhaltOid(),
                            addComponent("Tabelle", inhaltEntity.getTabelle().getTabellenUnterschrift(), null, inhaltEntity.getTabelle(),inhaltEntity.getInhaltOid())) );
                    }else if (inhaltEntity.getAbbildungsinhalt() != null){
                        components.add( new ComponentModel(inhaltEntity.getInhaltOid(),
                            addComponent("Abbildung", inhaltEntity.getAbbildungsinhalt().getBildUnterschrift(), inhaltEntity.getAbbildungsinhalt().getBildInhalt(),null,inhaltEntity.getInhaltOid() )));
                    }else{
                        String errorMessage = "FEHLER: Ungültige Komponente in Datenbank gefunden!";
                        Notification.show(errorMessage);
                        System.err.println(errorMessage);
                    }
                }
                updateComponentView();
                return;
            }
        }
    }

    /**
     * Die öffentliche Methode, soll aus der PflichtenheftEditor Klasse bedient werden, und bei Umselektierung
     * eines Kapitels, diese hier in der laufenden Instanz ändern.
     */
    public void setChapter(int kapitel){
        this.currentChapter = kapitel;
        this.resetComponents();
        this.loadChapterComponents();
    }

    private void reloadContent(){
        this.resetComponents();
        this.loadChapterComponents();
    }

    private void reloadSpecificContent(int inhaltOid){
        // gucken welche componte aus coomponent oid
        // dessen inhalt laden austauschen
        // reload content
        /*components.stream().filter(cmp -> cmp.getComponentOid() == inhaltOid).findFirst().get().setComponent(
            addComponent("Textfeld", service.readInhalt(inhaltOid).getTextinhalt().getTextInhalt(), null,null,inhaltOid )) ;*/
     //   TextArea text= components.stream().filter(cmp -> cmp.getComponentOid() == inhaltOid).findFirst().get().getComponent().getChildren().findFirst().get().getChildren().findFirst().get().;
      //  ;

     //   Component temp = component.getComponent().getChildren().findFirst().get().getChildren().findFirst().get();

        TextArea temp123 = (TextArea) components.stream().filter(cmp->cmp.getComponentOid()==inhaltOid).findFirst().get().getComponent().getChildren().findFirst().get()
                .getChildren().findFirst().get();  //component.getComponent().getChildren().findFirst().get().getChildren().findFirst().get();
        boolean focused = temp123.isAutofocus();
        System.err.println(temp123.getClass());
        temp123.setValue(service.readInhalt(inhaltOid).getTextinhalt().getTextInhalt());
        if (focused)
            temp123.focus();
    }

    /**
     * Die Methode ermöglicht es, bei der Erstellung einer Komponente (Textfeld, Abbildung, ...), dass zugehörige Buttons
     * zur Verschiebung der Komponente nach oben oder unten zu erstellen, oder zur Löschung erstellt werden und neben der
     * Komponente angezeigt werden
     * @return Toolbar zu einer bestimmten Komponente
     */
    private VerticalLayout getComponentToolbar(Component tempLayout){
        VerticalLayout buttonLayout = new VerticalLayout();
        Button upButton = new Button(new Icon(VaadinIcon.ARROW_UP));
        upButton.addClickListener(click1 -> {
            moveComponentUp(tempLayout);
        });
        Button downButton = new Button(new Icon(VaadinIcon.ARROW_DOWN));
        downButton.addClickListener(click1 -> {
            moveComponentDown(tempLayout);
        });
        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(click1 -> {
            removeComponent(tempLayout);
        });
        buttonLayout.add(upButton, downButton, deleteButton);
        return buttonLayout;
    }

    /**
     * Die Methode erstellt eine Komponente (Text, Abbildung, Tabelle) mit der dazugehörigen Toolbar.
     * @return Component
     */
    private HorizontalLayout addComponent(String component, String textInhalt, byte [] bytes, TabellenEntity tabelle, Integer componentOID){
        if (component == null) {
            Notification.show("Bitte wählen Sie eine Komponente aus!");
            throw new IllegalArgumentException("Bitte wählen Sie eine Komponente aus!");
        }

        HorizontalLayout tempLayout = new HorizontalLayout();
        tempLayout.setWidth("100%");
        VerticalLayout buttonLayout = getComponentToolbar(tempLayout);
        Div div = new Div();
        div.setWidth("200%");

        switch (component){
            case "Textfeld":
                TextArea textArea = new TextArea();
                textArea.setValue(textInhalt);

                div.add(textArea);
                textArea.setHeightFull();
                textArea.setWidthFull();
                textArea.setValueChangeMode(ValueChangeMode.LAZY);
                textArea.addValueChangeListener(event -> {
                    setChangesForBroadcast(componentOID);//TODO 1
                });
                tempLayout.add(div);
                tempLayout.add(buttonLayout);
                break;
            case "Abbildung":
                CustomPicUploadWithCaption pic = new CustomPicUploadWithCaption();

                if (bytes != null) {
                    pic.setBytes(bytes);
                    pic.setCaptionText(textInhalt);
                }
                div.add(pic);
                pic.addChangeListener(event -> setChangesForBroadcast(componentOID)); //TODO 2
                tempLayout.add(div);
                pic.setWidth("100%");
                tempLayout.add(buttonLayout);
                break;
            case "Tabelle":

                ArrayList<String> columnNames = new ArrayList<>();
                if (tabelle != null){

                    // Tabelle wird aus der DB geladen und angezeigt
                    ArrayList<String> captions = new ArrayList<>();
                    captions.addAll(Arrays.asList(tabelle.getSpaltenCaption1(), tabelle.getSpaltenCaption2(),
                            tabelle.getSpaltenCaption3(), tabelle.getSpaltenCaption4(), tabelle.getSpaltenCaption5()));
                    for (String caption: captions) {
                        if (caption != null)
                            columnNames.add(caption);
                    }
                    CustomGrid grid = new CustomGrid(columnNames, e-> setChangesForBroadcast(componentOID)); //TODO 3

                    if (textInhalt != null)
                        grid.setCaptionText(textInhalt);
                    grid.setWidth("160%");
                    ArrayList <GridRow> rows = new ArrayList<>();

                    tabelle.getZellen().stream().sorted(Comparator.comparing(TabellenzeileEntity::getAnordnungsIndex)).collect(Collectors.toList()).forEach(zelle -> {
                        ArrayList<String> row = new ArrayList<>();
                        row.addAll(Arrays.asList(zelle.getZellenWert1(), zelle.getZellenWert2(), zelle.getZellenWert3(), zelle.getZellenWert4(), zelle.getZellenWert5()));
                        rows.add(new GridRow(row));
                        //grid.addItem(zelle);
                    });
                    grid.setData(rows);

                    div.add(grid);
                    grid.setWidth("100%");
                    tempLayout.add(div);
                    tempLayout.add(buttonLayout);
                    grid.addCaptionChangeListener(e -> setChangesForBroadcast(componentOID)); //TODO 3
                }else{
                    // leere Tabelle wird erzeugt
                    Dialog dialog = new Dialog();
                    dialog.add(new TabelleErstellenView(spaltenNamenList -> {
                        columnNames.addAll(spaltenNamenList);
                        System.out.println("Spaltennamen: " + spaltenNamenList);
                        //Dialog Schließen
                        UI.getCurrent().getPage().executeJs("document.querySelector('vaadin-dialog-overlay').close()");

                        CustomGrid grid = new CustomGrid(columnNames, e->{setChangesForBroadcast(componentOID);}); //TODO 3
                        div.add(grid);
                        tempLayout.add(div);
                        tempLayout.add(buttonLayout);
                        grid.addCaptionChangeListener(e -> setChangesForBroadcast(componentOID)); //TODO 4
                        setChangesForBroadcast(null);
                    }));
                    dialog.setHeight("90%");
                    dialog.setWidth("80%");
                    dialog.open();
                }
                break;
        }
        return tempLayout;
    }

    private void setChangesForBroadcast(Integer componentOID ) {
        speichernBtn.click();
        String currentUser = this.currentUsername;

        String[] values = {
                String.valueOf(pflichtenheftEntity.getProjektOid()),
                String.valueOf(currentChapter),
                currentUser,
                (componentOID != null) ? String.valueOf(componentOID) : null
        };

        EditorBroadcaster.broadcast(values);
    }

    /**
     * Ermöglicht es eine Komponente hinzuzufügen und stellt die Toolbar dar, um das Erstellen der Komponente
     * zu ermöglichen
     * @return Toolbar bestehend aus einem Hinzufügen-Button, ComboBox und einem Speichern-Button
     */
    private Component getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addClassName("toolbar");
        Button addBtn = new Button("Hinzufügen");
        this.cb = new ComboBox<>();
        cb.setItems("Textfeld", "Abbildung", "Tabelle");
        cb.setValue("Textfeld");

        addBtn.addClickListener(click -> {
            HorizontalLayout tempLayout = this.addComponent(cb.getValue(), "", null, null,null);
            this.components.add( new ComponentModel(null, tempLayout) );
            if (cb.getValue() == "Textfeld" || cb.getValue() == "Abbildung")
                setChangesForBroadcast(null);
            updateComponentView();

        });

        toolbar.add(addBtn, cb);
        speichernBtn = new Button("Speichern");

        speichernBtn.addClickListener(click -> {
            System.out.println("FIRE");
            int anordnung = 0;
            KapitelEntity kapitel = null;

            if (pflichtenheftEntity.getKapitel().stream().filter(k -> k.getKapitelVordefiniert().getKapitelVordefiniertOid() == currentChapter).findFirst().isPresent()){
                kapitel = pflichtenheftEntity.getKapitel().stream().filter(k -> k.getKapitelVordefiniert().getKapitelVordefiniertOid() == currentChapter).
                        findFirst().get();
                System.err.println(kapitel);
            }else{
                kapitel = new KapitelEntity();
                kapitel.setKapitelVordefiniert(new KapitelvordefiniertEntity());
                kapitel.getKapitelVordefiniert().setKapitelVordefiniertOid(currentChapter);
                kapitel.setProjekt(pflichtenheftEntity);
                pflichtenheftEntity.getKapitel().add(kapitel);
                service.saveKapitel(kapitel);
            }

            Set<InhaltEntity> deleteList = new HashSet<>(kapitel.getInhalte());

            for (ComponentModel component : components) {
                InhaltEntity inhalt = null;
                if (component.getComponentOid() != null) {
                    // ÄNDERUNG
                    inhalt = kapitel.getInhalte().stream().filter(i -> i.getInhaltOid() == component.getComponentOid()).findFirst().get();
                } else {
                    // NEU ERSTELLUNG
                    inhalt = new InhaltEntity();
                    kapitel.getInhalte().add(inhalt);
                }
                deleteList.remove(inhalt);
                inhalt.setKapitelOid(kapitel.getKapitelOid());

                Component temp = component.getComponent().getChildren().findFirst().get().getChildren().findFirst().get();
                if (temp instanceof TextArea){
                    if (inhalt.getTextinhalt() == null){
                        TextinhaltEntity textinhalt = new TextinhaltEntity();
                        service.saveTextinhalt(textinhalt);
                        inhalt.setTextinhalt(textinhalt);
                    }
                    inhalt.getTextinhalt().setTextInhalt(((TextArea) temp).getValue());
                    service.saveTextinhalt(inhalt.getTextinhalt());
                }else if(temp instanceof CustomPicUploadWithCaption){
                    if (inhalt.getAbbildungsinhalt() == null){
                        AbbildungsinhaltEntity abbildungsinhalt = new AbbildungsinhaltEntity();
                        service.saveAbbildungsinhalt(abbildungsinhalt);
                        inhalt.setAbbildungsinhalt(abbildungsinhalt);
                    }
                    try {
                        inhalt.getAbbildungsinhalt().setBildInhalt(((CustomPicUploadWithCaption) temp).getImgBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    inhalt.getAbbildungsinhalt().setBildUnterschrift(((CustomPicUploadWithCaption) temp).getCaptionText());
                    service.saveAbbildungsinhalt(inhalt.getAbbildungsinhalt());
                }else if(temp instanceof CustomGrid){

                    if (inhalt.getTabelle() == null) {
                        TabellenEntity newTable = new TabellenEntity();
                        service.saveTable(newTable);
                        inhalt.setTabelle(newTable);
                    }
                    inhalt.getTabelle().setTabellenUnterschrift(((CustomGrid) temp).getCaptionText());
                    ArrayList<String> captions = ((CustomGrid) temp).getColumnNames();
                    inhalt.getTabelle().setSpaltenCaption1(captions.size() >=1? captions.get(0):null);
                    inhalt.getTabelle().setSpaltenCaption2(captions.size() >=2? captions.get(1):null);
                    inhalt.getTabelle().setSpaltenCaption3(captions.size() >=3? captions.get(2):null);
                    inhalt.getTabelle().setSpaltenCaption4(captions.size() >=4? captions.get(3):null);
                    inhalt.getTabelle().setSpaltenCaption5(captions.size() >=5? captions.get(4):null);

                    service.saveTable(inhalt.getTabelle());

                    Set<TabellenzeileEntity> deleteList2 = new HashSet<>(inhalt.getTabelle().getZellen());
                    for ( GridRow row : ((CustomGrid) temp).getData() ) {
                        for ( String test : row.getContent() ) {
                            System.err.print(test+"  ");
                        }
                        System.err.println();
                    }

                    int index = 0;
                    // FIXME SEHR UNOPTIMIERT. ERST ALLES LÖSCHEN UND DANN HINZUFÜGEN, damit bei Löscheung und Hinzufügen die gleiche ID verwendet wird
                    // TODO Mapping der einzelnen Zeilen mit der DB-OID, damit man Löschung und Hinzufügen vermeiden kann
                    for ( TabellenzeileEntity tabellenzeileEntity: deleteList2 ) {
                        inhalt.getTabelle().getZellen().remove(tabellenzeileEntity);
                        try{service.deleteTabellenzeile(tabellenzeileEntity);}catch(Exception e){}
                    }
                    inhalt.getTabelle().setZellen(new HashSet<>());

                    for (GridRow row : ((CustomGrid) temp).getData()) {
                        int finalIndex2 = index;
                        TabellenzeileEntity tabellenzeile = null;
                        int finalIndex = index;
                        if (inhalt.getTabelle().getZellen().stream().filter(z -> z.getAnordnungsIndex() == finalIndex).findFirst().isPresent()){
                            int finalIndex1 = index;
                            tabellenzeile = inhalt.getTabelle().getZellen().stream().filter(z -> z.getAnordnungsIndex() == finalIndex1).findFirst().get();
                        }else{
                            tabellenzeile = new TabellenzeileEntity();
                            tabellenzeile.setTabellenOid(inhalt.getTabelle().getTabellenOid());
                            inhalt.getTabelle().getZellen().add(tabellenzeile);
                        }

                        tabellenzeile.setAnordnungsIndex(index);

                        ArrayList<String> rows = row.getContent();
                        tabellenzeile.setZellenWert1(rows.size() >=1? rows.get(0):"");
                        tabellenzeile.setZellenWert2(rows.size() >=2? rows.get(1):"");
                        tabellenzeile.setZellenWert3(rows.size() >=3? rows.get(2):"");
                        tabellenzeile.setZellenWert4(rows.size() >=4? rows.get(3):"");
                        tabellenzeile.setZellenWert5(rows.size() >=5? rows.get(4):"");

                        service.saveTabellenzeile(tabellenzeile);
                        index++;
                    }
                }
                inhalt.setAnordnungIndex(anordnung);
                inhalt = service.saveInhalt(inhalt);
                component.setComponentOid(inhalt.getInhaltOid());//HOTFIX geändert, damit der Inhalt nicht neu erstellt und gelöscht wird!!!
                ++anordnung;
            }
            for(InhaltEntity inhalt : deleteList) {
                kapitel.getInhalte().removeIf(i -> i.getInhaltOid() == inhalt.getInhaltOid());
                if (inhalt.getTabelle() != null)
                    service.deleteTable(inhalt.getTabelle());
                if (inhalt.getTextinhalt() != null)
                    service.deleteTextinhalt(inhalt.getTextinhalt());
                if (inhalt.getAbbildungsinhalt() != null)
                    service.deleteAbbildungsinhalt(inhalt.getAbbildungsinhalt());
                service.deleteInhalt(inhalt);
            }
        });
        toolbar.add(speichernBtn);
        return toolbar;
    }
}