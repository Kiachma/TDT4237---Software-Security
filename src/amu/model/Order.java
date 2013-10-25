package amu.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    
    private List<Orderitem> orderItems;
    
    // TODO: Implement OrderItems as part of orders

    public Order(int id, Customer customer, Address address, Calendar createdDate, String value, int status, List<Orderitem> orderItems) {
        this.id = id;
        this.customer = customer;
        this.address = address;
        this.createdDate = createdDate;
        this.value = value;
        this.status = status;
        this.orderItems = orderItems;
    }

    public Order(Customer customer, Address address, String subtotal) {
        this.id = null;
        this.customer = customer;
        this.address = address;
        this.createdDate = null;
        this.value = subtotal;
        this.status = ORDER_PENDING;
    }
    
    public void addOrderitem(Orderitem item) {
    	if (orderItems == null) {
    		orderItems = new ArrayList<Orderitem>();
    	}
    	
    	if (item.getOrderId() != id) {
    		throw new IllegalArgumentException("Can't add item belonging to another order:" + item.getOrderId());
    	}
    	orderItems.add(item);
    }
    
    public List<Orderitem> getOrderitems() {
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
    	switch (status)
        {
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
    
}
