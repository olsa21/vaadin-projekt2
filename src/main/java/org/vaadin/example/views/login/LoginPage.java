package org.vaadin.example.views.login;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.vaadin.example.service.SpecificationsService;

@Route("/login")
public class LoginPage extends VerticalLayout implements BeforeEnterObserver {
    private final SpecificationsService service;
    private LoginForm login = new LoginForm();
    public LoginPage(SpecificationsService service) {
        this.service = service;

        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(login);
        Button createAccount = new Button("Create an account");
        createAccount.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        createAccount.addClickListener(e -> {
            UI.getCurrent().navigate("register");
        });
        add(createAccount);

        login.setAction("login");

        //login.addLoginListener(e -> {
        //    //boolean isAuthenticated = authenticate(e.getUsername(), e.getPassword());
        //    boolean isAuthenticated = service.credentialsCorrect(e.getUsername(), e.getPassword());
        //    if (isAuthenticated) {
        //        Notification.show("Login successful");
//
        //        login.setVisible(false);
        //        getUI().ifPresent(ui -> ui.navigate(""));
        //    } else {
        //        Notification.show("Login failed");
        //        login.setEnabled(true);
        //        login.setError(true);
        //    }
        //});
//
        //login.addForgotPasswordListener(e -> {
        //    Dialog dialog = new Dialog();
        //    dialog.add(new Text("Bitte kontaktieren Sie den Administrator"));
        //    dialog.open();
        //});
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}
