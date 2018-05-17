package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class WordsView {
    private JTable table;
    private JFrame frame;

    public WordsView() {
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(1, 1));

        frame = new JFrame("Найденные отношения");
        DefaultTableModel model = new DefaultTableModel(new Object[10][3],
                new String[]{"Субъект", "Отношение", "Объект"});
        table = new JTable(model);

        panel.add(new JScrollPane(table));
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(false);
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }

    public void clearTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        while(table.getRowCount() > 0) {
            model.removeRow(0);
        }
        model.fireTableDataChanged();
    }

    public void addTableRow(String subject, String property, String object) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{subject, property, object});
        model.fireTableDataChanged();
    }
}
