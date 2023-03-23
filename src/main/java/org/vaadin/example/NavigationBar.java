package org.vaadin.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.example.views.*;
import org.vaadin.example.views.editor.PflichtenheftEditor;
import org.vaadin.example.views.projectoverview.OpenProjectOverview;
import org.vaadin.example.views.projectoverview.ProjectOverview;

public class NavigationBar extends AppLayout {
    private static NavigationBar instance = null;
    private NavigationBar() {
        Tab overview = new Tab();
        overview.add(new RouterLink("Übersicht", ProjectOverview.class));
        Tab homeTab = new Tab();
        Tab erstellen = new Tab();
        erstellen.add(new RouterLink("Projekt erstellen", ProjektErstellenView.class));
        Tab openProjects = new Tab();
        openProjects.add(new RouterLink("Offene Projekte", OpenProjectOverview.class));
        /*homeTab.add(new RouterLink("Profil editieren", ProjektErstellenView.class));
        Tab detail1 = new Tab();
        detail1.add(new RouterLink("Projekt Detail mit Export", ProjektDetailMitExport.class));
        Tab detail2 = new Tab();
        detail2.add(new RouterLink("Projekt Detail", ProjektDetailView.class));*/

    Tab test = new Tab();
        //erstellen.add(new RouterLink("Offene Projektliste", OpenProjectOverview.class));
        test.add(new RouterLink("Projekt-Editor", PflichtenheftEditor.class));
        //erstellen.add(new RouterLink("Projektliste", ProjectOverview.class));

        //Tabs tabs = new Tabs(homeTab, detail1, detail2, erstellen);
        Tabs tabs = new Tabs(overview, erstellen, openProjects,test);
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        addToNavbar(tabs);
    }
    public static NavigationBar getInstance() {
        if (instance == null) {
            instance = new NavigationBar();
        }
        return instance;
    }
}



