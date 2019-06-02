/*
 * 
 */
package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Wrapper for output stream of HTTP response. Provides method write which first
 * check if the header was generated and if not, generate the header and then
 * send the header and the given content to the output stream.
 * <p>
 * Provides method that change the way header is generated.
 * 
 * @author Bruno Iljazović
 */
public class RequestContext {
	
	/**
	 * Instantiates a new request context.
	 *
	 * @param outputStream the output stream
	 * @param parameters the parameters
	 * @param persistentParameters the persistent parameters
	 * @param outputCookies the output cookies
	 * @param temporaryParameters the temporary parameters
	 * @param dispatcher the dispatcher
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies,
			Map<String, String> temporaryParameters, IDispatcher dispatcher) {
		this.outputStream = Objects.requireNonNull(outputStream);
		this.parameters = parameters == null ? new HashMap<>() : parameters;
		this.persistentParameters = persistentParameters == null ? new HashMap<>()
				: persistentParameters;
		this.outputCookies = outputCookies == null ? new ArrayList<>() : outputCookies;
		this.temporaryParameters = temporaryParameters == null ? new HashMap<>() 
			:temporaryParameters;
		this.dispatcher = dispatcher;
	}

	/**
	 * Instantiates a new request context.
	 *
	 * @param outputStream the output stream
	 * @param parameters the parameters
	 * @param persistentParameters the persistent parameters
	 * @param outputCookies the output cookies
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		this(outputStream, parameters, persistentParameters, outputCookies, null, null);
	}

	/**
	 * Represents one cookie to be sent in the header.
	 */
	public static class RCCookie {
		
		/** The name. */
		private String name;
		
		/** The value. */
		private String value;
		
		/** The domain. */
		private String domain;
		
		/** The path. */
		private String path;
		
		/** The max age. */
		private Integer maxAge;
		
		/** Whether this cookie is HTTP only or not. */
		private boolean HTTPOnly;

		/**
		 * Instantiates a new RC cookie.
		 *
		 * @param name the name
		 * @param value the value
		 * @param maxAge the max age
		 * @param domain the domain
		 * @param path the path
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			this(name, value, maxAge, domain, path, false);
		}
		
		/**
		 * Instantiates a new RC cookie.
		 *
		 * @param name the name
		 * @param value the value
		 * @param maxAge the max age
		 * @param domain the domain
		 * @param path the path
		 * @param HTTPOnly whether it is HTTP only
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path, boolean HTTPOnly) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
			this.HTTPOnly = HTTPOnly;
		}
	}

	/** The wrapped output stream. */
	private OutputStream outputStream;
	
	/** The charset generated from the encoding. */
	private Charset charset;
	
	/** The encoding of the HTTP content. */
	private String encoding = "UTF-8";
	
	/** The status code. */
	private int statusCode = 200;
	
	/** The status text. */
	private String statusText = "OK";
	
	/** The mime type/subtype. */
	private String mimeType = "text/html";
	
	/** The parameters. */
	private Map<String,String> parameters;
	
	/** The temporary parameters. */
	private Map<String,String> temporaryParameters;
	
	/** The persistent parameters. */
	private Map<String,String> persistentParameters;
	
	/** The output cookies. */
	private List<RCCookie> outputCookies;
	
	/** Whether the header was already generated or not. */
	private boolean headerGenerated = false;

	/** The content length. */
	private Long contentLength = null;
	
	/** Request dispatcher. */
	private IDispatcher dispatcher;
	
	/**
	 * Sets the encoding.
	 *
	 * @param encoding the new encoding
	 */
	public void setEncoding(String encoding) {
		if (headerGenerated) throw new RuntimeException("Header was already generated.");
		this.encoding = Objects.requireNonNull(encoding);
	}

	/**
	 * Sets the status code.
	 *
	 * @param statusCode the new status code
	 */
	public void setStatusCode(int statusCode) {
		if (headerGenerated) throw new RuntimeException("Header was already generated.");
		this.statusCode = Objects.requireNonNull(statusCode);
	}

	/**
	 * Sets the status text.
	 *
	 * @param statusText the new status text
	 */
	public void setStatusText(String statusText) {
		if (headerGenerated) throw new RuntimeException("Header was already generated.");
		this.statusText = Objects.requireNonNull(statusText);
	}

