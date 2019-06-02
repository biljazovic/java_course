package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Interface which represents one command for the turtle.
 * @author Bruno Iljazović
 */
public interface Command {

	/**
	 * Method which modifies the given stack of TurtleStates or draws something with the given
	 * painter.
	 * 
	 * @param ctx stack of TurtleStates
	 * @param painter provides drawLine method
	 */
	void execute(Context ctx, Painter painter);
}
