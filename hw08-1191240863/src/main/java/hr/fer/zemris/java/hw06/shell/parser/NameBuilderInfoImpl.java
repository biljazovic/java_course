package hr.fer.zemris.java.hw06.shell.parser;

import java.util.regex.Matcher;

/**
 * Extracts the capturing groups from the given matcher and provides access to
 * the groups and a modifiable StringBuilder.
 * 
 * @author Bruno Iljazović
 */
public class NameBuilderInfoImpl implements NameBuilderInfo {
	
	/** The modifiable string builder. */
	private StringBuilder stringBuilder;
	
	/** The capturing groups. */
	private String[] groups;
	
	/**
	 * Instantiates a new NameBuilderInfo
	 *
	 * @param matcher the matcher from which to extract groups
	 */
	public NameBuilderInfoImpl(Matcher matcher) {
		stringBuilder = new StringBuilder();
		groups = new String[matcher.groupCount() + 1];
		for (int i = 0; i < matcher.groupCount() + 1; ++i) {
			groups[i] = matcher.group(i);
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw06.shell.parser.NameBuilderInfo#getStringBuilder()
	 */
	@Override
	public StringBuilder getStringBuilder() {
		return stringBuilder;
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw06.shell.parser.NameBuilderInfo#getGroup(int)
	 */
	@Override
	public String getGroup(int index) {
		if (index < 0 || index >= groups.length) {
			throw new IllegalArgumentException("index was " + index);
		}
		return groups[index];
	}

}
