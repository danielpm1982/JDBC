package jdbc;
import javax.sql.RowSet;

public class Main2 {
    public static void main(String[] args) {
        DAOInterface dao2 = new DAO2("scheme1");
        dao2.openResources();
        
        String sql1 = "select * from client c left outer join department d on c.departmentNo=d.departmentNo union select * from client c right outer join department d on c.departmentNo=d.departmentNo";
        String message1 = "Simulated full outer join with MySQL:";
        RowSet rowSet = (RowSet)dao2.executeQuery(sql1, message1);
        ResultSetPrinter.printResultSet(rowSet);
        
        String sql2 = "select * from client c";
        String message2 = "Clients * selection:";
        rowSet = (RowSet)dao2.executeQuery(sql2, message2);
        ResultSetPrinter.printResultSet(rowSet);
        
        String sql3 = "insert into client(name, salary, birthDate, registeredIn, departmentNo) values ('client7','900','1985-4-10','2000-5-01 12:00:00','3')";
        String message3 = "inserting into client values 'client7','900','1985-04-10','2000-05-01 12:00:00.0','3'";
        dao2.insert(sql3, message3);
        String sql4 = "insert into client(name, salary, birthDate, registeredIn, departmentNo) values (?,'900','1985-4-10','2000-5-01 12:00:00','3')";
        String message4 = "inserting into client values 'client7','900','1985-04-10','2000-05-01 12:00:00.0','3'";
        dao2.insert(sql4, message4, "client7");
        rowSet = (RowSet)dao2.executeQuery(sql1, message1);
        ResultSetPrinter.printResultSet(rowSet);
        
        String sql5 = "delete from client where name='client7'";
        String message5 = "deleting from client where name=client7";
        dao2.delete(sql5, message5);
        String sql6 = "delete from client where name=?";
        String message6 = "deleting from client where name=client7";
        dao2.delete(sql6, message6, "client7");
        rowSet = (RowSet)dao2.executeQuery(sql1, message1);
        ResultSetPrinter.printResultSet(rowSet);
        
        String sql7 = "update client set departmentNo=1 where id=1";
        String message7 = "updating client set departmentNo=1 where id=1";
        dao2.update(sql7, message7);
        rowSet = (RowSet)dao2.executeQuery(sql1, message1);
        ResultSetPrinter.printResultSet(rowSet);
        String sql8 = "update client set departmentNo=? where id=?";
        String message8 = "update client set departmentNo=3 where id=1";
        dao2.update(sql8, message8, "3", "1");
        rowSet = (RowSet)dao2.executeQuery(sql1, message1);
        ResultSetPrinter.printResultSet(rowSet);
        
        dao2.closeResources();
    }
}

/*
RowSet interface wrapping (encapsulating) Connection, Statement and ResultSet objects in one same object (RowSet).
As RowSet objects ARE ResultSet objects, they can be passed as arguments for a parameter where a ResultSet should be passed,
as in DAOPrinter method printResultSet(ResultSet rs, String message).
For each operation, RowSet has been used as a simple Statement and as a PreparedStatement, in this last case by passing the values that
should be set at the DAO2 after setting the command (with the SQL with ?s) on the rowSet and before calling the execute() method on it.
*/
