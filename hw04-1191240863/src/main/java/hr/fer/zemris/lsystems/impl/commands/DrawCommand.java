package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Moves the turtle from the top of the stack and draws its path using the Painter. Turtle is 
 * moved by given number of unitLengths.
 * @author Bruno Iljazović
 */
public class DrawCommand implements Command {

	private double step;
	
	/**
	 * Size of the turtle's trace line.
	 */
	public final float PAINTER_SIZE =  1.0f;
	
	/**
	 * Instantiates new DrawCommand.
	 * 
	 * @param step turtle will move by step * unitLength.
	 */
	public DrawCommand(double step) {
		this.step = step;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState state = ctx.getCurrentState();
		
		Vector2D newPosition = state.position.translated(new Vector2D(
				state.direction.getX() * state.unitLength * step,
				state.direction.getY() * state.unitLength * step
		));
		
		painter.drawLine(
				state.position.getX(),
				state.position.getY(),
				newPosition.getX(),
				newPosition.getY(),
				ctx.getCurrentState().color,
				PAINTER_SIZE
		);
		
		state.position = newPosition;
	}

}
