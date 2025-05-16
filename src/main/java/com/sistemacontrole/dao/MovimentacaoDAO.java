// ✅ 1. Classe MovimentacaoDAO para registrar retiradas
package com.sistemacontrole.dao;

import com.sistemacontrole.util.UsuarioLogado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAO {

    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS retiradas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "produto TEXT NOT NULL," +
                "quantidade INTEGER NOT NULL," +
                "usuario TEXT NOT NULL," +
                "data TEXT NOT NULL" +
                ");";
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de retiradas: " + e.getMessage());
        }
    }

    public void registrarRetirada(String produto, int quantidade) {
        String sql = "INSERT INTO retiradas (produto, quantidade, usuario, data) VALUES (?, ?, ?, datetime('now'))";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, produto);
            pstmt.setInt(2, quantidade);
            pstmt.setString(3, UsuarioLogado.username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao registrar retirada: " + e.getMessage());
        }
    }

    public List<String[]> listarRetiradas() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT * FROM retiradas ORDER BY data DESC";
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("data"),
                        rs.getString("produto"),
                        String.valueOf(rs.getInt("quantidade")),
                        rs.getString("usuario")
                });
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar retiradas: " + e.getMessage());
        }
        return lista;
    }
}
