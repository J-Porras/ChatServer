/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;
import chatprotocol.*;

/**
 *
 * @author Porras
 */
public class Service implements IService{
    
    public static IService singleton;
    
    private Database theserver;
    
    private ClientDao clientDao;
    
    
    
    public static IService getInstance(){
        if(singleton == null){
            singleton = new Service();
        }
        return singleton;
    }
    
    public Service(){
        clientDao = new ClientDao();
    }
    
    public void setServer(Database database){
        theserver = database;
    }
    
    //----Clientes---
    
    
    
    @Override
    public void signin(Client client) throws Exception {
        clientDao.create(client);
    }
    
    @Override
    public Client login(Client client) throws Exception {
       return clientDao.read(client.getId());
    }

    @Override
    public void logout(Client client) throws Exception {
        
    }

    @Override
    public void post_msg(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
