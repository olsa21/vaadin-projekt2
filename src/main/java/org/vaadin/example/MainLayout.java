package org.vaadin.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.example.views.ProjektErstellenView;
import org.vaadin.example.views.projectoverview.OpenProjectOverview;
import org.vaadin.example.views.projectoverview.ProjectOverview;


public class MainLayout extends AppLayout {
    MainLayout(){
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Pflichtenheft-Editor");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink projectOverview = new RouterLink("Projektübersicht", ProjectOverview.class);
        projectOverview.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink projectCreateView = new RouterLink("Projekt erstellen", ProjektErstellenView.class);
        projectCreateView.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink openProjectsView = new RouterLink("Offene Projekte", OpenProjectOverview.class);

        addToDrawer(new VerticalLayout(
                projectOverview,
                projectCreateView,
                openProjectsView
        ));
    }


}
