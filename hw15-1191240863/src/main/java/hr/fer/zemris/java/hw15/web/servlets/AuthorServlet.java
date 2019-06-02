package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.forms.CommentForm;
import hr.fer.zemris.java.hw15.forms.EntryForm;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * This servlet handles display of user's blog entries, requests for editing and
 * creating new blogs, and displaying specific blog entry.
 * <p>
 * In the url the nick of the user is provided and if the currently logged in
 * user is that one, editing and creating blog entries is enabled.
 * <p>
 * Anonymous user can only see all entries and comment on them.
 * 
 * @author Bruno Iljazović
 */
@WebServlet("/servleti/author/*")
public class AuthorServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Additional url path ({@link HttpServletRequest#getPathInfo} split by '/'. */
	private String[] infos;
	
	/** User with the nick from the path. */
	private BlogUser user;
	
	/**
	 * Prepares the requests by validating the given nick, and checking permission
	 * for editing and creating new entries.
	 *
	 * @param req
	 *            the request
	 * @param resp
	 *            the response
	 * @return false if the request was already handled (e.g. an error occurred)
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ServletException
	 *             Signals that a servlet exception has occurred.
	 */
	private boolean prepare(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		String info = req.getPathInfo();
		if (info == null) {
			resp.sendRedirect(req.getContextPath() + "/servleti/main");
			return false;
		}
		infos = req.getPathInfo().substring(1).split("/");
		String nick = infos[0];
		req.setAttribute("nick", nick);
		
		user = DAOProvider.getDAO().getBlogUser(nick);
		if (user == null) {
			req.setAttribute("errorMessage", "User '" + nick + "' doesn't exist.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return false;
		}
		
		if (infos.length == 2 && (infos[1].equals("new") || infos[1].equals("edit"))) {
			if (!nick.equals(req.getSession().getAttribute("current.user.nick"))) {
				req.setAttribute("errorMessage", "You have no access to this page.");
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
				return false;
			}
		}
		
		if (infos.length > 2) {
			req.setAttribute("errorMessage", "Invalid path.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return false;
		}

		return true;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!prepare(req, resp)) return;

		if (infos.length == 1) {
			req.setAttribute("entries", user.getEntries());
			req.getRequestDispatcher("/WEB-INF/pages/userPage.jsp").forward(req, resp);
			return;
		}

		String type = infos[1];

		if (type.equals("new")) {
			req.setAttribute("form", new EntryForm());
			req.getRequestDispatcher("/WEB-INF/pages/entryEdit.jsp").forward(req, resp);
			return;
		}

		if (type.equals("edit")) {
			BlogEntry entry = getEntry(req, resp, req.getParameter("entryid"));
			if (entry == null) return;
			EntryForm form = new EntryForm();
			form.setValue("entryText", entry.getText());
			form.setValue("entryTitle", entry.getTitle());
			req.setAttribute("form", form);
			req.getRequestDispatcher("/WEB-INF/pages/entryEdit.jsp").forward(req, resp);
			return;
		}

		BlogEntry entry = getEntry(req, resp, type);
		if (entry == null) return;

		req.setAttribute("form", new EntryForm());
		req.setAttribute("entry", entry);
		req.getRequestDispatcher("/WEB-INF/pages/entryPage.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!prepare(req, resp)) return;
		if (infos.length == 2) {
			String type = infos[1];
			if (type.equals("new")) {
				doNewPost(req, resp);
				return;
			}
			if (type.equals("edit")) {
				doEditPost(req, resp);
				return;
			}
			doCommentPost(req, resp, type);
			return;
		}
	}
	
	/**
	 * Handles the post request for new blog entry.
	 *
	 * @param req the request
	 * @param resp the response
	 * @throws ServletException servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void doNewPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		EntryForm form = new EntryForm();
		form.fromHttpRequest(req);
		form.validate();

		if (form.hasErrors()) {
			req.setAttribute("form", form);
			req.getRequestDispatcher("/WEB-INF/pages/entryEdit.jsp").forward(req, resp);
			return;
		}

		user.getEntries().add(form.createEntry(user));

		resp.sendRedirect(req.getContextPath() + "/servleti/author/" + user.getNick());
	}
	
	/**
	 * Handles the post request for editing the blog entry
	 *
	 * @param req the request
	 * @param resp the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void doEditPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		EntryForm form = new EntryForm();
		form.fromHttpRequest(req);
		form.validate();

		if (form.hasErrors()) {
			req.setAttribute("form", form);
			req.getRequestDispatcher("/WEB-INF/pages/entryEdit.jsp").forward(req, resp);
			return;
		}

		BlogEntry entry = getEntry(req, resp, req.getParameter("entryid"));
		if (entry == null) return;

		entry.setLastModifiedAt(new Date());
		entry.setText(form.getValue("entryText"));
		entry.setTitle(form.getValue("entryTitle"));

		resp.sendRedirect(req.getContextPath() + "/servleti/author/" + user.getNick() + "/" + entry.getId());
	}

	/**
	 * Handles post request for adding and editing comments.
	 *
	 * @param req the request
	 * @param resp the response
	 * @param idString the id of the blog entry for this comment
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void doCommentPost(HttpServletRequest req, HttpServletResponse resp, String idString) 
			throws ServletException, IOException {
		BlogEntry entry = getEntry(req, resp, idString);
		if (entry == null) return;

		CommentForm form = new CommentForm();
		form.fromHttpRequest(req);
		form.validate();

		if (form.hasErrors()) {
			req.setAttribute("form", form);
			req.setAttribute("entry", entry);
			req.getRequestDispatcher("/WEB-INF/pages/entryPage.jsp").forward(req, resp);
			return;
		}

		entry.getComments().add(form.createComment(entry));

		resp.sendRedirect(req.getContextPath() + req.getServletPath() + req.getPathInfo());
	}
	
	/**
	 * Gets the blog entry from its id.
	 *
	 * @param req the request
	 * @param resp the response
	 * @param idString the id of the entry
	 * @return the entry with the given id
	 * @throws ServletException servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private BlogEntry getEntry(HttpServletRequest req, HttpServletResponse resp, String idString)
			throws ServletException, IOException {
		Long entryid = null;
		try {
			entryid = Long.parseLong(idString);
		} catch(NumberFormatException ex) {
			req.setAttribute("errorMessage", "No entry id provided.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return null;
		}

		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(entryid);
		if (entry == null || !entry.getBlogUser().getId().equals(user.getId())) {
			req.setAttribute("errorMessage", "You have no access to this page.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return null;
		}

		return entry;
	}
}
