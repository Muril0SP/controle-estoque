package com.sistemacontrole.view;

import com.sistemacontrole.dao.UsuarioDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioFrame extends JFrame {

    private JTextField txtUsuario, txtSenha;
    private JComboBox<String> comboDepartamento;
    private JTable tabelaUsuarios;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public UsuarioFrame() {
        setTitle("Gestão de Usuários");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        txtUsuario = new JTextField(15);
        txtSenha = new JTextField(15);
        comboDepartamento = new JComboBox<>(new String[]{"GERAL", "ALMOXARIFADO", "FINANCEIRO", "SUPERVISAO"});

        JButton btnAdicionar = new JButton("Adicionar Usuário");
        JButton btnExcluir = new JButton("Excluir Usuário Selecionado");

        formPanel.add(new JLabel("Usuário:"));
        formPanel.add(txtUsuario);
        formPanel.add(new JLabel("Senha:"));
        formPanel.add(txtSenha);
        formPanel.add(new JLabel("Departamento:"));
        formPanel.add(comboDepartamento);
        formPanel.add(btnAdicionar);
        formPanel.add(btnExcluir);

        btnAdicionar.addActionListener(e -> adicionarUsuario());
        btnExcluir.addActionListener(e -> excluirUsuario());

        tabelaUsuarios = new JTable();
        carregarTabela();

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabelaUsuarios), BorderLayout.CENTER);

        add(panel);
    }

    private void adicionarUsuario() {
        String usuario = txtUsuario.getText().trim();
        String senha = txtSenha.getText().trim();
        String departamento = comboDepartamento.getSelectedItem().toString();

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha usuário e senha.");
            return;
        }

        if (usuarioDAO.criarUsuario(usuario, senha, departamento)) {
            JOptionPane.showMessageDialog(this, "Usuário criado com sucesso!");
            txtUsuario.setText("");
            txtSenha.setText("");
            comboDepartamento.setSelectedIndex(0);
            carregarTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao criar usuário (usuário já existe?).");
        }
    }

    private void excluirUsuario() {
        int linha = tabelaUsuarios.getSelectedRow();
        if (linha != -1) {
            int id = Integer.parseInt(tabelaUsuarios.getValueAt(linha, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja excluir o usuário selecionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                usuarioDAO.excluirUsuario(id);
                carregarTabela();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir.");
        }
    }

    private void carregarTabela() {
        List<String[]> usuarios = usuarioDAO.listarUsuarios();
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Usuário", "Departamento"}, 0);
        for (String[] u : usuarios) {
            model.addRow(u);
        }
        tabelaUsuarios.setModel(model);
    }
}
