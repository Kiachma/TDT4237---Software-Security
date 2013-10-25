package amu.database;

import amu.model.Customer;
import amu.model.Order;
import amu.model.Orderitem;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class OrderDAO {

	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet resultSet = null;
    
    public List<Order> browse(Customer customer) {
        List<Order> orders = new ArrayList<Order>();

        try {
            connection = Database.getConnection();
            //select only main orders
            String query = "SELECT * FROM `order` WHERE customer_id=? AND parent IS NULL";
            statement = connection.prepareStatement(query);
            statement.setInt(1, customer.getId());
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
            	AddressDAO addressDAO = new AddressDAO();
            	OrderItemDAO orderitemDAO = new OrderItemDAO();
            	Calendar createdDate = Calendar.getInstance();

            	createdDate.setTime(resultSet.getDate("created"));
            	int orderId = resultSet.getInt("id");
            	//get all items from the order and any suborders:
            	List<Orderitem> orderItems = orderitemDAO.getItemsForMainOrder(orderId);
				orders.add(new Order(orderId,
                        customer, 
                        addressDAO.read(resultSet.getInt("address_id")), 
                        createdDate, 
                        resultSet.getString("value"), 
                        resultSet.getInt("status"),
                        orderItems));
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return orders;
    }
    
        public boolean add(Order order) {

        try {
            connection = Database.getConnection();

            String query = "INSERT INTO `order` (customer_id, address_id, created, value, status) VALUES (?, ?, CURDATE(), ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, order.getCustomer().getId());
            statement.setInt(2, order.getAddress().getId());
            statement.setBigDecimal(3, new BigDecimal(order.getValue()));
            statement.setInt(4, order.getStatus());
            statement.executeUpdate();
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                // TODO: Add OrderItems
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
