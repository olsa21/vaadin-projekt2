package org.vaadin.example.utility;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ResourceLoader {
    public static FileInputStream readFile(String filename){
        FileInputStream in = null;
        try {
            File file = ResourceUtils.getFile("classpath:" + filename);
            in = new FileInputStream(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return in;
    }
}