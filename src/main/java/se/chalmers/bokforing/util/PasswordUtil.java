package se.chalmers.bokforing.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * A class that contains helper functions for passwords.
 *
 * @author Dženan
 */
public class PasswordUtil {

    /**
     * RANDOM STRING
     *
     * @param length
     * @return String
     */
    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        String comb = "0123456789"
                + "abcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        for (int i = 0; i < length; i++) {
            sb.append(comb.charAt(rnd.nextInt(comb.length())));
        }
        return sb.toString();
    }

    /**
     * HASH
     *
     * @param plaintext
     * @return String
     */
    public static String hash(String plaintext) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plaintext.getBytes());
            return new String(hash);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }
}
