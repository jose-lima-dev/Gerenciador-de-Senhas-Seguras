package service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.security.SecureRandom;

/**
 * Classe responsável por criptografar e descriptografar dados
 * utilizando AES no modo GCM com uma chave secreta persistente.
 */
public class EncryptionService {
    private static final String AES = "AES";
    private static final String AES_MODE = "AES/GCM/NoPadding"; // AES com autenticação (GCM)
    private static final int TAG_LENGTH_BIT = 128; // Tamanho do tag de autenticação
    private static final int IV_LENGTH_BYTE = 12;  // Tamanho do vetor de inicialização (12 bytes para GCM)
    private static final String KEY_FILE = "data/master.key"; // Caminho para salvar a chave secreta

    private final SecretKey secretKey; // Chave usada para criptografia/descriptografia

    /**
     * Construtor que carrega ou cria a chave secreta.
     */
    public EncryptionService() {
        this.secretKey = loadOrCreateKey();
    }

    /**
     * Carrega a chave AES de um arquivo, ou gera uma nova se ela não existir.
     * @return Chave secreta AES
     */
    private SecretKey loadOrCreateKey() {
        try {
            File keyFile = new File(KEY_FILE);

            if (!keyFile.exists()) {
                // Cria o diretório se necessário
                keyFile.getParentFile().mkdirs();

                // Gera uma nova chave AES de 128 bits
                KeyGenerator keyGen = KeyGenerator.getInstance(AES);
                keyGen.init(128);
                SecretKey key = keyGen.generateKey();

                // Salva a chave no arquivo
                try (FileOutputStream fos = new FileOutputStream(keyFile)) {
                    fos.write(key.getEncoded());
                }

                return key;
            } else {
                // Carrega a chave existente
                byte[] keyBytes = Files.readAllBytes(keyFile.toPath());
                return new SecretKeySpec(keyBytes, AES);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar/criar chave de criptografia", e);
        }
    }

    /**
     * Criptografa o texto puro (plaintext) usando AES/GCM.
     * @param plaintext Texto a ser criptografado
     * @return Texto criptografado codificado em Base64
     */
    public String encrypt(String plaintext) {
        try {
            // Gera vetor de inicialização aleatório
            byte[] iv = new byte[IV_LENGTH_BYTE];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            // Inicializa o cifrador
            Cipher cipher = Cipher.getInstance(AES_MODE);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

            // Criptografa o texto
            byte[] encrypted = cipher.doFinal(plaintext.getBytes());

            // Junta IV + dados criptografados
            byte[] encryptedIVAndText = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, encryptedIVAndText, 0, iv.length);
            System.arraycopy(encrypted, 0, encryptedIVAndText, iv.length, encrypted.length);

            // Codifica em Base64 para facilitar o armazenamento
            return Base64.getEncoder().encodeToString(encryptedIVAndText);
        } catch (Exception e) {
            throw new RuntimeException("Erro na criptografia", e);
        }
    }

    /**
     * Descriptografa o texto criptografado em Base64.
     * @param encryptedText Texto criptografado (Base64)
     * @return Texto original (plaintext)
     */
    public String decrypt(String encryptedText) {
        try {
            // Decodifica de Base64
            byte[] decoded = Base64.getDecoder().decode(encryptedText);

            // Extrai IV
            byte[] iv = new byte[IV_LENGTH_BYTE];
            System.arraycopy(decoded, 0, iv, 0, iv.length);

            // Extrai dados criptografados
            int encryptedSize = decoded.length - IV_LENGTH_BYTE;
            byte[] encryptedBytes = new byte[encryptedSize];
            System.arraycopy(decoded, IV_LENGTH_BYTE, encryptedBytes, 0, encryptedSize);

            // Inicializa o cifrador para descriptografia
            Cipher cipher = Cipher.getInstance(AES_MODE);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            // Descriptografa e retorna como string
            byte[] decrypted = cipher.doFinal(encryptedBytes);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Erro na descriptografia", e);
        }
    }
}
