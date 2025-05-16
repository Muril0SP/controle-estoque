package com.sistemacontrole.dao;

import com.sistemacontrole.util.UsuarioLogado;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RetiradaDAO {

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
        String sql = "INSERT INTO retiradas(produto, quantidade, usuario, data) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, produto);
            pstmt.setInt(2, quantidade);
            pstmt.setString(3, UsuarioLogado.username);
            pstmt.setString(4, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao registrar retirada: " + e.getMessage());
        }
    }

    public List<String[]> listarRetiradas() {
        List<String[]> retiradas = new ArrayList<>();
        String sql = "SELECT * FROM retiradas ORDER BY id DESC";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                retiradas.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("produto"),
                        String.valueOf(rs.getInt("quantidade")),
                        rs.getString("usuario"),
                        rs.getString("data")
                });
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar retiradas: " + e.getMessage());
        }
        return retiradas;
    }
}
