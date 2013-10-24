/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu.action;

import amu.Utils;
import amu.database.BookListDAO;
import amu.model.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author JohnArne
 */
public class AddBookListAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "addBookList");
            return actionResponse;
        }
        
        if (request.getMethod().equals("POST")) {

            Map<String, String> messages = new HashMap<String, String>();
            request.setAttribute("messages", messages);
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            
            //Validate input
            boolean lengthsPassed = Utils.validateInputLengths(request, messages);
            boolean titlePassedAlpha = false;
            boolean descriptionPassedAlpha = false;
            if (lengthsPassed) {
            	titlePassedAlpha = Utils.validateAlphaNum(title);
                descriptionPassedAlpha = Utils.validateAlphaNum(description);
            	if (!titlePassedAlpha) {
                    messages.put("title", "Title field contains illegal characters");
            	}
                if (!descriptionPassedAlpha) {
                    messages.put("description", "Description field contains illegal characters");
                }
            }
            if (!lengthsPassed || !titlePassedAlpha || !descriptionPassedAlpha) {
                return new ActionResponse(ActionResponseType.FORWARD, "addBookList");
            }
            
            //create booklist and add it in database
            BookList booklist = new BookList();
            booklist.setTitle(title);
            booklist.setDescription(description);
            BookListDAO booklistDAO = new BookListDAO();
            if(booklistDAO.addBooklist(booklist, customer)){
                return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
            }
            messages.put("error", "an error occurred");
        }
        
        return new ActionResponse(ActionResponseType.FORWARD, "addBookList");
    }
    
}
