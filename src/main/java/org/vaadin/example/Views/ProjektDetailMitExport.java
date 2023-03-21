package org.vaadin.example.Views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.example.NavigationBar;

@Route("ProjektDetailMitExport")
public class ProjektDetailMitExport extends VerticalLayout {
    public ProjektDetailMitExport() {
        ComboBox<String> export = new ComboBox<>();
        export.setItems("PDF", "Word");
        export.setValue("PDF");

        add(NavigationBar.getInstance());
        add("ProjektDetailMitExport");
        add(new ProjektDetailView());//Beitreten Button separat einfügen wie unten
        add(new HorizontalLayout(new Button("Exportieren"), export));
    }
}
