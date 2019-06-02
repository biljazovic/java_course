package hr.fer.zemris.java.gui.layouts;

/**
 * Constraints for {@link CalcLayout} layout manager.
 * 
 * @author Bruno Iljazović
 */
public class RCPosition {
	
	/** The row. */
	private int row;
	
	/** The column. */
	private int column;

	/**
	 * Instantiates a new position with the given row and column.
	 *
	 * @param row the row
	 * @param column the column
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * Gets the row.
	 *
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Gets the column.
	 *
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}
}
