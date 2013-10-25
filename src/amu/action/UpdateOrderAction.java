/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amu.action;

import amu.database.AddressDAO;
import amu.database.OrderDAO;
import amu.database.OrderItemDAO;
import amu.model.Customer;
import amu.model.Order;
import amu.model.OrderItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author eaura
 */
class UpdateOrderAction implements Action {

    public UpdateOrderAction() {
    }

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	HttpSession session = request.getSession(true);
	Customer customer = (Customer) session.getAttribute("customer");
	OrderDAO orderDAO = new OrderDAO();
	AddressDAO addressDAO = new AddressDAO();
	Order oldOrder = orderDAO.findById(Integer.parseInt(request.getParameter("order_id")));
	Order newOrder = new Order(customer, addressDAO.read(Integer.parseInt(request.getParameter("address_id"))), "0");
	List<OrderItem> newItems = new ArrayList<OrderItem>();

	newOrder.setParent(oldOrder.getId());

	BigDecimal subTotal = BigDecimal.ZERO;

	Map<String, String[]> names = request.getParameterMap();
	for (String key : names.keySet()) {
	    if (key.startsWith("quantity_")) {
		String bookId = key.replaceFirst("quantity_", "");
		OrderItem oldItem = getItemFromOrder(Integer.parseInt(bookId), oldOrder);
		int oldQuantity = oldOrder.getBookSum(Integer.parseInt(bookId));
		int newQuantity = Integer.parseInt(names.get(key)[0]);
		subTotal = subTotal.add(BigDecimal.valueOf(newQuantity * Float.valueOf(oldItem.getPrice())));
		if (oldQuantity != newQuantity) {
		    int difference = newQuantity - oldQuantity;
		    newItems.add(new OrderItem(oldItem.getBook(), difference, oldItem.getPrice()));
		} else{
		    newItems.add(new OrderItem(oldItem.getBook(), 0, oldItem.getPrice()));
		}
	    }

	}
	newOrder.setValue(subTotal.toString());
	orderDAO.updateOrder(newOrder, newItems);

	ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "viewOrder");
	actionResponse.addParameter("id", request.getParameter("order_id"));
	return actionResponse;
    }
    
    public OrderItem getItemFromOrder(int id, Order order){
	for (OrderItem item : order.getCondenseOrderItems()){
	    if(item.getBook().getId()==id){
		return item;
	    }
	}
	return null;
    }

}
