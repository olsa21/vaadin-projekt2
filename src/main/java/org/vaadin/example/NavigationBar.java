package org.vaadin.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.example.Views.ProjektDetailMitExport;
import org.vaadin.example.Views.ProjektDetailView;
import org.vaadin.example.Views.ProjektErstellenView;

public class NavigationBar extends AppLayout {
    private static NavigationBar instance = null;
    private NavigationBar() {
        Tab homeTab = new Tab();
        homeTab.add(new RouterLink("Profil editieren", ProjektErstellenView.class));
        Tab detail1 = new Tab();
        detail1.add(new RouterLink("Projekt Detail mit Export", ProjektDetailMitExport.class));
        Tab detail2 = new Tab();
        detail2.add(new RouterLink("Projekt Detail", ProjektDetailView.class));
        Tab erstellen = new Tab();
        erstellen.add(new RouterLink("Projekt erstellen", ProjektErstellenView.class));

        Tabs tabs = new Tabs(homeTab, detail1, detail2, erstellen);
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



