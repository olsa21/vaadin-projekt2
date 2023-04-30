package org.vaadin.example.views.editor;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.example.views.MainLayout;
import org.vaadin.example.entity.PflichtenheftEntity;
import org.vaadin.example.model.ChapterModel;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;
import org.vaadin.example.views.project.ProjektDetailMitExport;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;

/**
 * Die Klasse ist dafür zuständig, dass der Pflichtenhefteditor angezeigt wird.
 * Dabei soll auf der linken Seite eine Baumstruktur mit den Kapiteln angezeigt werden.
 * Auf der rechten Seite soll entweder die Detailansicht eines Kapitels oder die Detailansicht des Projekts angezeigt werden.
 */
@PermitAll
@PageTitle("Projektübersicht")
@Route(value = "/project-editor", layout = MainLayout.class)
public class PflichtenheftEditor extends HorizontalLayout implements HasUrlParameter<String>
{
    private TreeGrid<ChapterModel> chapterOverview;
    private Button projektButton;
    private EditorBar editBar;
    private ProjektDetailMitExport projektDetailMitExport;
    final SpecificationsService service;
    PflichtenheftEntity pflichtenheftEntity;
    private int pflichtenheftOid;

    /**
     * Konstruktor, welcher den Pflichtenhefteditor initialisiert.
     * @param service Service, welcher für die Kommunikation mit der Datenbank zuständig ist.
     */
    public PflichtenheftEditor(SpecificationsService service){
        if (service == null) {
            throw new IllegalArgumentException("Service muss eine gültige Referenz besitzen!");
        }
        this.service = service;
        addClassName("editor-view");
        setSizeFull();
        setWidthFull();
    }

    /**
     * Methode, welche die Kapitelstruktur lädt und die baumartige Ansicht auf der linken Seite des Editors anzeigt.
     */
    private VerticalLayout createChapterOverview(){
        chapterOverview = new TreeGrid<>();
        chapterOverview.setHeightFull();

        ArrayList<ChapterModel> root = SpecificationBookChapters.getInstance(service).getChapter( null );
        chapterOverview.setItems(root, SpecificationBookChapters.getInstance(service)::getChapter);
        chapterOverview.addHierarchyColumn(ChapterModel::getChapterName).setSortable(false);
        projektButton = new Button("Pflichtenheft: " + pflichtenheftEntity.getTitel());

        VerticalLayout verticalLayout = new VerticalLayout(projektButton, chapterOverview);
        verticalLayout.setSpacing(false);
        verticalLayout.setWidth("40%");

        // Registrieren des Click-Listeners für den Button, sprich Detailanzeige zum Projekt
        projektButton.addClickListener(event -> {
            remove(editBar);
            projektDetailMitExport.setWidth("60%");
            add(projektDetailMitExport);
        });

        // Registrieren des Click-Listeners für ein Blatt des Baums, sprich Anzeige des Editors
        chapterOverview.addItemClickListener(event -> {
            if (!chapterOverview.getDataProvider().hasChildren(event.getItem())){
                remove(projektDetailMitExport);
                remove(editBar);
                editBar.setWidth("60%");
                add(editBar);
                Notification.show(event.getItem().getChapterName());
                editBar.setChapter( event.getItem().getChapterOid() );
            }
        });
        return verticalLayout;
    }

    /**
     * Methode, welche den Parameter des Pflichtenheftes aus der URL ausliest und das passende Pflichtenheft lädt.
     * @param beforeEvent
     * @param s
     */
    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        try {
            pflichtenheftOid = Integer.parseInt(s);
            pflichtenheftEntity = service.readPflichtenheft(pflichtenheftOid);

            if(!SecurityService.userIsMemberOf(pflichtenheftEntity)){
                UI.getCurrent().navigate("open-project-overview");
                return;
            }

            editBar = new EditorBar(service, pflichtenheftEntity);
            add(createChapterOverview());
            projektDetailMitExport = new ProjektDetailMitExport(this.service, pflichtenheftOid);
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            UI.getCurrent().navigate("open-project-overview");
        }
    }
}






