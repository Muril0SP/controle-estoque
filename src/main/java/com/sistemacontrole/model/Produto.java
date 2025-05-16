package com.sistemacontrole.model;

public class Produto {
    private int id;
    private String nome;
    private String codigo;
    private String categoria;
    private int quantidade;
    private double preco;
    private String imagem;   // NOVO CAMPO

    public Produto() {
    }

    public Produto(int id, String nome, String codigo, String categoria, int quantidade, double preco) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    // NOVO: Getters e Setters para imagem
    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }

    @Override
    public String toString() {
        return "Produto [id=" + id + ", nome=" + nome + ", codigo=" + codigo +
                ", categoria=" + categoria + ", quantidade=" + quantidade + ", preco=" + preco + "]";
    }
}
