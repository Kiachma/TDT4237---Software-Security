/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author eaura
 */
public class Utils {

    public static boolean validateInputLengths(HttpServletRequest request, Map<String, String> messages) {
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String field = names.nextElement();
            String input = request.getParameter(field);
            if (input == null || input.length() < 1) {
                messages.put(field, field + " cannot be blank.");//TODO Phase out
                messages.put("error", field + " cannot be blank.");
                return false;
            } else if (input.length() >= Config.MAX_INPUT_LENGTH) {
                messages.put(field, field + " can not be longer than " + Config.MAX_INPUT_LENGTH + " characters.");
                messages.put("error", field + " can not be longer than " + Config.MAX_INPUT_LENGTH + " characters.");//TODO Phase out
                return false;
            } else {
                messages.put(field, null);
            }
        }
        return true;
    }

    public static boolean validateAlphaNum(String input) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9\\såäöæø]");
        boolean hasSpecialChar = p.matcher(input).find();
        if (hasSpecialChar) {
            return false;
        }
        return true;
    }
}
