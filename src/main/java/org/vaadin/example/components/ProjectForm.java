package org.vaadin.example.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class ProjectForm extends FormLayout {
    public TextField titel = new TextField();
    public TextArea beschreibung = new TextArea();
    public DatePicker frist = new DatePicker();
    public TextField repo = new TextField();
    public Button speichern = new Button("Speichern");

    public ProjectForm() {
        setResponsiveSteps(
            new FormLayout.ResponsiveStep("0",1)
        );
        addFormItem(titel, "Titel");
        addFormItem(beschreibung, "Beschreibung");
        addFormItem(frist, "Frist");
        addFormItem(repo, "Repository");
        addFormItem(speichern, "");
        //add(

                //new HorizontalLayout(new Text("Titel"), titel),
                //new HorizontalLayout(new Text("Beschreibung"), beschreibung),
                //new HorizontalLayout(new Text("Frist"), frist),
                //new HorizontalLayout(new Text("Repository"), repo),
          //      speichern
        //);

        /*getChildren().forEach(item -> {
            if (item instanceof HorizontalLayout) {
                ((HorizontalLayout) item).setAlignItems(FlexComponent.Alignment.BASELINE);
            }
        });*/
    }
}
