package hr.fer.zemris.java.hw14.servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Generates chart of the voting results. Only those options that have non-zero
 * number of votes are displayed on the chart.
 * 
 * @author Bruno Iljazović
 */
@WebServlet("/servleti/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

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

		try {
			options = DAOProvider.getDao().getOptions(pollid);
		} catch(DAOException ex) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			ex.printStackTrace();
			return;
		}
		
		if (options.isEmpty()) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		PieDataset dataset = createDataset(options);
		
		JFreeChart chart = createChart(
				dataset,
				"Rezultati glasanja"
		);

		BufferedImage image = chart.createBufferedImage(500, 270);
		
		resp.setContentType("image/png");
		ImageIO.write(image, "png", resp.getOutputStream());
	}

	/**
	 * Creates the chart of the voting results.
	 *
	 * @param dataset
	 *            the data set
	 * @param title
	 *            the title
	 * @return the chart
	 */
	private JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(320);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.8f);
		plot.setIgnoreZeroValues(true);
		plot.setDarkerSides(true);
		return chart;
	}

	/**
	 * Creates the data set of the voting results.
	 *
	 * @param results
	 *            the voting results
	 * @return the data set for the chart
	 */
	private PieDataset createDataset(List<PollOption> results) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		for (PollOption option : results) {
			dataset.setValue(option.getTitle(), option.getVotesCount());
		}
		
		return dataset;
	}

}
