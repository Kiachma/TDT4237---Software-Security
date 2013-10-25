package amu.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {

    public static final int ORDER_DELIVERED = 2;
    public static final int ORDER_SHIPPED = 1;
    public static final int ORDER_PENDING = 0;
    public static final int ORDER_CANCELED = -1;

    private Integer id;
    private Customer customer;
    private Address address;
    private Calendar createdDate;
    private String value;
    private int status;
    private HashMap<Integer, OrderItem> itemMap;
    private Integer parent;

    private List<OrderItem> orderItems;

    // TODO: Implement OrderItems as part of orders
    public Order(int id, Customer customer, Address address, Calendar createdDate, String value, int status, List<OrderItem> orderItems) {
	this.id = id;
	this.customer = customer;
	this.address = address;
	this.createdDate = createdDate;
	this.value = value;
	this.status = status;
	this.orderItems = orderItems;
	updateOrderCost();
    }

    public Order(Customer customer, Address address, String subtotal) {
	this.id = null;
	this.customer = customer;
	this.address = address;
	this.createdDate = null;
	this.value = subtotal;
	this.status = ORDER_PENDING;
    }

    public void addOrderItem(OrderItem item) {
	if (orderItems == null) {
	    orderItems = new ArrayList<OrderItem>();
	}

	if (item.getOrderId() != id) {
	    throw new IllegalArgumentException("Can't add item belonging to another order:" + item.getOrderId());
	}
	orderItems.add(item);
    }

    /**
     * Update total cost of order based on its orderitems
     */
    public void updateOrderCost() {
	if (orderItems == null || orderItems.isEmpty()) {
	    return;
	}

	float total = (float) 0.0;
	for (OrderItem item : orderItems) {
	    float price = Float.parseFloat(item.getPrice());
	    total += price * item.getQuantity();
	}
	value = "" + total;
    }

    /**
     * Get a minimal booklist by combining modifier orders with the original Note that order_item_ids will be removed, as these won't exist in the database for the modified versions. Should be used for parent orders only.
     *
     * @return list with each book occurring max one time
     */
    public List<OrderItem> getCondenseOrderItems() {
	if (orderItems == null || orderItems.isEmpty()) {
	    throw new IllegalStateException("No list or no entries to condense");
	}
	List<OrderItem> compressed = new ArrayList<OrderItem>();
	Map<Integer, OrderItem> temp = new HashMap<Integer, OrderItem>();

	for (OrderItem item : orderItems) {
	    int bId = item.getBook().getId();
	    if (temp.containsKey(bId)) {
		OrderItem orderItem = new OrderItem(item.getBook(),  temp.get(bId).getQuantity() + item.getQuantity(), item.getPrice());
		temp.put(bId, orderItem);
	    } else {
		temp.put(bId, item);
	    }
	}
	compressed.addAll(temp.values());
	return compressed;
    }

    public List<OrderItem> getOrderItems() {
	return orderItems;
    }

    public Integer getId() {
	return id;
    }

    public Customer getCustomer() {
	return customer;
    }

    public Address getAddress() {
	return address;
    }

    public Calendar getCreatedDate() {
	return createdDate;
    }

    public String getValue() {
	return value;
    }

    public int getStatus() {
	return status;
    }

    public String getStatusText() {
	return Order.getStatusText(this.status);
    }

    public static String getStatusText(int status) {
	switch (status) {
	    case ORDER_DELIVERED:
		return "Delivered";
	    case ORDER_SHIPPED:
		return "Shipped";
	    case ORDER_PENDING:
	    default:
		return "Pending";
	    case ORDER_CANCELED:
		return "Canceled";
	}
    }

    public static List<OrderItem> makeOrderItems(
	    int orderId, Map<String, CartItem> cartObjects) {
	if (orderId < 0) {
	    throw new IllegalArgumentException("Invalid order id: can't be negative");
	} else if (cartObjects == null || cartObjects.isEmpty()) {
	    throw new IllegalArgumentException("No items to add!");
	}

	List<OrderItem> items = new ArrayList<OrderItem>();
	for (CartItem i : cartObjects.values()) {
	    Book book = i.getBook();
	    int quantity = i.getQuantity();
	    String price = "" + book.getPrice();
	    items.add(new OrderItem(orderId, book, quantity, price));
	}
	return items;
    }

    public HashMap<Integer, OrderItem> getItemMap() {
	return itemMap;
    }

    public OrderItem getOrderItemById(Integer id) {
	return itemMap.get(id);
    }

    public Integer getParent() {
	return parent;
    }

    public void setParent(Integer parent) {
	this.parent = parent;
    }

    public void setValue(String toString) {
	this.value = toString;
    }
    
    public int getBookSum(int id){
	int sum =0;
	for(OrderItem item : orderItems){
	    if(item.getBook().getId()==id){
		sum=sum+item.getQuantity();
	    }
	}
	return sum;
    }

}
