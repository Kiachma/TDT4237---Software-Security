package amu.action;

import amu.database.AddressDAO;
import amu.database.CreditCardDAO;
import amu.model.Address;
import amu.model.Cart;
import amu.model.CreditCard;
import amu.model.Customer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class SelectPaymentOptionAction implements Action {

    private Map<String, String> messages = new HashMap<String, String>();

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        Customer customer = (Customer) session.getAttribute("customer");

        CreditCardDAO creditCardDAO = new CreditCardDAO();
        List<CreditCard> creditCards = creditCardDAO.browse(customer);
        
        request.setAttribute("creditCards", creditCards);
        
        if (cart == null) {
            return new ActionResponse(ActionResponseType.REDIRECT, "viewCart");
        }
        

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "selectPaymentOption");
            return actionResponse;
        }

        if (cart.getShippingAddress() == null) {
            return new ActionResponse(ActionResponseType.REDIRECT, "selectShippingAddress");
        }
      
        request.setAttribute("messages", messages);
        // Handle credit card selection submission
        if (request.getMethod().equals("POST")) {
            if (request.getParameter("creditCardID") == null) {
                messages.put("Id", "Please choose a card");
                return new ActionResponse(ActionResponseType.REDIRECT, "selectPaymentOption");
            }
            CreditCard creditCard =null;
            for(CreditCard cc : creditCards){
                if(cc.getId()==Integer.parseInt(request.getParameter("creditCardID"))){
                    creditCard=cc;
                }
            }

            if (creditCard==null) {
                messages.put("Id", "Please choose a valid card");
                return new ActionResponse(ActionResponseType.REDIRECT, "selectPaymentOption");
            }
            cart.setCreditCard(creditCard);

            return new ActionResponse(ActionResponseType.REDIRECT, "reviewOrder");
        }

        // Else GET request
        return new ActionResponse(ActionResponseType.FORWARD, "selectPaymentOption");
    }
}
