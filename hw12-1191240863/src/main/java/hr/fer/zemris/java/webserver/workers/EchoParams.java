package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This worker displays given parameters in a HTML table.
 * 
 * @author Bruno Iljazović
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		
		context.setMimeType("text/html");
		
		context.write("<html><head><style>");
		context.write("table,th,td {border: 1px solid black; }");
		context.write("</style><body>");
		context.write("<h1>Parameters:</h1>");
		context.write("<table>");
		context.write("<tr><th>Name</th><th>Value</th></tr>");
		
		for (String name : context.getParameterNames()) {
			String value = context.getParameter(name);
			if (value != null) {
				value = '"' + value + '"';
			} else {
				value = "";
			}
			context.write("<tr><td>" + name + "</td>"
				+ "<td>" + value + "</td></tr>"
			);
		}
		context.write("</table></body></html>");
	}
}