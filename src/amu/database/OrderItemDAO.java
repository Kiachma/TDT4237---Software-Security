package amu.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import amu.model.Order;
import amu.model.Orderitem;

public class OrderItemDAO {
	private Connection connection = null;
	private PreparedStatement statement = null;
	private ResultSet rs = null;
	
	
	/**
	 * Use to get every item belonging to a main (the placed order).
	 * This includes items from sub/modified orders as well.
	 * @param orderId the id of an order without a parent
	 * @return list of orderitems, these can have negative amounts
	 */
	public List<Orderitem> getItemsForMainOrder(int orderId) {
		List<Orderitem> items = new ArrayList<Orderitem>();
		try {
            connection = Database.getConnection();
            String columns = "orders.id, order_item_id, book_id, quantity, price, " +
            		"order_items.status";
            //fetch all order items belonging to the main order (including sub orders)
            String query = "SELECT " + columns +
            		" FROM `orders` INNER JOIN `order_items` " +
            		"ON orders.id=order_items.order_id" +
            		"WHERE id=? OR parent=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, orderId);
            statement.setInt(2, orderId);
            rs = statement.executeQuery();

            while (rs.next()) {
            	Orderitem item = new Orderitem(
            			//orderId for the main order is used
            			rs.getInt("order_item_id"), orderId,
            			rs.getInt("book_id"), rs.getInt("quantity"),
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
}
