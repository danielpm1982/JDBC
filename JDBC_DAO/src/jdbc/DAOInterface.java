package jdbc;
import java.sql.ResultSet;

public interface DAOInterface {
    public void openResources();
    public void closeResources();
    public ResultSet executeQuery(String query, String message);
    public ResultSet executeQuery(String query, String message, String... argsToPreparedStatementSQL);
    public void insert(String insertSQL, String message);
    public void insert(String insertSQL, String message, String... argsToPreparedStatementSQL);
    public void delete(String deleteSQL, String message);
    public void delete(String deleteSQL, String message, String... argsToPreparedStatementSQL);
    public void update(String updateSQL, String message);
    public void update(String updateSQL, String message, String... argsToPreparedStatementSQL);
    public ResultSet updateViaResultSet(ResultSet rs, String columnName, int rowNumber, Class<?> type, String newValue);
}
