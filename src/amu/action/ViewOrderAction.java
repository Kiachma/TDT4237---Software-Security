/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amu.action;

import amu.database.BookDAO;
import amu.database.OrderDAO;
import amu.model.Book;
import amu.model.Customer;
import amu.model.Order;
import amu.model.OrderItem;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author eaura
 */
class ViewOrderAction implements Action {

    public ViewOrderAction() {
    }

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	HttpSession session = request.getSession(true);
	Customer customer = (Customer) session.getAttribute("customer");
	OrderDAO orderDAO = new OrderDAO();
	BookDAO bookDAO = new BookDAO();

	if (customer == null) {
	    ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
	    actionResponse.addParameter("from", "viewCustomer");
	    return actionResponse;
	}
	Order order = orderDAO.findById(Integer.parseInt(request.getParameter("id")));
	
	if (customer.getId() != order.getCustomer().getId()) {
	    session.invalidate();
	    ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
	    actionResponse.addParameter("from", "viewCustomer");
	    return actionResponse;
	}
	request.setAttribute("order", order);
	return new ActionResponse(ActionResponseType.FORWARD, "viewOrder");
    }

}
