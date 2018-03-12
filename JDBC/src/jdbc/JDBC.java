/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Daniel
 */
public class JDBC {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final String DATABASE_URL = "jdbc:mysql://localhost:3306/books?autoReconnect=true&useSSL=false";
        final String SELECT_QUERY = "select * from (select * from authors inner join authorisbn on authors.AuthorsID=authorisbn.AuthorID) x inner join titles on x.ISBN=titles.ISBN;";
        try(
            Connection conn = DriverManager.getConnection(DATABASE_URL,"root","root");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_QUERY);
        ){
            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount=metadata.getColumnCount();
            for(int i=1; i<=columnCount; i++){
                System.out.printf("%-8s\t",metadata.getColumnName(i));
            }
            System.out.println("");
            while(rs.next()){
                for(int i=1; i<=columnCount; i++){
                    System.out.printf("%-8s\t",rs.getObject(i));
                }
                System.out.println("");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
