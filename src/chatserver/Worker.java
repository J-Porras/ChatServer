package chatserver;


import chatprotocol.Protocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import chatprotocol.IService;
import chatprotocol.Client;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Worker {
    Socket skt;
    ObjectInputStream in;
    ObjectOutputStream out;
    Client client;

    public Worker(Socket skt, ObjectInputStream in, ObjectOutputStream out, Client user) {
        this.skt=skt;
        this.in=in;
        this.out=out;
        this.client=user;
    }

    boolean continuar;    
    public void start(){
        try {
            System.out.println("Worker atendiendo peticiones...");
            Thread t = new Thread(new Runnable(){
                public void run(){
                    try {
                        listen();
                    } catch (Exception ex) {
                        //Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            continuar = true;
            t.start();
        } catch (Exception ex) {  
        }
    }
    
    public void stop(){
        continuar=false;
    }
    
    public void listen() throws Exception{
        int method;
        while (continuar) {
            try {
                System.out.println("Worker: Inside listen ");
                method = in.readInt();
                switch(method){
                  
                    //case Protocol.LOGIN: done on accept
                    case Protocol.LOGOUT:
                        try {
                            Service.instance().logout(client);
                        } catch (Exception ex) {}
                        stop();
                        break; 
                        
                    case Protocol.MSG:
                        String message=null;
                        try {
                            message = (String)in.readObject();
                            Service.instance().post_msg(message,client.getDestino());
                        } catch (ClassNotFoundException ex) {}
                        break;  
                        
                    case Protocol.REQ_USERS:
                       
                        try {
                            System.out.println("case Protocol REQ_USERS ");
                            Service.instance().giveClients(client);
                        } 
                        catch (Exception e) {
                        }
                        
                    break;     
                        
                }
                out.flush();
            } catch (IOException  ex) {
                continuar = false;
            }                        
        }
    }
    
    public void deliver(String message){
        try {
            out.writeInt(Protocol.DELIVER);
            out.writeObject(message);
            out.flush();
        } 
        catch (IOException ex) {}
    }
    
    //on line friends
    public void deliverFriends(List<Client> friends){
        try {
            out.writeInt(Protocol.ON_USERS);
            out.writeObject(friends);
            out.flush();
        } catch (Exception e) {
        }
    }
}
