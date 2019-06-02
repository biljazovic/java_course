package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;
import hr.fer.zemris.math.Vector2D;

/**
 * This class lets the user configure and create Lindermayer systems. Its parameters can be set
 * by calling specific methods for each parameter or by writing the text file and then passing it 
 * to the {@link LSystemBuilderImpl#configureFromText(String[])} method. 
 * 
 * <p>A Lindermayer system consists of the intitial "axiom" string from which to begin construction
 * by following "production" rules that expand each symbol into some larger string of symbols.
 * 
 * <p>Each symbol can represent some command which is packaged in 
 * {@link hr.fer.zemris.lsystems.impl.commands}
 * 
 * <p>Example configuration files can be found in PROJECT_ROOT/examples folder.
 * @author Bruno Iljazović
 */
public class LSystemBuilderImpl implements LSystemBuilder {
	
	/** Dictionary which for each symbol (key) stores a Command instance (value) which that symbol 
	 * represents. */
	private Dictionary commands;

	/** Dictionary which for each symbol (key) stores a sequence of symbols into which that symbol
	 * expands */
	private Dictionary productions;
	
	/** Length of a unit move of the turtle. */
	private double unitLength = 0.1;

	/** UnitLength is multiplied by this factor iteration. */
	private double unitLengthDegreeScaler = 1.0;

	/** Coordinates of a origin point of the turtle. */
	private Vector2D origin = new Vector2D(0.0, 0.0);

	/** Initial angle of the turtle movement direction, 0 represents positive x axis, 90 positive
	 * y axis.*/
	private double angle = 0;

	/** Sequence of symbols from which to begin construction. */
	private String axiom = "";
	
	/**
	 * This class provides methods to generate and draw Lindermayer system configured by 
	 * {@link LSystemBuilderImpl} class using the given Painter. 
	 */
	private class LSystemImpl implements LSystem {

		/** color of the turtle if none is given in the system configuration */
		private final Color DEFAULT_COLOR = Color.BLACK;

		/**
		 * Calls {@link LSystemImpl#generate} method and executes commands one by one using the 
		 * dictionary {@link LSystemBuilderImpl#commands}. 
		 * <p>Initial state of the turtle is given by {@link LSystemBuilderImpl} instance.
		 * 
		 * @param level number of iterations
		 * @param painter Painter which is used for drawing turtle paths
		 * 
		 * @throws IllegalArgumentException if the given level is negative.
		 */
		@Override
		public void draw(int level, Painter painter) {
			Context context = new Context();
			context.pushState(new TurtleState(
					origin, 
					Vector2D.directionFromAngle(angle),
					DEFAULT_COLOR,
					unitLength * Math.pow(unitLengthDegreeScaler, level)
			));

			String commandString = generate(level); //throws IllegalArgumentException

			for (char ch : commandString.toCharArray()) {
				Command command = (Command) commands.get(Character.valueOf(ch));
				if (command != null) {
					command.execute(context, painter);
				}
			}
		}
		
		/**
		 * Recursively generates string after given number of iterations using the 
		 * {@link LSystemBuilderImpl#productions} dictionary.
		 * 
		 * <p>Iteration 0 corresponds to axiom string.
		 * 
		 * @param level number of iterations
		 * 
		 * @return generated string after given number of iterations.
		 * 
		 * @throws IllegalArgumentException if the given level is negative.
		 */
		@Override
		public String generate(int level) {
			if (level < 0) {
				throw new IllegalArgumentException("Level can't be negative. Was " + level + ".");
			}

			if (level == 0) return axiom;
			
			String parent = generate(level - 1);
			
			StringBuilder result = new StringBuilder();
			for (char ch : parent.toCharArray()) {
				String production = (String) productions.get(Character.valueOf(ch));
				if (production == null) {
					result.append(ch);
				}
				else {
					result.append(production);
				}
			}
			
			return result.toString();
		}
	}
	
	/**
	 * Default constructor. Instantiates empty dictionaries.
	 */
	public LSystemBuilderImpl() {
		commands = new Dictionary();
		productions = new Dictionary();
	}
	
	/**
	 * Returns an instance of {@link LSystemImpl} class which inherits all of the configured 
	 * parameters (e.g. origin of a turtle).
	 * 
	 * @return LsystemImpl instance.
	 */
	@Override
	public LSystem build() {
		return new LSystemImpl();
	}

