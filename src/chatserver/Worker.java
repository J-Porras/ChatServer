/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import chatprotocol.*;
import java.io.IOException;

/**
 *
 * @author Porras
 */
public class Worker {
    
    Socket skt;
    ObjectInputStream in;
    ObjectOutputStream out;
    Client client;
    boolean continuar;
    
    
    public Worker(Socket skt, ObjectInputStream in, ObjectOutputStream out, Client client) {
        this.skt = skt;
        this.in = in;
        this.out = out;
        this.client = client;
    }
    
    public void stop(){
        continuar = false;
    }
    
    
   public void start(){
        try {
            System.out.println("Worker atendiendo peticiones...");
            Thread t = new Thread(new Runnable(){
                public void run(){
                    listen();
                }
            });
            continuar = true;
            t.start();
        } catch (Exception ex) {  
        }
    }
   
   public void listen(){
        int method;
        while (continuar) {
            try {
                method = in.readInt();
                switch(method){
                //case Protocol.LOGIN: done on accept
                case Protocol.LOGOUT:
                    try {
                        Service.getInstance().logout(client);
                   
                    } catch (Exception ex) {}
                    stop();
                    break;                 
                case Protocol.MSG:
                    String message=null;
                    try {
                        message = (String)in.readObject();
                        Service.getInstance().post_msg(client.getId() + ": " + message);
                        
                    } catch (ClassNotFoundException ex) {}
                    break;                     
                }
                out.flush();
            } catch (IOException  ex) {
                continuar = false;
            }                        
        }
    }
    
    
}
