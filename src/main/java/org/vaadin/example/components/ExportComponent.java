package org.vaadin.example.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Export Komponente mit ComboBox (PDF, Word) mit Button
 */
public class ExportComponent extends VerticalLayout {
    public ComboBox<String> exportCombo = new ComboBox<>();
    public Button exportButton = new Button("Exportieren");
    public ExportComponent() {
        exportCombo.setItems("PDF", "Word");
        exportCombo.setValue("PDF");
        add(new HorizontalLayout(new Text("Export"), exportCombo, exportButton));

        getChildren().forEach(item -> {
            if (item instanceof HorizontalLayout) {
                ((HorizontalLayout) item).setAlignItems(Alignment.BASELINE);
            }
        });
    }
}
