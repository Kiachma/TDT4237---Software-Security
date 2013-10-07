/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu;

import amu.database.CustomerDAO;
import amu.model.Customer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Session;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author eaura
 */
public class Authentication {

    public static String hashPassword(String plainTextPassword) {
        String hashedPassword = null;
        try {
            hashedPassword = DatatypeConverter.printHexBinary(MessageDigest.getInstance("SHA1").digest((plainTextPassword + Config.SALT).getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hashedPassword;
    }
    
    public static void logInCustomer(HttpSession session, Customer customer){
        session.setAttribute("customer", customer);
    }
    
    public static void logOutCustomer(HttpSession session, Customer customer){
        session.setAttribute("customer", null);
    }
    
}
