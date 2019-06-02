package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Generates data for a page with voting results.
 * 
 * @author Bruno Iljazović
 */
@WebServlet("/servleti/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		long pollid;
		
		try {
			pollid = Long.parseLong(req.getParameter("pollID"));
		} catch(NumberFormatException ex) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		List<PollOption> options;
		Poll poll;

		try {
			poll = DAOProvider.getDao().getPoll(pollid);
			options = DAOProvider.getDao().getOptions(pollid);
		} catch(DAOException ex) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			ex.printStackTrace();
			return;
		}
		
		if (options.isEmpty() || poll == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		options.sort(PollOption.COMPARATOR_BY_VOTES);
		
		int winnersCount = 1;
		while (winnersCount < options.size()
				&& options.get(winnersCount).getVotesCount() == (options.get(0).getVotesCount())) {
			winnersCount++;
		}		
		
		req.setAttribute("poll", poll);
		req.setAttribute("winners", options.subList(0, winnersCount));
		req.setAttribute("options", options);

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
}
