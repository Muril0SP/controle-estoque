package com.sistemacontrole.dao;

import com.sistemacontrole.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS produtos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "codigo TEXT NOT NULL UNIQUE," +
                "categoria TEXT," +
                "quantidade INTEGER DEFAULT 0," +
                "preco REAL," +
                "imagem TEXT" +
                ");";
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            movimentacaoDAO.criarTabela();
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    public void inserir(Produto produto) throws SQLException {
        if (buscarPorCodigo(produto.getCodigo()) != null) {
            throw new SQLException("Já existe um produto com esse código.");
        }
        String sql = "INSERT INTO produtos(nome, codigo, categoria, quantidade, preco, imagem) VALUES(?,?,?,?,?,?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, produto.getNome());
            pstmt.setString(2, produto.getCodigo());
            pstmt.setString(3, produto.getCategoria());
            pstmt.setInt(4, produto.getQuantidade());
            pstmt.setDouble(5, produto.getPreco());
            pstmt.setString(6, produto.getImagem());
            pstmt.executeUpdate();
        }
    }

    public Produto buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM produtos WHERE codigo = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Produto p = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("codigo"),
                        rs.getString("categoria"),
                        rs.getInt("quantidade"),
                        rs.getDouble("preco")
                );
                p.setImagem(rs.getString("imagem"));
                return p;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar produto: " + e.getMessage());
        }
        return null;
    }

    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produto p = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("codigo"),
                        rs.getString("categoria"),
                        rs.getInt("quantidade"),
                        rs.getDouble("preco")
                );
                p.setImagem(rs.getString("imagem"));
                produtos.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

    public List<Produto> buscarPorNomeOuCodigo(String termo) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE nome LIKE ? OR codigo LIKE ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");
            pstmt.setString(2, "%" + termo + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Produto p = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("codigo"),
                        rs.getString("categoria"),
                        rs.getInt("quantidade"),
                        rs.getDouble("preco")
                );
                p.setImagem(rs.getString("imagem"));
                produtos.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar produtos: " + e.getMessage());
        }
        return produtos;
    }

    public void atualizar(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, categoria = ?, quantidade = ?, preco = ?, imagem = ? WHERE codigo = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, produto.getNome());
            pstmt.setString(2, produto.getCategoria());
            pstmt.setInt(3, produto.getQuantidade());
            pstmt.setDouble(4, produto.getPreco());
            pstmt.setString(5, produto.getImagem());
            pstmt.setString(6, produto.getCodigo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    public boolean retirarEstoque(String nomeProduto, int quantidade) {
        String sql = "UPDATE produtos SET quantidade = quantidade - ? WHERE nome = ? AND quantidade >= ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantidade);
            pstmt.setString(2, nomeProduto);
            pstmt.setInt(3, quantidade);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                movimentacaoDAO.registrarRetirada(nomeProduto, quantidade);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao retirar estoque: " + e.getMessage());
        }
        return false;
    }

    public void excluirPorCodigo(String codigo) {
        String sql = "DELETE FROM produtos WHERE codigo = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao excluir produto: " + e.getMessage());
        }
    }

    public List<String> listarNomesProdutos() {
        List<String> nomes = new ArrayList<>();
        String sql = "SELECT nome FROM produtos";
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                nomes.add(rs.getString("nome"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar nomes: " + e.getMessage());
        }
        return nomes;
    }
}
