package jdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
    public String[] searchAllClientsWithDepartments() {
        List<String> clientWithDepartmentList = new ArrayList<>();
        dao.openResources();
        try{
            String sql = "select * from client c left join department d on c.departmentNo=d.departmentNo union select * from client c right join department d on c.departmentNo=d.departmentNo order by id asc";
            ResultSet rs = dao.executeQuery(sql, "searching for all existing clients joined with their respective departments...");
            System.out.println("Found "+getResultSetRowCount(rs)+" results!");
            while(rs.next()){ //in case of join does not eliminate results where only the right join exists, but client values would be null and lead to errors. Another check must be done below:
                if(rs.getInt("id")!=0){ //if sql NULL is found, 0 is returned from the rs -> for checking if the value returned for the id column is not null... (for avoiding errors in case of only right join returns from the search, with null client values associated, in the join, with not null department values)
                    clientWithDepartmentList.add("id: "+rs.getInt("id")+" name: "+rs.getString("name")+" salary: "+rs.getBigDecimal("salary").toString()+" birthdate: "+rs.getDate("birthDate").toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))+" registeredIn: "+rs.getTimestamp("registeredIn").toLocalDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))+" departmentNo: "+rs.getInt("departmentNo")+" departmentName: "+rs.getString("departmentName"));
                }
            }
            System.out.println(clientWithDepartmentList);
        } catch(SQLException ex){
            ex.printStackTrace(System.out);
        } finally{
            dao.closeResources();
        }
        return clientWithDepartmentList.toArray(new String[0]);
    }
    @Override
    public Client[] searchClientByName(String name) {
        List<Client> clientList = new ArrayList<>();
        dao.openResources();
        try{
            String sql = "select * from client c where c.name like ?";
            ResultSet rs = dao.executeQuery(sql, "searching client(s) with name = "+name+"...", "%"+name+"%");
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
    public Client[] searchClientByExactName(String name) {
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
            if(getResultSetRowCount(rs)>0){ //check if rs is not empty, as there is no "while(rs.next())" here for that.
                rs.first(); //gets the first client of the result, which in most cases will be the only row if no duplicity exists for the id (PK).
                clientTemp = new Client();
                clientTemp.setId(rs.getInt("id"));
                clientTemp.setName(rs.getString("name"));
                clientTemp.setSalary(rs.getBigDecimal("salary"));
                clientTemp.setBirthDate(rs.getDate("birthDate").toLocalDate());
                clientTemp.setRegisteredIn(rs.getTimestamp("registeredIn").toLocalDateTime());
                clientTemp.setDepartmentNo(rs.getInt("departmentNo"));
            }
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
    public void deleteClient(Client client) { //must receive a valid Client, with not null valued attributes, not a mock object with only the name defined... otherwise the toString will lead to an error over the null values.
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
    @Override
    public Client[] executeLowLevelQuery(String sql){
        List<Client> clientList = new ArrayList<>();
        dao.openResources();
        try{
            ResultSet rs = dao.executeQuery(sql, "searching custom sql "+sql+"...");
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
