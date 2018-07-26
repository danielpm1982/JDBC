/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Daniel
 */
public class ResultSetTableModel extends AbstractTableModel{
    
    private final Connection connection;
    private final Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData resultSetMetaData;
    private int numberOfRows;
    private boolean connectedToDataBase = false;
    
    public ResultSetTableModel(String url, String userName, String password, String query) throws SQLException{
        connection=DriverManager.getConnection(url, userName, password);
        statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY,ResultSet.HOLD_CURSORS_OVER_COMMIT);
        connectedToDataBase=true;
        setQuery(query);
    }
    @Override
    
    public Class<?> getColumnClass(int columnIndex) throws IllegalStateException{
        if(!connectedToDataBase){
            throw new IllegalStateException("Not Connected to DataBase");
        }
        try {
            String className = resultSetMetaData.getColumnClassName(columnIndex+1);
            return Class.forName(className);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    return Object.class;
    }

    @Override
    public int getColumnCount() throws IllegalStateException{
        if(!connectedToDataBase){
            throw new IllegalStateException("Not Connected to DataBase");
        }
        try {
            return resultSetMetaData.getColumnCount();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @Override
    public String getColumnName(int column) throws IllegalStateException{
        if(!connectedToDataBase){
            throw new IllegalStateException("Not Connected to DataBase");
        }
        try {
            return resultSetMetaData.getColumnName(column+1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    public int getRowCount() throws IllegalStateException{
        if(!connectedToDataBase){
            throw new IllegalStateException("Not Connected to DataBase");
        }
        return numberOfRows;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) throws IllegalStateException{
        if(!connectedToDataBase){
            throw new IllegalStateException("Not Connected to DataBase");
        }
        try {
            resultSet.absolute(rowIndex+1);
            return resultSet.getObject(columnIndex+1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }
    
    protected void setQuery(String query) throws SQLException,IllegalStateException{
        if(!connectedToDataBase){
            throw new IllegalStateException("Not Connected to DataBase");
        }
        resultSet=statement.executeQuery(query);
        resultSetMetaData=resultSet.getMetaData();
        resultSet.last();
        numberOfRows=resultSet.getRow();
        fireTableStructureChanged();
    }
    
    public void disconnectFromDataBase(){
        if(connectedToDataBase){
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally{
                connectedToDataBase=false;
            }
        }
    }
}
