package hr.fer.zemris.java.hw06.shell.parser;

/**
 * Appends the constant string to the NameBuilderInfo object.
 * @author Bruno Iljazović
 */
public class NameBuilderImplConstString implements NameBuilder {
	
	/** The constant string. */
	private String constString;
	
	/**
	 * Instantiates a new name builder
	 *
	 * @param constString the constant string
	 */
	public NameBuilderImplConstString(String constString) {
		this.constString = constString;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw06.shell.parser.NameBuilder#execute(hr.fer.zemris.java.hw06.shell.parser.NameBuilderInfo)
	 */
	@Override
	public void execute(NameBuilderInfo info) {
		info.getStringBuilder().append(constString);
	}
	
	
}
