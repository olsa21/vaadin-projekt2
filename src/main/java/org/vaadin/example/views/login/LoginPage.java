package org.vaadin.example.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;

/**
 * View stellt ein Login-Formular zur Verfügung inkl. Verweis auf Registrierung
 */
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
    }

    /**
     * Prüft vor Aufruf ob ein Nutzer eingeloggt ist und leitet ggf. auf die Startseite weiter
     * oder ob die eingegebenen Zugangsdaten korrekt sind
     * @param beforeEnterEvent
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(!SecurityService.getLoggedInUsername().equals("anonymousUser")){
            beforeEnterEvent.forwardTo("");
        }

        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}
