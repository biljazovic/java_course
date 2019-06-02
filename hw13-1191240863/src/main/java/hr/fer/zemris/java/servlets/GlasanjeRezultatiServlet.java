package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.servlets.Util.Band;

/**
 * Generates data for a page with voting results.
 * 
 * @author Bruno Iljazović
 */
@WebServlet("/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String definitionFile = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		String resultFile = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		Map<Integer,Band> results = Util.readDefinitionsAndResults(definitionFile, resultFile);
		
		if (results == null) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		List<Band> bands = new ArrayList<>(results.values());
		Collections.sort(bands, (e, f) -> Integer.compare(f.getPoints(), e.getPoints()));
		
		int winnersCount = 1;
		while (winnersCount < bands.size()
				&& bands.get(winnersCount).getPoints().equals(bands.get(0).getPoints())) {
			winnersCount++;
		}		

		req.setAttribute("winners", bands.subList(0, winnersCount));
		req.setAttribute("bands", bands);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
}
