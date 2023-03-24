package org.vaadin.example.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ClickableAvatar extends HorizontalLayout {
    private Avatar avatar;

    private void init(){
        addClickListener(e -> {
            //redirect to route profil-edit
            UI.getCurrent().navigate("profil-edit");
        });
        add(avatar);
    }

    public ClickableAvatar() {
        this.avatar = new Avatar();
        init();
    }

    public ClickableAvatar(String name) {
        this.avatar = new Avatar(name);
        init();
    }

    public ClickableAvatar(Avatar avatar) {
        this.avatar = avatar;
        init();
    }
}
