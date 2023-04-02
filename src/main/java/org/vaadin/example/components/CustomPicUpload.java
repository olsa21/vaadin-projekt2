package org.vaadin.example.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

public class CustomPicUpload extends HorizontalLayout {
    private MemoryBuffer buffer;
    private Upload upload;

    private Image image = new Image();

    public CustomPicUpload() {
        buffer = new MemoryBuffer();
        upload = new Upload(buffer);

        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setMaxFileSize(16777216);
        upload.setDropAllowed(true);
        upload.setUploadButton(new Button("Datei hochladen"));

        upload.addSucceededListener(event -> {
            image.setSrc("data:image/png;base64," + DatatypeConverter.printBase64Binary(getBytes()));
            image.setHeight("100px");
            image.setWidth("100px");
        });

        add(upload, image);
    }

    public byte[] getBytes() {
        try {
            return buffer.getInputStream().readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
