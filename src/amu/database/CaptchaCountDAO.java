/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author JohnArne
 */
public final class CaptchaCountDAO {
    
    /*
     * Returns the captcha count of the email provided.
     * If the email doesn't exist it creates a new element in the table
     * and starts a count at "0".
     */
    public int getCount(String email){
        int count = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        
        try{
            connection = Database.getConnection();
            
            String query = "SELECT Count FROM captchacount WHERE email = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            result = statement.executeQuery();
            
            if(result.next()){
                count = result.getInt("count");
            }else{
                query = "INSERT INTO captchacount (email, count) VALUES(?,?)";
                statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setInt(2, 0);
                statement.executeUpdate();
            }
            
        }catch(SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "SQLException");
        }finally{
            Database.close(connection, statement, result);
        }
        
        
        return count; 
    }
    
    public int incrementCount(String email){
        
        String query = "UPDATE captchacount SET count = count + 1 WHERE email = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        
        try{
            connection = Database.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.executeUpdate();
            
        }catch(SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "SQL Exception query: "+query);
        }finally{
            Database.close(connection, statement);
        }
        
        return getCount(email);
    }
    
    public void resetCount(String email){
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "UPDATE captchacount SET count = 0 WHERE email = ?";
        try{
            connection = Database.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.executeUpdate();
        }catch(SQLException e){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "SQL Eception");
        }finally{
            Database.close(connection, statement);
        }
        
    }
}
