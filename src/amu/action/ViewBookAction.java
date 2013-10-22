package amu.action;

import amu.database.*;
import amu.model.Book;
import amu.model.Customer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class ViewBookAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.findByISBN(request.getParameter("isbn"));
        
        if (book != null) {
            request.setAttribute("book", book);
        }
        

        
        if (request.getMethod().equals("POST")) {
            HttpSession session = request.getSession(true);
            int book_id = Integer.parseInt(request.getParameter("book_id"));
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null) {
                ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
                actionResponse.addParameter("from", "viewBook");
                return actionResponse;
            }
            String selected_booklist = request.getParameter("booklist_selection");
            System.out.println("Selected booklist: "+selected_booklist);
            System.out.println("Selected book: "+book_id);
            int booklistID=Integer.parseInt(selected_booklist);
            
            BookListDAO booklistDAO = new BookListDAO();
            if(booklistDAO.addBookToList(book_id, booklistID)){
                book = bookDAO.findByID(book_id);
                if(book!=null){
                    request.setAttribute("book", book);
                }
                return new ActionResponse(ActionResponseType.FORWARD, "viewBook");
            }
            
        }
        
        return new ActionResponse(ActionResponseType.FORWARD, "viewBook");
    }
}
