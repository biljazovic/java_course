package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.forms.RegisterForm;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Handles registration of new users.
 * 
 * @author Bruno Iljazović
 */
@WebServlet("/servleti/register")
public class RegisterServlet extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("form", new RegisterForm());
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RegisterForm form = new RegisterForm();
		form.fromHttpRequest(req);
		
		form.validate();
		if (form.hasErrors()) {
			req.setAttribute("form", form);
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
			return;
		}
		
		BlogUser user = form.createUser();
		
		DAOProvider.getDAO().addBlogUser(user);
		
		req.getSession().setAttribute("current.user.id", user.getId());
		req.getSession().setAttribute("current.user.fn", user.getFirstName());
		req.getSession().setAttribute("current.user.ln", user.getLastName());
		req.getSession().setAttribute("current.user.nick", user.getNick());

		resp.sendRedirect(req.getContextPath() + "/servleti/main");
	}
}
