package service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

/**
 * Serviço responsável por gerenciar a autenticação em dois fatores (2FA) usando Google Authenticator.
 */
public class TwoFactorAuthService {
    private static final String SECRET_FILE = "data/2fa.key"; // Caminho do arquivo onde a chave secreta será salva
    private final GoogleAuthenticator gAuth; // Instância do autenticador
    private GoogleAuthenticatorKey secretKey; // Chave secreta gerada ou carregada

    public TwoFactorAuthService() {
        this.gAuth = new GoogleAuthenticator(); // Inicializa o autenticador
        loadOrCreateSecretKey(); // Carrega ou gera nova chave secreta
    }

    /**
     * Gera uma nova chave secreta ou carrega a existente do arquivo.
     */
    private void loadOrCreateSecretKey() {
        try {
            File secretFile = new File(SECRET_FILE);
            if (!secretFile.exists()) {
                // Cria diretórios se não existirem
                secretFile.getParentFile().mkdirs();

                // Gera uma nova chave secreta
                secretKey = gAuth.createCredentials();

                // Salva a chave em um arquivo
                Files.write(secretFile.toPath(), secretKey.getKey().getBytes(StandardCharsets.UTF_8));
            } else {
                // Lê a chave do arquivo existente
                String savedKey = new String(Files.readAllBytes(secretFile.toPath()), StandardCharsets.UTF_8);
                secretKey = new GoogleAuthenticatorKey.Builder(savedKey).build(); // Constrói a chave
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar/criar chave 2FA", e);
        }
    }

    /**
     * Verifica se a autenticação 2FA já foi configurada.
     * @return true se a chave 2FA estiver salva.
     */
    public boolean isConfigured() {
        return new File(SECRET_FILE).exists();
    }

    /**
     * Retorna o objeto contendo a chave secreta do usuário.
     */
    public GoogleAuthenticatorKey getSecretKey() {
        return secretKey;
    }

    /**
     * Gera a URL do QR Code que pode ser escaneada no Google Authenticator.
     *
     * @param user Nome do usuário que será mostrado no app autenticador.
     * @return URL do QR Code.
     */
    public String getQRCode(String user) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL("GerenciadorSenhasSeguras", user, secretKey);
    }

    /**
     * Verifica se o código informado é válido.
     *
     * @param code Código TOTP fornecido pelo usuário.
     * @return true se o código for válido para a chave secreta.
     */
    public boolean verifyCode(int code) {
        return gAuth.authorize(secretKey.getKey(), code);
    }
}
