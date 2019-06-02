package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.servlets.Util.Band;
import hr.fer.zemris.java.servlets.Util.StrAndInt;

/**
 * Generates data for a page where user can vote for their favorite band.
 * 
 * @author Bruno Iljazović
 */
@WebServlet("/glasanje")
public class GlasanjeServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String filename = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		Map<Integer, Band> bands = Util.readDefinitions(filename);
		if (bands == null) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		List<StrAndInt> nameAndIds = new ArrayList<>();
		
		for (Map.Entry<Integer, Band> entry : bands.entrySet()) {
			nameAndIds.add(new StrAndInt(entry.getValue().getName(), entry.getKey()));
		}
		nameAndIds.sort(null);
		
		req.setAttribute("bands", nameAndIds);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
}
