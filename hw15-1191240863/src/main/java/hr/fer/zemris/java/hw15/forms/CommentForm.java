package hr.fer.zemris.java.hw15.forms;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;

/**
 * Comment form.
 * 
 * @author Bruno Iljazović
 */
public class CommentForm extends AbstractForm {
	
	/**
	 * Creates the blog comment object from this form.
	 *
	 * @param entry the parent entry of this comment
	 * @return the new blog comment
	 */
	public BlogComment createComment(BlogEntry entry) {
		BlogComment comment = new BlogComment();

		comment.setBlogEntry(entry);
		comment.setMessage(getValue("comment"));
		comment.setPostedOn(new Date());
		comment.setUserEmail(getValue("email"));
		
		return comment;
	}

	@Override
	public void validate() {
		if (getValue("comment").isEmpty()) {
			errors.put("comment", "Comment is required.");
		}
		if (getValue("email").isEmpty()) {
			errors.put("email", "Email is required.");
		} else if (!emailPattern.matcher(getValue("email")).matches()) {
			errors.put("email", "Email is not valid.");
		}
	}

	@Override
	public void fromHttpRequest(HttpServletRequest req) {
		setValue("comment", req.getParameter("comment"));
		if (req.getSession().getAttribute("current.user.id") != null) {
			String userNick = (String)req.getSession().getAttribute("current.user.nick");
			setValue("email", DAOProvider.getDAO().getBlogUser(userNick).getEmail() );
		} else {
			setValue("email", req.getParameter("email"));
		}
	}
}
