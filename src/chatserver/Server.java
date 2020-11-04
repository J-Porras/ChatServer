/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import chatprotocol.Client;
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
                System.out.println("try catch de clase Server");
                Socket skt = srv.accept();
                ObjectInputStream in = new ObjectInputStream(skt.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(skt.getOutputStream() );
                System.out.println("Socket aceptado");
                try {
                    System.out.println("Recibiendo metodo");
                    int method = in.readInt(); // should be Protocol.LOGIN  
                    System.out.println("Server: Metodo recibido: " + Integer.toString(method));

                    Client user=(Client)in.readObject();    
                    System.out.println("Server: Cliente recibido: " + user.getId());
                    try {
                        user=Service.instance().login(user);
                        System.out.println("Server: Cliente encontrado");
                        out.writeInt(Protocol.ERROR_NO_ERROR);
                        System.out.println("Server: Enviado protocolo no error");
                        out.writeObject(user);
                        out.flush();
                        System.out.println("Server: Objeto flusheado de vuelta");
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
    
    public void deliver(String message,Client cl){
        for(Worker wk:workers){
            if (wk.client.getId() == cl.getId()) {
                wk.deliver(message);
            }
        }        
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
    
}