	/**
	 * Parses the given text line by line and modifies the L-system and turtle parameters.
	 * Examples of the valid configuration files can be found in PROJECT_ROOT/examples folder.
	 * 
	 * @param lines configuration text.
	 * 
	 * @return modified LSystemBuilderImpl instance.
	 * 
	 * @throws IllegalArgumentException if the given configuration text is invalid.
	 */
	@Override
	public LSystemBuilder configureFromText(String[] lines) {
		for (String line : lines) {
			String[] splitLine = line.split("\\s+");
			
			if (splitLine.length == 0 || splitLine[0].equals("")) {
				continue;
			}
			
			int maxSplitLineLength = -1;
			
			try {
				switch (splitLine[0]) {
				case "origin":
					setOrigin(Double.parseDouble(splitLine[1]), Double.parseDouble(splitLine[2]));

					maxSplitLineLength = 3;
					break;
				case "angle":
					setAngle(Double.parseDouble(splitLine[1]));
					
					maxSplitLineLength = 2;
					break;
				case "unitLength":
					setUnitLength(Double.parseDouble(splitLine[1]));
					
					maxSplitLineLength = 2;
					break;
				case "unitLengthDegreeScaler":
					double scaler;

					//merge together all parameters into splitLine[1];
					for (int i = 2; i < splitLine.length; ++i) {
						splitLine[1] += splitLine[i];
					}

					if (splitLine[1].indexOf('/') != -1) {
						String[] fraction = splitLine[1].split("/", 2);
						scaler = Double.parseDouble(fraction[0]) / Double.parseDouble(fraction[1]);
					}
					else {
						scaler = Double.parseDouble(splitLine[1]);
					}

					setUnitLengthDegreeScaler(scaler);
					break;
				case "axiom":
					setAxiom(splitLine[1]);
					
					maxSplitLineLength = 2;
					break;
				case "production":
					if (splitLine[1].length() != 1) {
						throw new IllegalArgumentException("key for production must be one symbol."
								+ "Was " + splitLine[1] + ".");
					}
					registerProduction(splitLine[1].charAt(0), splitLine[2]);
					
					maxSplitLineLength = 3;
					break;
				case "command":
					String command;
					if (splitLine[1].length() != 1) {
						throw new IllegalArgumentException("key for command must be one symbol."
								+ "Was " + splitLine[1] + ".");
					}
					if (splitLine.length == 3) {
						command = splitLine[2];
					}
					else {
						command = splitLine[2] + " " + splitLine[3];
					}
					registerCommand(splitLine[1].charAt(0), command);
					
					maxSplitLineLength = 4;
					break;
				default:
					throw new IllegalArgumentException(
							"Invalid command name. Was " + splitLine[0] + ".");
				}
			} catch(NumberFormatException | IndexOutOfBoundsException ex) {
				throw new IllegalArgumentException("Invalid directive. Was " + line + ".");
			}

			if (maxSplitLineLength != -1 && splitLine.length > maxSplitLineLength) {
				throw new IllegalArgumentException("Invalid directive. Was " + line + ".");
			}
		}

		return this;
	}

	/**
	 * Parses the given string for command name and its parameters. Adds the entry (symbol, command) 
	 * to the {@link LSystemBuilderImpl#commands} dictionary. 
	 * <p>Multiple commands with the same symbol are not allowed. 
	 * 
	 * @param symbol symbol for the command
	 * @param action name and parameters of the command
	 * 
	 * @return modified LSystemBuilderImpl instance.
	 * 
	 * @throws IllegalArgumentException if the given string is not valid.
	 */
	@Override
	public LSystemBuilder registerCommand(char symbol, String action) {
		String[] actionArray = action.split("\\s+");
		Object value = null;
		

		//check if action string was only white-spaces
		if (actionArray.length == 0) {
			throw new IllegalArgumentException("No command name given.");
		}
		
		//if the command name was "push" or "pop" (first letter 'p') no command argument 
		//should be given
		if (actionArray[0].charAt(0) == 'p' && actionArray.length != 1 
				|| actionArray[0].charAt(0) != 'p' && actionArray.length != 2) {
			throw new IllegalArgumentException("Invalid action string. Was " + action + ".");
		}

		try {
			switch (actionArray[0]) {
			case "draw":
				value = new DrawCommand(Double.parseDouble(actionArray[1]));
				break;
			case "skip":
				value = new SkipCommand(Double.parseDouble(actionArray[1]));
				break;
			case "scale":
				value = new ScaleCommand(Double.parseDouble(actionArray[1]));
				break;
			case "rotate":
				value = new RotateCommand(Double.parseDouble(actionArray[1]));
				break;
			case "push":
				value = new PushCommand();
				break;
			case "pop":
				value = new PopCommand();
				break;
			case "color":
				value = new ColorCommand(new Color(Integer.parseInt(actionArray[1], 16)));
				break;
			default:
				throw new IllegalArgumentException(
						"Invalid action name. Action was " + action + ".");
			}
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(
					"Invalid parameter for action. Action was " + action + ".");
		}

		Character key = Character.valueOf(symbol);

		//This code could be optimized by only calling put method and then comparing dictionary 
		//sizes before and after insertion.
		if (commands.get(key) != null) {
			throw new IllegalArgumentException(
					"Command with symbol " + symbol + " already existed.");
		}
		commands.put(key, value);

		return this;
	}

