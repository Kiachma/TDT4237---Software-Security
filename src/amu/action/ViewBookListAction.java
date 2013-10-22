
package amu.action;

import amu.database.BookListDAO;
import amu.model.BookList;
import amu.model.Customer;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class ViewBookListAction implements Action{

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception{
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "viewBookList");
            return actionResponse;
        }
        
        BookListDAO booklistDAO = new BookListDAO();
        int booklistID = (int) Integer.parseInt(request.getParameter("id"));
        BookList booklist = booklistDAO.getListByID(booklistID);
        if(booklist!= null){
            request.setAttribute("booklist", booklist);
        }
        
        return new ActionResponse(ActionResponseType.FORWARD, "viewBookList");
    }
    
    
}
