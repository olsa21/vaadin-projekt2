package org.vaadin.example.components;


import com.vaadin.flow.component.textfield.PasswordField;

/**
 * Eine benutzerdefinierte PasswordField, die es dem Benutzer ermöglicht, ein Passwort einzugeben.
 * Das Passwort muss Bestehen aus:
 * mindestens 8 Zeichen,
 * mindestens 1 Großbuchstaben,
 * mindestens 1 Kleinbuchstaben,
 * mindestens 1 Zahl
 */
public class CustomPasswordField extends PasswordField {
    private final String pattern= "^(?=.*[a-z])(?=.*[A-Z]).{8,}$" ;

    /**
     * Konstruktor - erstellt ein neues PasswordField ohne Label
     */
    public CustomPasswordField() {
        setPattern(pattern);
        setHelperText("Das Password muss haben: Klein- und Großbuchstaben sowie eine Länge von mindestens 8 Zeichen");

        setErrorMessage("Unzulässiges Passwort");
    }

    /**
     * Konstruktor - erstellt ein neues PasswordField mit Label
     * @param labeltext Labeltext
     */
    public CustomPasswordField(String labeltext) {
        setLabel(labeltext);
        setPattern(pattern);
        setHelperText("Das Password muss haben: Klein- und Großbuchstaben sowie eine Länge von mindestens 8 Zeichen");

        setErrorMessage("Unzulässiges Passwort");
    }
}
