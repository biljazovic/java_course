package hr.fer.zemris.java.hw06.shell.parser;

/**
 * Provides a method to modify a NameBuilderInfo object.
 * 
 * @author Bruno Iljazović
 */
public interface NameBuilder {

	/**
	 * Modifies the given NameBuilderInfo object.
	 * @param info object to be modified
	 */
	void execute(NameBuilderInfo info);
}
