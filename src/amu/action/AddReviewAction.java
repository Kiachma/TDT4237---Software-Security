/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu.action;

import amu.Utils;
import amu.database.BookDAO;
import amu.database.CreditCardDAO;
import amu.database.ReviewDAO;
import amu.model.CreditCard;
import amu.model.Customer;
import amu.model.Review;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author eaura
 */
public class AddReviewAction implements Action {

    private Map<String, String> messages = null;

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");
        Map<String, String> values = new HashMap<>();
        request.setAttribute("values", values);
        values.put("vote", request.getParameter("vote"));
        values.put("review", request.getParameter("review"));
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

            ReviewDAO reviewDAO = new ReviewDAO();
            BookDAO bookDAO = new BookDAO();
            Review review = new Review();
            review.setAuthor(customer);
            review.setRating(Integer.parseInt(request.getParameter("vote")));
            review.setReview(request.getParameter("review"));
            review.setBook(bookDAO.findByISBN(request.getParameter("isbn")));
            if (reviewDAO.add(review)) {
                return actionResponse;
            }

            messages.put("error", "An error occured.");
        }


        return actionResponse;
    }

    private boolean validateInput(HttpServletRequest request) {
        return Utils.validateInputLengths(request, messages) && validateVote(request);
    }

    private boolean validateVote(HttpServletRequest request) {
        int vote =Integer.parseInt(request.getParameter("vote"));
        if (vote<1 || vote>5) {
            messages.put("vote", "A vote has to be between 1 and 5");
            return false;
        }
        return true;
    }
}
