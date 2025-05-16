package com.sistemacontrole;

import com.formdev.flatlaf.FlatLightLaf;
import com.sistemacontrole.dao.ProdutoDAO;
import com.sistemacontrole.dao.UsuarioDAO;
import com.sistemacontrole.view.LoginFrame;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {
    public static void main(String[] args) {
        try {
            // Ativa o visual FlatLaf
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Falha ao inicializar FlatLaf");
        }

        // Cria tabelas
        ProdutoDAO produtoDAO = new ProdutoDAO();
        produtoDAO.criarTabela();

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.criarTabela();

        // Cria usuário admin se nenhum existir
        if (!existeUsuarioCadastrado()) {
            usuarioDAO.criarUsuario("admin", "admin123", "SUPERVISAO");
            System.out.println("Usuário padrão criado: admin / admin123");
        }

        // Abre a interface de login
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    private static boolean existeUsuarioCadastrado() {
        String sql = "SELECT COUNT(*) AS total FROM usuarios";
        try (Connection conn = com.sistemacontrole.dao.Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar usuários: " + e.getMessage());
        }
        return false;
    }
}
