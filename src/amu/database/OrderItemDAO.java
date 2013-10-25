package amu.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import amu.model.OrderItem;

public class OrderItemDAO {

    private Connection connection = null;
    private PreparedStatement statement = null;
    private ResultSet rs = null;

    /**
     * Use to get every item belonging to a main (the placed order). This includes items from sub/modified order as well.
     *
     * @param orderId the id of an order without a parent
     * @return list of orderitems, these can have negative amounts
     */
    public List<OrderItem> getItemsForMainOrder(int orderId) {
	List<OrderItem> items = new ArrayList<OrderItem>();
	try {
	    connection = Database.getConnection();
	    String columns = "order.id, order_item_id, book_id, quantity, price, "
		    + "order_items.status";
	    //fetch all order items belonging to the main order (including sub order)
	    String query = "SELECT " + columns
		    + " FROM `order` INNER JOIN `order_items` "
		    + "ON order.id=order_items.order_id"
		    + " WHERE id=? OR parent=?";
	    statement = connection.prepareStatement(query);
	    statement.setInt(1, orderId);
	    statement.setInt(2, orderId);
	    rs = statement.executeQuery();

	    while (rs.next()) {
		BookDAO bookDAO = new BookDAO();
		OrderItem item = new OrderItem(
			//orderId for the main order is used
			rs.getInt("order_item_id"), orderId,
			bookDAO.findByID(rs.getInt("book_id")), rs.getInt("quantity"),
			rs.getString("price"), rs.getInt("status"));
		items.add(item);
	    }
	} catch (SQLException exception) {
	    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
	} finally {
	    Database.close(connection, statement, rs);
	}

	return items;
    }

    public boolean storeItemsForOrder(int orderId, List<OrderItem> items) {
	if (items == null || items.isEmpty()) {
	    throw new IllegalArgumentException("Can't store empty/null list");
	} else if (orderId < 0) {
	    throw new IllegalArgumentException("Invalid order number: Can't be negative");
	}

	try {
	    connection = Database.getConnection();
	    String query = "INSERT INTO `order_items` (order_id, book_id, quantity, "
		    + "price, status) VALUES (?, ?, ?, ?, ?)";
	    statement = connection.prepareStatement(query);
	    
	    int rows = 0;
	    for (OrderItem item : items) {
		statement.setInt(1, orderId);
		statement.setInt(2, item.getBook().getId());
		statement.setInt(3, item.getQuantity());
		statement.setBigDecimal(4, new BigDecimal(item.getPrice()));
		statement.setInt(5, item.getStatus());
		rows += statement.executeUpdate();
	    }

	    if (rows != items.size()) {
		Exception e = new IllegalStateException("Non-matching number of rows updated!");
		Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
		return false;
	    }
	} catch (SQLException exception) {
	    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
	} finally {
	    Database.close(connection, statement, rs);
	}

	return true;
    }

    public boolean add(OrderItem item) {
	Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "INSERT INTO order_items (order_id, book_id, quantity, price,status) VALUES (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, item.getOrderId());
            statement.setInt(2, item.getBook().getId());
            statement.setInt(3, item.getQuantity());
            statement.setString(4, item.getPrice());
	    statement.setInt(5, item.getStatus());

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
