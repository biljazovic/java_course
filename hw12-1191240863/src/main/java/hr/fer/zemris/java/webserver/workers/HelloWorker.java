package hr.fer.zemris.java.webserver.workers;

import java.text.SimpleDateFormat;
import java.util.Date;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This worker greets the user, and if the parameter name exists prints the
 * number of character in the value of that parameter
 * 
 * @author Bruno Iljazović
 */
public class HelloWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();

		context.setMimeType("text/html");
		String name = context.getParameter("name");

		context.write("<html><body>");
		context.write("<h1>Zdravo!!!</h1>");
		context.write("<p>Sada je: " + sdf.format(now) + "</p>");

		if (name == null || name.trim().isEmpty()) {
			context.write("<p>Niste poslali svoje ime!</p>");
		} else {
			context.write("<p>Vaše ime ima " + name.trim().length() + " slova.</p>");
		}

		context.write("</body></html>");
	}
}