package com.sistemacontrole.view;

import com.sistemacontrole.util.UsuarioLogado;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Dashboard - Sistema de Controle de Estoque");
        setSize(800, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new MigLayout("fill", "[30%][70%]", "[]"));

        // Painel do logo
        JPanel logoPanel = new JPanel(new BorderLayout());
        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon logo = new ImageIcon("imagens/logo.png");
            Image image = logo.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            lblLogo.setText("LOGO");
        }
        logoPanel.add(lblLogo, BorderLayout.CENTER);

        // Painel do conteúdo + botão logout
        JPanel contentPanel = new JPanel(new MigLayout("wrap 1", "[center]", "[][]10[][][][][]20[]"));

        // Botão logout no canto superior direito
        JButton btnLogout = new JButton();
        btnLogout.setToolTipText("Logout");
        try {
            ImageIcon icon = new ImageIcon("imagens/logout.png");
            Image image = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            btnLogout.setIcon(new ImageIcon(image));
        } catch (Exception ex) {
            btnLogout.setText("Sair");
        }
        btnLogout.setPreferredSize(new Dimension(32, 32));
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.add(new JLabel(), BorderLayout.CENTER);
        topBar.add(btnLogout, BorderLayout.EAST);

        JLabel lblTitulo = new JLabel("Bem-vindo à Gestão de Estoque");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblUsuario = new JLabel("Usuário: " + UsuarioLogado.username +
                " | Departamento: " + UsuarioLogado.departamento);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsuario.setHorizontalAlignment(SwingConstants.CENTER);

        // Botões principais
        JButton btnMovimentacao = new JButton("<html><center>Movimentação de Estoque<br><font size='3'>(Efetuar Retirada)</font></center></html>");
        JButton btnEstoque = new JButton("Gestão de Estoque");
        JButton btnHistorico = new JButton("Histórico de Retiradas");
        JButton btnUsuarios = new JButton("Gestão de Usuários");

        Dimension buttonSize = new Dimension(280, 50);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        for (JButton btn : new JButton[]{btnMovimentacao, btnEstoque, btnHistorico, btnUsuarios}) {
            btn.setPreferredSize(buttonSize);
            btn.setFont(buttonFont);
        }

        // Ações
        btnMovimentacao.addActionListener(e -> new TelaMovimentacaoEstoque().setVisible(true));

        btnEstoque.addActionListener(e -> {
            String dep = UsuarioLogado.departamento;
            if (dep.equals("ALMOXARIFADO") || dep.equals("FINANCEIRO") || dep.equals("SUPERVISAO")) {
                new TelaPrincipal().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Você não tem permissão para acessar o módulo de Estoque.");
            }
        });

        btnHistorico.addActionListener(e -> {
            String dep = UsuarioLogado.departamento;
            if (dep.equals("FINANCEIRO") || dep.equals("SUPERVISAO")) {
                new TelaHistoricoRetiradas().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Você não tem permissão para acessar o Histórico de Retiradas.");
            }
        });

        btnUsuarios.addActionListener(e -> {
            String dep = UsuarioLogado.departamento;
            if (dep.equals("SUPERVISAO")) {
                new UsuarioFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Você não tem permissão para acessar o módulo de Usuários.");
            }
        });

        // Desenvolvedor
        JLabel lblDesenvolvedor = new JLabel(
                "<html><div style='text-align: center;'>Desenvolvido por: Murilo Aparecido de Oliveira | RA 230122<br/>Curso: Análise e Desenvolvimento de Sistemas</div></html>"
        );
        lblDesenvolvedor.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesenvolvedor.setHorizontalAlignment(SwingConstants.CENTER);

        // Adiciona componentes
        contentPanel.add(topBar, "growx, wrap");
        contentPanel.add(lblTitulo);
        contentPanel.add(lblUsuario);
        contentPanel.add(btnMovimentacao, "center");
        contentPanel.add(btnEstoque, "center");
        contentPanel.add(btnHistorico, "center");
        contentPanel.add(btnUsuarios, "center");
        contentPanel.add(lblDesenvolvedor, "gaptop 20");

        mainPanel.add(logoPanel, "growy");
        mainPanel.add(contentPanel, "grow");

        add(mainPanel);
    }
}
