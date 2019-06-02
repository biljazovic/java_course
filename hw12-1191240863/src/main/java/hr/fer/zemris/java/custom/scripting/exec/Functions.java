package hr.fer.zemris.java.custom.scripting.exec;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiConsumer;
import static java.util.Map.entry;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Provides number of functions that can be used in Smart Script scripts.
 * Functions can be accessed directly, or by their keys using
 * {@link Functions#get(String)} method.
 * 
 * @author Bruno Iljazović
 */
public class Functions {
	
	/** Calculates sin of a number in degrees. */
	public static BiConsumer<Stack<Object>, RequestContext> SIN = (stack, context) -> {
		double value = ValueWrapper.transform(stack.pop()).doubleValue();
		value = Math.sin(Math.toRadians(value));
		stack.push(Double.valueOf(value));
	};

	/**
	 * Formats the given decima number using the given format compatible with
	 * {@link DecimalFormat}
	 */
	public static BiConsumer<Stack<Object>, RequestContext> DECFMT = (stack, context) -> {
		String format =  (String) stack.pop();
		double value = ValueWrapper.transform(stack.pop()).doubleValue();
		stack.push((new DecimalFormat(format)).format(value));
	};

	/** Adds two numbers. */
	public static BiConsumer<Stack<Object>, RequestContext> ADD = (stack, context) -> {
		ValueWrapper value2 = new ValueWrapper(stack.pop());
		ValueWrapper value1 = new ValueWrapper(stack.pop());
		value1.add(value2.getValue());
		stack.push(value1.getValue());
	};
	
	/** Multiplies two numbers. */
	public static BiConsumer<Stack<Object>, RequestContext> MULTIPLY = (stack, context) -> {
		ValueWrapper value2 = new ValueWrapper(stack.pop());
		ValueWrapper value1 = new ValueWrapper(stack.pop());
		value1.multiply(value2.getValue());
		stack.push(value1.getValue());
	};

	/** Divides two numbers. */
	public static BiConsumer<Stack<Object>, RequestContext> DIVIDE = (stack, context) -> {
		ValueWrapper value2 = new ValueWrapper(stack.pop());
		ValueWrapper value1 = new ValueWrapper(stack.pop());
		value1.divide(value2.getValue());
		stack.push(value1.getValue());
	};

	/** Subtracts second number from the first one. */
	public static BiConsumer<Stack<Object>, RequestContext> SUBTRACT = (stack, context) -> {
		ValueWrapper value2 = new ValueWrapper(stack.pop());
		ValueWrapper value1 = new ValueWrapper(stack.pop());
		value1.subtract(value2.getValue());
		stack.push(value1.getValue());
	};

	/** Duplicates the given number by pushing it on the stack twice. */
	public static BiConsumer<Stack<Object>, RequestContext> DUP = (stack, context) -> {
		stack.push(stack.peek());
	};

	/** Swaps the top two objects from the stack. */
	public static BiConsumer<Stack<Object>, RequestContext> SWAP = (stack, context) -> {
		Object value1 = stack.pop();
		Object value2 = stack.pop();
		stack.push(value1);
		stack.push(value2);
	};

	/** Sets the mime type to be the given string. */
	public static BiConsumer<Stack<Object>, RequestContext> SETMIMETYPE = (stack, context) -> {
		context.setMimeType(stack.pop().toString());
	};

	/** Gets the parameter with the given name and given default value */
	public static BiConsumer<Stack<Object>, RequestContext> PARAMGET = (stack, context) -> {
		Object defaultValue = stack.pop();
		String name = stack.pop().toString();
		String value = context.getParameter(name);
		stack.push(value == null ? defaultValue : value);
	};

	/** Gets the persistent parameter with the given name and given default value */
	public static BiConsumer<Stack<Object>, RequestContext> PPARAMGET = (stack, context) -> {
		Object defaultValue = stack.pop();
		String name = stack.pop().toString();
		String value = context.getPersistentParameter(name);
		stack.push(value == null ? defaultValue : value);
	};

	/** Sets the persistent parameter with given name and value. */
	public static BiConsumer<Stack<Object>, RequestContext> PPARAMSET = (stack, context) -> {
		String name = stack.pop().toString();
		String value = stack.pop().toString();
		context.setPersistentParameter(name, value);
	};

	/** Deletes the persistent parameter with the given name. */
	public static BiConsumer<Stack<Object>, RequestContext> PPARAMDEL = (stack, context) -> {
		String name = stack.pop().toString();
		context.removePersistentParameter(name);
	};

	/** Gets the temporary parameter with the given name and given default value */
	public static BiConsumer<Stack<Object>, RequestContext> TPARAMGET = (stack, context) -> {
		Object defaultValue = stack.pop();
		String name = stack.pop().toString();
		String value = context.getTemporaryParameter(name);
		stack.push(value == null ? defaultValue : value);
	};

	/** Sets the temporary parameter with given name and value. */
	public static BiConsumer<Stack<Object>, RequestContext> TPARAMSET = (stack, context) -> {
		String name = stack.pop().toString();
		String value = stack.pop().toString();
		context.setTemporaryParameter(name, value);
	};

	/** Deletes the temporary parameter with the given name. */
	public static BiConsumer<Stack<Object>, RequestContext> TPARAMDEL = (stack, context) -> {
		String name = stack.pop().toString();
		context.removeTemporaryParameter(name);
	};

	/** The functions. */
	private static Map<String, BiConsumer<Stack<Object>, RequestContext>> functions = Map.ofEntries(
		entry("+", ADD),
		entry("-", SUBTRACT),
		entry("*", MULTIPLY),
		entry("/", DIVIDE),
		entry("sin", SIN),
		entry("decfmt", DECFMT),
		entry("dup", DUP),
		entry("swap", SWAP),
		entry("setMimeType", SETMIMETYPE),
		entry("paramGet", PARAMGET),
		entry("pparamGet", PPARAMGET),
		entry("pparamSet", PPARAMSET),
		entry("pparamDel", PPARAMDEL),
		entry("tparamGet", TPARAMGET),
		entry("tparamSet", TPARAMSET),
		entry("tparamDel", TPARAMDEL)
	);
	
	/**
	 * Gets the function with the given name.
	 *
	 * @param name the name of the function
	 * @return the function, or null if the function with the given name doesn't exist
	 */
	public static BiConsumer<Stack<Object>, RequestContext> get(String name) {
		return functions.get(name);
	}
}
