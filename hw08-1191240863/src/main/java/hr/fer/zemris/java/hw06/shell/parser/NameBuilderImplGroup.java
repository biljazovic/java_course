package hr.fer.zemris.java.hw06.shell.parser;

/**
 * Modifies the NameBuilderInfo by appending a capturing group while possibly
 * left-padding it with spaces or zeros.
 * @author Bruno Iljazović
 */
public class NameBuilderImplGroup implements NameBuilder {
	
	/** The group index. */
	private int groupIndex;
	
	/** The minimum width of appended string. */
	private int minWidth;
	
	/** True if the padding is made from zeros, false if it is from spaces. */
	private boolean fillWithZeros;
	
	/**
	 * Instantiates a new name builder with no minimum width.
	 *
	 * @param groupIndex the group index
	 */
	public NameBuilderImplGroup(int groupIndex) {
		this(groupIndex, 0, false);
	}

	/**
	 * Instantiates a new name builder with minimum width.
	 *
	 * @param groupIndex the group index
	 * @param minWidth the minimum width
	 * @param fillWithZeros whether to make padding from zeros or spaces
	 */
	public NameBuilderImplGroup(int groupIndex, int minWidth, boolean fillWithZeros) {
		if (groupIndex < 0 || minWidth < 0) {
			throw new IllegalArgumentException("group index and minWidth must be non-negative "
					+ "Was: " + groupIndex + ", " + minWidth);
		}
		this.groupIndex = groupIndex;
		this.minWidth = minWidth;
		this.fillWithZeros = fillWithZeros;
	}


	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.hw06.shell.parser.NameBuilder#execute(hr.fer.zemris.java.hw06.shell.parser.NameBuilderInfo)
	 */
	@Override
	public void execute(NameBuilderInfo info) {
		String group = info.getGroup(groupIndex);
		

		if (minWidth > group.length()) {
			info.getStringBuilder().append(repeatChar(
					fillWithZeros ? '0' : ' ', 
					minWidth - group.length()
			));
		}
		
		info.getStringBuilder().append(group);
	}

	/**
	 * Returns the string made by repeating the given character given number of times.
	 *
	 * @param ch the character to be repeated
	 * @param count the count
	 * @return character repeated count number of times
	 */
	private static String repeatChar(char ch, int count) {
		StringBuilder result = new StringBuilder(count);
		for (int i = 0; i < count; ++i) {
			result.append(ch);
		}
		return result.toString();
	}
	
}
