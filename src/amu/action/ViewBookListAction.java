
package amu.action;

import amu.database.BookDAO;
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
        int booklistID = Integer.parseInt(request.getParameter("id"));
        
        //if delete parameter is passed, delete the corresponding book from this list
        String delete_isbn = null;
        delete_isbn = request.getParameter("delete_isbn");
        System.out.println(delete_isbn);
        if(delete_isbn!=null){
            int title_id = new BookDAO().findByISBN(delete_isbn).getTitle().getId();
            booklistDAO.removeBookFromList(title_id, booklistID);
        }
        BookList booklist = booklistDAO.getListByID(booklistID, customer.getId());
        if(booklist!= null){
            request.setAttribute("booklist", booklist);
            request.setAttribute("booklistkey", booklistID);
        }
        
        return new ActionResponse(ActionResponseType.FORWARD, "viewBookList");
    }
    
    
}