	/**
	 * Sets the mime type.
	 *
	 * @param mimeType the new mime type
	 */
	public void setMimeType(String mimeType) {
		if (headerGenerated) throw new RuntimeException("Header was already generated.");
		this.mimeType = Objects.requireNonNull(mimeType);
	}
	
	/**
	 * Sets the content length.
	 *
	 * @param contentLength the new content length
	 */
	public void setContentLength(long contentLength) {
		if (headerGenerated) throw new RuntimeException("Header was already generated.");
		this.contentLength = contentLength;
	}

	/**
	 * Gets the request dispatcher.
	 *
	 * @return the request dispatcher
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}
	
	/**
	 * Adds the RC cookie to the output cookies.
	 *
	 * @param cookie the cookie
	 */
	public void addRCCookie(RCCookie cookie) {
		Objects.requireNonNull(cookie);
		Objects.requireNonNull(cookie.name);
		Objects.requireNonNull(cookie.value);
		outputCookies.add(cookie);
	}

	
	/**
	 * Gets the parameter.
	 *
	 * @param name the name of parameter
	 * @return the parameter
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * Gets the set of parameter names.
	 *
	 * @return the parameter names
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}

	
	/**
	 * Gets the persistent parameter.
	 *
	 * @param name the name of parameter
	 * @return the persistent parameter
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	/**
	 * Gets the set of persistent parameter names.
	 *
	 * @return the persistent parameter names
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}
	
	/**
	 * Sets the persistent parameter.
	 *
	 * @param name the name of parameter
	 * @param value the value of parameter
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(Objects.requireNonNull(name), value);
	}
	
	/**
	 * Removes the persistent parameter.
	 *
	 * @param name the name of parameter
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	
	/**
	 * Gets the temporary parameter.
	 *
	 * @param name the name of parameter
	 * @return the temporary parameter
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}
	
	/**
	 * Gets the set of temporary parameter names.
	 *
	 * @return the temporary parameter names
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}
	
	/**
	 * Sets the temporary parameter.
	 *
	 * @param name the name of parameter
	 * @param value the value of parameter
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(Objects.requireNonNull(name), value);
	}
	
	/**
	 * Removes the temporary parameter.
	 *
	 * @param name the name of parameter
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}
	
	
	/**
	 * Generates header and returns its byte array using charset ISO_8859_1
	 *
	 * @return the generated header
	 */
	private byte[] generateHeader() {
		headerGenerated = true;
		charset = Charset.forName(encoding);
		
		String mimeExtra = mimeType.startsWith("text")
				? "; charset=" + encoding 
				: "";
		
		String contentLengthStr = contentLength == null ? "" : 
			"Content-Length: " + contentLength.toString() + "\r\n";
		
		StringBuilder cookies = new StringBuilder();
		for (RCCookie cookie : outputCookies) {
			cookies.append(String.format("Set-Cookie: %s=\"%s\"", cookie.name, cookie.value));
			if (cookie.domain != null) {
				cookies.append("; Domain=" + cookie.domain);
			}
			if (cookie.path != null) {
				cookies.append("; Path=" + cookie.path);
			}
			if (cookie.maxAge != null) {
				cookies.append("; Max-Age=" + cookie.maxAge);
			}
			if (cookie.HTTPOnly) {
				cookies.append("; HttpOnly");
			}
			cookies.append("\r\n");
		}
		
		return ("HTTP/1.1 " + statusCode + " " + statusText + "\r\n"
				+ "Content-Type: " + mimeType + mimeExtra + "\r\n"
				+ contentLengthStr
				+ cookies.toString()
				+ "\r\n"
		).getBytes(StandardCharsets.ISO_8859_1);
	}
	
	/**
	 * Writes the given data to the wrapped output stream, after writing the header,
	 * if not already written
	 *
	 * @param data
	 *            the data to be written
	 * @return this
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public RequestContext write(byte[] data) throws IOException {
		if (!headerGenerated) {
			outputStream.write(generateHeader());
		}
		outputStream.write(data);
		return this;
	}
	
	/**
	 * Writes the given text to the wrapped output stream, after writing the header,
	 * if not already written
	 *
	 * @param text the text
	 * @return this
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public RequestContext write(String text) throws IOException {
		if (!headerGenerated) {
			outputStream.write(generateHeader());
		}
		outputStream.write(text.getBytes(charset));
		return this;
	}

}
