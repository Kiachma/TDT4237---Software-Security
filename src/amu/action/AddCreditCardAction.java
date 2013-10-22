package amu.action;

import amu.Authentication;
import amu.Utils;
import amu.database.CreditCardDAO;
import amu.model.CreditCard;
import amu.model.Customer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class AddCreditCardAction implements Action {

    private static final String CARDHOLDER_NAME = "cardholderName";
	private Map<String, String> messages = null;
	private Customer customer;

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "addCreditCard");
            return actionResponse;
        }

        if (request.getMethod().equals("POST")) {
            messages = new HashMap<>();
            request.setAttribute("messages", messages);
            if (!validateInput(request)) {
                initializeDate(request);
                return new ActionResponse(ActionResponseType.FORWARD, "addCreditCard");
            }


            Calendar expiryDate = Calendar.getInstance();
            expiryDate.set(Integer.parseInt(request.getParameter("expiryYear")), Integer.parseInt(request.getParameter("expiryMonth")), 1);

            CreditCardDAO creditCardDAO = new CreditCardDAO();
            CreditCard creditCard = new CreditCard(
                    customer,
                    request.getParameter("creditCardNumber"),
                    expiryDate,
                    request.getParameter(CARDHOLDER_NAME));

            Map<String, String> values = new HashMap<>();
            request.setAttribute("values", values);
            values.put("creditCardNumber", request.getParameter("creditCardNumber"));
            values.put("expiryDate", request.getParameter("expiry"));
            values.put(CARDHOLDER_NAME, request.getParameter(CARDHOLDER_NAME));

            if (creditCardDAO.add(creditCard)) {
                return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
            }

            messages.put("error", "An error occured.");
        }
        initializeDate(request);

        return new ActionResponse(ActionResponseType.FORWARD, "addCreditCard");
    }

    private boolean validateInput(HttpServletRequest request) {
        boolean inputLengthsOK = Utils.validateInputLengths(request, messages);
        boolean nameAlphanum = Utils.validateAlphaNum(request.getParameter(CARDHOLDER_NAME));
        if (!nameAlphanum) {
        	messages.put("error", "Name field cannot contain special characters");
        }
        boolean passwordOK = false;
        if (inputLengthsOK && nameAlphanum) {
        	passwordOK = Authentication.verifyPassword(customer, request.getParameter("password"));
        	if (!passwordOK) {
        		messages.put("error", "Authentication error");
        	}
        }
		return inputLengthsOK && nameAlphanum && passwordOK && validateCreditCard(request);
    }

    private boolean validateCreditCard(HttpServletRequest request) {
        /*
         * ^                 # start-of-string
         (?=.*[0-9])       # a digit must occur at least once
         (?=.*[a-z])       # a lower case letter must occur at least once
         (?=.*[A-Z])       # an upper case letter must occur at least once
         (?=.*[@#$%^&+=])  # a special character must occur at least once
         .{8,42}             # anything, at least eight places though
         $                 # end-of-string
         */
        if (!request.getParameter("creditCardNumber").matches("^(?=.*[0-9]).{1,16}$")) {
            messages.put("creditCardNumber", "Credit card has to be between 1 and 16 digits");
            return false;
        }
        return true;
    }

    public void initializeDate(HttpServletRequest request) {
        // (request.getMethod().equals("GET"))
        Calendar calendar = new GregorianCalendar();

        List<String> years = new ArrayList<>();
        request.setAttribute("years", years);
        for (Integer offset = 0; offset < 10; offset++) {
            years.add(Integer.valueOf(calendar.get(Calendar.YEAR) + offset).toString());
        }

        Map<String, String> months = new HashMap<>();
        request.setAttribute("months", months);
        for (Integer month = 0; month < 12; month++) {
            months.put(month.toString(), Integer.valueOf(month - 1).toString());
        }
    }
}
