package org.vaadin.example.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class CustomPicUploadWithCaptionAndScaling extends VerticalLayout {
    private CustomPicUpload picUpload;
    private TextField caption;

    public CustomPicUploadWithCaptionAndScaling(){
        picUpload = new CustomPicUpload();
        caption = new TextField("Abbildungsbezeichnung");

        add(picUpload, caption);
    }

    public byte[] getBytes() {
        return picUpload.getBytes();
    }

    public String getCaptionText(){
        return caption.getValue();
    }

    public void setBytes(byte[] bytes) {
        picUpload.setBytes(bytes);
    }

    public void setCaptionText(String text){
        caption.setValue(text);
    }
}
