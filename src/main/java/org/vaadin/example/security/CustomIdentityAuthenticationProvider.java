package org.vaadin.example.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.vaadin.example.entity.MitarbeiterEntity;
import org.vaadin.example.service.SpecificationsService;

/**
 * Die Klasse ist dafür zuständig eine Authentifizierung zu überprüfen. Hierbei wird die Methode
 * isValidUser aufgerufen, welche die Anmeldedaten überprüft.
 * 
 */
@Service
public class CustomIdentityAuthenticationProvider implements AuthenticationProvider {

    private final SpecificationsService service;

    public CustomIdentityAuthenticationProvider(SpecificationsService service) {
        this.service = service;
    }

    /**
     * Methode überprüft, ob die Anmeldedaten korrekt sind. Hierbei wird in der Datenbank
     * nach dem Benutzer gesucht und überprüft, ob das übergebene Passwort korrekt ist
     * @param username der übergebene Benutzername
     * @param password das übergebene Passwort
     * @return UserDetails-Objekt, wenn die Anmeldedaten korrekt sind, ansonsten null
     */
    UserDetails isValidUser(String username, String password) {
        MitarbeiterEntity mitarbeiter = service.findSpecificUser(username);
        if (mitarbeiter != null){
            if (service.authenticateUser(username, password)){
                return User.builder()
                        .username(mitarbeiter.getBenutzername())
                        .password(mitarbeiter.getPasswort())
                        .roles("USER")
                        .build();
            }
        }
        return null;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = isValidUser(username, password);

        if (userDetails != null){
            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        }else{
            throw new BadCredentialsException("Inkorrekte Benutzeranmeldedaten!");
        }
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType
                .equals(UsernamePasswordAuthenticationToken.class);
    }
}
