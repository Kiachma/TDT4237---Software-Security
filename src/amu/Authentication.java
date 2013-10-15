/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu;

import amu.database.CustomerDAO;
import amu.model.Customer;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import sun.misc.BASE64Encoder;

/**
 *
 * @author eaura
 */
public class Authentication {

    public static String hashPassword(String plainTextPassword, String salt) {
        String hashedPassword = null;
        String tmp = plainTextPassword;
        try {
            for (int i = 0; i < 1000; i++) {

                tmp = DatatypeConverter.printHexBinary(MessageDigest.getInstance("SHA1").digest((tmp + salt + Config.PEPPER).getBytes()));
            }
            hashedPassword = tmp;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hashedPassword;
    }

    public static void logInCustomer(HttpSession session, Customer customer) {
        session.setAttribute("customer", customer);
    }

    public static void logOutCustomer(HttpSession session, Customer customer) {
        session.setAttribute("customer", null);
    }

    public static String generateSalt() throws NoSuchAlgorithmException {
        // Uses a secure Random not a simple Random
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // Salt generation 64 bits long
        byte[] bSalt = new byte[8];
        random.nextBytes(bSalt);
        // Digest computation
        return byteToBase64(bSalt);
    }

    /**
     * From a String returns a base 64 representation
     *
     * @param data String
     * @return String
     * @throws IOException
     */
    public static String byteToBase64(byte[] data) {
        BASE64Encoder endecoder = new BASE64Encoder();
        return endecoder.encode(data);
    }
    
    /**
     * Verify that the password matches the customer's
     * @param customer Customer object
     * @param password Plaintext password
     * @return
     */
    public static boolean verifyPassword(Customer customer, String password) {
        CustomerDAO customerDAO = new CustomerDAO();
        String customerSalt = customerDAO.getSalt(customer.getId());
		String hashedPassword = Authentication.hashPassword(password, customerSalt);
        return customer.getPassword().equals(hashedPassword);
    }
}