	/**
	 * Adds new production with given symbol to the dictionary of productions. If the symbol already
	 * existed in the dictionary exception is thrown. Production string should not contain 
	 * white-spaces.
	 * 
	 * @param symbol symbol which is expanded by this production
	 * @param production string which given symbol expands into
	 * 
	 * @return modified LSystemBuilderImpl instance.
	 * 
	 * @throws IllegalArgumentException if the item with key symbol already existed in the 
	 * dictionary {@code productions} or if the production string was invalid.
	 */
	@Override
	public LSystemBuilder registerProduction(char symbol, String production) {
		if (!(production.replaceAll("\\s+", "").equals(production))) {
			throw new IllegalArgumentException(
					"production string contained some white-spaces. Was " + production + ".");
		}

		Character key = Character.valueOf(symbol);
		
		if (productions.get(key) != null) {
			throw new IllegalArgumentException(
					"Production with symbol " + symbol + " already existed.");
		}
		productions.put(Character.valueOf(symbol), production);

		return this;
	}

	/**
	 * Sets the initial direction of the turtle movement by giving its angle with the x axis in 
	 * degrees.
	 * 
	 * @param angle angle of the turtle direction with the x axis in degrees
	 * 
	 * @return modified LSystemBuilderImpl instance.
	 */
	@Override
	public LSystemBuilder setAngle(double angle) {
		this.angle = angle;
		return this;
	}

	/**
	 * Sets the axiom of the L-system. Given string should be without white-spaces.
	 * 
	 * @param axiom axiom of the L-system.
	 * 
	 * @return modified LSystemBuilderImpl instance.
	 * 
	 * @throws IllegalArgumentException if the given axiom contains white-spaces.
	 */
	@Override
	public LSystemBuilder setAxiom(String axiom) {
		if (!(axiom.replaceAll("\\s+", "").equals(axiom))) {
			throw new IllegalArgumentException(
					"axiom string contained some white-spaces. Was " + axiom + ".");
		}
		this.axiom = axiom;
		return this;
	}

	/**
	 * Sets the origin of the turtle. Coordinates should be between 0.0 and 1.0, inclusive.
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * 
	 * @return modified LSystemBuilderImpl instance.
	 * 
	 * @throws IllegalArgumentException if the given coordinates are not between 0.0 and 1.0
	 */
	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		if (x < 0.0 || x > 1.0 || y < 0.0 || y > 1.0) {
			throw new IllegalArgumentException("Invalid origin coordinates, should be between "
					+ "0.0 and 1.0 (inclusive). Was (" + x + ", " + y + ").");
		}
		origin = new Vector2D(x, y);
		return this;
	}

	/**
	 * Sets the unit Length of the turtle. This length should be non-negative.
	 * 
	 * @param unitLength non-negative unitLength of the turtle.
	 * 
	 * @return modified LSystemBuilderImpl instance.
	 * 
	 * @throws IllegalArgumentException if the given length is negative.
	 */
	@Override
	public LSystemBuilder setUnitLength(double unitLength) {
		if (unitLength < 0.0) {
			throw new IllegalArgumentException(
					"unitLength must be non-negative. Was " + unitLength + ".");
		}
		this.unitLength = unitLength;
		return this;
	}


	/**
	 * Sets the scaler of the unitLength. By this factor unitLength will be multiplied every 
	 * iteration.
	 * 
	 * @param unitLengthDegreeScaler scaler of the unitLength by iteration.
	 * 
	 * @return modified LSystemBuilderImpl instance.
	 */
	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
		this.unitLengthDegreeScaler = unitLengthDegreeScaler;
		return this;
	}

}
