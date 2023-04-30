package org.vaadin.example.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.vaadin.example.entity.PflichtenheftEntity;

@Component
/**
 * Klasse enthält Methoden, unter anderem für den Login und Logout
 */
public class SecurityService {

    private static final String LOGOUT_SUCCESS_URL = "/";

    public UserDetails getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }
        return null;
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }

    /**
     * Gibt den Benutzernamen des angemeldeten Benutzers zurück
     * @return den angemeldeten Benutzernamen
     */
    public static String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return username;
    }

    /**
     * Methode überprüft, ob der angemeldete Benutzer Mitglied des mitgegebenen Pflichtenheftes ist
     * @param pflichtenheftEntity das Pflichtenheft, welches überprüft werden soll
     * @return true, wenn der Benutzer Mitglied des Pflichtenheftes ist, ansonsten false
     */
    public static boolean userIsMemberOf(PflichtenheftEntity pflichtenheftEntity){
        if(pflichtenheftEntity == null){
            return false;
        }

        return pflichtenheftEntity.getMitarbeiter().stream()
                .filter(m -> m.getBenutzername().equals(SecurityService.getLoggedInUsername()))
                .findFirst()
                .orElse(null) != null;
    }
}