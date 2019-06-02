package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Sets the color of the turtle on the top of the stack to the given color.
 * @author Bruno Iljazović
 */
public class ColorCommand implements Command {

	private Color color;

	/**
	 * Instantiates new ColorCommand with the given color
	 * 
	 * @param color new color of the turtle
	 */
	public ColorCommand(Color color) {
		this.color = color;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().color = color;
	}

}
