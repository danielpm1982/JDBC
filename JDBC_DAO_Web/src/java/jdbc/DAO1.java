package jdbc;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DAO1 implements DAOInterface{
    private final String DATABASE_URL;
    private final String USER;
    private final String PASSWORD;
    private Connection conn;
    Statement statement;
    PreparedStatement preparedStatement;
    public DAO1(String schemeNameAtLocalHostMySQLServer) {
        DATABASE_URL = "jdbc:mysql://localhost:3306/"+schemeNameAtLocalHostMySQLServer+"?verifyServerCertificate=false&useSSL=true";
        USER = "root";
        PASSWORD = "root";
        conn = null;
        statement = null;
        preparedStatement = null;
    }
    @Override
    public void openResources(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DATABASE_URL,USER,PASSWORD);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch(SQLException | ClassNotFoundException ex) {
            ex.printStackTrace(System.out);
        }
    }
    @Override
    public void closeResources(){
        try{
            if(conn!=null){
                conn.close();
            }
            if(statement!=null){
                statement.close();
            }
            if(preparedStatement!=null){
                preparedStatement.close();
            }
        } catch(SQLException ex){
            ex.printStackTrace(System.out);
        }
    }
    private PreparedStatement createPreparedStatement(String preparedStatementSQL){
        try{
            preparedStatement = conn.prepareStatement(preparedStatementSQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch(SQLException ex){
            ex.printStackTrace(System.out);
        }
        return preparedStatement;
    }
    @Override
    public ResultSet executeQuery(String query, String message){
        System.out.println(message);
        ResultSet rs = null;
        try{
            rs = statement.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return rs;
    }
    @Override
    public ResultSet executeQuery(String query, String message, String... argsToPreparedStatementSQL){
        System.out.println(message);
        ResultSet rs = null;
        try{
            preparedStatement = createPreparedStatement(query);
            for(int i=0; i<argsToPreparedStatementSQL.length; i++){
                preparedStatement.setString(i+1, argsToPreparedStatementSQL[i]);
            }
            rs = preparedStatement.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return rs;
    }
    @Override
    public void insert(String insertSQL, String message){
        System.out.println(message);
        try{
            statement.executeUpdate(insertSQL);
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
            preparedStatement = createPreparedStatement(insertSQL);
            for(int i=0; i<argsToPreparedStatementSQL.length; i++){
                preparedStatement.setString(i+1, argsToPreparedStatementSQL[i]);
            }
            preparedStatement.executeUpdate();
            System.out.println("Inserted!");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Failed to insert!");
        }
    }
    @Override
    public void delete(String deleteSQL, String message){
        System.out.println(message);
        try{
            statement.executeUpdate(deleteSQL);
            System.out.println("Deleted!");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Failed to delete!");
        }
    }
    @Override
    public void delete(String deleteSQL, String message, String... argsToPreparedStatementSQL){
        System.out.println(message);
        try{
            preparedStatement = createPreparedStatement(deleteSQL);
            for(int i=0; i<argsToPreparedStatementSQL.length; i++){
                preparedStatement.setString(i+1, argsToPreparedStatementSQL[i]);
            }
            preparedStatement.executeUpdate();
            System.out.println("Deleted!");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Failed to delete!");
        }
    }
    @Override
    public void update(String updateSQL, String message){
        System.out.println(message);
        try{
            statement.executeUpdate(updateSQL);
            System.out.println("Updated!");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Failed to update!");
        }
    }
    @Override
    public void update(String updateSQL, String message, String... argsToPreparedStatementSQL){
        System.out.println(message);
        try{
            preparedStatement = createPreparedStatement(updateSQL);
            for(int i=0; i<argsToPreparedStatementSQL.length; i++){
                preparedStatement.setString(i+1, argsToPreparedStatementSQL[i]);
            }
            preparedStatement.executeUpdate();
            System.out.println("Updated!");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Failed to update!");
        }
    }
    @Override
    public ResultSet updateViaResultSet(ResultSet rs, String columnName, int rowNumber, Class<?> type, String newValue){
        if(rs!=null&&getResultSetRowCount(rs)>0){
            try {
                rs.absolute(rowNumber);
                switch (type.getSimpleName()) {
                    case "Integer":
                        System.out.println("updating "+columnName+" column value at row "+rowNumber+" from "+rs.getInt(columnName)+" to "+newValue+"...");
                        rs.updateInt(columnName, Integer.parseInt(newValue));
                        rs.updateRow();
                        System.out.println("Updated!");
                        break;
                    case "String":
                        System.out.println("updating "+columnName+" column value at row "+rowNumber+" from "+rs.getString(columnName)+" to "+newValue+"...");
                        rs.updateString(columnName, newValue);
                        rs.updateRow();
                        System.out.println("Updated!");
                        break;
                    case "BigDecimal":
                        System.out.println("updating "+columnName+" column value at row "+rowNumber+" from "+rs.getBigDecimal(columnName)+" to "+newValue+"...");
                        rs.updateBigDecimal(columnName, new BigDecimal(newValue));
                        rs.updateRow();
                        System.out.println("Updated!");
                        break;
                    case "LocalDate":
                        System.out.println("updating "+columnName+" column value at row "+rowNumber+" from "+rs.getDate(columnName)+" to "+newValue+"...");
                        rs.updateDate(columnName, Date.valueOf(LocalDate.parse(newValue)));
                        rs.updateRow();
                        System.out.println("Updated!");
                        break;
                    case "Timestamp":
                        System.out.println("updating "+columnName+" column value at row "+rowNumber+" from "+rs.getTimestamp(columnName)+" to "+newValue+"...");
                        rs.updateTimestamp(columnName, Timestamp.valueOf(LocalDateTime.parse(newValue)));
                        rs.updateRow();
                        System.out.println("Updated!");
                        break;
                    default:
                        System.out.println("updating "+columnName+" column value at row "+rowNumber+" from "+rs.getObject(columnName)+" to "+newValue+"...");
                        rs.updateObject(columnName, newValue);
                        rs.updateRow();
                        System.out.println("Updated!");
                }
                rs.beforeFirst();
            } catch (NumberFormatException | SQLException ex) {
                ex.printStackTrace(System.out);
            }
        } else{
            System.out.println("Null or Empty ResultSet!");
        }
        return rs;
    }
    private int getResultSetRowCount(ResultSet rs){
        try {
            if(!isResultSetEmpty(rs)){
                rs.last();
                int rowCount = rs.getRow();
                rs.beforeFirst();
                return rowCount;
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        return 0;
    }
    private boolean isResultSetEmpty(ResultSet rs) throws SQLException{
        return !rs.isBeforeFirst();
    }
}

/*
DAO class that deals with:
- opening and closing resources (Connection, Statement, PreparedStatement, Resultset objects); 
- query through simple statement and through preparedStatement;
- insert through simple statement and through preparedStatement;
- delete through simple statement and through preparedStatement;
- update through simple statement and through preparedStatement;
- update through resultSet (delete and insert through resultSet could also be implemented);
*/
