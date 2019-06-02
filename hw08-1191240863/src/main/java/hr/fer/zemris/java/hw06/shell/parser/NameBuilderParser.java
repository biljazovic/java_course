package hr.fer.zemris.java.hw06.shell.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Splits the given expression into groups ${...} and everything between them.
 * ${ cannot be without the closing brace.
 * 
 * @author Bruno Iljazović
 */
public class NameBuilderParser {
	
	/**
	 * The NameBuilder that keeps reference to all the NameBuilders that modify the
	 * NameBuilderInfo object
	 */
	NameBuilder multiBuilder;

	/**
	 * Instantiates a new NameBuilder parser.
	 *
	 * @param expression the expression
	 */
	public NameBuilderParser(String expression) {
		multiBuilder = new NameBuilderImplMultiBuilder(parse(expression));
	}

	/**
	 * Gets the name builder.
	 *
	 * @return the name builder
	 */
	public NameBuilder getNameBuilder() {
		return multiBuilder;
	}

	/**
	 * Parses the given expression into the NameBuilders that each modify a
	 * NameBuilderInfo object.
	 *
	 * @param expression
	 *            the expression to be parsed
	 * @return the list of NameBuilders - result of parsing
	 */
	private List<NameBuilder> parse(String expression) {
		Matcher matcher = Pattern.compile("\\$\\{(.*?)\\}").matcher(expression);

		int end = 0;
		List<NameBuilder> builders = new ArrayList<>();

		while (matcher.find()) {
			if (matcher.start() > end) {
				builders.add(
						new NameBuilderImplConstString(expression.substring(end, matcher.start())));
			}
			
			String group = matcher.group(1);
			String[] groupArray = group.split(",", 2);
			
			String first = groupArray[0].trim();
			Integer firstInt = null;
			try {
				firstInt = Integer.parseInt(first);
			} catch(NumberFormatException ex) {
				throw new IllegalArgumentException(ex);
			}

			if (groupArray.length == 2) {
				String second = groupArray[1].trim();
				Integer secondInt = null;
				try {
					secondInt = Integer.parseInt(second);
				} catch(NumberFormatException ex) {
					throw new IllegalArgumentException(ex);
				}
				
				builders.add(new NameBuilderImplGroup(firstInt, secondInt, second.charAt(0) == '0'));
			} else {
				builders.add(new NameBuilderImplGroup(firstInt));
			}
			
			end = matcher.end();
		}
		
		//check for unclosed ${ after all groups
		matcher = Pattern.compile("\\$\\{").matcher(expression);

		//start at the end of the last group, or 0 if there were no groups
		if (matcher.find(end)) {
			throw new IllegalArgumentException("Unclosed ${ at " + matcher.start() + ".");
		}
		
		//combine everything after last group
		if (end < expression.length()) {
			builders.add(new NameBuilderImplConstString(expression.substring(end)));
		}
		
		return builders;
	}
}
