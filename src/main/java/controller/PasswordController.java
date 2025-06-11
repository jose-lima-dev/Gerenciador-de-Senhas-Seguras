package controller;

import java.util.List;
import java.util.Scanner;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import dao.PasswordDAO;
import model.PasswordEntry;
import service.EncryptionService;
import service.LeakCheckService;
import service.MasterPasswordService;
import service.PasswordGenerator;
import service.TwoFactorAuthService;

public class PasswordController {
    // Instância dos serviços e DAO usados pelo controlador
    private final PasswordDAO passwordDAO = new PasswordDAO();
    private final EncryptionService encryptionService = new EncryptionService();
    private final PasswordGenerator passwordGenerator = new PasswordGenerator();
    private final TwoFactorAuthService twoFactorAuthService = new TwoFactorAuthService();
    private final LeakCheckService leakCheckService = new LeakCheckService();
    private final MasterPasswordService masterPasswordService = new MasterPasswordService();

    /**
     * Autentica o usuário com senha mestra e 2FA (Google Authenticator).
     */
    public boolean authenticateUser(Scanner scanner) {
        try {
            boolean isFirstAccess = !masterPasswordService.isMasterPasswordSet();

            if (isFirstAccess) {
                // Primeira vez: solicita criação de senha mestra
                System.out.println("\n=== Primeiro Acesso - Configuração Inicial ===");
                System.out.println("\nVocê precisa definir uma senha mestra para o sistema.");
                System.out.println("Esta senha será necessária para acessar o gerenciador.");

                // Requisitos mínimos
                System.out.println("\nRequisitos da senha mestra:");
                System.out.println("- Mínimo de 8 caracteres");
                System.out.println("- Pelo menos uma letra maiúscula");
                System.out.println("- Pelo menos uma letra minúscula");
                System.out.println("- Pelo menos um número");
                System.out.println("- Pelo menos um caractere especial (!@#$%^&*()-_=+)");

                while (true) {
                    System.out.print("\nDigite a senha mestra desejada: ");
                    String password = scanner.nextLine();

                    if (!isPasswordStrong(password)) {
                        System.out.println("\nA senha não atende aos requisitos mínimos de segurança.");
                        continue;
                    }

                    System.out.print("Digite novamente para confirmar: ");
                    String confirmPassword = scanner.nextLine();

                    if (!password.equals(confirmPassword)) {
                        System.out.println("\nAs senhas não coincidem. Tente novamente.");
                        continue;
                    }

                    masterPasswordService.setMasterPassword(password);
                    System.out.println("\n✅ Senha mestra definida com sucesso!");
                    break;
                }
            } else {
                // Acesso normal: solicita senha mestra e valida
                System.out.print("\nDigite a senha mestra: ");
                String masterPassword = scanner.nextLine();

                if (!masterPasswordService.verifyMasterPassword(masterPassword)) {
                    System.out.println("\n❌ Senha mestra incorreta.");
                    return false;
                }
            }

            // Configuração do 2FA se for o primeiro acesso
            if (isFirstAccess) {
                System.out.println("\n=== Configuração da Autenticação de Dois Fatores (2FA) ===");
                System.out.println("Para aumentar a segurança, você precisa configurar o 2FA.");

                System.out.println("\nSiga os passos:");
                System.out.println("1. Instale o Google Authenticator no seu celular");
                System.out.println("2. Escaneie o QR Code ou digite o código manualmente");

                GoogleAuthenticatorKey key = twoFactorAuthService.getSecretKey();

                // Mostra o código secreto e o QR code
                System.out.println("\n=== CÓDIGO SECRETO ===");
                System.out.println("⚠️ Guarde este código em um local seguro!");
                System.out.println(key.getKey());

                System.out.println("\n=== QR CODE ===");
                System.out.println("Escaneie este QR Code com o Google Authenticator:");
                System.out.println(twoFactorAuthService.getQRCode("usuario@example.com"));

                System.out.println("\nApós configurar, o app mostrará um código de 6 dígitos.");
            }

            // Solicita e verifica o código 2FA
            System.out.print("\nDigite o código de 6 dígitos do Google Authenticator: ");
            String code = scanner.nextLine();

            try {
                boolean valid = twoFactorAuthService.verifyCode(Integer.parseInt(code));
                if (!valid) {
                    System.out.println("❌ Código 2FA incorreto.");
                    return false;
                }
                System.out.println("✅ Autenticação bem-sucedida!");
                return true;
            } catch (NumberFormatException e) {
                System.out.println("❌ Código inválido. Digite apenas os 6 números.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Erro durante a autenticação: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica se a senha atende aos critérios de segurança.
     */
    private boolean isPasswordStrong(String password) {
        if (password.length() < 8) return false;
        if (!password.matches(".*[A-Z].*")) return false;
        if (!password.matches(".*[a-z].*")) return false;
        if (!password.matches(".*\\d.*")) return false;
        if (!password.matches(".*[!@#$%^&*()\\-_=+].*")) return false;
        return true;
    }

    /**
     * Adiciona uma nova senha ao banco de dados (com criptografia).
     */
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

    /**
     * Lista todas as senhas salvas no banco.
     */
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

    /**
     * Gera uma senha aleatória e segura com o tamanho informado.
     */
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

    /**
     * Verifica se uma senha foi vazada utilizando API pública.
     */
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

    /**
     * Remove uma senha do banco após confirmar a senha mestra.
     */
    public void deletePassword(Scanner scanner) {
        // Solicita senha mestra antes de remover
        System.out.println("\n⚠️ A remoção de credenciais requer autenticação.");
        System.out.print("Digite a senha mestra para confirmar a exclusão: ");
        String masterPassword = scanner.nextLine();

        if (!masterPasswordService.verifyMasterPassword(masterPassword)) {
            System.out.println("❌ Senha mestra incorreta. Operação cancelada.");
            return;
        }

        // Lista senhas disponíveis
        List<PasswordEntry> passwords = passwordDAO.listPasswords();
        if (passwords.isEmpty()) {
            System.out.println("Não há senhas cadastradas para remover.");
            return;
        }

        System.out.println("\nSenhas disponíveis para remoção:");
        for (PasswordEntry entry : passwords) {
            System.out.println(entry);
        }

        // Solicita ID da senha a ser removida
        System.out.print("\nDigite o ID da senha que deseja remover: ");
        String input = scanner.nextLine();

        int id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido. Operação cancelada.");
            return;
        }

        // Confirma a exclusão
        System.out.print("Tem certeza que deseja remover esta senha? (s/N): ");
        String confirm = scanner.nextLine().toLowerCase();

        if (!confirm.equals("s")) {
            System.out.println("Operação cancelada pelo usuário.");
            return;
        }

        boolean deleted = passwordDAO.deletePassword(id);
        if (deleted) {
            System.out.println("✅ Senha removida com sucesso.");
        } else {
            System.out.println("❌ Senha com o ID informado não encontrada.");
        }
    }
}
