package amu.database;

import amu.model.Address;
import amu.model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddressDAO {

    private Connection connection = null;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;
    private CustomerDAO customerDAO =null;
    
    public List<Address> browse(Customer customer) {
        List<Address> addresses = new ArrayList<Address>();

        try {
            connection = Database.getConnection();
            String query = "SELECT id, address FROM address WHERE customer_id=?";
            statement = connection.prepareStatement(query);

            statement.setInt(1, customer.getId());

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                addresses.add(new Address(resultSet.getInt("id"), customer, resultSet.getString("address")));
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return addresses;
    }

    public Address read(int id) {
        Address address = null;
        customerDAO= new CustomerDAO();
        try {
            connection = Database.getConnection();

            String query = "SELECT * FROM address WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
            	Customer customer =customerDAO.findById(resultSet.getInt("customer_id"));
                address = new Address(id, customer, resultSet.getString("address")); 
            } 
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return address;
    }

    public boolean edit(Address address) {

        try {
            connection = Database.getConnection();

            String query = "UPDATE address SET address=? WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, address.getAddress());
            statement.setInt(2, address.getId());

            if (statement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return false;
    }
    
    public boolean add(Address address) {

        try {
            connection = Database.getConnection();

            String query = "INSERT INTO address (customer_id, address) VALUES (?, ?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, address.getCustomer().getId());
            statement.setString(2, address.getAddress());

            if (statement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return false;
    }

    public boolean delete(int id) {

        try {
            connection = Database.getConnection();

            String query = "DELETE FROM address WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            if (statement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return false;
    }
}
