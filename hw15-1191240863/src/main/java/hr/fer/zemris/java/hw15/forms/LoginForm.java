package hr.fer.zemris.java.hw15.forms;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.Util;
import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Login form.
 * 
 * @author Bruno Iljazović
 */
public class LoginForm extends AbstractForm {

	@Override
	public void validate() {
		String nick = getValue("nick");
		String password = getValue("password");
		if (nick.isEmpty()) {
			errors.put("nick", "Nick is required.");
			return;
		}
		if (password.isEmpty()) {
			errors.put("password", "Password is required.");
			return;
		}
		BlogUser user = DAOProvider.getDAO().getBlogUser(nick);
		if (user == null) {
			setValue("nick", null);
			setValue("password", null);
			errors.put("nick", "User with the given nick doesn't exist.");
			return;
		}
		String passwordHash = Util.getPasswordHash(password);
		if (!passwordHash.equals(user.getPasswordHash())) {
			setValue("password", null);
			errors.put("password", "Incorrect password.");
		}
	}
	
	@Override
	public void fromHttpRequest(HttpServletRequest req) {
		setValue("nick", req.getParameter("nick"));
		setValue("password", req.getParameter("password"));
	}
}
