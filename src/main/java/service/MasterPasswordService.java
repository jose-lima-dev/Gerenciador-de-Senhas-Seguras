package service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class MasterPasswordService {
    private static final String MASTER_PASSWORD_FILE = "data/master.pwd";
    private static final String SALT_FILE = "data/master.salt";

    public boolean isMasterPasswordSet() {
        return new File(MASTER_PASSWORD_FILE).exists() && new File(SALT_FILE).exists();
    }

    public void setMasterPassword(String password) {
        try {
            // Cria o diretório se não existir
            new File(MASTER_PASSWORD_FILE).getParentFile().mkdirs();

            // Gera um salt aleatório
            byte[] salt = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            // Salva o salt
            Files.write(new File(SALT_FILE).toPath(), salt);

            // Gera o hash da senha com o salt
            String hashedPassword = hashPassword(password, salt);

            // Salva o hash
            Files.write(new File(MASTER_PASSWORD_FILE).toPath(),
                    hashedPassword.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            throw new RuntimeException("Erro ao definir senha mestra", e);
        }
    }

    public boolean verifyMasterPassword(String password) {
        try {
            // Lê o salt
            byte[] salt = Files.readAllBytes(new File(SALT_FILE).toPath());

            // Lê o hash armazenado
            String storedHash = new String(
                    Files.readAllBytes(new File(MASTER_PASSWORD_FILE).toPath()),
                    StandardCharsets.UTF_8
            );

            // Gera o hash da senha fornecida
            String hashedPassword = hashPassword(password, salt);

            // Compara os hashes
            return storedHash.equals(hashedPassword);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar senha mestra", e);
        }
    }

    private String hashPassword(String password, byte[] salt) throws Exception {
        // Usa SHA-256 para gerar o hash
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}