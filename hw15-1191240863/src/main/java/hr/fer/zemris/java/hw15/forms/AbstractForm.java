package hr.fer.zemris.java.hw15.forms;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * Abstract form that implements methods that all forms share - modifying
 * values, modifying errors, checking for errors.
 * 
 * @author Bruno Iljazović
 */
public abstract class AbstractForm {

	/** The email pattern. */
	protected Pattern emailPattern = Pattern.compile(".+@.+\\..+");

	/** The nick pattern. */
	protected Pattern nickPattern = Pattern.compile("[a-zA-Z0-9]+([_.-]?[a-zA-Z0-9]+)*");

	/** The values. */
	protected Map<String, String> values = new HashMap<>();
	
	/** The errors. */
	protected Map<String, String> errors = new HashMap<>();
	
	/**
	 * Validates this form.
	 */
	public abstract void validate();
	
	/**
	 * Fills values from http request.
	 *
	 * @param req the request
	 */
	public abstract void fromHttpRequest(HttpServletRequest req);
	
	/**
	 * Checks for errors.
	 *
	 * @return whether there are any errors
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	/**
	 * Checks for specific error
	 *
	 * @param value the value to check error for
	 * @return whether there is an error with the given value as key
	 */
	public boolean hasError(String value) {
		return errors.containsKey(value);
	}
	
	/**
	 * Gets the specific error.
	 *
	 * @param key the error key
	 * @return the error with the given key, or null if there is none
	 */
	public String getError(String key) {
		return errors.get(key);
	}
	
	/**
	 * Gets the value with the given key.
	 *
	 * @param key the key of the value
	 * @return the value from the given key
	 */
	public String getValue(String key) {
		String value = values.get(key);
		return value == null ? "" : value;
	}
	
	/**
	 * Sets the value as (key, value) pair.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void setValue(String key, String value) {
		values.put(key, value);
	}
}
