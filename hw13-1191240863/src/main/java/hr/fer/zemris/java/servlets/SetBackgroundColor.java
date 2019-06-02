package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets the value of the attribute that is used for background color of all
 * pages in this web application to the color given in request parameters.
 * 
 * @author Bruno Iljazović
 */
@WebServlet("/setcolor")
public class SetBackgroundColor extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		String color = req.getParameter("bgcolor");
		if (color == null || !color.matches("[0-9A-Fa-f]{6}")) {
			color = "FFFFFF";
		}
		req.getSession().setAttribute("pickedBgCol", color);
		System.out.println("Set color to " + color);
		req.getRequestDispatcher("/colors.jsp").forward(req, resp);
	}
}
