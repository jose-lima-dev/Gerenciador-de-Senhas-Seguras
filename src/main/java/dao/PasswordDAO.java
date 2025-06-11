package dao;

import model.PasswordEntry;
import util.DatabaseHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por interagir com o banco de dados SQLite
 * para operações de CRUD das entradas de senha.
 */
public class PasswordDAO {

    /**
     * Adiciona uma nova entrada de senha ao banco de dados.
     *
     * @param entry Objeto PasswordEntry contendo serviço, usuário e senha criptografada.
     */
    public void addPassword(PasswordEntry entry) {
        String sql = "INSERT INTO passwords(service, username, password) VALUES (?, ?, ?)";

        // Conexão com o banco de dados e preparação do comando SQL
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Define os valores para os parâmetros do SQL
            pstmt.setString(1, entry.getService());
            pstmt.setString(2, entry.getUsername());
            pstmt.setString(3, entry.getPassword());

            // Executa o comando de inserção
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar senha: " + e.getMessage());
        }
    }

    /**
     * Lista todas as senhas armazenadas no banco de dados.
     *
     * @return Lista de PasswordEntry com os dados recuperados.
     */
    public List<PasswordEntry> listPasswords() {
        List<PasswordEntry> list = new ArrayList<>();
        String sql = "SELECT id, service, username, password, created_at FROM passwords";

        // Conexão com o banco e execução da consulta
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Percorre os resultados e cria objetos PasswordEntry
            while (rs.next()) {
                PasswordEntry entry = new PasswordEntry(
                        rs.getString("service"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                list.add(entry);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar senhas: " + e.getMessage());
        }

        return list;
    }

    /**
     * Remove uma senha do banco de dados com base no ID.
     *
     * @param id ID da entrada a ser removida.
     * @return true se a senha foi removida com sucesso, false caso contrário.
     */
    public boolean deletePassword(int id) {
        String sql = "DELETE FROM passwords WHERE id = ?";

        // Conexão com o banco e preparação da exclusão
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            // Executa a exclusão e verifica se alguma linha foi afetada
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar senha: " + e.getMessage());
            return false;
        }
    }

}
