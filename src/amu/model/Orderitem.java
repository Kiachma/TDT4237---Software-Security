package amu.model;

public class Orderitem {

	private Integer orderItemId;
	private int orderId;
	private int bookId;
	private int quantity;
	private String price;
	private int status;
	
	
	public Orderitem(int orderItemId, int orderId, int bookId, int quantity,
			String price, int status) {
		this.orderItemId = orderItemId;
		this.orderId = orderId;
		this.bookId = bookId;
		this.quantity = quantity;
		this.price = price;
		this.status = status;
	}


	public Orderitem(int orderId, int bookId, int quantity, String price) {
		this.orderItemId = null;
		this.orderId = orderId;
		this.bookId = bookId;
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


	public int getBookId() {
		return bookId;
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
	
}
