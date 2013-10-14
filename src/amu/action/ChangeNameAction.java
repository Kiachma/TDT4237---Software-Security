package amu.action;

import amu.database.CustomerDAO;
import amu.model.Customer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

class ChangeNameAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "changeName");
            return actionResponse;
        }

        if (request.getMethod().equals("POST")) {

            Map<String, String> messages = new HashMap<String, String>();
            request.setAttribute("messages", messages);
            
            String name = request.getParameter("name");
            if(validateName(name)){
                customer.setName(name);   
            }else{
                messages.put("illegalName", "Illegal name");
            }

            CustomerDAO customerDAO = new CustomerDAO();
            if (customerDAO.edit(customer)) { // Customer name was successfully changed
                return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
            } else {
                messages.put("name", "Something went wrong here.");
                return new ActionResponse(ActionResponseType.FORWARD, "changeName");
            }
        }

        // (request.getMethod().equals("GET")) {
        return new ActionResponse(ActionResponseType.FORWARD, "changeName");
    }
    /*
     * Validate that the name does not contain any illegal characters
     * in regards to XSS
     * not allowed to contain 
     * &
     * <
     * >
     * "
     * '
     * /
     */
    private boolean validateName(String name){
        if(name==null){
            return false;
        }
        char[] badChars = new char[]{'&', '<', '>', '"', 39, 47};
        for(int i=0;i<badChars.length;i++){
            for(int j=0; j<name.length(); j++){
                if (badChars[i]==name.charAt(j)){
                    return false;
                }   
            }
            
        }
        return true;
    }
}
