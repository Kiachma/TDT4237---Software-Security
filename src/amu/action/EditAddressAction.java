package amu.action;

import amu.Authentication;
import amu.Utils;
import amu.database.AddressDAO;
import amu.model.Address;
import amu.model.Customer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class EditAddressAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");
        List<String> messages = new ArrayList<String>();
        request.setAttribute("messages", messages);
        
        //handle redirect errors
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
        
        int addrID = -1;
        try {
        	addrID = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {}//ignore
        
        //invalid ID
        if (addrID < 0) {
        	return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
        }

        AddressDAO addressDAO = new AddressDAO();
        Address address = addressDAO.read(addrID);
        
        //address doesn't belong to the user
        if(address.getCustomer().getId()!=customer.getId()){
        	 return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
        }
        
        if (request.getMethod().equals("POST")) {
            //Validate input
            
            //store messages from validateInputLengths in the list
            Map<String, String> messages2 = new HashMap<String, String>();
            boolean lengthsPassed = Utils.validateInputLengths(request, messages2);
            int size = messages2.size();
            if (size != 0) {
            	messages.addAll(messages2.values());
            }
            
            boolean addressOk = false;
            if (lengthsPassed) {
            	addressOk = Utils.validateAlphaNum(request.getParameter("address"));
            	if (!addressOk) {
            		messages.add("Address contains illegal characters");
            	}
            }
			if (!lengthsPassed || !addressOk) {
            	return new ActionResponse(ActionResponseType.FORWARD, "editAddress");
            }
            
            // Validate password
            String password = request.getParameter("password");
            boolean matches = Authentication.verifyPassword(customer, password);
            if (!matches) {
            	messages.add("Authentication error. Please try again.");
            	ActionResponse r = new ActionResponse(ActionResponseType.REDIRECT, "editAddress");
            	r.addParameter("id", "" + addrID);
            	r.addParameter("e", "auth");
            	return r;
            }
            
            //input ok, proceed

            address.setAddress(request.getParameter("address"));
            
            if (addressDAO.edit(address)) {
                return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
            }

            messages.add("An error occured.");
        }

        // (request.getMethod().equals("GET")) 
        request.setAttribute("address", address);
        return new ActionResponse(ActionResponseType.FORWARD, "editAddress");
    }

}
