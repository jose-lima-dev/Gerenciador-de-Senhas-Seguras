package controller;

import dao.PasswordDAO;
import model.PasswordEntry;
import service.EncryptionService;
import service.LeakCheckService;
import service.PasswordGenerator;
import service.TwoFactorAuthService;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import java.util.List;
import java.util.Scanner;

public class PasswordController {
    private final PasswordDAO passwordDAO = new PasswordDAO();
    private final EncryptionService encryptionService = new EncryptionService();
    private final PasswordGenerator passwordGenerator = new PasswordGenerator();
    private final TwoFactorAuthService twoFactorAuthService = new TwoFactorAuthService();
    private final LeakCheckService leakCheckService = new LeakCheckService();

    // Simples autenticação com senha mestre e 2FA TOTP
    public boolean authenticateUser(Scanner scanner) {
        System.out.print("Digite a senha mestre: ");
        String masterPassword = scanner.nextLine();

        // No mundo real, comparar com hash da senha mestre armazenada
        if (!"senha123".equals(masterPassword)) {
            System.out.println("Senha mestre incorreta.");
            return false;
        }

        // Gera a chave secreta TOTP
        GoogleAuthenticatorKey key = twoFactorAuthService.generateSecretKey();
        String secret = key.getKey();

        // Exibe o segredo e o QR Code (em URL otpauth)
        System.out.println("Configure seu app 2FA com este segredo: " + secret);
        System.out.println("Ou escaneie este QR Code no Google Authenticator:");
        System.out.println(twoFactorAuthService.getQRCode("usuario@example.com", key));

        System.out.print("Digite o código 2FA: ");
        String code = scanner.nextLine();

        try {
            boolean valid = twoFactorAuthService.verifyCode(secret, Integer.parseInt(code));
            if (!valid) {
                System.out.println("Código 2FA incorreto.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Código inválido.");
            return false;
        }
    }

    public void addPassword(Scanner scanner) {
        System.out.print("Serviço: ");
        String service = scanner.nextLine();

        System.out.print("Usuário: ");
        String username = scanner.nextLine();

        System.out.print("Senha: ");
        String password = scanner.nextLine();

        String encrypted = encryptionService.encrypt(password);

        PasswordEntry entry = new PasswordEntry(service, username, encrypted);

        passwordDAO.addPassword(entry);

        System.out.println("Senha adicionada e salva no banco com sucesso!");
    }

    public void listPasswords() {
        List<PasswordEntry> list = passwordDAO.listPasswords();

        if (list.isEmpty()) {
            System.out.println("Nenhuma senha cadastrada.");
            return;
        }

        System.out.println("Senhas cadastradas:");
        for (PasswordEntry entry : list) {
            System.out.println(entry);
        }
    }

    public void generateSecurePassword(Scanner scanner) {
        System.out.print("Tamanho da senha a gerar (ex: 12): ");
        String input = scanner.nextLine();
        int length;
        try {
            length = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Número inválido.");
            return;
        }

        String generated = passwordGenerator.generatePassword(length);
        System.out.println("Senha gerada: " + generated);
    }

    public void checkPasswordLeak(Scanner scanner) {
        System.out.print("Digite a senha para verificar vazamento: ");
        String password = scanner.nextLine();

        boolean leaked = leakCheckService.isPasswordLeaked(password);
        if (leaked) {
            System.out.println("⚠️ Atenção! Esta senha foi exposta em vazamentos.");
        } else {
            System.out.println("✅ Senha segura. Não encontrada em vazamentos conhecidos.");
        }
    }

    public void deletePassword(Scanner scanner) {
        System.out.print("Digite o ID da senha para excluir: ");
        String input = scanner.nextLine();
        int id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Número inválido.");
            return;
        }

        boolean deleted = passwordDAO.deletePassword(id);
        if (deleted) {
            System.out.println("Senha removida com sucesso.");
        } else {
            System.out.println("Senha com o ID informado não encontrada.");
        }
    }
}
