package hr.fer.zemris.java.hw15.forms;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Blog entry form.
 * 
 * @author Bruno Iljazović
 */
public class EntryForm extends AbstractForm {

	@Override
	public void validate() {
		if (getValue("entryTitle").isEmpty()) {
			errors.put("entryTitle", "Title is required.");
			return;
		}
		if (getValue("entryText").isEmpty()) {
			errors.put("entryText", "Text is required");
			return;
		}
	}
	
	/**
	 * Creates the new blog entry object from this form.
	 *
	 * @param user the parent user.
	 * @return new blog entry object
	 */
	public BlogEntry createEntry(BlogUser user) {
		BlogEntry entry = new BlogEntry();

		entry.setBlogUser(user);
		entry.setCreatedAt(new Date());
		entry.setLastModifiedAt(entry.getCreatedAt());
		entry.setText(getValue("entryText"));
		entry.setTitle(getValue("entryTitle"));

		return entry;
	}

	@Override
	public void fromHttpRequest(HttpServletRequest req) {
		setValue("entryTitle", req.getParameter("entryTitle"));
		setValue("entryText", req.getParameter("entryText"));
	}

}
