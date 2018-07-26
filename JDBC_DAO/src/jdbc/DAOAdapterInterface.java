package jdbc;
public interface DAOAdapterInterface {
    public Client[] searchAllClients();
    public Client[] searchClientByName(String name);
    public Client searchClientById(int id);
    public void insertClient(Client client);
    public void deleteClient(Client client);
    public void updateClient(Client oldClient, Client newClient);
}
