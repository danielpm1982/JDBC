package jdbc;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

public class Main1 {
    public static void main(String[] args) throws ClassNotFoundException{
        DAOInterface dao = new DAO1("scheme1");
        dao.openResources();
        
        String sql1 = "select * from client c left outer join department d on c.departmentNo=d.departmentNo union select * from client c right outer join department d on c.departmentNo=d.departmentNo";
        String message1 = "Simulated full outer join with MySQL:";
        ResultSet resultSet1 = dao.executeQuery(sql1, message1);
        ResultSetPrinter.printResultSet(resultSet1);
        
        String sql2 = "select * from client c";
        String message2 = "Clients * selection:";
        ResultSet resultSet2 = dao.executeQuery(sql2, message2);
        ResultSetPrinter.printResultSet(resultSet2);
        
        String message3 = "Updated Resultset and Database:";
        dao.updateViaResultSet(resultSet2, "name", 1, Class.forName("java.lang.String"), "client1");
        dao.updateViaResultSet(resultSet2, "salary", 1, Class.forName("java.math.BigDecimal"), "50000");
        dao.updateViaResultSet(resultSet2, "birthDate", 1, Class.forName("java.time.LocalDate"), LocalDate.of(1980, Month.MARCH, 22).toString());
        dao.updateViaResultSet(resultSet2, "registeredIn", 1, Class.forName("java.time.LocalDateTime"), LocalDateTime.of(1990, Month.JANUARY, 1, 12, 30).toString());
        dao.updateViaResultSet(resultSet2, "departmentNo", 1, Class.forName("java.lang.Integer"), "3");
        System.out.println(message3);
        ResultSetPrinter.printResultSet(resultSet2);
        
        String sql4 = "select * from client c where c.name like ?";
        String message4 = "Clients ? selection"+" (? = c%1):";
        ResultSet resultSet4 = dao.executeQuery(sql4, message4, "c%1");
        ResultSetPrinter.printResultSet(resultSet4);
        
        String sql5 = "select * from client c where c.name like ? or c.name like ?";
        String message5 = "Clients ? selection"+" (? = c%1 or ? = c%2):";
        ResultSet resultSet5 = dao.executeQuery(sql5, message5, "c%1", "%2");
        ResultSetPrinter.printResultSet(resultSet5);
        
        String sql6 = "select * from client c where c.departmentNo = ? and c.salary >= ?";
        String message6 = "Clients * from client c where c.departmentNo = ? and c.salary >= ?"+" (? = 3 and ? >= 50000):";
        ResultSet resultSet6 = dao.executeQuery(sql6, message6, "3", "50000");
        ResultSetPrinter.printResultSet(resultSet6);
        
        String sql7 = "insert into client(name, salary, birthDate, registeredIn, departmentNo) values ('client7','900','1985-4-10','2000-5-01 12:00:00','3')";
        String message7 = "inserting into client values 'client7','900','1985-04-10','2000-05-01 12:00:00.0','3'";
        dao.insert(sql7, message7);
        resultSet1 = dao.executeQuery(sql1, message1);
        ResultSetPrinter.printResultSet(resultSet1);
        
        String sql8 = "delete from client where name='client7'";
        String message8 = "deleting from client where name=client7";
        dao.delete(sql8, message8);
        resultSet1 = dao.executeQuery(sql1, message1);
        ResultSetPrinter.printResultSet(resultSet1);
        
        String sql9 = "update client set departmentNo=1 where id=1";
        String message9 = "updating client departmentNo=1 where id=1";
        dao.update(sql9, message9);
        resultSet1 = dao.executeQuery(sql1, message1);
        ResultSetPrinter.printResultSet(resultSet1);
        
        String sql10 = "insert into client(name, salary, birthDate, registeredIn, departmentNo) values (?,'900','1985-4-10','2000-5-01 12:00:00','3')";
        String message10 = "inserting into client values 'client7','900','1985-04-10','2000-05-01 12:00:00.0','3' via preparedStatement";
        dao.insert(sql10, message10, "client7");
        resultSet1 = dao.executeQuery(sql1, message1);
        ResultSetPrinter.printResultSet(resultSet1);
        
        String sql11 = "delete from client where name=?";
        String message11 = "deleting from client where name=client7 via preparedStatement";
        dao.delete(sql11, message11, "client7");
        resultSet1 = dao.executeQuery(sql1, message1);
        ResultSetPrinter.printResultSet(resultSet1);
        
        String sql12 = "update client set departmentNo=? where id=?";
        String message12 = "updating client departmentNo=3 where id=1 via preparedStatement";
        dao.update(sql12, message12, "3", "1");
        resultSet1 = dao.executeQuery(sql1, message1);
        ResultSetPrinter.printResultSet(resultSet1);
        
        dao.closeResources();
    }
}

/*
Client test class that uses DAO to make a lot of query and update operations using Statement and PreparedStatement, as well as ResultSet, passing
sql statements and messages for the DAO class to manage the objects and proceed the queries and updates with the DB.
*/
