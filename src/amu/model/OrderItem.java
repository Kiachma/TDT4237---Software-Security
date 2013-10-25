package amu.model;

public class OrderItem {

	private Integer orderItemId;
	private int orderId;
	private Book book;
	private int quantity;
	private String price;
	private int status;
	
	
	public OrderItem(int orderItemId, int orderId, Book book, int quantity,
			String price, int status) {
		this.orderItemId = orderItemId;
		this.orderId = orderId;
		this.book = book;
		this.quantity = quantity;
		this.price = price;
		this.status = status;
	}


	public OrderItem(int orderId, Book book, int quantity, String price) {
		this.orderItemId = null;
		this.orderId = orderId;
		this.book = book;
		this.quantity = quantity;
		this.price = price;
		//TODO: Consider if individual items can be plucked/shipped and order status
		//depends on these.
		this.status = Order.ORDER_PENDING;
	}


	public Integer getOrderItemId() {
		return orderItemId;
	}


	public int getOrderId() {
		return orderId;
	}


	public Book getBook() {
		return book;
	}


	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public String getPrice() {
		return price;
	}


	public int getStatus() {
		return status;
	}
	
	public String getStatusText() {
		return Order.getStatusText(status);
	}
	
	public float getTotalPrice(){
	    return quantity*Float.valueOf(price);
	}
	
}
