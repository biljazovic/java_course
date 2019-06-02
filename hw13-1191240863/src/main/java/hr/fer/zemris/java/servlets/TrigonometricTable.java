package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Generates data for HTML table of numbers from a to b and their sine and
 * cosine values. a and b are given through request parameters.
 * <p>
 * If a is greater than b, they are swapped. If b > a + 720, b is set to a +
 * 720. Default values for a and b are 0 and 360, respectively.
 * 
 * @author Bruno Iljazović
 */
@WebServlet("/trigonometric")
public class TrigonometricTable extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String aString = req.getParameter("a");
		String bString = req.getParameter("b");
		
		int aNumber = 0;
		int bNumber = 360;
		
		try {
			aNumber = Integer.parseInt(aString);
		} catch(NumberFormatException ignorable) {}

		try {
			bNumber = Integer.parseInt(bString);
		} catch(NumberFormatException ignorable) {}
		
		if (aNumber > bNumber) {
			int temp = aNumber;
			aNumber = bNumber;
			bNumber = temp;
		}
		
		bNumber = Math.min(bNumber, aNumber + 720);
		
		List<NumberSinCos> list = new ArrayList<>();
		
		DecimalFormat dfm = new DecimalFormat("0.00000");
		
		for (int i = aNumber; i <= bNumber; i++) {
			double sin = Math.sin(Math.toRadians(i));
			double cos = Math.cos(Math.toRadians(i));
			list.add(new NumberSinCos(i, dfm.format(sin), dfm.format(cos)));
		}
		
		req.setAttribute("trigValues", list);
		
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
	
	/**
	 * Represents one (number, sine value, cosine value) triplet.
	 */
	public static class NumberSinCos {

		/** The number. */
		private int number;
		
		/** The sine value of the number. */
		private String sin;
		
		/** The cosine value of the number. */
		private String cos;

		/**
		 * Gets the number.
		 *
		 * @return the number
		 */
		public int getNumber() {
			return number;
		}

		/**
		 * Gets the sin value
		 *
		 * @return the sin value
		 */
		public String getSin() {
			return sin;
		}

		/**
		 * Gets the cos value
		 *
		 * @return the cos value
		 */
		public String getCos() {
			return cos;
		}
		
		/**
		 * Instantiates a new (number, sin, cos) triplet
		 */
		public NumberSinCos() {
			this(0, "0", "1");
		}

		/**
		 * Instantiates a new (number, sin, cos) triplet
		 *
		 * @param number the number
		 * @param sin the sin value
		 * @param cos the cos value
		 */
		public NumberSinCos(int number, String sin, String cos) {
			this.number = number;
			this.sin = sin;
			this.cos = cos;
		}
	}
}
