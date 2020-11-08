/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import chatprotocol.Client;
import chatprotocol.Mensaje;
import chatprotocol.Protocol;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;

public class Server {
    ServerSocket srv;
    List<Worker> workers; 
    
    public Server() {
        try {
            srv = new ServerSocket(Protocol.PORT);
            workers =  Collections.synchronizedList(new ArrayList<Worker>());
        } 
        catch (IOException ex) {}
    }
    
    public void run(){
        Service localService = (Service)(Service.instance());
        localService.setSever(this);
        boolean continuar = true;
        while (continuar) {
            
            try {
                Socket skt = srv.accept();
                ObjectInputStream in = new ObjectInputStream(skt.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(skt.getOutputStream() );
                try {
                    int method = in.readInt(); // should be Protocol.LOGIN  
                

                    Client user=(Client)in.readObject();
                    
   
                    try {
                        user=Service.instance().login(user);
                        
                        
                        out.writeInt(Protocol.ERROR_NO_ERROR);
                        
                        
                        out.writeObject(user);
                        out.flush();
                        
                        
                        Worker worker = new Worker(skt,in,out,user); 
                        workers.add(worker);                      
                        worker.start();                            
                    } catch (Exception ex) {
                       out.writeInt(Protocol.ERROR_LOGIN);
                       out.flush();
                    }                          
                } 
                catch (ClassNotFoundException ex) {}                

            } 
            catch (IOException ex) {}
        }
    }
    

    
    public void deliver(Mensaje msg){
        System.out.println("\nSERVER: deliveando\n");
        for (int i = 0; i < workers.size(); i++) {
            if (workers.get(i).client.getNickname() == msg.getDestino().getNickname()) {
                workers.get(i).deliver(msg);
                System.out.println("\nworker encontrado y psasdo por deliver\n");
                break;
            }
        }
        System.out.println("\nWorker no encontrado\n");
    } 
    
    public void remove(Client u){
        for(Worker wk:workers) {
            if(wk.client.equals(u)){
                workers.remove(wk);
                try { wk.skt.close();} catch (IOException ex) {}
                break;
            }
        }
    }
    
    public void giveFriends(Client cl){
     
        List<Client> friends = Collections.synchronizedList(new ArrayList<Client>());
        for (int i = 0; i < cl.getFriends().size(); i++) {
            for (int j = 0; j < this.workers.size(); j++) {
                if (cl.getFriends().get(i).getId() == workers.get(i).client.getId()) {
                    if (workers.get(i).client.getIsonline()) {
                        friends.add(workers.get(i).client);
                        
                    }
                    break;
                    
                }
            }  
        }
        
        for(Worker wk:workers){
            if (wk.client.getId() == cl.getId()) {
                wk.deliverFriends(friends);
            }
        } 
        
    }
    
    
    
}
