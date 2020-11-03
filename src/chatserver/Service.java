package chatserver;


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
        //users.put("jperez", new User("jperez","111","Juan"));
        //users.put("mreyes", new User("mreyes","222","Maria"));
        //users.put("parias", new User("parias","333","Pedro"));                
    }
    
    public void setSever(Server srv){
        this.srv=srv;
    }
    
    public void post(String m){
        srv.deliver(m);
        // TODO if the receiver is not active, store it temporarily
    }
    /*
    public Client login(Client u) throws Exception{
        Client result=users.get(u.getId());
        if(result==null)  throw new Exception("User does not exist");
        if(!result.getPassword().equals(u.getPassword()))throw new Exception("User does not exist");
        return result;
    } 
*/
    public void logout(Client p) throws Exception{
        srv.remove(p);
    }    

    @Override
    public Client login(Client u) throws Exception {
        Client result=clientDao.read(u.getId());
        if(result==null)  throw new Exception("User does not exist");
        System.out.print("sirve");
        if(!result.getPassword().equals(u.getPassword()))throw new Exception("User does not exist");
        return result;
    }

    @Override
    public void signin(Client client) throws Exception {
        
    }

    @Override
    public void post_msg(String m, Client client) {
        srv.deliver(m);
    }

    @Override
    public void giveClients() {
      
    }
}
