package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Similar to DrawCommand, but doesn't draw anything. Only moves the turtle from the top of the 
 * stack by a given number of unitLengths.
 * @author Bruno Iljazović
 */
public class SkipCommand implements Command {

	private double step;

	/**
	 * Instantiates the new SkipCommand.
	 * @param step turtle will move by step * unitLength.
	 */
	public SkipCommand(double step) {
		this.step = step;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState state = ctx.getCurrentState();
		
		state.position.translate(new Vector2D(
				state.direction.getX() * state.unitLength * step,
				state.direction.getY() * state.unitLength * step
		));
	}

}
