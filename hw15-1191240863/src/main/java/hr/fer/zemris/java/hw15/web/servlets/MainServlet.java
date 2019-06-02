package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.forms.LoginForm;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Handles the login process and displays the list of currently registered
 * users. New user registration is handled with {@link RegisterServlet}.
 *
 * @author Bruno Iljazović
 */
@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("form", new LoginForm());
		req.setAttribute("users", DAOProvider.getDAO().getListOfBlogUsers());
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		LoginForm form = new LoginForm();
		form.fromHttpRequest(req);
		form.validate();

		if (form.hasErrors()) {
			req.setAttribute("form", form);
			req.setAttribute("users", DAOProvider.getDAO().getListOfBlogUsers());
			req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
			return;
		}
		
		BlogUser user = DAOProvider.getDAO().getBlogUser(form.getValue("nick"));
		
		req.getSession().setAttribute("current.user.id", user.getId());
		req.getSession().setAttribute("current.user.fn", user.getFirstName());
		req.getSession().setAttribute("current.user.ln", user.getLastName());
		req.getSession().setAttribute("current.user.nick", user.getNick());

		resp.sendRedirect(req.getContextPath() + "/servleti/main");
	}
}
