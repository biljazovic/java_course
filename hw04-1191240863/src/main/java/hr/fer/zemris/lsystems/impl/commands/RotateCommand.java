package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Rotates the direction of the top of the stack's turtle movement by the given number of degrees.
 * @author Bruno Iljazović
 */
public class RotateCommand implements Command {

	private double angle;
	
	/**
	 * Instantiates the new RotateCommand with the given angle.
	 * @param angle Angle in degrees by which the turtle should be rotated.
	 */
	public RotateCommand(double angle) {
		this.angle = angle;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().direction.rotate(angle);
	}
}
