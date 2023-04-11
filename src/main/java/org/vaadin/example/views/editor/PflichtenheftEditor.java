package org.vaadin.example.views.editor;


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
import org.vaadin.example.model.ChapterModel;
import org.vaadin.example.service.SpecificationsService;
import org.vaadin.example.views.ProjektDetailMitExport;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;

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


    public PflichtenheftEditor(SpecificationsService service){
        System.out.println("=>2");
        this.service = service;

        addClassName("editor-view");
        setSizeFull();
        setWidthFull();
    }

    private VerticalLayout createChapterOverview(){
        chapterOverview = new TreeGrid<>();
        chapterOverview.setHeightFull();

        //Service wird erstmal mitgegeben
        ArrayList<ChapterModel> root = SpecificationBookChapters.getInstance(service).getChapter( null );
        //chapterOverview.setItems(root, SpecificationBookChapters.getInstance(service)::getChapter);
        chapterOverview.setItems(root, SpecificationBookChapters.getInstance(service)::getChapter);
        //chapterOverview.addHierarchyColumn(String::toString).setSortable(false);
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
                //editBar = new EditorBar();
                editBar.setWidth("60%");
                add(editBar);
                Notification.show(event.getItem().getChapterName());
                editBar.setChapter( event.getItem().getChapterOid() ); //TODO:
            }
        });

        return verticalLayout;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        pflichtenheftOid = Integer.parseInt(s);
        pflichtenheftEntity = service.readPflichtenheft(pflichtenheftOid);
        editBar = new EditorBar(service, pflichtenheftEntity);
        Notification.show(pflichtenheftEntity.getTitel());
        Notification.show("Parameter: " + s);
        System.out.println("=>1");
        add(createChapterOverview());
        projektDetailMitExport = new ProjektDetailMitExport(this.service, pflichtenheftOid);
    }
}






