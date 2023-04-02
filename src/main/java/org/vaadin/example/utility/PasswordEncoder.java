package org.vaadin.example.utility;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Die Utility-Klasse ist dafür zuständig eine Methode anzubieten, welche ein Passwort (ein beliebiger Text) in einen
 * SHA-256 Hash umwandelt.
 */
public final class PasswordEncoder {
    private static final String HASH_ALGORITHM = "SHA-256";

    private PasswordEncoder(){}

    /**
     * Die statische Methode sorgt dafür, dass ein übergebener Text in einen SHA-256 Hash umgewandelt wird.
     * @param password das übergebene Passwort
     * @return den SHA-256 Hash
     * @throws NoSuchAlgorithmException
     */
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        String hashedPassword = null;
        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);

        byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
        hashedPassword = DatatypeConverter.printHexBinary(digest).toLowerCase();
        return hashedPassword;
    }

}
