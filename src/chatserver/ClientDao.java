/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import chatprotocol.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Porras
 */

public class ClientDao {
    
    //metodos basicos 
    
    public void create(Client cl) throws SQLException, Exception{
        String sqlcommand =  "inserto into Client (id_client,nombre,password,nickname,isonline) "
                + "values(?,?,?,?,?)";
        PreparedStatement stm = Database.instance().prepareStatement(sqlcommand);
        
        stm.setString(1,cl.getId());
        stm.setString(2,cl.getNombre());
        stm.setString(3,cl.getPassword());
        stm.setString(4,cl.getNickname());
        stm.setString(5, "true");//dice que no es case sensitive y que 1 y 0 sirven
        
        int count = Database.instance().executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Usuario ya existe");
        }
        
    }
    
    public Client read(String id) throws Exception{
        String sqlcommand = "select * from Client where id = ?";
        PreparedStatement stm = Database.instance().prepareStatement(sqlcommand);
        stm.setString(1, id);
        ResultSet rs =  Database.instance().executeQuery(stm);           
        if (rs.next()) {
            return from(rs);
        }
        else{
            throw new Exception ("Cliente no Existe");
        }
    }
    
    
    public Client from (ResultSet rs){
        try {
            Client r= new Client();
            r.setId(rs.getString("id"));
            r.setNombre(rs.getString("nombre"));
            return r;
        } catch (SQLException ex) {
            return null;
        }
    }
    
}
