package org.vaadin.example.components;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomPicUpload extends HorizontalLayout {
    private MemoryBuffer buffer;
    private Upload upload;

    private Image image = new Image();
    private byte[] imageBytes;
    private byte[] bufferBytes;

    public CustomPicUpload() {
        buffer = new MemoryBuffer();
        upload = new Upload(buffer);

        upload.setAcceptedFileTypes("image/jpeg", "image/png");
        upload.setMaxFileSize(16777216);
        upload.setDropAllowed(true);
        upload.setUploadButton(new Button("Datei hochladen"));

        upload.addSucceededListener(event -> {
            bufferBytes = getBytesBuffer();
            image.setSrc("data:image/png;base64," + DatatypeConverter.printBase64Binary(bufferBytes));
            image.setHeight("100px");
            image.setWidth("100px");
        });

        add(upload, image);
    }


    public void clear() {
        imageBytes = null;
        bufferBytes = null;
        buffer = new MemoryBuffer();
        upload.setReceiver(buffer);
        upload.clearFileList();
        image.setSrc("");
    }

    public void addUploadSucceededListener(ComponentEventListener<SucceededEvent> listener) {
        upload.addSucceededListener(listener);
    public byte[] getImageBytes() {
        return imageBytes;
    }

    public byte[] getBufferBytes() {
        return bufferBytes;
    }

    public byte[] getDownScaledBytesBuffer() throws IOException {
        try {
            // Originalbild aus dem Upload holen
            InputStream inputStream = buffer.getInputStream();
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            // Bild auf 512 x 512 Pixel verkleinern
            BufferedImage resizedImage = Scalr.resize(bufferedImage, Scalr.Method.SPEED, Scalr.Mode.AUTOMATIC, 512, 512);

            // Bytes des verkleinerten Bildes als Base64 kodieren
            String fileName = buffer.getFileName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            System.out.println("fileExtension: " + fileExtension);
            switch (fileExtension){
                case "jpg": ImageIO.write(resizedImage, "jpg", byteArrayOutputStream);
                    break;
                case "png": ImageIO.write(resizedImage, "png", byteArrayOutputStream);
            }

            if(fileExtension.equals("jpg")) {
                ImageIO.write(resizedImage, "jpg", byteArrayOutputStream);
            } else if(fileExtension.equals("png")) {
                ImageIO.write(resizedImage, "png", byteArrayOutputStream);
            }else{
                throw new IllegalArgumentException("Dateiendung nicht unterstützt");
            }

            return byteArrayOutputStream.toByteArray();
        } catch (IOException | IllegalArgumentException | ImagingOpException e) {
            return null;
        }
    }


    public byte[] getBytesBuffer() {
        try {
            byte[] bytes = buffer.getInputStream().readAllBytes();
            if (bytes.length == 0) {
                return null;
            } else {
                return bytes;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public void setImage(byte[] bytes) {
        imageBytes = bytes;
        image.setSrc("data:image/png;base64," + DatatypeConverter.printBase64Binary(imageBytes));
        image.setHeight("100px");
        image.setWidth("100px");
    }
}
