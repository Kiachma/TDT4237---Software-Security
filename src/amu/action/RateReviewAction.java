/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amu.action;

import amu.database.BookDAO;
import amu.database.ReviewDAO;
import amu.database.ReviewVoteDAO;
import amu.model.Book;
import amu.model.Customer;
import amu.model.Review;
import amu.model.ReviewVote;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author eaura
 */
public class RateReviewAction implements Action {

    private Map<String, String> messages = null;

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	HttpSession session = request.getSession(true);
	Customer customer = (Customer) session.getAttribute("customer");
	Map<String, String> values = new HashMap<>();
	request.setAttribute("values", values);

	values.put("isbn", request.getParameter("isbn"));
	if (customer == null) {
	    ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
	    actionResponse.addParameter("from", "viewBook");
	    return actionResponse;
	}
	ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "viewBook");
	actionResponse.addParameter("isbn", request.getParameter("isbn"));
	if (request.getMethod().equals("POST")) {
	    messages = new HashMap<>();
	    request.setAttribute("messages", messages);
	    if (!validateInput(request)) {
		return actionResponse;
	    }

	    BookDAO bookDAO = new BookDAO();
	    ReviewVoteDAO reviewVoteDAO = new ReviewVoteDAO();
	    ReviewDAO reviewDAO = new ReviewDAO();
	    Book book = bookDAO.findByISBN(request.getParameter("isbn"));
	    Review review = reviewDAO.findById(Integer.parseInt(request.getParameter("review")));
	    ReviewVote reviewVote = new ReviewVote();
	    reviewVote.setCustomer(customer);
	    reviewVote.setReview(review);
	    review.setBook(book);
	    if (request.getParameter("vote").equals("1")) {
		reviewVote.setHelpful();
	    } else {
		reviewVote.setUnHelpful();
	    }
	    if (reviewVoteDAO.add(reviewVote)) {
		return actionResponse;
	    }

	    messages.put("error", "An error occured.");
	}
	return actionResponse;
    }

    private boolean validateInput(HttpServletRequest request) {
	int vote = Integer.parseInt(request.getParameter("vote"));
	if (!(vote == 0 || vote == 1)) {
	    messages.put("vote", "A vote has to be true of false");
	    return false;
	}
	return true;
    }

}
