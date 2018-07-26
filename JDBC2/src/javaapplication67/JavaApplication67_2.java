package javaapplication67;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

public class JavaApplication67_2 {
    //this boolean is only for printing a "\n" before the second table display and on, but not before the first.
    static boolean firstTableAlreadyDisplayed=false;
    public static void main(String[] args) {
        final String DATABASE_URL = "jdbc:mysql://localhost:3306/books?autoReconnect=true&useSSL=false";
        final String USER="root";
        final String PASSWORD="root";
        final String QUERY1="select * from authors";
        final String QUERY2="select * from authorisbn";
        final String QUERY3="select * from titles";
        //final String QUERY4="select * from (select * from authors left join authorisbn on authors.AuthorsID=authorisbn.AuthorID) x union (select * from (select * from authors right join authorisbn on authors.AuthorsID=authorisbn.AuthorID) y);";
        final String QUERY5="select * from (select * from authors a left outer join authorisbn i on a.AuthorsID=i.AuthorID) x left outer join titles t on x.ISBN=t.ISBN union select * from (select * from authors a right outer join authorisbn i on a.AuthorsID=i.AuthorID) x right outer join titles t on x.ISBN=t.ISBN order by Title asc;";
        final String QUERY6="select * from (select * from pstmt3 union select * from pstmt4) z where z.FirstName like ? order by Title asc;";
        final Book bookToInsert1 = new Book("Apollo","Pinheiro",32165,"livro7",1,2007);
        final Book bookToInsert2 = new Book("Viludo","Pinheiro",79456,"livro8",5,2003);
        
        //using RowSets instead of explicit Connection, DriverManager, Statement, PreparedStatement or ResultSet.
        try(JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet();
            JdbcRowSet rowSetAuthors = RowSetProvider.newFactory().createJdbcRowSet();
            JdbcRowSet rowSetAuthorisbn = RowSetProvider.newFactory().createJdbcRowSet();
            JdbcRowSet rowSetTitles = RowSetProvider.newFactory().createJdbcRowSet();){
            
            //setAndExecuteRowSet(rowSet, DATABASE_URL, USER, PASSWORD, QUERY5);
            //using parameterized sql statements with RowSets, which use, internally, PreparedStatements.
            setAndExecuteRowSet2(rowSet, DATABASE_URL, USER, PASSWORD, QUERY6);
            printColumnNames(rowSet);
            printRowValues(rowSet);
            firstTableAlreadyDisplayed=true;
            
//            setAndExecuteRowSet(rowSetAuthors, DATABASE_URL, USER, PASSWORD, QUERY1);
//            setAndExecuteRowSet(rowSetAuthorisbn, DATABASE_URL, USER, PASSWORD, QUERY2);
//            setAndExecuteRowSet(rowSetTitles, DATABASE_URL, USER, PASSWORD, QUERY3);
//            insertRow(bookToInsert1, rowSetAuthors, rowSetAuthorisbn, rowSetTitles);
//            insertRow(bookToInsert2, rowSetAuthors, rowSetAuthorisbn, rowSetTitles);
            
            //search DB again and print the updated results after insertions above.
            rowSet.execute();
            printColumnNames(rowSet);
            printRowValues(rowSet);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    //gets the columnCount and columnNames to print the table header
    public static void printColumnNames(ResultSet rs) throws SQLException{
        if(firstTableAlreadyDisplayed){
            System.out.println("");
        }
        int columnCount = rs.getMetaData().getColumnCount();
        for (int i=1;i<=columnCount;i++){
            System.out.printf("%-15s",rs.getMetaData().getColumnName(i));
        }
        System.out.println("");
        for (int i=1;i<=columnCount;i++){
            System.out.printf("%-15s","-------------");
        }
        System.out.println("");
    }
    
    //sets the rs to the zero row and iterates it until the end, printing column values according to each columnTypeName.
    public static void printRowValues(ResultSet rs) throws SQLException{
        int columnCount = rs.getMetaData().getColumnCount();
        rs.beforeFirst();
        while(rs.next()){
            for(int i=1;i<=columnCount;i++){
                if(rs.getMetaData().getColumnTypeName(i).equals("INT")){
                    if(rs.getInt(i)!=0){
                        System.out.printf("%-15s",rs.getInt(i));
                    } else{
                        System.out.printf("%-15s","NULL");
                    }
                } else if(rs.getMetaData().getColumnTypeName(i).equals("VARCHAR")){
                    if(rs.getString(i)!=null){
                        System.out.printf("%-15s",rs.getString(i));
                    } else{
                        System.out.printf("%-15s","NULL");
                    }
                }
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public static void insertRow(Book bookToInsert, JdbcRowSet rowSetAuthors, JdbcRowSet rowSetAuthorisbn, JdbcRowSet rowSetTitles) throws SQLException{
        rowSetAuthors.moveToInsertRow();
        rowSetAuthors.updateString(2, bookToInsert.getFirstName());
        rowSetAuthors.updateString(3, bookToInsert.getLastName());
        rowSetAuthors.insertRow();
        rowSetAuthors.last();
        bookToInsert.setAuthorsID(rowSetAuthors.getInt(1));
        bookToInsert.setAuthorID(rowSetAuthors.getInt(1));
        rowSetAuthorisbn.moveToInsertRow();
        rowSetAuthorisbn.updateInt(1, bookToInsert.getAuthorID());
        rowSetAuthorisbn.updateInt(2, bookToInsert.getISBN());
        rowSetAuthorisbn.insertRow();
        rowSetTitles.moveToInsertRow();
        rowSetTitles.updateInt(1, bookToInsert.getISBN());
        rowSetTitles.updateString(2, bookToInsert.getTitle());
        rowSetTitles.updateInt(3, bookToInsert.getEditionNumber());
        rowSetTitles.updateInt(4, bookToInsert.getCopyRight());
        rowSetTitles.insertRow();
        System.out.println(bookToInsert+" successfully inserted!");
    }
    
    public static void setAndExecuteRowSet(JdbcRowSet rowSet, String DATABASE_URL, String USER, String PASSWORD, String QUERY) throws SQLException{
        rowSet.setUrl(DATABASE_URL);
        rowSet.setUsername(USER);
        rowSet.setPassword(PASSWORD);
        rowSet.setCommand(QUERY);
        rowSet.execute();
    }
    
    //defining the String parameter for the parameterized query
    public static void setAndExecuteRowSet2(JdbcRowSet rowSet, String DATABASE_URL, String USER, String PASSWORD, String QUERY) throws SQLException{
        rowSet.setUrl(DATABASE_URL);
        rowSet.setUsername(USER);
        rowSet.setPassword(PASSWORD);
        rowSet.setCommand(QUERY);
        rowSet.setString(1, "%"+getSearchString(rowSet)+"%");
        rowSet.execute();
    }
    
    public static String getSearchString(JdbcRowSet rowSet) throws SQLException{
        Scanner s = new Scanner(System.in);
        System.out.println("For searching for a specific FirstName, type the name below:");
        String firstNameToSearch = s.nextLine();
        System.out.println("Searching DB for: "+firstNameToSearch+"...");
        return firstNameToSearch;
    }
}

/*
For setting the query and for executing the query, either executeStatement() for Statements or setX() and execute() 
for PreparedStatements, the RowSet object is used directly. In the same way, for getting
the results, or for inserting or updating rows in the DB, the RowSet object is used directly. As RowSet encapsulates
the Connection object, as well as the PreparedStatement and ResultSet, the methods for using these two are accessed
through the same RowSet object, differently from when explicit PreparedStatements and ResultSets are used with
separated functionalities.
*/
