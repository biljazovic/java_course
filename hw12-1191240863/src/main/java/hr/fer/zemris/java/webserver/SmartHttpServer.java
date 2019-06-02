package hr.fer.zemris.java.webserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Multithreaded HTTP server. You have to provide it a valid configuration file that includes 
 * server IP address and port, domain name, maximum number of threads, path to document root, 
 * session timeout length, and paths to mime and workers configuration files.
 * <p> Starting the server is done by calling {@link SmartHttpServer#start} method.
 * 
 * @author Bruno Iljazović
 */
public class SmartHttpServer {

	/** The address that this server listens on. */
	private String address;

	/** The domain name of server. */
	private String domainName;

	/** The port that this server listens on. */
	private int port;

	/** Number of worker threads. */
	private int workerThreads;

	/** The session timeout in seconds. */
	private int sessionTimeout;

	/** The mime types. */
	private Map<String, String> mimeTypes = new HashMap<>();

	/** The server thread. */
	private ServerThread serverThread;

	/** The thread pool. */
	private ExecutorService threadPool;

	/** The document root folder. */
	private Path documentRoot;
	
	/** The server socket. */
	private ServerSocket serverSocket;
	
	/** The workers map. */
	private Map<String, IWebWorker> workersMap = new HashMap<>();

	/** The sessions map. */
	private Map<String, SessionMapEntry> sessions = new HashMap<>();
	
	/** The random object for generating session id's. */
	private Random sessionRandom = new Random();
	
	/**
	 * Instantiates a new smart HTTP server configured from the provided
	 * configuration file.
	 *
	 * @param configFileName
	 *            the path to the main configuration file
	 * @throws IllegalArgumentException
	 *             if the provided configuration file(s) isn't valid or has some errors
	 */
	public SmartHttpServer(String configFileName) {
		try {
			Properties properties = new Properties();
			properties.load(Files.newInputStream(Paths.get(configFileName)));
			
			address = Objects.requireNonNull(properties.getProperty("server.address"));
			domainName = Objects.requireNonNull(properties.getProperty("server.domainName"));
			port = Integer.parseInt(properties.getProperty("server.port"));
			workerThreads = Integer.parseInt(properties.getProperty("server.workerThreads"));
			documentRoot = Paths.get(properties.getProperty("server.documentRoot"));
			if (!Files.isDirectory(documentRoot)) {
				throw new IllegalArgumentException("Invalid document root folder.");
			}
			sessionTimeout = Integer.parseInt(properties.getProperty("session.timeout"));

			extractMimeTypes(Objects.requireNonNull(properties.getProperty("server.mimeConfig")));

			//IllegalArgumentException from the extractWorkers isn't checked, wanted behavior
			extractWorkers(Objects.requireNonNull(properties.getProperty("server.workers")));

			serverThread = new ServerThread();

		} catch(IOException ex) {
			throw new IllegalArgumentException("Error while reading the configuration file(s)", ex);
		} catch(NullPointerException ex) {
			throw new IllegalArgumentException("Some properties were not found in configuration "
				+ "files", ex);
		}
	}
	
	/**
	 * Parses the given workers configuration file. Every keyword from the
	 * configuration file is mapped to an instance of a worker specified by its
	 * fully qualified class name.
	 * 
	 * @param workersConfig
	 *            path to the workers configuration file
	 * @throws IOException
	 *             if the given configuration isn't readable file
	 * @throws IllegalArgumentException
	 *             if the given configuration file is of invalid format, or the
	 *             given class names don't specify an existing worker.
	 */
	private void extractWorkers(String workersConfig) throws IOException {
		for (String line : Files.readAllLines(Paths.get(workersConfig))) {
			line = line.trim();
			if (line.isEmpty() || line.charAt(0) == '#') continue;
			String[] lineSplit = line.split("=");
			if (lineSplit.length != 2) {
				throw new RuntimeException();
			}
			String path = lineSplit[0].trim();
			String fqcn = lineSplit[1].trim();
			
			try {
				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
				IWebWorker iww = (IWebWorker)(referenceToClass.getDeclaredConstructor().newInstance());
				if (workersMap.containsKey(path))
					throw new IllegalArgumentException(
						"Multiple lines with the same path in the workers configuration file");
				workersMap.put(path, iww);
			} catch(ReflectiveOperationException e) {
				throw new IllegalArgumentException(
					"Invalid class name in workers configuration file", e);
			}
		}
	}

	/**
	 * Parses the given mime configuration file. Key is the file extension and the
	 * value is the corresponding content type/subtype
	 * 
	 * @param mimeConfig
	 *            path to the configuration file
	 * @throws IOException
	 *             if the given configuration file is not regular file
	 */
	private void extractMimeTypes(String mimeConfig) throws IOException {
		Properties properties = new Properties();
		properties.load(Files.newInputStream(Paths.get(mimeConfig)));
		for (String key : properties.stringPropertyNames()) {
			mimeTypes.put(key, properties.getProperty(key));
		}
	}

	/**
	 * Starts the server, if not already running.
	 */
	protected synchronized void start() {
		if (!serverThread.isAlive()) {
			serverThread.start();
		}
		threadPool = Executors.newFixedThreadPool(workerThreads);
	}

	/**
	 * Suggests the server to stop running. Server stops accepting additional
	 * requests, but current running workers are not interrupted.
	 */
	protected synchronized void stop() {
		try {
			serverSocket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		threadPool.shutdown();
	}

	/**
	 * A server thread that listens for requests from the clients. When a request
	 * comes, it wraps it in a {@link ClientWorker} and adds it to the thread pool.
	 * <p>
	 * It also starts daemon thread that periodically (every 5 minutes) checks for
	 * old sessions and deletes them from the {@link SmartHttpServer#sessions} map.
	 * 
	 * @author Bruno Iljazović
	 */
	protected class ServerThread extends Thread {
		
		@Override
		public void run() {
			Thread sessionCleanup = new Thread(() -> {
				while (true) {
					synchronized(SmartHttpServer.this) {
						for (Iterator<Map.Entry<String, SessionMapEntry>> it = 
								sessions.entrySet().iterator(); it.hasNext();) {
							if (it.next().getValue().validUntil < System.currentTimeMillis() / 1000) {
								it.remove();
							}
						}
					}
					try {
						Thread.sleep(5 * 60 * 1000);
					} catch(InterruptedException ignorable) {}
				}
			});
			sessionCleanup.setDaemon(true);
			sessionCleanup.start();
			try {
				serverSocket = new ServerSocket(port, 0, InetAddress.getByName(address));
				while (true) {
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.execute(cw);
				}
			} catch(SocketException ex) {
				return;
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Represents one session.
	 * 
	 * @author Bruno Iljazović
	 */
	private static class SessionMapEntry {

		/** The unique 20-upper-case-letter session id. */
		@SuppressWarnings("unused")
		String sid;
		
		/** Host name of this session. */
		String host;
		
		/** Time in seconds when this session expires. */
		long validUntil;
		
		/** Map of persistent parameters for this session. */
		Map<String, String> map;

		/**
		 * Instantiates a new session.
		 *
		 * @param sid unique session id
		 * @param host the host name
		 * @param validUntil time when the session expires
		 * @param map persistent parameters map
		 */
		public SessionMapEntry(String sid, String host, long validUntil, Map<String, String> map) {
			super();
			this.sid = sid;
			this.host = host;
			this.validUntil = validUntil;
			this.map = map;
		}
	}
	
	/**
	 * Worker class for one client request.
	 * 
	 * @author Bruno Iljazović
	 */
	private class ClientWorker implements Runnable, IDispatcher {

		/** The client socket. */
		private Socket csocket;

		/** The input stream of client socket. */
		private PushbackInputStream istream;

		/** The output stream of client socket. */
		private OutputStream ostream;

		/** The HTTP version. */
		private String version;

		/** The request method. */
		private String method;

		/** The host name used by request. */
		private String host;

		/** The parameters from the requested path. */
		private Map<String, String> params = new HashMap<String, String>();

		/** The temporary parameters.. */
		private Map<String, String> tempParams = new HashMap<String, String>();

		/** The persistent parameters of a session. */
		private Map<String, String> permParams = new HashMap<String, String>();

		/** The output cookies. */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();

		/** The sid of session of this request. */
		private String SID;
		
		/** The context of this request. */
		private RequestContext context = null;

		/**
		 * Instantiates a new client worker.
		 *
		 * @param csocket the client socket
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		@Override
		public void run() {
			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = csocket.getOutputStream();
				
				List<String> request = readRequest();
				if (request == null) {
					sendError(400, "Bad Request");
					return;
				}
				
				String[] firstLine = request.isEmpty() ? null : request.get(0).split("\\s+");
				if (firstLine == null || firstLine.length != 3) {
					sendError(400, "Bad Request");
					return;
				}

				method = firstLine[0].toUpperCase();
				version = firstLine[2].toUpperCase();
				
				if (!method.equals("GET")) {
					sendError(400, "Bad Request");
					return;
				}
				if (!version.equals("HTTP/1.0") && !version.equals("HTTP/1.1")) {
					sendError(505, "HTTP Version Not Supported");
					return;
				}
				
				host = domainName;
				Pattern hostPattern = Pattern.compile("(?i:Host):\\s+(.+?)(:\\d+)?\\s*");
				for (String header : request) {
					Matcher matcher = hostPattern.matcher(header);
					if (matcher.matches()) {
						host = matcher.group(1);
						break;
					}
				}

				checkSesssion(request);
				
				String path, paramString = null;
				String[] requestedPathSplit = firstLine[1].split("\\?");
				if (requestedPathSplit.length > 2) {
					sendError(400, "Bad Request");
					return;
				} else if (requestedPathSplit.length == 2) {
					paramString = requestedPathSplit[1];
				}
				path = requestedPathSplit[0];

				parseParameters(paramString);
				internalDispatchRequest(path, true);

			} catch(IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					csocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * If client sent a session id of existing session, that session hasn't expired,
		 * and session host name is equal to the request's, that session's expiration
		 * date is refreshed.
		 * <p>
		 * Otherwise, new session is created.
		 *
		 * @param request
		 *            the request header
		 */
		private void checkSesssion(List<String> request) {

			//these could be moved to server constructor
			Pattern cookieStart = Pattern.compile("(?i:Cookie):\\s+(.*)");
			Pattern cookies = Pattern.compile("(.+?)=([^;]+);?");
			
			String sidCandidate = null;
		l:	for (String line : request) {
				Matcher mt = cookieStart.matcher(line);
				if (!mt.matches()) continue;
				mt = cookies.matcher(mt.group(1));
				while (mt.find()) {
					if (mt.group(1).trim().toLowerCase().equals("sid")) {
						sidCandidate = mt.group(2);
						break l; //only first SID found is considered
					}
				}
			}
			
			if (sidCandidate != null && !sidCandidate.isEmpty()) {
				if (sidCandidate.charAt(0) == '"') {
					sidCandidate = sidCandidate.substring(1, sidCandidate.length() - 1);
				}
				synchronized(SmartHttpServer.this) {
					SessionMapEntry sessionEntry = sessions.get(sidCandidate);
					if (sessionEntry != null && 
							sessionEntry.host.equals(host) &&
							sessionEntry.validUntil >= System.currentTimeMillis() / 1000) {
						sessionEntry.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
						permParams = sessionEntry.map;
						return;
					}
				}
			}
			
			synchronized (SmartHttpServer.this) {
				SID = sessionRandom.ints().limit(20).collect(
					StringBuilder::new,
					(str, num) -> { str.append((char)(65 + Math.abs(num) % 26)); },
					StringBuilder::append
				).toString();
				SessionMapEntry sessionEntry = new SessionMapEntry(
					SID, 
					host,
					System.currentTimeMillis() / 1000 + sessionTimeout, 
					new ConcurrentHashMap<>()
				);
				permParams = sessionEntry.map;
				sessions.put(SID, sessionEntry);
			}
			outputCookies.add(new RCCookie("sid", SID, null, host, "/", true));
		}
		
		/**
		 * Parses the parameters from the client requested path.
		 *
		 * @param paramString the param string
		 */
		private void parseParameters(String paramString) {
			if (paramString == null) return;
			for (String parameter : paramString.split("&")) {
				if (parameter.isEmpty()) continue;
				String[] parameterSplit = parameter.split("=",-1);
				if (parameterSplit.length > 2) continue;
				if (parameterSplit[0].isEmpty()) continue;
				String value = parameterSplit.length == 2 ? parameterSplit[1] : null;
				params.put(parameterSplit[0], value);
			}
		}

		/**
		 * Sends error to the client with the given status code and text.
		 *
		 * @param statusCode
		 *            the status code
		 * @param statusText
		 *            the status text
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 */
		private void sendError(int statusCode, String statusText) throws IOException {
			ostream.write(
				("HTTP/1.1 "+statusCode+" "+statusText+"\r\n"+
				"Server: Smart HTTP Server\r\n"+
				"Content-Type: text/plain;charset=UTF-8\r\n"+
				"Content-Length: 0\r\n"+
				"Connection: close\r\n"+
				"\r\n").getBytes(StandardCharsets.ISO_8859_1)
			);
			ostream.flush();
		} 

		/**
		 * Reads complete request header from the client and returns the header as list
		 * of lines.
		 *
		 * @return request header as list of strings
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 */
		private List<String> readRequest() throws IOException {
			byte[] request = readRequest(istream);
			if (request == null) {
				return null;
			}
			String requestStr = new String(request, StandardCharsets.ISO_8859_1);

			List<String> headers = new ArrayList<String>();

			String currentLine = "";
			for(String s : requestStr.split("\n")) {
				if(s.isEmpty()) break;
				char c = s.charAt(0);
				if(c==9 || c==32) {
					currentLine += s;
				} else {
					if(!currentLine.isEmpty()) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if(!currentLine.isEmpty()) {
				headers.add(currentLine);
			}

			return headers;
		}

		/**
		 * Processes the client request and returns the HTTP headers as byte array.
		 *
		 * @param is
		 *            the input stream
		 * @return HTTP headers
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 */
		private byte[] readRequest(InputStream is) throws IOException {

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			l: while (true) {
				int b = is.read();
				if (b == -1) return null;
				if (b != 13) {
					bos.write(b);
				}
				switch (state) {
				case 0:
					state = b == 13 ? 1 : (b == 10 ? 3 : 0);
					break;
				case 1:
					state = b == 10 ? 2 : 0;
					break;
				case 2:
					state = b == 13 ? 3 : 0;
					break;
				case 3:
					if (b == 10) {
						break l;
					}
					state = 0;
					break;
				}
			}
			return bos.toByteArray();
		}
		
		/**
		 * Gets the mime from the file extension. Default is application/octet-stream
		 *
		 * @param string the file extension
		 * @return the corresponding mime, or application/octet-stream if none found
		 */
		private String getMime(String string) {
			string = string.toLowerCase();
			int index = string.lastIndexOf('.');
			if (index != -1 && index != string.length() - 1) {
				String mime = mimeTypes.get(string.substring(index + 1));
				if (mime != null) return mime;
			}
			return "application/octet-stream";
		}
		
		/**
		 * Analyzes the requested path and processes it, or sends error back to the
		 * client if something is wrong with the request.
		 *
		 * @param urlPath
		 *            requested path
		 * @param directCall
		 *            whether this method is called from within this class or not
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 */
		public void internalDispatchRequest(String urlPath, boolean directCall) throws IOException {
			
			if (urlPath.startsWith("/ext/")) {
				processEXT(urlPath);
				return;
			}
			
			IWebWorker iww = workersMap.get(urlPath);
			if (iww != null) {
				if (context == null) {
					context = new RequestContext(ostream, params, permParams, outputCookies,
						tempParams, this);
				}
				try {
					iww.processRequest(context);
				} catch (Exception e) {
					sendError(500, "Internal Server Error: " + e.getMessage());
					e.printStackTrace();
				}
				return;
			}
			
			if (urlPath.startsWith("/private") && directCall == true) {
				sendError(404, "File Not Found");
				return;
			}
			
			urlPath = urlPath.substring(1);
			
			Path requestedPath = documentRoot.resolve(Paths.get(urlPath));
			if (!requestedPath.startsWith(documentRoot)) {
				sendError(403, "Forbidden");
				return;
			}
			if (!Files.isReadable(requestedPath) || !Files.isRegularFile(requestedPath)) {
				sendError(404, "File Not Found");
				return;
			}
			
			String filename = requestedPath.getFileName().toString();
			
			if (filename.endsWith(".smscr")) {
				processSMSCR(requestedPath);
			} else {
				String mime = getMime(filename);

				if (context == null) {
					context = new RequestContext(ostream, params, permParams, outputCookies);
				}
				context.setMimeType(mime);
				context.setContentLength(Files.size(requestedPath));
				context.write(Files.readAllBytes(requestedPath));
				ostream.flush();
			}
		}
		
		/**
		 * Processes direct worker call (called with /ext/...)
		 *
		 * @param urlPath the url path of the form /ext/...
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		private void processEXT(String urlPath) throws IOException {
			try {
				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(
					"hr.fer.zemris.java.webserver.workers." + urlPath.substring(5));
				IWebWorker iww = (IWebWorker)(referenceToClass.getDeclaredConstructor().newInstance());
				if (context == null) {
					context = new RequestContext(ostream, params, permParams, outputCookies,
						tempParams, this);
				}
				try {
					iww.processRequest(context);
				} catch (Exception e) {
					sendError(500, "Internal Server Error: " + e.getMessage());
					e.printStackTrace();
				}
			} catch(ReflectiveOperationException ex) {
				sendError(400, "Bad Request");
			}
		}
		
		/**
		 * Processes Smart Script script.
		 *
		 * @param requestedPath the path of the script.
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		private void processSMSCR(Path requestedPath) throws IOException {
			String script = new String(
				Files.readAllBytes(requestedPath),
				StandardCharsets.UTF_8
			);
			SmartScriptParser parser = null;
			try {
				parser = new SmartScriptParser(script);
			} catch (SmartScriptParserException e) {
				sendError(500, "Internal Server Error: " + e.getMessage());
				e.printStackTrace();
				return;
			} 
			new SmartScriptEngine(
				parser.getDocumentNode(), 
				context == null ? context = new RequestContext(ostream, params, permParams,
					outputCookies, tempParams, this) : context
			).execute();
			ostream.flush();
		}
		

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}
	}
}