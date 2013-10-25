/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu.action;

import amu.database.BookListDAO;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JohnArne
 */
public class ViewPublicBookListsAction implements Action{

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BookListDAO booklistDAO = new BookListDAO();
        HashMap<String, String> booklists = new HashMap<String,String>();
        booklists = booklistDAO.getPublicBookListsTitles();
        request.setAttribute("booklists", booklists);
        return new ActionResponse(ActionResponseType.FORWARD, "viewPublicBookLists");
    }
    
}
