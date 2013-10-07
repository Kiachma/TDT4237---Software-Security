/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu.action;

import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.registry.infomodel.User;

/**
 *
 * @author eaura
 */
class LogoutCustomerAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        session.setAttribute("customer", null);
        session.invalidate();
        return new ActionResponse(ActionResponseType.FORWARD, "loginCustomer");
    }
}
