package amu.action;

import amu.Authentication;
import amu.database.AddressDAO;
import amu.model.Address;
import amu.model.Customer;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class DeleteAddressAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        List<String> messages = new ArrayList<String>();
        request.setAttribute("messages", messages);
        
        Customer customer = (Customer) session.getAttribute("customer");
        
        String errors = request.getParameter("e");
        if (errors != null) {
        	switch(errors) {
        	case "auth" : {
        		messages.add("Authentication error. Please try again.");
        		break;
        	}
        	default : {
        		messages.add("An error occured");
        	}
        	}
        }

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "viewCustomer"); 
            return actionResponse;
        }

        AddressDAO addressDAO = new AddressDAO();
        Address address;

        int addrID = -1;
        try {
        	addrID = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {}//ignore
        
		if (request.getMethod().equals("POST")) {
            
            
         // Validate password
            String password = request.getParameter("password");
            boolean matches = Authentication.verifyPassword(customer, password);
            if (!matches) {
            	ActionResponse r = new ActionResponse(ActionResponseType.REDIRECT, "deleteAddress");
            	r.addParameter("id", "" + addrID);
            	r.addParameter("e", "auth");
            	return r;
            }

            if (addrID >= 0 && addressDAO.delete(addrID)) {
                return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
            } else {
            	messages.add("An error occured.");
            	return new ActionResponse(ActionResponseType.FORWARD, "deleteAddress");
            }
        }

        // (request.getMethod().equals("GET")) 
		if (addrID < 0) {
			return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
		}
        address = addressDAO.read(addrID); 
        request.setAttribute("address", address);
        return new ActionResponse(ActionResponseType.FORWARD, "deleteAddress");
    }
}
