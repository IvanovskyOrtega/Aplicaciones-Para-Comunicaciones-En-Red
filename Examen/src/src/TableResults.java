package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TableResults extends JFrame {
 
    public TableResults(DefaultTableModel model) {
        //super();
        setTitle("Results");
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/icon.png")));
        JPanel contentPane = new JPanel(new GridLayout(1,0));
 
        JTable table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
 
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
 
        //Add the scroll pane to this panel.
        contentPane.add(scrollPane);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPane.setOpaque(true); //content panes must be opaque
        setContentPane(contentPane);
 
        pack();
    }
    
}