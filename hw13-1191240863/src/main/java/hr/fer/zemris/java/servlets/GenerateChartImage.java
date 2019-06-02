package hr.fer.zemris.java.servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;

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

/**
 * Generates chart from operating system browsing statistics.
 * 
 * @author Bruno Iljazović
 */
@WebServlet("/reportImage")
public class GenerateChartImage extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PieDataset dataset = createDataset();
		
		JFreeChart chart = createChart(
				dataset,
				"Desktop/Laptop operating system browsing statistics"
		);
		
		BufferedImage image = chart.createBufferedImage(500, 270);

		resp.setContentType("image/png");
		ImageIO.write(image, "png", resp.getOutputStream());
	}
	
	/**
	 * Creates and returns the chart from the given data set and title of the chart.
	 *
	 * @param dataset
	 *            the data set
	 * @param title
	 *            the title of the chart
	 * @return the generated chart
	 */
	private JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;
	}

	/**
	 * Creates the data set for the chart.
	 *
	 * @return data set
	 */
	private PieDataset createDataset() {
		DefaultPieDataset result = new DefaultPieDataset();
		result.setValue("Windows", 81.73);
		result.setValue("macOS", 13.16);
		result.setValue("Linux", 1.66);
		result.setValue("Other", 3.44);
		return result;
	}
}
