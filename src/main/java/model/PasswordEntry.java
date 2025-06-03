package model;

public class PasswordEntry {
    private int id; // Opcional para o banco
    private String service;
    private String username;
    private String password; // senha já criptografada

    public PasswordEntry(String service, String username, String password) {
        this.id = id;
        this.service = service;
        this.username = username;
        this.password = password;
    }

    // getters e setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Serviço: " + service + ", Usuário: " + username + ", Senha (criptografada): " + password;
    }
}
