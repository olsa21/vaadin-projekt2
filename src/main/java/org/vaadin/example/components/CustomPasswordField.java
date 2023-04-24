package org.vaadin.example.components;


import com.vaadin.flow.component.textfield.PasswordField;

public class CustomPasswordField extends PasswordField {
    private String pattern= "^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$" ;
    public CustomPasswordField() {
        setPattern(pattern);
        setHelperText("Das Password muss haben: Klein- und Großbuchstaben sowie eine Länge von mindestens 8 Zeichen");

        setErrorMessage("Unzulässiges Passwort");
    }
    public CustomPasswordField(String label) {
        setLabel(label);
        setPattern(pattern);
        setHelperText("Das Password muss haben: Klein- und Großbuchstaben sowie eine Länge von mindestens 8 Zeichen");

        setErrorMessage("Unzulässiges Passwort");
    }
}
