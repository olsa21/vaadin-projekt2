package org.vaadin.example.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;

/**
 * Diese Klasse ist dafür zuständig, dass eine Avatar-Komponente klickbar ist.
 * Da die Avatar-Komponente keine Möglichkeit bietet, einen ClickListener zu registrieren,
 * wird hier eine eigene Komponente erstellt, die eine Avatar-Komponente enthält und
 * einen ClickListener registriert.
 */
public class ClickableAvatar extends HorizontalLayout {
    private Avatar avatar;

    private void init(){
        addClickListener(e -> {
            UI.getCurrent().navigate("profil-edit");
        });
        add(avatar);
    }

    /**
     * Standard-Konstruktor, welcher keinen Avatar übergeben bekommt und dieses erstellt.
     */
    public ClickableAvatar() {
        this.avatar = new Avatar();
        init();
    }

    /**
     * Konstruktor, welcher einen Namen übergeben bekommt und einen Avatar mit diesem Namen erstellt.
     * @param name der Name des Avatars
     */
    public ClickableAvatar(String name) {
        if (name == null){
            throw new IllegalArgumentException("Name darf nicht null sein!");
        }
        this.avatar = new Avatar(name);
        init();
    }

    /**
     * Konstruktor, welcher einen Avatar übergeben bekommt.
     * @param avatar der Avatar
     */
    public ClickableAvatar(Avatar avatar) {
        if (avatar == null){
            throw new IllegalArgumentException("Avatar darf nicht null sein!");
        }
        this.avatar = avatar;
        init();
    }

    /**
     * Ändert den Namen des Avatars
     * @param name
     */
    public void setAvatarName(String name){
        avatar.setName(name);
    }

    /**
     * Methode, welche das Bild des Avatars setzt.
     * @param imageData das Bild des Avatars
     */
    public void setPicture(byte[] imageData){
        if(imageData != null) {
            avatar.setImageResource(new StreamResource("image.png", () -> new ByteArrayInputStream(imageData)));
        }
        else {
            avatar.setImage(null);
        }
    }
}
