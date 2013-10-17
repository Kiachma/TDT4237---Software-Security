package amu.action;

import amu.Authentication;
import amu.Utils;
import amu.database.CustomerDAO;
import amu.model.Customer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
            String salt = customerDAO.getSalt(customer.getId());
            if (!Authentication.hashPassword(oldPassword, salt).equals(customer.getPassword())) {
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
            try {
                salt = Authentication.generateSalt();
                customer.setSalt(salt);
                customer.setPassword(Authentication.hashPassword(password[0], salt));
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ChangePasswordAction.class.getName()).log(Level.SEVERE, null, ex);
                messages.add("An error occured.");
                return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
            }
            if (customerDAO.edit(customer) == false) {
                messages.add("An error occured.");
                return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
            }

            // Email change successful, return to viewCustomer
            return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");

        }

        // (request.getMethod().equals("GET")) 
        return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
    }
}
