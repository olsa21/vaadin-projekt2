package org.vaadin.example.views.login;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.vaadin.example.components.CustomPasswordField;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.security.SecurityService;
import org.vaadin.example.service.SpecificationsService;
import org.vaadin.example.utility.PasswordEncoder;

import java.util.ArrayList;
import java.util.stream.Stream;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY_INLINE;

/**
 * Die Klasse ist dafür zuständig, dass eine passende View für die Registrierung angezeigt wird.
 */
@AnonymousAllowed
@Route("/register")
public class RegisterView extends VerticalLayout  implements BeforeEnterObserver {

    private TextField username;
    private CustomPasswordField password;
    private PasswordField passwordRepeat;
    private EmailField email;
    private TextField firstName;
    private TextField lastName;
    private Button submitButton;
    private Button backToLoginButton;
    private final SpecificationsService service;

    /**
     * Konstruktor, welche die View initialisiert.
     * @param service Service, welcher für die Kommunikation mit der Datenbank zuständig ist.
     */
    public RegisterView(SpecificationsService service){
        if (service == null)
            throw new IllegalArgumentException("Service darf nicht null sein!");
        this.service = service;
        setAlignItems(Alignment.CENTER);
        H2 title = new H2("Registrieren");
        init();

        setRequiredIndicatorVisible(username, password, passwordRepeat, email, firstName, lastName);
        add(title, username, password, passwordRepeat, email, firstName, lastName, submitButton, backToLoginButton);
    }

    /**
     * Initialisiert die View.
     */
    private void init(){
        username = new TextField("Benutzername");
        password = new CustomPasswordField("Passwort");
        passwordRepeat = new PasswordField("Passwort wiederholen");
        email = new EmailField("E-Mail");
        firstName = new TextField("Vorname");
        lastName = new TextField("Nachname");
        submitButton = new Button("Registrieren");
        backToLoginButton = new Button("Zurück zum Login");

        backToLoginButton.addThemeVariants(LUMO_TERTIARY_INLINE);
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        submitButton.addClickListener(e -> submitRegister());
        backToLoginButton.addClickListener(e -> backToLogin());
    }

    /**
     * Erstellt eine Notification, welche bei einem erfolgreichen Registrieren angezeigt wird.
     * @param components Die Komponenten, welche in der Notification angezeigt werden sollen.
     */
    private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
        Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
    }

    /**
     * Methode, welche eine Registrierung initiiert.
     */
    private void submitRegister(){
        ArrayList<HasValueAndElement<?,?>> requiredFields = new ArrayList<>();
        requiredFields.add(username);
        requiredFields.add(password);
        requiredFields.add(passwordRepeat);
        requiredFields.add(email);
        requiredFields.add(firstName);
        requiredFields.add(lastName);

        for (HasValueAndElement<?,?> requiredField : requiredFields) {
            if (requiredField.getValue().toString().isEmpty()) {
                createSubmitError().open();
                return;
            }
        }
        // prüfen, ob Benutzername schon vergeben ist
        if (service.findSpecificUser(username.getValue()) != null){
            username.setInvalid(true);
            createSubmitError().open();
            return;
        }
        // prüfen, ob E-Mail schon vergeben ist
        if (service.findSpecificUserByMail(email.getValue())){
            email.setInvalid(true);
            createSubmitError().open();
            return;
        }

        if (!password.isInvalid() && password.getValue().equals(passwordRepeat.getValue())){
            try{
                MitarbeiterEntity mitarbeiter = new MitarbeiterEntity();
                mitarbeiter.setBenutzername(username.getValue());
                String hashedPassword = PasswordEncoder.hashPassword((password.getValue()));
                mitarbeiter.setPasswort(hashedPassword);

                mitarbeiter.setMail(email.getValue());
                mitarbeiter.setVorname(firstName.getValue());
                mitarbeiter.setNachname(lastName.getValue());

                service.addMitarbeiter(mitarbeiter);
            }catch(Exception e){
                createSubmitError().open();
                return;
            }
            createSubmitSuccess().open();
        }else{
            passwordRepeat.setInvalid(true);
            createSubmitError().open();
        }
    }

    /**
     * Methode, leitet den Benutzer zurück zum Login.
     */
    private void backToLogin(){
        getUI().ifPresent(ui -> ui.navigate("login"));
    }

    /**
     * Methode, welche ein Button erstellt, welcher die Notification schließt.
     * @param notification Die Notification, welche angezeigt werden soll.
     * @return Der Button, welcher die Notification schließt.
     */
    private static Button createCloseBtn(Notification notification) {
        Button closeBtn = new Button(
                VaadinIcon.CLOSE_SMALL.create(),
                clickEvent -> notification.close());
        closeBtn.addThemeVariants(LUMO_TERTIARY_INLINE);
        return closeBtn;
    }

    /**
     * Methode, welche eine Notification erstellt, welche bei einem erfolgreichen Registrieren angezeigt wird.
     * @return Die Notification, welche angezeigt werden soll.
     */
    private static Notification createSubmitSuccess() {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        Icon icon = VaadinIcon.CHECK_CIRCLE.create();
        Div info = new Div(new Text("Registrierung war erfolgreich!"));

        HorizontalLayout layout = new HorizontalLayout(
                icon, info, createCloseBtn(notification));
        layout.setAlignItems(Alignment.CENTER);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.setDuration(2000);
        notification.add(layout);

        return notification;
    }

    /**
     * Methode, welche eine Notification erstellt, welche bei einem fehlgeschlagenen Registrieren angezeigt wird.
     * @return Die Notification, welche angezeigt werden soll.
     */
    private static Notification createSubmitError() {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

        Icon icon = VaadinIcon.WARNING.create();
        Div info = new Div(new Text("Registrierung war leider nicht erfolgreich!"));

        HorizontalLayout layout = new HorizontalLayout(
                icon, info, createCloseBtn(notification));
        layout.setAlignItems(Alignment.CENTER);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.add(layout);

        return notification;
    }

    /**
     * Prüft vor Aufruf ob ein Nutzer eingeloggt ist und leitet ggf. auf die Startseite weiter
     * @param beforeEnterEvent
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(!SecurityService.getLoggedInUsername().equals("anonymousUser")){
            beforeEnterEvent.forwardTo("");
        }
    }
}
