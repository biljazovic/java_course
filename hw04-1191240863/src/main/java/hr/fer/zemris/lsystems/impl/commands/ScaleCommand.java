package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Scales the unitLength of the turtle on the top of the stack by a given factor.
 * @author Bruno Iljazović
 */
public class ScaleCommand implements Command {

	private double factor;
	
	/**
	 * Instantiates the new ScaleCommand with the given scaler.
	 * @param factor scaler by which unitLength of the turtle should be changed.
	 */
	public ScaleCommand(double factor) {
		this.factor = factor;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().unitLength *= factor;
	}

}
