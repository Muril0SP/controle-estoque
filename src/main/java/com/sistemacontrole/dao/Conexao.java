package com.sistemacontrole.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:sqlite:estoque.db";

    public static Connection conectar() throws SQLException {
        try {
            // Força o carregamento do driver SQLite
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver SQLite não encontrado.", e);
        }

        // Conecta ao banco local
        return DriverManager.getConnection(URL);
    }
}
