package org.vaadin.example.components;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.data.value.ValueChangeMode;

public class CustomPicUploadWithCaption extends VerticalLayout {
    private CustomPicUpload picUpload;
    private TextField caption;

    public CustomPicUploadWithCaption(){
        picUpload = new CustomPicUpload();
        caption = new TextField("Abbildungsbezeichnung");

        add(picUpload, caption);
    }

    public void addChangeListener(ComponentEventListener<SucceededEvent> listener) {
        picUpload.addUploadSucceededListener(listener);
        caption.setValueChangeMode(ValueChangeMode.LAZY);
        caption.addValueChangeListener(event -> {
            if (event.getValue() != null && !event.getValue().isEmpty()){
                listener.onComponentEvent(null);
            }
        });
    }
    public byte[] getBytes() {
        return picUpload.getBytesBuffer();
    }

    public String getCaptionText(){
        return caption.getValue();
    }

    public void setBytes(byte[] bytes) {
        picUpload.setImage(bytes);
    }

    public byte[] getImgBytes(){
        return picUpload.getImageBytes();
    }

    public void setCaptionText(String text){
        caption.setValue(text);
    }
}
