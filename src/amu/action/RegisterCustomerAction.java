package amu.action;

import amu.Mailer;
import amu.Utils;
import amu.database.CustomerDAO;
import amu.model.Customer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class RegisterCustomerAction implements Action {

    private Map<String, String> messages = new HashMap<String, String>();

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (request.getMethod().equals("POST")) {
            return doPost(request);
        }

        // Else we show the register form
        return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
    }

    private ActionResponse doPost(HttpServletRequest request) {
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.findByEmail(request.getParameter("email"));
        request.setAttribute("messages", messages);
        if (!validateInput(request)) {
            return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
        }
        if (customer == null) {
            customer = populateCustomer(request);

            customer = customerDAO.register(customer);

            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "activateCustomer");

            activateUser(actionResponse, customer, request);

            return actionResponse;
        } else {
            return new ActionResponse(ActionResponseType.REDIRECT, "registrationError");
        }
    }

    private boolean validateInput(HttpServletRequest request) {
        return Utils.validateInputLengths(request, messages) && validateEmail(request)
                && validateAlphaNum(request, messages) && validatePassword(request);

    }

    private boolean validatePassword(HttpServletRequest request) {
        /*
         * ^                 # start-of-string
         (?=.*[0-9])       # a digit must occur at least once
         (?=.*[a-z])       # a lower case letter must occur at least once
         (?=.*[A-Z])       # an upper case letter must occur at least once
         (?=.*[@#$%^&+=])  # a special character must occur at least once
         .{8,42}             # anything, at least eight places though
         $                 # end-of-string
         */
        if (!request.getParameter("password").matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$")) {
            messages.put("password", "password must be between 8 and 40 charactes long and contain: a special character(@#$%^&+=), an upper case letter, a lower case letter and a digit ");
            return false;
        }
        messages.put("password", null);
        return true;
    }

    private boolean validateEmail(HttpServletRequest request) {
        try {
            InternetAddress emailAddr = new InternetAddress(request.getParameter("email"));
            emailAddr.validate();
        } catch (AddressException ex) {
            messages.put("email", "Not a valid email address ");
            return false;
        }
        messages.put("email", null);
        return true;
    }

    private Customer populateCustomer(HttpServletRequest request) {
        Customer customer = new Customer();
        customer.setEmail(request.getParameter("email"));
        customer.setName(request.getParameter("name"));
        customer.setPassword(CustomerDAO.hashPassword(request.getParameter("password")));
        customer.setActivationToken(CustomerDAO.generateActivationCode());
        return customer;
    }

    private void activateUser(ActionResponse actionResponse, Customer customer, HttpServletRequest request) {
        actionResponse.addParameter("email", customer.getEmail());

        StringBuilder sb = new StringBuilder();
        sb.append("Welcome to Amu-Darya, the really insecure bookstore!\n\n");
        sb.append("To activate your account, click <a href='http://");
        sb.append(request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
        sb.append(actionResponse.getURL() + actionResponse.getParameterString());
        sb.append("&activationToken=" + customer.getActivationToken());
        sb.append("'>here</a>, or use this activation code: " + customer.getActivationToken());

        Mailer.send(customer.getEmail(), "Activation required", sb.toString());
    }

    private boolean validateAlphaNum(HttpServletRequest request, Map<String, String> messages) {
        if(!Utils.validateAlphaNum(request.getParameter("name"))){
            messages.put("name", "name has to be alphanumeric");
            return false;
        }
        messages.put("name", null);
        return true;
        
    }
}
