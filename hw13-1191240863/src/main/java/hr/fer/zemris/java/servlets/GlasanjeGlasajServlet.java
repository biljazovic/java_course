package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.servlets.Util.Band;

/**
 * Registers one vote, for the band with the id equal to the given parameter
 * "id". Results are appended to the results file.
 * 
 * @author Bruno Iljazović
 */
@WebServlet("/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String filename = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		Map<Integer,Band> results = Util.readResults(filename);

		Integer votedId = null;
		
		try {
			votedId = Integer.parseInt(req.getParameter("id"));
		} catch(NumberFormatException ex) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		Integer points = 1;
		Band band = results.get(votedId);
		if (band != null) {
			points = band.getPoints() + 1;
		}
		
		results.put(votedId, new Band(null, null, points));
		writeResults(results, filename);
		
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}

	/**
	 * Writes results to the results file.
	 *
	 * @param results
	 *            the voting results
	 * @param filename
	 *            the results file name
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeResults(Map<Integer,Band> results, String filename) throws IOException {
		StringBuilder resultsStr = new StringBuilder();
		for (Map.Entry<Integer, Band> entry : results.entrySet()) {
			resultsStr.append(entry.getKey() + "\t" + entry.getValue().getPoints() + "\n");
		}
		synchronized (GlasanjeGlasajServlet.class) {
			Files.write(Paths.get(filename), resultsStr.toString().getBytes(StandardCharsets.UTF_8));
		}
	}
}
