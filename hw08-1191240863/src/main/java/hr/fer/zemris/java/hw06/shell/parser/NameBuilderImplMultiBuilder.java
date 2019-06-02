package hr.fer.zemris.java.hw06.shell.parser;

import java.util.List;

/**
 * Keeps the list of NameBuilders.
 * Modifes the NameBuilderInfo object by calling {@link NameBuilder#execute} method on that object
 * from every NameBuilder from the list.
 * 
 * @author Bruno Iljazović
 */
public class NameBuilderImplMultiBuilder implements NameBuilder {
	
	/** The builders. */
	private List<NameBuilder> builders;
	
	/**
	 * Instantiates a new name builder with the list of NameBuilders.
	 *
	 * @param builders the builders
	 */
	public NameBuilderImplMultiBuilder(List<NameBuilder> builders) {
		this.builders = builders;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw06.shell.parser.NameBuilder#execute(hr.fer.zemris.java.hw06.shell.parser.NameBuilderInfo)
	 */
	@Override
	public void execute(NameBuilderInfo info) {
		builders.forEach(builder -> builder.execute(info));
	}
	
}
