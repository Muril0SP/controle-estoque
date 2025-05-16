// ✅ TelaHistoricoRetiradas.java minimalista e alinhado
package com.sistemacontrole.view;

import com.sistemacontrole.dao.MovimentacaoDAO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaHistoricoRetiradas extends JFrame {

    private JTable tabela;
    private MovimentacaoDAO dao = new MovimentacaoDAO();

    public TelaHistoricoRetiradas() {
        setTitle("Histórico de Retiradas");
        setSize(700, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[grow]", "[]10[grow]"));

        JLabel lblTitulo = new JLabel("Histórico de Retiradas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        tabela = new JTable();
        JScrollPane scrollPane = new JScrollPane(tabela);

        panel.add(lblTitulo, "center");
        panel.add(scrollPane, "grow");

        add(panel);
        carregarTabela();
    }

    private void carregarTabela() {
        List<String[]> retiradas = dao.listarRetiradas();
        DefaultTableModel model = new DefaultTableModel(new String[]{"Data", "Produto", "Quantidade", "Usuário"}, 0);
        for (String[] r : retiradas) {
            model.addRow(r);
        }
        tabela.setModel(model);
    }
}
