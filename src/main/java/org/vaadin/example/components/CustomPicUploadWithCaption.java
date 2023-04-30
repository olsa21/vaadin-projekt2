package org.vaadin.example.components;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.io.IOException;

/**
 * Die Klasse ist dafür zuständig, dass ein Bild mit einer Bildunterschrift hochgeladen werden kann.
 * Diese Komponente ist speziell für den Editor des Pflichtenheftes gedacht, da ein Benutzer Abbildungen
 * hochladen können soll mit den dazugehörigen Bildunterschriften.
 */
public class CustomPicUploadWithCaption extends VerticalLayout {
    private CustomPicUpload picUpload;
    private TextArea caption;

    /**
     * Standard-Konstruktor, welcher die Komponenten initialisiert und hinzufügt.
     */
    public CustomPicUploadWithCaption(){
        picUpload = new CustomPicUpload();
        caption = new TextArea("Abbildungsbezeichnung");
        add(picUpload, caption);
    }

    /**
     * Methode zum Hinzufügen eines ChangeListeners, welcher aufgerufen wird, wenn ein Bild erfolgreich
     * hochgeladen wurde und die Bildunterschrift geändert wurde.
     * @param listener der ChangeListener
     */
    public void addChangeListener(ComponentEventListener<SucceededEvent> listener) {
        if (listener == null){
            throw new IllegalArgumentException("Listener darf nicht null sein!");
        }
        picUpload.addUploadSucceededListener(listener);
        caption.setValueChangeMode(ValueChangeMode.LAZY);
        caption.addValueChangeListener(event -> {
            if (event.getValue() != null && !event.getValue().isEmpty()){
                listener.onComponentEvent(null);
            }
        });
    }

    /**
     * Methode, welche das Bild (speziell das im Buffer) als Byte-Array zurückgibt.
     * @return das Bild als Byte-Array
     */
    public byte[] getBytes() {
        return picUpload.getBytesBuffer();
    }

    /**
     * Methode, welche die Bildunterschrift zurückgibt.
     * @return die Bildunterschrift
     */
    public String getCaptionText(){
        return caption.getValue();
    }

    /**
     * Methode, welche das Bild setzt.
     * @param bytes das Bild als Byte-Array
     */
    public void setBytes(byte[] bytes) {
        picUpload.setImage(bytes);
    }

    /**
     * Methode, welche das Bild als Byte-Array zurückgibt.
     * @return das Bild als Byte-Array
     * @throws IOException
     */
    public byte[] getImgBytes() throws IOException {
        return picUpload.getImgBytes();
    }

    /**
     * Methode, welche die Bildunterschrift setzt.
     * @param text die Bildunterschrift
     */
    public void setCaptionText(String text){
        caption.setValue(text);
    }
}
