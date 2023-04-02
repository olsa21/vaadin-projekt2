package org.vaadin.example;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.example.components.ClickableAvatar;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;
import org.vaadin.example.views.ProjektErstellenView;
import org.vaadin.example.views.projectoverview.OpenProjectOverview;
import org.vaadin.example.views.projectoverview.ProjectOverview;


public class MainLayout extends AppLayout {
    private final SecurityService securityService;
    private final SpecificationsService service;
    MainLayout(SecurityService securityService, SpecificationsService service){
        this.securityService = securityService;
        this.service = service;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Pflichtenheft-Editor");
        //logo.addClassNames("text-l", "m-m");
        logo.getStyle().set("font-size", "var(--lumo-font-size-l)");

        MitarbeiterEntity mitarbeiter = service.findSpecificUser(SecurityService.getLoggedInUsername());
        ClickableAvatar clickableAvatar = new ClickableAvatar(mitarbeiter.getVorname() + " " + mitarbeiter.getNachname());
        clickableAvatar.setPicture(mitarbeiter.getProfilbild());


        Button logOut = new Button("Abmelden", e -> {
            securityService.logout();
        });

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, clickableAvatar, logOut);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.DASHBOARD, "Projektübersicht", ProjectOverview.class),
                createTab(VaadinIcon.CART, "Projekterstellung", ProjektErstellenView.class),
                createTab(VaadinIcon.USER_HEART, "Projektliste (offen)", OpenProjectOverview.class));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class<?> classLink) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        // Demo has no routes
        link.setRoute((Class<? extends Component>) classLink);
        link.setTabIndex(-1);

        return new Tab(link);
    }

    private void createDrawer() {
        RouterLink projectOverview = new RouterLink("Projektübersicht", ProjectOverview.class);
        projectOverview.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink projectCreateView = new RouterLink("Projekt erstellen", ProjektErstellenView.class);
        projectCreateView.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink openProjectsView = new RouterLink("Offene Projekte", OpenProjectOverview.class);

        /**addToDrawer(new VerticalLayout(
         projectOverview,
         projectCreateView,
         openProjectsView
         ));*/
        addToDrawer(
            getTabs()
        );
    }


}
