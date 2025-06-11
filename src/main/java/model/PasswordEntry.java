package model;

/**
 * Classe modelo que representa uma entrada de senha no sistema.
 * Contém informações sobre o serviço, o nome de usuário e a senha criptografada.
 */
public class PasswordEntry {
    private int id; // Identificador da entrada (geralmente fornecido pelo banco de dados)
    private String service; // Nome do serviço (ex: "Gmail", "Facebook")
    private String username; // Nome de usuário utilizado no serviço
    private String password; // Senha criptografada associada ao serviço

    /**
     * Construtor que inicializa a entrada de senha com serviço, usuário e senha.
     *
     * @param service Nome do serviço
     * @param username Nome de usuário
     * @param password Senha criptografada
     */
    public PasswordEntry(String service, String username, String password) {
        this.service = service;
        this.username = username;
        this.password = password;
    }

    // Getters e setters

    /**
     * Retorna o ID da entrada (útil para operações de exclusão).
     */
    public int getId() {
        return id;
    }

    /**
     * Define o ID da entrada. Normalmente usado quando o dado vem do banco.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna o nome do serviço.
     */
    public String getService() {
        return service;
    }

    /**
     * Retorna o nome de usuário.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retorna a senha criptografada.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retorna uma representação textual da entrada, com senha criptografada visível.
     * Ideal para fins de depuração ou listagem.
     */
    @Override
    public String toString() {
        return "Serviço: " + service + ", Usuário: " + username + ", Senha (criptografada): " + password;
    }
}
