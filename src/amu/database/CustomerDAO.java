package amu.database;

import amu.Config;
import amu.model.Customer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.logging.*;
import javax.sql.*;
import javax.xml.bind.DatatypeConverter;

public final class CustomerDAO {

    public static String generateActivationCode() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[8];
        random.nextBytes(bytes);
        return DatatypeConverter.printHexBinary(bytes);
    }

    public Customer findByEmail(String email) {
        Customer customer = null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();


            String query = "SELECT * FROM customer WHERE email=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findByEmail SQL Query: " + query);


            if (resultSet.next()) {
                customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPassword(resultSet.getString("password"));
                customer.setName(resultSet.getString("name"));
                customer.setActivationToken(resultSet.getString("activation_token"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return customer;
    }

    public boolean edit(Customer customer) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "UPDATE customer SET email=?, password=?, name=?, salt=? WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, customer.getEmail());
            statement.setString(2, customer.getPassword());
            statement.setString(3, customer.getName());
            statement.setInt(4, customer.getId());
            statement.setString(5, customer.getSalt());

            if (statement.executeUpdate() == 0) {
                return false; // No rows were affected
            } else {
                return true;
            }

        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
            return false;
        } finally {
            Database.close(connection, statement, resultSet);
        }
    }

    public Customer register(Customer customer) {

        DataSource dataSource = null;
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = Database.getConnection();


            String query = "INSERT INTO customer (email, password, name, activation_token, salt) VALUES (?,?,?,?,?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, customer.getEmail());
            statement.setString(2, customer.getPassword());
            statement.setString(3, customer.getName());
            statement.setString(4, customer.getActivationToken());
            statement.setString(5, customer.getSalt());

            statement.executeUpdate();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "register SQL Query: " + query);

        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement);
        }

        return findByEmail(customer.getEmail());
    }

    public Customer activate(Customer customer) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();
            statement = connection.createStatement();

            String query = "UPDATE customer SET activation_token=NULL WHERE email='"
                    + customer.getEmail()
                    + "'";
            statement.executeUpdate(query);
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "activate SQL Query: " + query);

            customer.setActivationToken(null);

        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return customer;
    }

    public Customer findById(int id) {
        Customer customer = null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();


            String query = "SELECT * FROM customer WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findByEmail SQL Query: " + query);


            if (resultSet.next()) {
                customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPassword(resultSet.getString("password"));
                customer.setName(resultSet.getString("name"));
                customer.setActivationToken(resultSet.getString("activation_token"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return customer;
    }

    public String getSalt(int id) {
        String salt = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();


            String query = "SELECT salt FROM customer WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findByEmail SQL Query: " + query);


            if (resultSet.next()) {
                salt=resultSet.getString("salt");
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return salt;
    }
}