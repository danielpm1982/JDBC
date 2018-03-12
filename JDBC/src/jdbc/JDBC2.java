/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 *
 * @author Daniel
 */
public class JDBC2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final String DATABASE_URL = "jdbc:mysql://localhost:3306/books?autoReconnect=true&useSSL=false";
        final String SELECT_QUERY = "select * from (select * from authors inner join authorisbn on authors.AuthorsID=authorisbn.AuthorID) x inner join titles on x.ISBN=titles.ISBN;";
        final String USER_NAME = "root";
        final String PASSWORD = "root";
        /*using JdbcRowSet to encapsulate Connection, Statement and Resultset set and manipulation.*/
        try(
            JdbcRowSet jdbcRowSet = RowSetProvider.newFactory().createJdbcRowSet();
        ){
            jdbcRowSet.setUrl(DATABASE_URL);
            jdbcRowSet.setUsername(USER_NAME);
            jdbcRowSet.setPassword(PASSWORD);
            jdbcRowSet.setCommand(SELECT_QUERY);
            jdbcRowSet.execute();
            
            ResultSetMetaData metadata = jdbcRowSet.getMetaData();
            int columnCount=metadata.getColumnCount();
            for(int i=1; i<=columnCount; i++){
                System.out.printf("%-8s\t",metadata.getColumnName(i));
            }
            System.out.println("");
            for(int i=1; i<=columnCount; i++){
                System.out.printf("%-8s\t","-------------");
            }
            System.out.println("");
            int numberOfRows = 0;
            while(jdbcRowSet.next()){
                for(int i=1; i<=columnCount; i++){
                    System.out.printf("%-8s\t",jdbcRowSet.getObject(i));
                }
                System.out.println("");
                numberOfRows++;
            }
            System.out.printf("%-8s\t","-------------");
            System.out.println("");
            System.out.println("Total RowSet results: "+numberOfRows+".");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
