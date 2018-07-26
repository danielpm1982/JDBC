package jdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOAdapter implements DAOAdapterInterface{
    private final DAOInterface dao;
    public DAOAdapter(DAOInterface dao) {
        this.dao = dao;
    }
    @Override
    public Client[] searchAllClients() {
        List<Client> clientList = new ArrayList<>();
        dao.openResources();
        try{
            String sql = "select * from client";
            ResultSet rs = dao.executeQuery(sql, "searching for all existing clients...");
            System.out.println("Found "+getResultSetRowCount(rs)+" results!");
            while(rs.next()){
                Client clientTemp = new Client();
                clientTemp.setId(rs.getInt("id"));
                clientTemp.setName(rs.getString("name"));
                clientTemp.setSalary(rs.getBigDecimal("salary"));
                clientTemp.setBirthDate(rs.getDate("birthDate").toLocalDate());
                clientTemp.setRegisteredIn(rs.getTimestamp("registeredIn").toLocalDateTime());
                clientTemp.setDepartmentNo(rs.getInt("departmentNo"));
                clientList.add(clientTemp);
            }
        } catch(SQLException ex){
            ex.printStackTrace(System.out);
        } finally{
            dao.closeResources();
        }
        return clientList.toArray(new Client[0]);
    }
    @Override
    public Client[] searchClientByName(String name) {
        List<Client> clientList = new ArrayList<>();
        dao.openResources();
        try{
            String sql = "select * from client c where c.name like ?";
            ResultSet rs = dao.executeQuery(sql, "searching client(s) with name = "+name+"...", name);
            while(rs.next()){
                Client clientTemp = new Client();
                clientTemp.setId(rs.getInt("id"));
                clientTemp.setName(rs.getString("name"));
                clientTemp.setSalary(rs.getBigDecimal("salary"));
                clientTemp.setBirthDate(rs.getDate("birthDate").toLocalDate());
                clientTemp.setRegisteredIn(rs.getTimestamp("registeredIn").toLocalDateTime());
                clientTemp.setDepartmentNo(rs.getInt("departmentNo"));
                clientList.add(clientTemp);
            }
        } catch(SQLException ex){
            ex.printStackTrace(System.out);
        } finally{
            dao.closeResources();
        }
        return clientList.toArray(new Client[0]);
    }
    @Override
    public Client searchClientById(int id) {
        Client clientTemp = null;
        dao.openResources();
        try{
            String sql = "select * from client c where c.id = ?";
            ResultSet rs = dao.executeQuery(sql, "searching client(s) with id = "+id+"...", String.valueOf(id));
            rs.first();
            clientTemp = new Client();
            clientTemp.setId(rs.getInt("id"));
            clientTemp.setName(rs.getString("name"));
            clientTemp.setSalary(rs.getBigDecimal("salary"));
            clientTemp.setBirthDate(rs.getDate("birthDate").toLocalDate());
            clientTemp.setRegisteredIn(rs.getTimestamp("registeredIn").toLocalDateTime());
            clientTemp.setDepartmentNo(rs.getInt("departmentNo"));
        } catch(SQLException ex){
            ex.printStackTrace(System.out);
        } finally{
            dao.closeResources();
        }
        return clientTemp;
    }
    
    @Override
    public void insertClient(Client client) {
        dao.openResources();
        String sql = "insert into client(name, salary, birthDate, registeredIn, departmentNo) values (?,?,?,?,?)";
        dao.insert(sql, "inserting client -> "+client.getClientWithoutId(), client.getName(), client.getSalary().toString(), client.getBirthDate().toString(), client.getRegisteredIn().toString(), String.valueOf(client.getDepartmentNo()));
        dao.closeResources();
    }
    @Override
    public void deleteClient(Client client) {
        dao.openResources();
        String sql = "delete from client where name=?";
        dao.delete(sql, "deleting client(s) by name -> "+client.getClientWithoutId(), client.getName());
        dao.closeResources();
    }
    @Override
    public void updateClient(Client oldClient, Client newClient) { //oldClient must have a valid Id, not the -1 default one. Must search the DB for the real id value before passing the argument.
        dao.openResources();
        String sql = "update client c set c.name=?, c.salary=?, c.birthDate=?, c.registeredIn=?, c.departmentNo=? where c.id=?";
        dao.update(sql, "updating client(s) by id ->\nfrom: "+oldClient+"\nto: "+newClient.getClientWithoutId(), newClient.getName(), newClient.getSalary().toString(), newClient.getBirthDate().toString(), newClient.getRegisteredIn().toString(), String.valueOf(newClient.getDepartmentNo()), String.valueOf(oldClient.getId()));
        dao.closeResources();
    }
    private int getResultSetRowCount(ResultSet rs){
        int rowCount=-1;
        try{
            rs.last();
            rowCount = rs.getRow();
            rs.beforeFirst();
        } catch (SQLException ex){
            ex.printStackTrace(System.out);
        }
        return rowCount;
    }
}
