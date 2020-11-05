package chatserver;


import Database.ClientDao;
import chatprotocol.IService;
import chatprotocol.Client;
import java.util.HashMap;
import java.util.Map;

public class Service implements IService{
    private ClientDao clientDao;
    private static IService theInstance;
    
    
    public static IService instance(){
        if (theInstance==null){ 
            theInstance=new Service();
        }
        return theInstance;
    }
    
    Server srv;
    Map<String,Client> users;

    public Service() {        
        users =  new HashMap();
        clientDao = new ClientDao();               
    }
    
    public void setSever(Server srv){
        this.srv=srv;
    }
    
    public void post(String m){
        //srv.deliver(m);
        // TODO if the receiver is not active, store it temporarily
    }

    public void logout(Client p) throws Exception{
        srv.remove(p);
    }    

    @Override
    public Client login(Client u) throws Exception {
        Client result = null;
        System.out.println("Service server: Metodo login");
        
        result=clientDao.read(u.getId());
        System.out.println("Resultado de la busqueda en la DB");
        
        if(result==null)  
            throw new Exception("User does not exist");
        
        System.out.print("encontro usuario no null");
        
        if(!result.getPassword().equals(u.getPassword()))
            throw new Exception("User does not exist");
        return result;
    }

    @Override
    public void signin(Client client) throws Exception {
        
    }

    @Override
    public void post_msg(String m, Client client) {
        srv.deliver(m,client);
    }

    @Override
    public void giveClients(Client c) throws Exception {
        System.out.println("Service: inside give Clients");
        srv.giveFriends(c);
    }

    @Override
    public Client addFriend(Client client) throws Exception {
        Client result = null;
        
        result=clientDao.read(client.getId());
          
        if(result==null)  
            throw new Exception("User does not exist");
        
        
        if(!result.getPassword().equals(client.getPassword()))
            throw new Exception("User does not exist");
        
        return result;
    }
    
    


}
