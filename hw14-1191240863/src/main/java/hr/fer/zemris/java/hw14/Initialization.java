package hr.fer.zemris.java.hw14;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * This listener initializes the connection pool at the application startup. It
 * also creates initial poll data if it is missing from the database.
 * <p>
 * You can add new polls by modifying WEB-INF/poll-definitions/polls.txt. You
 * must add title, message and the filename of the poll options file. There are
 * two example poll options file present, bands.txt and movies.txt.
 * 
 * @author Bruno Iljazović
 */
@WebListener
public class Initialization implements ServletContextListener {
	
	/** The poll definitions root folder. */
	private final String definitionsRoot = "/WEB-INF/poll-definitions/";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		ComboPooledDataSource cpds = intializePool(sce);
		
		try (Connection dbConnection = cpds.getConnection();
				Statement st = dbConnection.createStatement()
		) {
			DatabaseMetaData dbmd = dbConnection.getMetaData();

			try (ResultSet rs = dbmd.getTables(null, null, "POLLS", null);) {
				if (!rs.next()) {
					st.executeUpdate("CREATE TABLE Polls ("
							+ "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
							+ "title VARCHAR(150) NOT NULL, "
							+ "message CLOB(2048) NOT NULL)"
					);
				}
			}
			try (ResultSet rs = dbmd.getTables(null, null, "POLLOPTIONS", null);) {
				if (!rs.next()) {
					st.executeUpdate("CREATE TABLE PollOptions ("
							+ "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
							+ "optionTitle VARCHAR(100) NOT NULL,"
							+ "optionLink VARCHAR(150) NOT NULL," 
							+ "pollID BIGINT,"
							+ "votesCount BIGINT,"
							+ "FOREIGN KEY (pollID) REFERENCES Polls(id))"
					);
				}
			}
			try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Polls");) {
				rs.next();
				if (rs.getInt(1) == 0) { //empty table POLLS
					populateTables(sce, dbConnection);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}
	
	/**
	 * Intializes connection pool using dbsettings.properties file.
	 *
	 * @param sce the servlet context event
	 * @return the connection pool
	 */
	private ComboPooledDataSource intializePool(ServletContextEvent sce) {
		Properties settings = new Properties();
		try {
			settings.load(Files.newInputStream(Paths.get(
					sce.getServletContext().getRealPath("/WEB-INF/dbsettings.properties")
			)));
		} catch (IOException ex) {
			throw new RuntimeException("Problem while reading 'dbsettings.properties' file.", ex);
		}
		
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException ex) {
			throw new RuntimeException("Error while initializing pool.", ex);
		}
		try {
			cpds.setJdbcUrl(String.format("jdbc:derby://%s:%s/%s;user=%s;password=%s",
					Objects.requireNonNull(settings.getProperty("host")),
					Objects.requireNonNull(settings.getProperty("port")),
					Objects.requireNonNull(settings.getProperty("name")),
					Objects.requireNonNull(settings.getProperty("user")),
					Objects.requireNonNull(settings.getProperty("password"))
			));
		} catch(NullPointerException ex) {
			throw new RuntimeException("Missing properties in 'dbsettings.properties' file", ex);
		}
		return cpds;
	}
	
	/**
	 * Populates tables POLLS and POLLOPTIONS with values from the definitions
	 * files.
	 *
	 * @param sce
	 *            the servlet context event
	 * @param dbConnection
	 *            the database connection
	 * @throws SQLException
	 *             signals that an SQL exception has occurred
	 * @throws IOException
	 *             signals that an I/O exception has occurred
	 */
	private void populateTables(ServletContextEvent sce, Connection dbConnection)
			throws SQLException, IOException {

		try (
			PreparedStatement pstPolls = dbConnection.prepareStatement(
					"INSERT INTO Polls (title, message) VALUES (?,?)",
					Statement.RETURN_GENERATED_KEYS
			);
			PreparedStatement pstOptions = dbConnection.prepareStatement(
							"INSERT INTO PollOptions "
							+ "(optionTitle, optionLink, pollID, votesCount) "
							+ "VALUES (?,?,?,?)"
			);		
		) {
			for (String poll : Files.readAllLines(Paths.get(sce.getServletContext()
					.getRealPath(definitionsRoot + "polls.txt")
			))) {
				if (poll.trim().isEmpty()) continue;
				String[] splitPoll = poll.split("\t");
				pstPolls.setString(1, splitPoll[0].trim());
				pstPolls.setString(2, splitPoll[1].trim());
				pstPolls.executeUpdate();
				try (ResultSet rs = pstPolls.getGeneratedKeys();) {
					rs.next();
					int pollId = rs.getInt(1);
					pstOptions.setInt(3, pollId);
				}
				pstOptions.setInt(4, 0);
				for (String option : Files.readAllLines(Paths.get(sce.getServletContext()
						.getRealPath(definitionsRoot + splitPoll[2])
				))) {
					String[] splitOption = option.split("\t");
					pstOptions.setString(1, splitOption[0].trim());
					pstOptions.setString(2, splitOption[1].trim());
					pstOptions.executeUpdate();
				}
			}
		} 
	}
	

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}