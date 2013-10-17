/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu;

import amu.model.Customer;
import javax.servlet.http.HttpSession;
import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 *
 * @author eaura
 */
public class Authentication {

    public static String hashPassword(String plainTextPassword) {
	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
	String encryptedPassword = passwordEncryptor.encryptPassword(plainTextPassword); 
        return encryptedPassword;
    }

    public static void logInCustomer(HttpSession session, Customer customer) {
        session.setAttribute("customer", customer);
    }

    public static void logOutCustomer(HttpSession session, Customer customer) {
        session.setAttribute("customer", null);
    }

    
    /**
     * Verify that the password matches the customer's
     * @param customer Customer object
     * @param password Plaintext password
     * @return
     */
    public static boolean verifyPassword(Customer customer, String password) {
	StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        return passwordEncryptor.checkPassword( password,customer.getPassword());
    }
}
