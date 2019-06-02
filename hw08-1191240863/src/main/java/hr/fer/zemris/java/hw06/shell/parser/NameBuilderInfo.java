package hr.fer.zemris.java.hw06.shell.parser;

/**
 * Combines one StringBuilder and collection of capturing groups that can be
 * accessed by index.
 * 
 * @author Bruno Iljazović
 */
public interface NameBuilderInfo {

	/**
	 * Gets the string builder.
	 *
	 * @return the string builder
	 */
	StringBuilder getStringBuilder();

	/**
	 * Gets the capturing group at the given index.
	 *
	 * @param index the index of the group
	 * @return the group at the given index
	 */
	String getGroup(int index);
}
