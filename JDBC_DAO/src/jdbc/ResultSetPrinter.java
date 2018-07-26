package jdbc;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetPrinter {
    private static Class<?> getColumnClassType(ResultSet rs, int columnNumber) throws SQLException{
        if(rs!=null){
            ResultSetMetaData metadata = rs.getMetaData();
            String columnClassName = metadata.getColumnClassName(columnNumber);
            Class<?> t = null;
            try {
                t = Class.forName(columnClassName);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace(System.out);
            }
            return t;
        }
        return null;
    }
    private static <T extends Object> T getColumnValue(ResultSet rs, int columnNumber, Class<T> type) throws SQLException{ //get specific-casted values instead of generic ones
        if(rs!=null){
            switch(type.getSimpleName()){
                case "Integer":
                    return type.cast(rs.getInt(columnNumber));
                case "String":
                    return type.cast(rs.getString(columnNumber));
                case "BigDecimal":
                    return type.cast(rs.getBigDecimal(columnNumber));
                case "Date":
                    return type.cast(rs.getDate(columnNumber));
                case "Timestamp":
                    return type.cast(rs.getTimestamp(columnNumber));
                default:
                    return type.cast(rs.getObject(columnNumber));
            }
        }
        return null;
    }
    public static void printResultSet(ResultSet rs){
        try {
            rs.beforeFirst();
            int numberOfColumns = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= numberOfColumns; i++) {
                System.out.printf("%-32s\t", rs.getMetaData().getColumnName(i));
            }
            System.out.println("");
            while (rs.next()) {
                for (int i = 1; i <= numberOfColumns; i++) {
                    Class<?> columnClassType = getColumnClassType(rs, i);
                    System.out.printf("%-32s\t", columnClassType.getSimpleName() + ":" + getColumnValue(rs, i, columnClassType));
                }
                System.out.println("");
            }
            System.out.println("");
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
}

/*
Class that receives a ResultSet object and prints its contents in a tabular format.
ps.: It's not necessary the two private methods above for printing only - as Object-type generic values would do, 
but these two specific private methods can be reused for other purposes, regarding the manipulation of the 
casted values for other types of operations, before printing.
*/
