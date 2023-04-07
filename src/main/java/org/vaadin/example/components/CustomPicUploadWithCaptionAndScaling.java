package org.vaadin.example.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class CustomPicUploadWithCaptionAndScaling extends VerticalLayout {
    CustomPicUpload picUpload;
    TextField caption;
    public CustomPicUploadWithCaptionAndScaling(){
        picUpload = new CustomPicUpload();
        caption = new TextField("Abbildungsbezeichnung");

        add(picUpload, caption);
    }
}
