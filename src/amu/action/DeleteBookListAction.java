/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu.action;

import amu.Authentication;
import amu.database.BookListDAO;
import amu.model.BookList;
import amu.model.Customer;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author JohnArne
 */
public class DeleteBookListAction implements Action{
    
    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        List<String> messages = new ArrayList<String>();
        request.setAttribute("messages", messages);

        Customer customer = (Customer) session.getAttribute("customer");

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
            actionResponse.addParameter("from", "viewBookList"); 
            return actionResponse;
        }

        BookListDAO booklistDAO = new BookListDAO();
        BookList booklist;

        int booklistID = -1;
        try {
                booklistID = Integer.parseInt(request.getParameter("booklistkey"));
        } catch (Exception e) {}//ignore
        System.out.println("BOOKLIST ID: "+booklistID);
        request.setAttribute("booklistkey", booklistID);
        
        if (request.getMethod().equals("POST")) {

         // Validate password
            String password = request.getParameter("password");
            boolean matches = Authentication.verifyPassword(customer, password);
            if (!matches) {
                ActionResponse r = new ActionResponse(ActionResponseType.REDIRECT, "deleteBookList");
                r.addParameter("booklistkey", "" + booklistID);
                r.addParameter("e", "auth");
                return r;
            }
            
            if (booklistID >= 0 && booklistDAO.removeBookList(booklistID)) {
                return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
            } else {
                messages.add("An error occured.");
                return new ActionResponse(ActionResponseType.FORWARD, "deleteBookList");
            }
        }

        // (request.getMethod().equals("GET")) 
        
        if (booklistID < 0) {
            ActionResponse actionresponse = new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
            return actionresponse;
        }
        request.setAttribute("booklist", booklistDAO.getListByID(booklistID));
        return new ActionResponse(ActionResponseType.FORWARD, "deleteBookList");
    }
}
