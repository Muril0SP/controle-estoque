package com.sistemacontrole.dao;

import com.sistemacontrole.util.PasswordUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "departamento TEXT DEFAULT 'GERAL'" +
                ");";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de usuários: " + e.getMessage());
        }
    }

    public boolean validarLogin(String username, String password) {
        String sql = "SELECT password FROM usuarios WHERE username = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                String enteredHash = PasswordUtils.hashPassword(password);
                return storedHash.equals(enteredHash);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao validar login: " + e.getMessage());
        }
        return false;
    }

    // 🔐 Método para buscar o departamento do usuário logado
    public String getDepartamentoUsuario(String username) {
        String sql = "SELECT departamento FROM usuarios WHERE username = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("departamento");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar departamento: " + e.getMessage());
        }
        return null;
    }

    public boolean criarUsuario(String username, String password, String departamento) {
        String sql = "INSERT INTO usuarios(username, password, departamento) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, PasswordUtils.hashPassword(password));
            pstmt.setString(3, departamento);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao criar usuário: " + e.getMessage());
            return false;
        }
    }

    public List<String[]> listarUsuarios() {
        List<String[]> usuarios = new ArrayList<>();
        String sql = "SELECT id, username, departamento FROM usuarios";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("username"),
                        rs.getString("departamento")
                });
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
        }
        return usuarios;
    }

    public void excluirUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao excluir usuário: " + e.getMessage());
        }
    }
}
