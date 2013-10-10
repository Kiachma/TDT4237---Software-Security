/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amu;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author eaura
 */
public class Utils {
	private static HashMap<String, String> friendlyFieldNames;
	
	//static initializer: run methods when class is first accessed
	static {
		fillFriendlyFieldnames();
	}
	
	/**
	 * Get a field name suited for displaying to end users
	 * @param field Name of the field
	 * @return The same field if a friendly version isn't found
	 */
	public static String getFriendlyFieldName(String field) {
		if (field != null && friendlyFieldNames.containsKey(field)) {
			return friendlyFieldNames.get(field);
		}
		return field;
	}
	
	/**
	 * Add display names for fields to the list. This should read from a config file.
	 */
	private static void fillFriendlyFieldnames() {
		if (friendlyFieldNames != null) {
			throw new RuntimeException("Friendly field names already initialized");
		}
		friendlyFieldNames = new HashMap<>();
		//TODO: Add config file processing
		//general idea:
		friendlyFieldNames.put("recaptcha_response_field", "CAPTCHA response");
	}

    public static boolean validateInputLengths(HttpServletRequest request, Map<String, String> messages) {
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String field = names.nextElement();
            String friendlyField = getFriendlyFieldName(field);
            String input = request.getParameter(field);
            if (input == null || input.length() < 1) {
                messages.put(field, friendlyField + " cannot be blank.");//TODO Phase out
                messages.put("error", friendlyField + " cannot be blank.");
                return false;
            } else if (input.length() >= Config.MAX_INPUT_LENGTH) {
                messages.put(field, friendlyField + " can not be longer than " + Config.MAX_INPUT_LENGTH + " characters.");
                messages.put("error", friendlyField + " can not be longer than " + Config.MAX_INPUT_LENGTH + " characters.");//TODO Phase out
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

    public static boolean validatePassword(String password) {
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$")) {
            return false;
        }
        return true;
    }
}
