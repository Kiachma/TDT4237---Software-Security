package amu.action;

import amu.database.*;
import amu.model.Book;
import amu.model.Customer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

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
            HashMap<String, String> messages = new HashMap<String, String>();
            request.setAttribute("messages", messages);
            int book_title_id = Integer.parseInt(request.getParameter("book_title_id"));
            String book_isbn = request.getParameter("book_isbn");
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null) {
                ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
                actionResponse.addParameter("from", "viewBook");
                return actionResponse;
            }
            String selected_booklist = request.getParameter("booklist_selection");
            System.out.println("Selected booklist: "+selected_booklist);
            System.out.println("Selected book: "+book_isbn);
            int booklistID=Integer.parseInt(selected_booklist);
            
            BookListDAO booklistDAO = new BookListDAO();
            book = bookDAO.findByISBN(book_isbn);
            if(book!=null){
                request.setAttribute("book", book);
            }
            if(booklistDAO.addBookToList(book_title_id, booklistID)){
                messages.put("added", "Book was added to list");
                return new ActionResponse(ActionResponseType.FORWARD, "viewBook");
            }else{
                messages.put("failed", "Book already exists in this list");
                return new ActionResponse(ActionResponseType.FORWARD, "viewBook");
            }
            
        }
        
        return new ActionResponse(ActionResponseType.FORWARD, "viewBook");
    }
}
