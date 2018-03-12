package jdbc;
public interface DAOAdapterInterface {
    public Client[] searchAllClients();
    public String[] searchAllClientsWithDepartments();
    public Client[] searchClientByName(String name);
    public Client[] searchClientByExactName(String name);
    public Client searchClientById(int id);
    public void insertClient(Client client);
    public void deleteClient(Client client);
    public void updateClient(Client oldClient, Client newClient);
    public Client[] executeLowLevelQuery(String sql);
}
