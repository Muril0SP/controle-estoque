package com.sistemacontrole.view;

import com.sistemacontrole.dao.ProdutoDAO;
import com.sistemacontrole.model.Produto;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class TelaPrincipal extends JFrame {

    private JTextField txtNome, txtCodigo, txtCategoria, txtQuantidade, txtPreco, txtValorTotal, txtBuscar;
    private JTable tabelaProdutos;
    private JLabel lblImagemPreview;
    private String caminhoImagem = null;
    private ProdutoDAO dao = new ProdutoDAO();
    private String codigoEdicao = null;

    public TelaPrincipal() {
        setTitle("Sistema de Controle de Estoque");
        setSize(1000, 700);                           // tamanho ajustado
        setResizable(false);                          // opcional: bloqueia redimensionamento
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);  // 👈 CORREÇÃO AQUI
        setLocationRelativeTo(null);

        initComponents();
        dao.criarTabela();
        carregarTabela();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new MigLayout("wrap 4", "[grow][grow][grow][120px]", "[][][][][][][]"));
        JLabel lblLogo = new JLabel();

        try {
            ImageIcon logo = new ImageIcon("imagens/logo.png");
            Image image = logo.getImage().getScaledInstance(200, 50, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            lblLogo.setText("LOGO");
        }

        txtNome = new JTextField(15);
        txtCodigo = new JTextField(15);
        txtCategoria = new JTextField(15);
        txtQuantidade = new JTextField(15);
        txtPreco = new JTextField(15);
        txtValorTotal = new JTextField(15);
        txtValorTotal.setEditable(false);
        txtBuscar = new JTextField(20);

        lblImagemPreview = new JLabel();
        lblImagemPreview.setPreferredSize(new Dimension(120, 120));
        lblImagemPreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JButton btnSelecionarImagem = new JButton("Selecionar Imagem");
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnSalvar = new JButton("Salvar Alterações");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnBuscar = new JButton("Buscar");

        btnSelecionarImagem.addActionListener(e -> selecionarImagem());
        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnEditar.addActionListener(e -> prepararEdicao());
        btnSalvar.addActionListener(e -> salvarAlteracoes());
        btnExcluir.addActionListener(e -> excluirProduto());
        btnBuscar.addActionListener(e -> buscarProduto());

        txtQuantidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calcularValorTotal();
            }
        });

        txtPreco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calcularValorTotal();
            }
        });

        panel.add(lblLogo, "span 4, center");
        panel.add(new JLabel("Nome:"));
        panel.add(txtNome, "span 2, growx");
        panel.add(lblImagemPreview, "spany 7, right");

        panel.add(new JLabel("Código:"));
        panel.add(txtCodigo, "span 2, growx");

        panel.add(new JLabel("Categoria:"));
        panel.add(txtCategoria, "span 2, growx");

        panel.add(new JLabel("Quantidade:"));
        panel.add(txtQuantidade, "span 2, growx");

        panel.add(new JLabel("Preço Unitário:"));
        panel.add(txtPreco, "span 2, growx");

        panel.add(new JLabel("Valor Total:"));
        panel.add(txtValorTotal, "span 2, growx");

        panel.add(btnSelecionarImagem, "span 4, center");
        panel.add(btnAdicionar, "split 4");
        panel.add(btnEditar);
        panel.add(btnSalvar);
        panel.add(btnExcluir);

        JPanel panelBusca = new JPanel(new MigLayout("fill", "[][grow][]", "[]"));
        panelBusca.add(new JLabel("Buscar:"));
        panelBusca.add(txtBuscar, "growx");
        panelBusca.add(btnBuscar);

        tabelaProdutos = new JTable();
        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> preencherCampos());

        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(panelBusca, BorderLayout.SOUTH);
        getContentPane().add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);
    }

    private void calcularValorTotal() {
        try {
            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
            double preco = Double.parseDouble(txtPreco.getText().trim());
            txtValorTotal.setText(String.format("%.2f", quantidade * preco));
        } catch (NumberFormatException e) {
            txtValorTotal.setText("");
        }
    }

    private void selecionarImagem() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                File pastaImagens = new File("imagens");
                if (!pastaImagens.exists()) pastaImagens.mkdir();
                String extensao = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
                String novoNome = "produto_" + System.currentTimeMillis() + extensao;
                File destino = new File(pastaImagens, novoNome);
                Files.copy(selectedFile.toPath(), destino.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                caminhoImagem = destino.getPath();
                exibirImagemPreview(caminhoImagem);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao copiar imagem: " + e.getMessage());
            }
        }
    }

    private void exibirImagemPreview(String path) {
        if (path != null && new File(path).exists()) {
            ImageIcon icon = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH));
            lblImagemPreview.setIcon(icon);
        } else {
            lblImagemPreview.setIcon(null);
        }
    }

    private void adicionarProduto() {
        try {
            Produto p = new Produto();
            p.setNome(txtNome.getText().trim());
            p.setCodigo(txtCodigo.getText().trim());
            p.setCategoria(txtCategoria.getText().trim());
            p.setQuantidade(Integer.parseInt(txtQuantidade.getText().trim()));
            p.setPreco(Double.parseDouble(txtPreco.getText().trim()));
            p.setImagem(caminhoImagem);
            dao.inserir(p);
            limparCampos();
            carregarTabela();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar produto: " + ex.getMessage());
        }
    }

    private void prepararEdicao() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha != -1) {
            codigoEdicao = tabelaProdutos.getValueAt(linha, 2).toString();
            preencherCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.");
        }
    }

    private void salvarAlteracoes() {
        if (codigoEdicao != null) {
            try {
                Produto p = new Produto();
                p.setNome(txtNome.getText().trim());
                p.setCodigo(codigoEdicao);
                p.setCategoria(txtCategoria.getText().trim());
                p.setQuantidade(Integer.parseInt(txtQuantidade.getText().trim()));
                p.setPreco(Double.parseDouble(txtPreco.getText().trim()));
                p.setImagem(caminhoImagem);
                dao.atualizar(p);
                limparCampos();
                carregarTabela();
                codigoEdicao = null;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar alterações: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum produto selecionado para edição.");
        }
    }

    private void excluirProduto() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha != -1) {
            String codigo = tabelaProdutos.getValueAt(linha, 2).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja excluir o produto?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dao.excluirPorCodigo(codigo);
                limparCampos();
                carregarTabela();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.");
        }
    }

    private void buscarProduto() {
        carregarTabelaComLista(dao.buscarPorNomeOuCodigo(txtBuscar.getText().trim()));
    }

    private void preencherCampos() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha != -1) {
            txtNome.setText(tabelaProdutos.getValueAt(linha, 1).toString());
            txtCodigo.setText(tabelaProdutos.getValueAt(linha, 2).toString());
            txtCategoria.setText(tabelaProdutos.getValueAt(linha, 3).toString());
            txtQuantidade.setText(tabelaProdutos.getValueAt(linha, 4).toString());
            txtPreco.setText(tabelaProdutos.getValueAt(linha, 5).toString());
            calcularValorTotal();
            txtCodigo.setEditable(false);
            Produto produto = dao.buscarPorCodigo(txtCodigo.getText());
            if (produto != null) {
                caminhoImagem = produto.getImagem();
                exibirImagemPreview(caminhoImagem);
            }
        }
    }

    private void carregarTabela() {
        carregarTabelaComLista(dao.listarTodos());
    }

    private void carregarTabelaComLista(List<Produto> produtos) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nome", "Código", "Categoria", "Quantidade", "Preço", "Valor Total"}, 0);
        for (Produto p : produtos) {
            model.addRow(new Object[]{p.getId(), p.getNome(), p.getCodigo(), p.getCategoria(), p.getQuantidade(), p.getPreco(), String.format("%.2f", p.getPreco() * p.getQuantidade())});
        }
        tabelaProdutos.setModel(model);
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCodigo.setText("");
        txtCategoria.setText("");
        txtQuantidade.setText("");
        txtPreco.setText("");
        txtValorTotal.setText("");
        txtBuscar.setText("");
        txtCodigo.setEditable(true);
        caminhoImagem = null;
        lblImagemPreview.setIcon(null);
        tabelaProdutos.clearSelection();
        codigoEdicao = null;
    }
}
