package demo;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Program that demonstrates the {@link LSystemBuilderImpl} class. It draws a Koch curve in a 
 * separate window. You can change how many iterations you want to be drawn, from 0 to 6.
 * <p>Configuration of the L-System is given by configuration text.
 * 
 * @author Bruno Iljazović
 */
public class Glavni2 {

	/**
	 * Method that is called when this program is run.
	 * @param args Arguments from the command line.
	 */
	public static void main(String[] args) {
		LSystemViewer.showLSystem(createKochCurve(LSystemBuilderImpl::new));
	}

	private static LSystem createKochCurve(LSystemBuilderProvider provider) {
		String[] data = new String[] {
				"origin 0.05 0.4",
				"angle 0",
				"unitLength 0.9",
				"unitLengthDegreeScaler 1.0 / 3.0",
				"",
				"command F draw 1",
				"command + rotate 60",
				"command - rotate -60",
				"",
				"axiom F",
				"",
				"production F F+F--F+F"
		};
		return provider.createLSystemBuilder().configureFromText(data).build();
	}
}
