package service;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String SPECIAL_CHAR = "!@#$%^&*()-_=+<>?";

    private static final String PASSWORD_ALLOW = CHAR_LOWER + CHAR_UPPER + NUMBER + SPECIAL_CHAR;

    private final SecureRandom random = new SecureRandom();

    public String generatePassword(int length) {
        if (length < 8) length = 8; // mínimo recomendado

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rndCharIndex = random.nextInt(PASSWORD_ALLOW.length());
            sb.append(PASSWORD_ALLOW.charAt(rndCharIndex));
        }
        return sb.toString();
    }
}
