/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc2;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Daniel
 */
public class MyFrame extends JFrame{
    
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/books?autoReconnect=true&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String DEFAULT_QUERY = "select * from (select * from authors inner join authorisbn on authors.AuthorsID=authorisbn.AuthorID) x inner join titles on x.ISBN=titles.ISBN;";
    private static ResultSetTableModel resultSetTableModel;
    
    public MyFrame(){
        try {
            resultSetTableModel=new ResultSetTableModel(DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY);
            final JTextArea textArea = new JTextArea(DEFAULT_QUERY, 3, 100);
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            JScrollPane scrollPane = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            
            JButton submitButton = new JButton("Submit Query");
            
            Box box1 = Box.createHorizontalBox();
            box1.add(scrollPane);
            box1.add(submitButton);
            
            JTable resultTable = new JTable(resultSetTableModel);
            
            JLabel filterLabel = new JLabel("Filter:");
            final JTextField filterTextField = new JTextField();
            JButton filterButton = new JButton("Apply Filter");
            
            Box box2 = Box.createHorizontalBox();
            box2.add(filterLabel);
            box2.add(filterTextField);
            box2.add(filterButton);
            
            this.getContentPane().add(box1,BorderLayout.NORTH);
            this.getContentPane().add(new JScrollPane(resultTable));
            this.getContentPane().add(box2,BorderLayout.SOUTH);
            
            submitButton.addActionListener(x->{
                try {
                    resultSetTableModel.setQuery(textArea.getText());
                } catch (SQLException | IllegalStateException ex) {
                    JOptionPane.showMessageDialog(null,ex.getMessage(),"Database ERROR!",JOptionPane.ERROR_MESSAGE);
                    try {
                        resultSetTableModel.setQuery(DEFAULT_QUERY);
                        textArea.setText(DEFAULT_QUERY);
                    } catch (SQLException | IllegalStateException ex2) {
                        JOptionPane.showMessageDialog(null,ex2.getMessage(),"Database ERROR!",JOptionPane.ERROR_MESSAGE);
                        resultSetTableModel.disconnectFromDataBase();
                        System.exit(1);
                    }
                }
            });
            final TableRowSorter<TableModel> sorter = new TableRowSorter<>(resultSetTableModel);
            resultTable.setRowSorter(sorter);
            filterButton.addActionListener(x->{
                String text = filterTextField.getText();
                if(text.length()==0){
                    sorter.setRowFilter(null);
                }
                else{
                    try{
                        sorter.setRowFilter(RowFilter.regexFilter(text));
                    } catch(PatternSyntaxException ex3){
                        JOptionPane.showMessageDialog(null, "Bad Regex Pattern", "Bad Regex Pattern",JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    resultSetTableModel.disconnectFromDataBase();
                    System.exit(1);
                }
            });
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"DataBase Error",JOptionPane.ERROR_MESSAGE);
            resultSetTableModel.disconnectFromDataBase();
            System.exit(1);
        }
    }
}
