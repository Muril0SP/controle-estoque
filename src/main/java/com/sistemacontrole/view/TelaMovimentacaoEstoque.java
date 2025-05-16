package com.sistemacontrole.view;

import com.sistemacontrole.dao.ProdutoDAO;
import com.sistemacontrole.model.Produto;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class TelaMovimentacaoEstoque extends JFrame {

    private JComboBox<String> comboProdutos;
    private JTextField txtEstoqueAtual, txtPreco, txtQuantidade, txtValorTotal;
    private JLabel lblImagemPreview;
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    public TelaMovimentacaoEstoque() {
        setTitle("Movimentação de Estoque");
        setSize(700, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new MigLayout("fillx, wrap 4", "[right][150px][right][150px]", "[]10[]10[]10[]20[]"));

        comboProdutos = new JComboBox<>();
        carregarProdutos();
        comboProdutos.setEditable(true);
        comboProdutos.addActionListener(e -> atualizarDetalhesProduto());

        txtEstoqueAtual = new JTextField();
        txtEstoqueAtual.setEditable(false);

        txtPreco = new JTextField();
        txtPreco.setEditable(false);

        txtQuantidade = new JTextField();
        txtValorTotal = new JTextField();
        txtValorTotal.setEditable(false);

        txtQuantidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calcularValorTotal();
            }
        });

        lblImagemPreview = new JLabel();
        lblImagemPreview.setPreferredSize(new Dimension(300, 200));
        lblImagemPreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Linha 1
        panel.add(new JLabel("Produto:"), "align label");
        panel.add(comboProdutos, "growx");
        panel.add(new JLabel("Qtd. em Estoque:"), "align label");
        panel.add(txtEstoqueAtual, "growx");

        // Linha 2
        panel.add(new JLabel("Preço Unitário:"), "align label");
        panel.add(txtPreco, "growx");
        panel.add(new JLabel("Valor Total:"), "align label");
        panel.add(txtValorTotal, "growx");

        // Linha 3
        panel.add(new JLabel("Quantidade a Retirar:"), "align label");
        panel.add(txtQuantidade, "growx");
        panel.add(new JLabel("Imagem do Produto:"), "align label");
        panel.add(lblImagemPreview, "span 1, growx");

        // Botão
        JButton btnConfirmar = new JButton("Confirmar Retirada");
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 16));
        btnConfirmar.addActionListener(e -> retirarEstoque());

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnConfirmar);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
    }

    private void carregarProdutos() {
        List<String> nomes = produtoDAO.listarNomesProdutos();
        comboProdutos.removeAllItems();
        for (String nome : nomes) {
            comboProdutos.addItem(nome);
        }
    }

    private void atualizarDetalhesProduto() {
        String produtoSelecionado = comboProdutos.getSelectedItem() != null ? comboProdutos.getSelectedItem().toString() : "";
        if (!produtoSelecionado.isEmpty()) {
            List<Produto> lista = produtoDAO.buscarPorNomeOuCodigo(produtoSelecionado);
            if (!lista.isEmpty()) {
                Produto produto = lista.get(0);
                txtEstoqueAtual.setText(String.valueOf(produto.getQuantidade()));
                txtPreco.setText(String.format("%.2f", produto.getPreco()));

                if (produto.getImagem() != null && !produto.getImagem().isEmpty()) {
                    File imgFile = new File(produto.getImagem());
                    if (imgFile.exists()) {
                        exibirImagemPreview(produto.getImagem());
                    } else {
                        lblImagemPreview.setIcon(null);
                    }
                } else {
                    lblImagemPreview.setIcon(null);
                }
            }
        }
    }

    private void exibirImagemPreview(String path) {
        if (path != null && new File(path).exists()) {
            ImageIcon icon = new ImageIcon(path);
            Image image = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
            lblImagemPreview.setIcon(new ImageIcon(image));
            lblImagemPreview.revalidate();
            lblImagemPreview.repaint();
        } else {
            lblImagemPreview.setIcon(null);
        }
    }

    private void calcularValorTotal() {
        try {
            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
            double preco = Double.parseDouble(txtPreco.getText().replace(",", ".").trim());
            double total = quantidade * preco;
            txtValorTotal.setText(String.format("%.2f", total));
        } catch (NumberFormatException e) {
            txtValorTotal.setText("");
        }
    }

    private void retirarEstoque() {
        String produto = comboProdutos.getSelectedItem() != null ? comboProdutos.getSelectedItem().toString() : "";
        String quantidadeStr = txtQuantidade.getText().trim();

        if (produto.isEmpty() || quantidadeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o produto e a quantidade.");
            return;
        }

        try {
            int quantidade = Integer.parseInt(quantidadeStr);
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Informe uma quantidade válida.");
                return;
            }

            boolean sucesso = produtoDAO.retirarEstoque(produto, quantidade);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Retirada realizada com sucesso.");
                txtQuantidade.setText("");
                txtValorTotal.setText("");
                txtEstoqueAtual.setText("");
                lblImagemPreview.setIcon(null);
                carregarProdutos();
            } else {
                JOptionPane.showMessageDialog(this, "Estoque insuficiente ou produto não encontrado.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe uma quantidade numérica válida.");
        }
    }
}
