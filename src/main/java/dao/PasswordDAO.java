package dao;

import model.PasswordEntry;
import util.DatabaseHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordDAO {

    public void addPassword(PasswordEntry entry) {
        String sql = "INSERT INTO passwords(service, username, password) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entry.getService());
            pstmt.setString(2, entry.getUsername());
            pstmt.setString(3, entry.getPassword());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar senha: " + e.getMessage());
        }
    }

    public List<PasswordEntry> listPasswords() {
        List<PasswordEntry> list = new ArrayList<>();
        String sql = "SELECT id, service, username, password, created_at FROM passwords";

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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

    public boolean deletePassword(int id) {
        String sql = "DELETE FROM passwords WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // true se removeu algum registro

        } catch (SQLException e) {
            System.err.println("Erro ao deletar senha: " + e.getMessage());
            return false;
        }
    }

}
