/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu.action;

import amu.database.BookListDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import amu.model.*;
import java.util.HashMap;
import amu.database.*;

/**
 *
 * @author JohnArne
 */
class PublishBookListAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionResponse actionresponse = new ActionResponse(ActionResponseType.FORWARD, "viewBookList");
        int booklistkey = Integer.parseInt(request.getParameter("booklistkey"));
        request.setAttribute("booklistkey", booklistkey);
        HashMap<String, String> messages = new HashMap<String, String>();
        request.setAttribute("messages", messages);
        
        Customer customer = (Customer)request.getSession().getAttribute("customer");
        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "viewBookList");
            return actionResponse;
        }
        request.setAttribute("booklist", new BookListDAO().getListByID(booklistkey, customer.getId()));
        
        BookListDAO booklistDAO = new BookListDAO();
        if(!booklistDAO.makeBookListPublic(booklistkey)){
            return new ActionResponse(ActionResponseType.FORWARD, "viewCustomer");
        }
        
        return actionresponse;
    }
    
    
}
