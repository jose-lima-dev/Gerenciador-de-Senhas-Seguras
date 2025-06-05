package ap.pp;

import controller.PasswordController;
import util.DatabaseHelper;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        DatabaseHelper.createTable();

        PasswordController controller = new PasswordController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Gerenciador de Senhas Seguras ===");

        if (!controller.authenticateUser(scanner)) {
            System.out.println("Autenticação falhou. Encerrando.");
            scanner.close();
            return;
        }

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1 - Adicionar senha");
            System.out.println("2 - Listar senhas");
            System.out.println("3 - Gerar senha segura");
            System.out.println("4 - Verificar vazamento de senha");
            System.out.println("5 - Remover senha");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1" -> controller.addPassword(scanner);
                case "2" -> controller.listPasswords();
                case "3" -> controller.generateSecurePassword(scanner);
                case "4" -> controller.checkPasswordLeak(scanner);
                case "5" -> controller.deletePassword(scanner);
                case "0" -> {
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }
}
