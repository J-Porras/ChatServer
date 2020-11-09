package chatserver;


import Database.ClientDao;
import chatprotocol.IService;
import chatprotocol.Client;
import chatprotocol.Mensaje;
import java.util.HashMap;
import java.util.Map;

public class Service implements IService{
    private ClientDao clientDao;
    private static IService theInstance;
    

        
    Server srv;
    Map<String,Client> users;

    
    public static IService instance(){
        if (theInstance==null){ 
            theInstance=new Service();
        }
        return theInstance;
    }

    public Service() {        
        users =  new HashMap();
        clientDao = new ClientDao();               
    }
    
    public void setSever(Server srv){
        this.srv=srv;
    }
    
    public void logout(Client p) throws Exception{
        srv.remove(p);
    }    

    @Override
    public Client login(Client u) throws Exception {
        Client result = null;
        
        
        result=clientDao.read(u.getId());
        
        
        if(result==null)  
            throw new Exception("User does not exist");
        
       
        
        if(!result.getPassword().equals(u.getPassword()))
            throw new Exception("User does not exist");
        return result;
    }


    @Override
    public void post(Mensaje mensaje) {
        System.out.println("\nEnviando al server...\n");
        srv.deliver(mensaje);
    }

    @Override
    public void giveClients(Client c) throws Exception {
        
       // srv.giveFriends(c);
    }

    @Override
    public Client addFriend(Client client) throws Exception {
        Client result = null;
        
        result=clientDao.read(client.getId());
          
        if(result==null)  
            throw new Exception("User does not exist");
        
      
 
        return result;
    }
    
    


}
