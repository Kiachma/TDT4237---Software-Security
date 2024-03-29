package amu.action;

import amu.Authentication;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import amu.Config;
import amu.database.BookListDAO;
import amu.database.CaptchaCountDAO;
import amu.database.CustomerDAO;
import amu.model.Customer;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

class LoginCustomerAction implements Action {

    private CustomerDAO customerDAO;
    
    @Override
    public ActionResponse execute(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        Map<String, String> values = new HashMap<String, String>();
        
        request.setAttribute("values", values);
        if (ActionFactory.hasKey(request.getParameter("from"))) {
            values.put("from", request.getParameter("from"));
        }
        
        if (request.getMethod().equals("POST")) {
            return authenticateUser(request, values);
        }
        
        return new ActionResponse(ActionResponseType.FORWARD, "loginCustomer");
    }
    
    private ActionResponse authenticateUser(HttpServletRequest request,
            Map<String, String> values) {
        request.getSession().invalidate();
        HttpSession session = request.getSession(true);
        String email = request.getParameter("email");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Login attempt with username "+email);
        
        String passwd = request.getParameter("password");
        
        Map<String, String> messages = new HashMap<String, String>();
        request.setAttribute("messages", messages);
//        Integer count = (Integer) session.getAttribute("loginCount");
//        count = count == null ? 1 : count;
//        session.setAttribute("loginCount", count + 1);
//        
        
        CaptchaCountDAO captchacountDAO = new CaptchaCountDAO();
        int count = captchacountDAO.getCount(email);
        session.setAttribute("loginCount", count);
        captchacountDAO.incrementCount(email);
        if (count >= 3 && !validateCaptcha(request)) {
            messages.put("email", "Wrong captcha.");
            return new ActionResponse(ActionResponseType.FORWARD,
                    "loginCustomer");
        }
        
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.findByEmail(request
                .getParameter("email"));
        
        if (customer != null) {
            if (customer.getActivationToken() == null) {
                if (checkPasswd(passwd, customer)) {
                    BookListDAO booklistDAO = new BookListDAO();
                    HashMap booklists = booklistDAO.getBookListsTitles(customer);
                    session.setAttribute("booklists", booklists);
                    
                    captchacountDAO.resetCount(email);
                    session.setAttribute("customer", customer);
                    if (ActionFactory.hasKey(request.getParameter("from"))) {
                        return new ActionResponse(ActionResponseType.REDIRECT,
                                request.getParameter("from"));
                    }
                }
            } else { // customer.getActivationToken() != null
                return new ActionResponse(ActionResponseType.REDIRECT,
                        "activateCustomer");
            }
        }
        messages.put("email", "Incorrect login details.");

        // Forward to login form with error messages
        return new ActionResponse(ActionResponseType.FORWARD, "loginCustomer");
    }
    
    private boolean validateCaptcha(HttpServletRequest request) {
        
        String remoteAddr = request.getRemoteAddr();
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(Config.RECAPTCHA_PRIVATE_KEY);
        
        String challenge = request.getParameter("recaptcha_challenge_field");
        String uresponse = request.getParameter("recaptcha_response_field");
        if (challenge != null && uresponse != null) {
            ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr,
                    challenge, uresponse);
            return reCaptchaResponse.isValid();
        }
        
        return false;
    }
    
    private boolean checkPasswd(String passwd, Customer customer) {
        customerDAO = new CustomerDAO();
        return Authentication.verifyPassword(customer, passwd);
    }
}
