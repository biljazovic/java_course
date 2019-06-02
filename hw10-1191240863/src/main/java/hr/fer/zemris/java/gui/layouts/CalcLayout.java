package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom layout manager that lays the added components in the 5x7 grid with
 * grid elements (1,1) through (1,5) merged into one component so that there are
 * 31 possible components total.
 * <p> Each row is of the same height and each column is of the same width.
 * <p> Gap between the rows and the columns can be given through the constructor
 * 
 * @author Bruno Iljazović
 */
public class CalcLayout implements LayoutManager2 {
	
	/** The gap between rows and between columns. */
	private int spacer;
	
	/** Number of rows. */
	private final int ROWS = 5;

	/** Number of columns. */
	private final int COLUMNS = 7;
	
	/** Components in this layout. */
	private Component[][] grid;
	
	/**
	 * Instantiates a new layout with gap set to 0.
	 */
	public CalcLayout() {
		this(0);
	}

	/**
	 * Instantiates a new layout with the given gap.
	 *
	 * @param spacer the gap between rows and between columns
	 */
	public CalcLayout(int spacer) {
		if (spacer < 0) throw new CalcLayoutException("Gap cannot be negative");
		this.spacer = spacer;
		grid = new Component[ROWS][COLUMNS];
	}


	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (grid[i][j] == comp) {
					grid[i][j] = null;
				}
			}
		}
	}
	
	/**
	 * type of size (preferred, minimum or maximum)
	 */
	private enum SizeType {
		
		/** The preferred size. */
		PREFERRED,
		
		/** The minimum size. */
		MINIMUM,
		
		/** The maximum size. */
		MAXIMUM
	};
	
	/**
	 * Helper method for calculating preferred, maximum and minimum layout sizes.
	 *
	 * @param parent the parent container
	 * @param type preferred, minimum or maximum
	 * @return the dimension of the layout
	 */
	private Dimension layoutSize(Container parent, SizeType type) {
		double resHeight = 0, resWidth = 0;
		if (type == SizeType.MAXIMUM) {
			resHeight = Integer.MAX_VALUE;
			resWidth = Integer.MAX_VALUE;
		}
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (grid[i][j] == null) continue;

				Dimension compDim;

				if (type == SizeType.PREFERRED) {
					compDim = grid[i][j].getPreferredSize();
				} else if (type == SizeType.MINIMUM) {
					compDim = grid[i][j].getMinimumSize();
				} else {
					compDim = grid[i][j].getMaximumSize();
				}

				if (compDim == null) continue;

				if (type == SizeType.MAXIMUM) {
					resHeight = Math.min(resHeight, compDim.getHeight());
				} else {
					resHeight = Math.max(resHeight, compDim.getHeight());
				}
				
				double width = compDim.getWidth();
				if (i == 0 && j == 0) {
					width = (width - 4 * spacer) / 5;
				}
				
				if (type == SizeType.MAXIMUM) {
					resWidth = Math.min(resWidth, width);
				} else {
					resWidth = Math.max(resWidth, width);
				}
			}
		}
		
		Dimension result = new Dimension();
		Insets insets = parent.getInsets();
		result.setSize(
				COLUMNS * resWidth + (COLUMNS-1) * spacer + (insets.left + insets.right),
				ROWS * resHeight + (ROWS-1) * spacer + (insets.bottom + insets.top)
		);
		
		return result;
	}
		

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return layoutSize(parent, SizeType.PREFERRED);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return layoutSize(parent, SizeType.MINIMUM);
	}

	@Override
	public Dimension maximumLayoutSize(Container parent) {
		return layoutSize(parent, SizeType.MAXIMUM);
	}


	@Override
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		int maxWidth = parent.getWidth() - (insets.left + insets.right);
		int maxHeight = parent.getHeight() - (insets.top + insets.bottom);
		
		int width = (maxWidth - (COLUMNS-1) * spacer) / COLUMNS;
		int height = (maxHeight - (ROWS-1) * spacer) / ROWS;
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (grid[i][j] == null) continue;

				int newWidth = width;
				if (i == 0 && j == 0) {
					newWidth = 5 * width + 4 * spacer;
				}
				int newHeight = height;
				int newX = j * width + j * spacer + insets.left;
				int newY = i * height + i * spacer + insets.top;
				
				grid[i][j].setBounds(newX, newY, newWidth, newHeight);
			}
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if (constraints == null) throw new CalcLayoutException("constraints were null");
		
		RCPosition position = null;
		
		if (constraints instanceof String) {
			position = parsePositionFromString((String) constraints);
		} else if (constraints instanceof RCPosition) {
			position = (RCPosition) constraints;
		} else {
			throw new CalcLayoutException(
					"Invalid constraints type. Was " + constraints.getClass().getName());
		}
		
		int row = position.getRow();
		int column = position.getColumn();
		
		if (!validIndices(row, column)) {
			throw new CalcLayoutException(
					String.format("Invalid constraints: %d, %d", row, column));
		}
		
		if (grid[row-1][column-1] != null) {
			if (grid[row-1][column-1] == comp) return;
			throw new CalcLayoutException("Cannot put multiple components in the same position.");
		}
		
		grid[row-1][column-1] = comp;
	}

	/**
	 * Returns whether the given indices are valid position for component.
	 *
	 * @param row the row
	 * @param column the column
	 * @return true, iff the indices are valid
	 */
	private boolean validIndices(int row, int column) {
		return !(row < 1 || row > ROWS ||  column < 1 || column > COLUMNS ||  
				row == 1 && column > 1 && column < 6);
	}

	/** The pattern for parsing positions from string. */
	private final Pattern pattern = Pattern.compile("\\s*(.*?)\\s*,\\s*(.*?)\\s*");
	
	/**
	 * Parses the position from string.
	 *
	 * @param constraints string "x,y" 
	 * @return the position generated from string
	 */
	private RCPosition parsePositionFromString(String constraints) {
		Matcher matcher = pattern.matcher(constraints);
		if (!matcher.matches()) {
			throw new CalcLayoutException("Invalid constraints string: " + constraints);
		}
		int row, column;
		try {
			row = Integer.parseInt(matcher.group(1));
			column = Integer.parseInt(matcher.group(2));
		} catch(NumberFormatException ex) {
			throw new CalcLayoutException("Invalid constraints string: ", ex);
		}
		return new RCPosition(row, column);
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
	}

}
