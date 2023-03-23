package org.vaadin.example;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.example.views.ProfilEditView;
import org.vaadin.example.views.ProjektErstellenView;

@Route("app-layout-navbar-placement")
// tag::snippet[]
public class AppLayoutNavbarPlacement extends AppLayout {

    public AppLayoutNavbarPlacement() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("MyApp");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        Tabs tabs = getTabs();

        addToDrawer(tabs);
        addToNavbar(toggle, title);
    }
    // end::snippet[]

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.DASHBOARD, "Übersicht", ProfilEditView.class),
                createTab(VaadinIcon.CART, "Projekterstellung", ProjektErstellenView.class),
                createTab(VaadinIcon.USER_HEART, "Projektliste (offen)", ProfilEditView.class),
                createTab(VaadinIcon.PACKAGE, "Products", ProfilEditView.class),
                createTab(VaadinIcon.RECORDS, "Documents", ProfilEditView.class),
                createTab(VaadinIcon.LIST, "Tasks", ProfilEditView.class),
                createTab(VaadinIcon.CHART, "Analytics", ProfilEditView.class));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class<?> profilEditViewClass) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        // Demo has no routes
        link.setRoute((Class<? extends Component>) profilEditViewClass);
        link.setTabIndex(-1);

        return new Tab(link);
    }

    // tag::snippet[]
}
// end::snippet[]