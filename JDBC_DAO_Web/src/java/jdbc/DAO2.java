package jdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.RowSet;
import javax.sql.rowset.RowSetProvider;

public class DAO2 implements DAOInterface{
    private final String DATABASE_URL;
    private final String USER;
    private final String PASSWORD;
    private static RowSet rowSet;
    public DAO2(String schemeNameAtLocalHostMySQLServer) {
        DATABASE_URL = "jdbc:mysql://localhost:3306/"+schemeNameAtLocalHostMySQLServer+"?verifyServerCertificate=false&useSSL=true";
        USER = "root";
        PASSWORD = "root";
        rowSet = null;
    }
    @Override
    public void openResources(){
        try{
//            rowSet = RowSetProvider.newFactory().createJdbcRowSet();
            rowSet = RowSetProvider.newFactory().createCachedRowSet();
            Class.forName("com.mysql.jdbc.Driver");
            rowSet.setUrl(DATABASE_URL);
            rowSet.setUsername(USER);
            rowSet.setPassword(PASSWORD);
        } catch(SQLException | ClassNotFoundException e){
            e.printStackTrace(System.out);
        }
    }
    @Override
    public void closeResources(){
        try{
            if(rowSet!=null){
                rowSet.close();
            }
        } catch(SQLException e){
            e.printStackTrace(System.out);
        }
    }
    @Override
    public ResultSet executeQuery(String query, String message){
        System.out.println(message);
        try{    
            rowSet.setCommand(query);
            rowSet.execute();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return rowSet;
    }
    @Override
    public ResultSet executeQuery(String query, String message, String... argsToPreparedStatementSQL) {
        System.out.println(message);
        try{    
            rowSet.setCommand(query);
            for(int i=0; i<argsToPreparedStatementSQL.length; i++){
                rowSet.setString(i+1, argsToPreparedStatementSQL[i]);
            }
            rowSet.execute();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return rowSet;
    }
    @Override
    public void insert(String insertSQL, String message){
        System.out.println(message);
        try{
            rowSet.setCommand(insertSQL);
            rowSet.execute();
            System.out.println("Inserted!\n");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Failed to insert!\n");
        }
    }
    @Override
    public void insert(String insertSQL, String message, String... argsToPreparedStatementSQL){
        System.out.println(message);
        try{
            rowSet.setCommand(insertSQL);
            for(int i=0; i<argsToPreparedStatementSQL.length; i++){
                rowSet.setString(i+1, argsToPreparedStatementSQL[i]);
            }
            rowSet.execute();
            System.out.println("Inserted!\n");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Failed to insert!\n");
        }
    }
    @Override
    public void delete(String deleteSQL, String message){
        System.out.println(message);
        try{
            rowSet.setCommand(deleteSQL);
            rowSet.execute();
            System.out.println("Deleted!\n");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Failed to delete!\n");
        }
    }
    @Override
    public void delete(String deleteSQL, String message, String... argsToPreparedStatementSQL){
        System.out.println(message);
        try{
            rowSet.setCommand(deleteSQL);
            for(int i=0; i<argsToPreparedStatementSQL.length; i++){
                rowSet.setString(i+1, argsToPreparedStatementSQL[i]);
            }
            rowSet.execute();
            System.out.println("Deleted!\n");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Failed to delete!\n");
        }
    }
    @Override
    public void update(String updateSQL, String message){
        System.out.println(message);
        try{
            rowSet.setCommand(updateSQL);
            rowSet.execute();
            System.out.println("Updated!\n");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Failed to update!\n");
        }
    }
    @Override
    public void update(String updateSQL, String message, String... argsToPreparedStatementSQL){
        System.out.println(message);
        try{
            rowSet.setCommand(updateSQL);
            for(int i=0; i<argsToPreparedStatementSQL.length; i++){
                rowSet.setString(i+1, argsToPreparedStatementSQL[i]);
            }
            rowSet.execute();
            System.out.println("Updated!\n");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Failed to update!\n");
        }
    }
    @Override
    public ResultSet updateViaResultSet(ResultSet rs, String columnName, int rowNumber, Class<?> type, String newValue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
