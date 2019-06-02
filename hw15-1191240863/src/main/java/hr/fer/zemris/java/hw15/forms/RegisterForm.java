package hr.fer.zemris.java.hw15.forms;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.Util;
import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Register form.
 * 
 * @author Bruno Iljazović
 */
public class RegisterForm extends AbstractForm {
	
	/**
	 * Creates new user from this form.
	 * 
	 * @return the new user.
	 */
	public BlogUser createUser() {
		String passwordHash = Util.getPasswordHash(getValue("password"));
		BlogUser user = new BlogUser();
		user.setFirstName(getValue("firstName"));
		user.setLastName(getValue("lastName"));
		user.setEmail(getValue("email"));
		user.setNick(getValue("nick"));
		user.setPasswordHash(passwordHash);
		return user;
	}

	@Override
	public void validate() {
		if (getValue("firstName").isEmpty()) {
			errors.put("firstName", "First name is required.");
		}
		if (getValue("lastName").isEmpty()) {
			errors.put("lastName", "Last name is required.");
		}
		if (getValue("email").isEmpty()) {
			errors.put("email", "Email is required.");
		} else if (!emailPattern.matcher(getValue("email")).matches()) {
			errors.put("email", "Email is not valid.");
		}
		if (getValue("password").isEmpty()) {
			errors.put("password", "Password is required.");
		}
		if (getValue("nick").isEmpty()) {
			errors.put("nick", "Nick is required.");
		} else if (!nickPattern.matcher(getValue("nick")).matches()) {
			errors.put("nick", "Nick is of invalid format.");
		} else {
			BlogUser user = DAOProvider.getDAO().getBlogUser(getValue("nick"));
			if (user != null) {
				errors.put("nick", "User with this nick already exists.");
			}
		}
		if (!errors.isEmpty()) {
			setValue("password", null);
		}
	}

	@Override
	public void fromHttpRequest(HttpServletRequest req) {
		setValue("firstName", req.getParameter("firstName"));
		setValue("lastName", req.getParameter("lastName"));
		setValue("email", req.getParameter("email"));
		setValue("nick", req.getParameter("nick"));
		setValue("password", req.getParameter("password"));
	}

}
