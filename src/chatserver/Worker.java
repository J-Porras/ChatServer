package chatserver;


import chatprotocol.Protocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import chatprotocol.IService;
import chatprotocol.Client;
import chatprotocol.Mensaje;
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
                        
                        try {
                           System.out.println("llego\n");
                            Mensaje message = (Mensaje)in.readObject();//aqui esta el error // ya vengo sigue sin servir se loopea aqui
                            //serializado
                            System.out.println("Worker: protocolo mensaje recibido\n");
                            System.out.println("\nWorker: mensaje parseado correctly\n");
                            Service.instance().post(message);
                            

                        } catch (ClassNotFoundException ex) {
                         System.out.println("Worker: protocolo mensaje no recibido\n");
                        }
                        break;  
                        
                    case Protocol.REQ_USERS:
                       
                        try {
                            
                            Service.instance().giveClients(client);
                            
                        } 
                        catch (Exception e) {
                        }
                    break;   
                    
                    
                    case Protocol.ADD_USER:
                        try {
                            
                            Client newfriend = Service.instance().addFriend(client);
                            out.writeInt(Protocol.ERROR_NO_ERROR);
                            out.writeObject(newfriend);
                            out.flush();
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

    //on line friends
    public void deliverFriends(List<Client> friends){
        try {
            out.writeInt(Protocol.ON_USERS);
            out.writeObject(friends);
            out.flush();
        } catch (Exception e) {
        }
    }

    void deliver(Mensaje msg) {
        try {
            out.writeInt(Protocol.DELIVER);
            out.writeObject(msg);
            System.out.println("\nWorker: Mensaje enviado por out\n");
        
            out.flush();
        } 
        catch (IOException ex) {}
    }
    
    
}
