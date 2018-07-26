//FOR USING THIS CLASS, FIRST ADD THE DRIVER "mysql-connector-java-5.1.39-bin.jar" TO THE LIBRARIES OF THE PROJECT.
//AND CHECK IF A COMPATIBLE DB IS ALREADY CREATED (AND IF THE RDBMS IS RUNNING!), OTHERWISE CREATE A COMPATIBLE DB WITH 
//THE PROPER SQL SCHEMA.

package javaapplication67;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JavaApplication67 {
    //this boolean is only for printing a "\n" before the second table display and on, but not before the first.
    static boolean firstTableAlreadyDisplayed=false;
    public static void main(String[] args) {
        final String DATABASE_URL = "jdbc:mysql://localhost:3306/books?autoReconnect=true&useSSL=false";
        final String USER="root";
        final String PASSWORD="root";
        final String QUERY1="select * from authors";
        final String QUERY2="select * from authorisbn";
        final String QUERY3="select * from titles";
        final String QUERY4="select * from (select * from authors left join authorisbn on authors.AuthorsID=authorisbn.AuthorID) x union (select * from (select * from authors right join authorisbn on authors.AuthorsID=authorisbn.AuthorID) y);";
        final String QUERY5="select * from (select * from authors a left outer join authorisbn i on a.AuthorsID=i.AuthorID) x left outer join titles t on x.ISBN=t.ISBN union select * from (select * from authors a right outer join authorisbn i on a.AuthorsID=i.AuthorID) x right outer join titles t on x.ISBN=t.ISBN order by Title asc;";
        //pstmt3 and pstmt4 are views previously defined and run at the RDBMS (left and right join union = full outer join).
        final String QUERY6="select * from (select * from pstmt3 union select * from pstmt4) z where z.FirstName like ? order by Title asc;";
        final Book bookToInsert1 = new Book("Apollo","Pinheiro",32165,"livro7",1,2007);
        final Book bookToInsert2 = new Book("Viludo","Pinheiro",79456,"livro8",5,2003);
        //resultSet had to be declared outside the try with resources area to make it reassignable to the same query again (updated).
        //auto-closeble resources cannot be reassigned (only can be assigned once at the try with resources area).
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
            PreparedStatement pstmt = conn.prepareStatement(QUERY6);
            ){
            //rs = stmt.executeQuery(QUERY5);
            rs = setAndExecutePreparedStatement(pstmt, rs);
            printColumnNames(rs);
            printRowValues(rs);
            firstTableAlreadyDisplayed=true;
            /*insert two rows using different statements and resultSets. If only one statement is used, it will close the
            resultSets each time it deals with the other, and it won't work at the insert method - which will say some rs are closed.
            If a resultSet receives a join result from multiple tables at the DB, it also won't work, because only resultSets from
            single tables can be updatable, regardless of whether the statement is set as ResultSet.CONCUR_UPDATABLE or not.
            */
//            Statement stmtAuthors = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT); 
//            ResultSet rsAuthors = stmtAuthors.executeQuery(QUERY1);
//            Statement stmtAuthorisbn = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT); 
//            ResultSet rsAuthorisbn = stmtAuthorisbn.executeQuery(QUERY2);
//            Statement stmtTitles = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT); 
//            ResultSet rsTitles = stmtTitles.executeQuery(QUERY3);
//            insertRow(bookToInsert1, rsAuthors, rsAuthorisbn, rsTitles);
//            insertRow(bookToInsert2, rsAuthors, rsAuthorisbn, rsTitles);
            
            //search DB again and print the updated results after insertions above.
//            rs = stmt.executeQuery(QUERY5);
//            printColumnNames(rs);
//            printRowValues(rs);
        } catch(SQLException e){
            e.printStackTrace();
        } finally{
            if(rs!=null){
                try {
                    if(!rs.isClosed()){
                        rs.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
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
    //moves each rs to the insert row, updates the row with the book field values to be inserted and inserts the row at the DB.
    //authorsID is autoincremented by the RDBMS, so the value must be got after insertion at authors table... and the value obtained is
    //then assigned, dinamically, to authorsID and authorID at the book respective fields, and then got from the object
    //to the authorisbn table as its foreign key. ISBN at authorisbn and titles tables are also the same to make the 
    //relation valid (foreign key at the titles table). The books inserted, with all its fields completed, are then shown as 
    //successfully inserted.
    public static void insertRow(Book bookToInsert, ResultSet rsAuthors, ResultSet rsAuthorisbn, ResultSet rsTitles) throws SQLException{
        rsAuthors.moveToInsertRow();
        rsAuthors.updateString(2, bookToInsert.getFirstName());
        rsAuthors.updateString(3, bookToInsert.getLastName());
        rsAuthors.insertRow();
        rsAuthors.last();
        bookToInsert.setAuthorsID(rsAuthors.getInt(1));
        bookToInsert.setAuthorID(rsAuthors.getInt(1));
        rsAuthorisbn.moveToInsertRow();
        rsAuthorisbn.updateInt(1, bookToInsert.getAuthorID());
        rsAuthorisbn.updateInt(2, bookToInsert.getISBN());
        rsAuthorisbn.insertRow();
        rsTitles.moveToInsertRow();
        rsTitles.updateInt(1, bookToInsert.getISBN());
        rsTitles.updateString(2, bookToInsert.getTitle());
        rsTitles.updateInt(3, bookToInsert.getEditionNumber());
        rsTitles.updateInt(4, bookToInsert.getCopyRight());
        rsTitles.insertRow();
        System.out.println(bookToInsert+" successfully inserted!");
    }
    public static ResultSet setAndExecutePreparedStatement(PreparedStatement pstmt, ResultSet rs) throws SQLException{
        Scanner s = new Scanner(System.in);
        System.out.println("For searching for a specific FirstName, type the name below:");
        String firstNameToSearch = s.nextLine();
        System.out.println("Searching DB for: "+firstNameToSearch+"...");
        pstmt.setString(1, "%"+firstNameToSearch+"%");
        pstmt.execute();
        rs = pstmt.getResultSet();
        return rs;
    }
}


/*
For setting the query and for executing the query, either executeStatement() for Statements or setX() and execute() 
for PreparedStatements, the Statement (or PreparedStatement) object is used directly. On the other hand, for getting
the results, or for inserting or updating rows in the DB, the ResultSet object is used directly. In the case of simple
Statements execution, the executeQuery returns a ResultSet with the results of the statement, and in the case of
PreparedStatements the execute() doens't return the ResultSet but this can be got through the method getResultSet() of
the same PreparedStatemet object.
*/
