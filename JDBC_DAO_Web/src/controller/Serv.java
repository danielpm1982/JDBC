package controller;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jdbc.*;
public class Serv extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String command = request.getParameter("command");
        if(command.equals("searchAll")){
            request.setAttribute("command", "select * from client");
            DAOAdapterInterface dao = new DAOAdapter(new DAO1("scheme1"));
            List<Client> allClientList = Arrays.asList(dao.searchAllClients());
            request.setAttribute("resultSize", String.format("%1$02d",allClientList.size()));
            request.setAttribute("clientList", allClientList);
            RequestDispatcher rd = request.getRequestDispatcher("responseSearchAllByNameById.jsp");
            rd.forward(request, response);
        }
        if(command.equals("searchAllWithDept")){
            request.setAttribute("command", "select * from client c left outer join department d on c.departmentNo=d.departmentNo union select * from client c right outer join department d on c.departmentNo=d.departmentNo");
            DAOAdapterInterface dao = new DAOAdapter(new DAO1("scheme1"));
            List<String> clientWithDepartmentStringList = Arrays.asList(dao.searchAllClientsWithDepartments());
            request.setAttribute("resultSize", String.format("%1$02d",clientWithDepartmentStringList.size()));
            request.setAttribute("clientList", clientWithDepartmentStringList);
            RequestDispatcher rd = request.getRequestDispatcher("responseSearchAllByNameById.jsp");
            rd.forward(request, response);
        }
        if(command.equals("searchByName")){
            request.setAttribute("command", "select * from client c where c.name like ?");
            DAOAdapterInterface dao = new DAOAdapter(new DAO1("scheme1"));
            List<Client> clientList = Arrays.asList(dao.searchClientByName(request.getParameter("input")));
            request.setAttribute("resultSize", String.format("%1$02d",clientList.size()));
            request.setAttribute("clientList", clientList);
            RequestDispatcher rd = request.getRequestDispatcher("responseSearchAllByNameById.jsp");
            rd.forward(request, response);
        }
        if(command.equals("searchById")){
            request.setAttribute("command", "select * from client c where c.id like ?");
            DAOAdapterInterface dao = new DAOAdapter(new DAO1("scheme1"));
            Client client = dao.searchClientById(Integer.parseInt(request.getParameter("input")));
            List<Client> clientList = new ArrayList<>();
            if(client!=null){
                clientList = Arrays.asList(client);
            }
            request.setAttribute("resultSize", String.format("%1$02d",clientList.size()));
            request.setAttribute("clientList", clientList);
            RequestDispatcher rd = request.getRequestDispatcher("responseSearchAllByNameById.jsp");
            rd.forward(request, response);
        }
        if(command.equals("deleteByName")){
            request.setAttribute("command", "delete from client where name=?");
            DAOAdapterInterface dao = new DAOAdapter(new DAO1("scheme1"));
            Client[] clientList = dao.searchClientByExactName(request.getParameter("input"));
            if(clientList.length>0){
                Stream.of(clientList).forEach(x->dao.deleteClient(x));
                request.setAttribute("resultMessage", "all client entries with name = '"+request.getParameter("input")+"' have been successfully deleted!");
            } else{
                request.setAttribute("resultMessage", "no entries with name = '"+request.getParameter("input")+"' have been found!");
            }
            List<Client> allClientList = Arrays.asList(dao.searchAllClients());
            request.setAttribute("resultSize", String.format("%1$02d",allClientList.size()));
            request.setAttribute("clientList", allClientList);
            RequestDispatcher rd = request.getRequestDispatcher("responseDeleteInsertUpdate.jsp");
            rd.forward(request, response);
        }
        if(command.equals("insert")){
            request.setAttribute("command", "insert into client(name, salary, birthDate, registeredIn, departmentNo) values (?,?,?,?,?)");
            DAOAdapterInterface dao = new DAOAdapter(new DAO1("scheme1"));
            Client clientTemp = new Client(request.getParameter("name"), new BigDecimal(request.getParameter("salary")), LocalDate.parse(request.getParameter("birthDate")), LocalDateTime.parse(request.getParameter("registeredIn"), DateTimeFormatter.ISO_LOCAL_DATE_TIME), Integer.parseInt(request.getParameter("departmentNo")));
            dao.insertClient(clientTemp);
            request.setAttribute("resultMessage", "New Client: "+clientTemp.getClientWithoutId()+" has been successfully inserted!");
            List<Client> allClientList = Arrays.asList(dao.searchAllClients());
            request.setAttribute("resultSize", String.format("%1$02d",allClientList.size()));
            request.setAttribute("clientList", allClientList);
            RequestDispatcher rd = request.getRequestDispatcher("responseDeleteInsertUpdate.jsp");
            rd.forward(request, response);
        }
        if(command.equals("update")){
            request.setAttribute("command", "update client c set c.name=?, c.salary=?, c.birthDate=?, c.registeredIn=?, c.departmentNo=? where c.id=?");
            DAOAdapterInterface dao = new DAOAdapter(new DAO1("scheme1"));
            Client oldClient = dao.searchClientById(Integer.valueOf(request.getParameter("id")));
            if(oldClient!=null){
                Client newClient = new Client(request.getParameter("name"), new BigDecimal(request.getParameter("salary")), LocalDate.parse(request.getParameter("birthDate")), LocalDateTime.parse(request.getParameter("registeredIn")), Integer.parseInt(request.getParameter("departmentNo")));
                newClient.setId(oldClient.getId());
                dao.updateClient(oldClient, newClient);
                request.setAttribute("resultMessage", "Updated Client: "+newClient+" has been successfully updated!");
            } else{
                request.setAttribute("resultMessage", "no entries with id = '"+request.getParameter("id")+"' have been found!");
            }
            List<Client> allClientList = Arrays.asList(dao.searchAllClients());
            request.setAttribute("resultSize", String.format("%1$02d",allClientList.size()));
            request.setAttribute("clientList", allClientList);
            RequestDispatcher rd = request.getRequestDispatcher("responseDeleteInsertUpdate.jsp");
            rd.forward(request, response);
        }
        if(command.equals("executeCustomSqlQuery")){
            request.setAttribute("command", request.getParameter("input"));
            DAOAdapterInterface dao = new DAOAdapter(new DAO1("scheme1"));
            Client[] clientArray = dao.executeLowLevelQuery(request.getParameter("input"));
            List<Client> allClientList = Arrays.asList(clientArray);
            request.setAttribute("resultSize", String.format("%1$02d",allClientList.size()));
            request.setAttribute("clientList", allClientList);
            RequestDispatcher rd = request.getRequestDispatcher("responseSearchAllByNameById.jsp");
            rd.forward(request, response);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
