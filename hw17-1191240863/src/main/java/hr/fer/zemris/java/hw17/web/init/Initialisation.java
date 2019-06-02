package hr.fer.zemris.java.hw17.web.init;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import hr.fer.zemris.java.hw17.model.PicturesDB;

/**
 * Initialises the pictures database.
 * 
 * @author Bruno Iljazović
 */
@WebListener
public class Initialisation implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			PicturesDB.initialise(sce);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
