package amu.database;

import amu.model.CartItem;
import amu.model.Customer;
import amu.model.OrderItem;
import amu.model.Order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
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
		List<OrderItem> orderItems = orderitemDAO.getItemsForMainOrder(orderId);
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

 public boolean add(Order order, Map<String, CartItem> cartObjects) {

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
            	int orderId = resultSet.getInt(Statement.RETURN_GENERATED_KEYS);
            	//save bought books in db for the order:
            	if (new OrderItemDAO().storeItemsForOrder(
            			orderId, Order.makeOrderItems(orderId, cartObjects))) {
            		return true;
            	}
            	
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return false;
    }

    public Order findById(int id) {
	Order order = null;
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;

	try {
	    connection = Database.getConnection();

	    String query = "SELECT * FROM `order` WHERE id=?";

	    statement = connection.prepareStatement(query);
	    statement.setInt(1, id);
	    resultSet = statement.executeQuery();

	    if (resultSet.next()) {

		AddressDAO addressDAO = new AddressDAO();
		OrderItemDAO orderitemDAO = new OrderItemDAO();
		Calendar createdDate = Calendar.getInstance();
		CustomerDAO customerDAO = new CustomerDAO();
		List<OrderItem> orderItems = orderitemDAO.getItemsForMainOrder(id);
		order = new Order(id,
			customerDAO.findById(resultSet.getInt("customer_id")),
			addressDAO.read(resultSet.getInt("address_id")),
			createdDate,
			resultSet.getString("value"),
			resultSet.getInt("status"),
			orderItems);
	    }
	} catch (SQLException exception) {
	    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
	} finally {
	    Database.close(connection, statement, resultSet);
	}

	return order;
    }

}
