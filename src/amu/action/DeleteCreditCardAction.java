package amu.action;

import amu.Authentication;
import amu.database.CreditCardDAO;
import amu.model.CreditCard;
import amu.model.Customer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class DeleteCreditCardAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "viewCustomer");
            return actionResponse;
        }

        CreditCardDAO creditCardDAO = new CreditCardDAO();
        CreditCard creditCard;

        int ccId = -1;//always invalid ID
        try {
        	ccId =  Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {}//ignore
        
		if (request.getMethod().equals("POST")) {
            List<String> messages = new ArrayList<String>();
            request.setAttribute("messages", messages);
            
            //invalid credit card id
            if (ccId < 0) {
            	return logBadIDAndLogoutCustomer(session, customer);
            }
            
            boolean passwordOk = Authentication.verifyPassword(customer, request.getParameter("password"));
			if (!passwordOk) {
				messages.add("Authentication error");
        		ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "deleteCreditCard");
        		actionResponse.addParameter("id", "" + ccId);
        		actionResponse.addParameter("e", "auth");
        		return actionResponse;
        	}

            if (creditCardDAO.delete(ccId)) {
                return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
            }

            messages.add("An error occured.");
        }

        // (request.getMethod().equals("GET"))
		String errors = request.getParameter("e"); 
		if (errors != null) {
			List<String> messages = new ArrayList<String>();
            request.setAttribute("messages", messages);
            String m = "";
            switch(errors) {
            case "auth" : {
            	m = "Authentication issue";
            	break;
            }
            default : {m = "An error occured";}
            }
            messages.add(m);
		}
		
		//avoid trying to create a card if the id isn't a number
        creditCard = (ccId < 0) ? null : creditCardDAO.read(ccId);
        if (creditCard!=null && creditCard.getCustomer().getId() == customer.getId()) {
            request.setAttribute("creditCard", creditCard);
            return new ActionResponse(ActionResponseType.FORWARD, "deleteCreditCard");
        } else {
            return logBadIDAndLogoutCustomer(session, customer);
        }
    }

	private ActionResponse logBadIDAndLogoutCustomer(HttpSession session,
			Customer customer) {
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Customer ("+customer.getId()+") tried to delete an invalid credit card");
		Authentication.logOutCustomer(session, customer);
		return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
	}
}
