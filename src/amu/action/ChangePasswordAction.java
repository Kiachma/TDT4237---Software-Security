package amu.action;

import amu.Authentication;
import amu.Utils;
import amu.database.CreditCardDAO;
import amu.database.CustomerDAO;
import amu.model.CreditCard;
import amu.model.Customer;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jasypt.util.text.StrongTextEncryptor;

class ChangePasswordAction implements Action {

    private CustomerDAO customerDAO;

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
	HttpSession session = request.getSession(true);
	Customer customer = (Customer) session.getAttribute("customer");

	if (customer == null) {
	    ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
	    actionResponse.addParameter("from", "changePassword");
	    return actionResponse;
	}

	if (request.getMethod().equals("POST")) {
	    customerDAO = new CustomerDAO();
	    List<String> messages = new ArrayList<String>();
	    request.setAttribute("messages", messages);
	    String oldPassword = request.getParameter("oldPassword");
	    if (!Authentication.verifyPassword(customer, oldPassword)) {
		messages.add("Wrong old password");
		return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
	    }
	    String[] password = request.getParameterValues("password");

	    // Validate that new email is typed in the same both times
	    if (password[0].equals(password[1]) == false) {
		messages.add("Password and repeated password did not match. Please try again.");
		return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
	    }

	    // Validate that new email complexity
	    if (!Utils.validatePassword(password[0])) {
		messages.add("Password must be between 8 and 40 charactes long and contain: a special character(@#$%^&+=), an upper case letter, a lower case letter and a digit ");
		return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
	    }

	    // Validation OK, do business logic
	    String newPassword = Authentication.hashPassword(password[0]);
	    updateCreditCards(newPassword, customer);
	    customer.setPassword(newPassword);
	   

	    if (customerDAO.edit(customer) == false) {
		messages.add("An error occured.");
		return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
	    }
	    session.setAttribute("customer", customer);
	    // Email change successful, return to viewCustomer
	    return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");

	}

	// (request.getMethod().equals("GET")) 
	return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
    }

    private void updateCreditCards(String newPassword, Customer customer) {
	CreditCardDAO ccdao = new CreditCardDAO();
	List<CreditCard> cards = ccdao.browse(customer);
	for (CreditCard card : cards) {
	    String oldNumber = card.getDecryptedCreditCardNumber();
	    StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
	    textEncryptor.setPassword(newPassword);
	    card.setCreditCardNumber(textEncryptor.encrypt(oldNumber));
	    ccdao.update(card);
	}

    }
}
