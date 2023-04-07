package org.vaadin.example.views.editor;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;
import org.vaadin.example.entity.PflichtenheftEntity;
import org.vaadin.example.model.Mitarbeiter;
import org.vaadin.example.model.Pflichtenheft;
import org.vaadin.example.service.SpecificationsService;
import org.vaadin.example.views.ProjektDetailMitExport;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;

@PermitAll
@PageTitle("Projektübersicht")
@Route(value = "/project-editor", layout = MainLayout.class)
public class PflichtenheftEditor extends HorizontalLayout implements HasUrlParameter<String>
{
    private TreeGrid<String> chapterOverview;
    private Button projektButton;
    private EditorBar editBar;
    private ProjektDetailMitExport projektDetailMitExport;
    final SpecificationsService service;
    PflichtenheftEntity pflichtenheftEntity;

    private int pflichtenheftOid;


    public PflichtenheftEditor(SpecificationsService service){
        System.out.println("=>2");
        this.service = service;

        pflichtenheftEntity = service.readPflichtenheft(pflichtenheftOid);
        System.err.println(pflichtenheftEntity);

        editBar = new EditorBar();

        addClassName("editor-view");
        setSizeFull();
        setWidthFull();

        add(createChapterOverview());

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
                editBar = new EditorBar();
                editBar.setWidth("60%");
                add(editBar);
            }
        });
    }

    private VerticalLayout createChapterOverview(){
        chapterOverview = new TreeGrid<>();
        chapterOverview.setHeightFull();

        //Service wird erstmal mitgegeben
        ArrayList<String> root = SpecificationBookChapters.getInstance(service).getChapter(null);
        chapterOverview.setItems(root, SpecificationBookChapters.getInstance(service)::getChapter);
        chapterOverview.addHierarchyColumn(String::toString).setSortable(false);

        projektButton = new Button("Pflichtenheft: " + pflichtenheftOid);

        VerticalLayout verticalLayout = new VerticalLayout(projektButton, chapterOverview);
        verticalLayout.setSpacing(false);
        verticalLayout.setWidth("40%");

        return verticalLayout;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        pflichtenheftOid = Integer.parseInt(s);
        Notification.show("Parameter: " + s);
        System.out.println("=>1");
        projektDetailMitExport = new ProjektDetailMitExport(this.service, pflichtenheftOid);
    }
}






