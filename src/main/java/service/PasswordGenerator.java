package service;

import java.security.SecureRandom;

/**
 * Classe responsável por gerar senhas seguras aleatórias com letras, números e símbolos especiais.
 */
public class PasswordGenerator {
    // Conjuntos de caracteres permitidos
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase(); // ABCDEFGHIJKLMNOPQRSTUVWXYZ
    private static final String NUMBER = "0123456789";
    private static final String SPECIAL_CHAR = "!@#$%^&*()-_=+<>?";

    // Conjunto total de caracteres permitidos
    private static final String PASSWORD_ALLOW = CHAR_LOWER + CHAR_UPPER + NUMBER + SPECIAL_CHAR;

    // Gerador de números seguros aleatórios
    private final SecureRandom random = new SecureRandom();

    /**
     * Gera uma senha aleatória com os caracteres permitidos.
     *
     * @param length Tamanho da senha desejada (mínimo de 8).
     * @return Senha gerada.
     */
    public String generatePassword(int length) {
        // Garante o tamanho mínimo de 8 caracteres por segurança
        if (length < 8) length = 8;

        StringBuilder sb = new StringBuilder(length);

        // Gera caracteres aleatórios até atingir o tamanho desejado
        for (int i = 0; i < length; i++) {
            int rndCharIndex = random.nextInt(PASSWORD_ALLOW.length());
            sb.append(PASSWORD_ALLOW.charAt(rndCharIndex));
        }

        return sb.toString(); // Retorna a senha final
    }
}
