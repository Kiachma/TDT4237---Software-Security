package amu.action;

import amu.Mailer;
import amu.Utils;
import amu.model.Customer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class CustomerSupportAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "customerSupport");
            return actionResponse;
        }
        
        if (request.getMethod().equals("POST")) {
            String subject = request.getParameter("subject");
            String content = request.getParameter("content");
            if (Utils.validateAlphaNum(subject) && Utils.validateAlphaNum(content) ){
                Mailer.send(request.getParameter("department"), 
                    request.getParameter("subject"), 
                    request.getParameter("content"), 
                    request.getParameter("fromAddr"), 
                    request.getParameter("fromName"));
                return new ActionResponse(ActionResponseType.REDIRECT, "customerSupportSuccessful");
            }
            // TODO: Send receipt to customer
        } 

        return new ActionResponse(ActionResponseType.FORWARD, "customerSupport");
    }
    
}
