package app;

import java.io.File;
import java.util.Scanner;

import controller.PasswordController;
import util.DatabaseHelper;

public class App {
    public static void main(String[] args) {
        try {
            // Cria o diretório 'data' se ele não existir (para armazenar o banco de dados)
            File dataDir = new File("data");
            if (!dataDir.exists() && !dataDir.mkdirs()) {
                System.err.println("Erro: Não foi possível criar o diretório data/");
                return; // Encerra se não for possível criar o diretório
            }

            // Inicializa o banco de dados (cria a tabela se necessário)
            try {
                DatabaseHelper.createTable();
            } catch (Exception e) {
                System.err.println("Erro ao criar banco de dados: " + e.getMessage());
                e.printStackTrace();
                return; // Encerra se ocorrer erro na criação da tabela
            }

            // Instancia o controlador principal e o scanner para entrada do usuário
            PasswordController controller = new PasswordController();
            Scanner scanner = new Scanner(System.in);

            System.out.println("\n=== Gerenciador de Senhas Seguras ===");

            // Executa o processo de autenticação do usuário (login com 2FA, etc.)
            try {
                if (!controller.authenticateUser(scanner)) {
                    System.out.println("\nAutenticação falhou. Encerrando.");
                    scanner.close();
                    return;
                }
            } catch (Exception e) {
                System.err.println("\nErro durante a autenticação: " + e.getMessage());
                e.printStackTrace();
                scanner.close();
                return;
            }

            // Loop principal do menu interativo
            while (true) {
                try {
                    // Exibe o menu de opções
                    System.out.println("\nMenu:");
                    System.out.println("\n1 - Adicionar senha");
                    System.out.println("2 - Listar senhas");
                    System.out.println("3 - Gerar senha segura");
                    System.out.println("4 - Verificar vazamento de senha");
                    System.out.println("5 - Remover senha");
                    System.out.println("0 - Sair");
                    System.out.print("\nEscolha uma opção: ");

                    String option = scanner.nextLine(); // Lê a opção do usuário

                    // Executa a ação conforme a escolha do usuário
                    switch (option) {
                        case "1" -> controller.addPassword(scanner); // Adiciona uma nova senha
                        case "2" -> controller.listPasswords(); // Lista todas as senhas
                        case "3" -> controller.generateSecurePassword(scanner); // Gera uma senha segura
                        case "4" -> controller.checkPasswordLeak(scanner); // Verifica se uma senha foi vazada
                        case "5" -> controller.deletePassword(scanner); // Remove uma senha
                        case "0" -> {
                            System.out.println("\nSaindo...");
                            scanner.close(); // Fecha o scanner e encerra a aplicação
                            return;
                        }
                        default -> System.out.println("\nOpção inválida."); // Opção não reconhecida
                    }
                } catch (Exception e) {
                    // Captura erros durante a execução de qualquer operação
                    System.err.println("Erro durante a operação: " + e.getMessage());
                    e.printStackTrace();
                    System.out.println("\nPressione ENTER para continuar...");
                    scanner.nextLine(); // Pausa para o usuário ver a mensagem de erro
                }
            }
        } catch (Exception e) {
            // Captura erros inesperados que possam ocorrer no processo todo
            System.err.println("Erro fatal: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
