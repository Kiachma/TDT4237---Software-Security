package amu.action;

import amu.Authentication;
import amu.Config;
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

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

class RegisterCustomerAction implements Action {

    private Map<String, String> messages = new HashMap<String, String>();

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (request.getMethod().equals("POST")) {
            CustomerDAO customerDAO = new CustomerDAO();
            Customer customer = customerDAO.findByEmail(request.getParameter("email"));
            
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
       boolean passwordOK=Utils.validatePassword(request.getParameter("password"));
       if(passwordOK){
                      messages.put("password", null);

       }else{
           messages.put("password", "password must be between 8 and 40 charactes long and contain: a special character(@#$%^&+=), an upper case letter, a lower case letter and a digit ");
       }
        return Utils.validateInputLengths(request, messages) && validateEmail(request)
                && validateAlphaNum(request, messages) && passwordOK && validateCaptcha(request);

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
        customer.setPassword(Authentication.hashPassword(request.getParameter("password")));
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
    
    private boolean validateCaptcha(HttpServletRequest request) {

		String remoteAddr = request.getRemoteAddr();
		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		reCaptcha.setPrivateKey(Config.RECAPTCHA_PRIVATE_KEY);

		String challenge = request.getParameter("recaptcha_challenge_field");
		String uresponse = request.getParameter("recaptcha_response_field");
        if(challenge!=null && uresponse!=null){
            ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr,
                            challenge, uresponse);
            boolean valid = reCaptchaResponse.isValid();
            if (valid) return true;
        }
        messages.put("captcha", "CAPTCHA challenge failed");       
                
        return false;
	}
}
