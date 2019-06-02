package demo;

import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Program that demonstrates the {@link LSystemBuilderImpl} class. 
 * You can change how many iterations you want to be drawn, from 0 to 6.
 * <p>Configuration of the L-System is taken from the text file that you have to load. 
 * <p>Example configuration files are given in PROJECT_ROOT/examples directory
 * 
 * @author Bruno Iljazović
 */
public class Glavni3 {

	/**
	 * Method that is called when this program is run.
	 * @param args Arguments from the command line.
	 */
	public static void main(String[] args) {
		LSystemViewer.showLSystem(LSystemBuilderImpl::new);
	}

}
