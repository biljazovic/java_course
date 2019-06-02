package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Main program that starts the GUI. It takes one argument from the command line
 * - path to the text file. Text file has to contain at least 6 lines that
 * describe one {@link BarChart}. Example:
 * 
 * <pre>Number of people in the car 
 * Frequency 
 * 1,8 2,20 3,22 4,10 5,4 
 * 0 
 * 22 
 * 2</pre>
 * 
 * After reading the given file, chart is drawn.
 * 
 * @author Bruno Iljazović
 */
public class BarChartDemo extends JFrame {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new chart demo.
	 *
	 * @param filename the configuration file name
	 * @param barChart generated BarChart object
	 */
	public BarChartDemo(String filename, BarChart barChart) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("BarChartDemo");
		setLocation(100, 100);
		setSize(700, 500);
		getRootPane().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		initGUI(filename, barChart);
	}
	
	/**
	 * Initializes the GUI.
	 *
	 * @param filename the configuration file name
	 * @param barChart generated BarChart object.
	 */
	private void initGUI(String filename, BarChart barChart) {

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout( ));
		
		JLabel fileLabel = new JLabel(filename, SwingConstants.CENTER);
		cp.add(fileLabel, BorderLayout.PAGE_START);
		
		final BarChartComponent comp = new BarChartComponent(barChart);
		
		cp.add(comp, BorderLayout.CENTER);
	}
	
	/** Number of lines read in the configuration file. */
	private static final int NUM_LINES = 6;
	
	
	/** Maximum number of y coordinates. */
	private static final int MAX_Y_COORDS = 1000;

	/**
	 * This method is called when the program is run.
	 *
	 * @param args the command line arguments. First argument should be a path to text file.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			JOptionPane.showMessageDialog(null, "No input file given", "Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		
		List<String> lines = null;
		
		try {
			lines = Files.lines(Paths.get(args[0])).limit(NUM_LINES).collect(Collectors.toList());
		} catch(IOException ex) {
			JOptionPane.showMessageDialog(null, "Error while reading the file " + args[0], "Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		
		BarChart barChart = null;
		
		try {
			barChart = parse(lines);
		} catch(IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		
		BarChart barChartFinal = barChart;
		
		SwingUtilities.invokeLater(() -> {
			BarChartDemo window = new BarChartDemo(
					Paths.get(args[0]).toAbsolutePath().normalize().toString(),
					barChartFinal
			);
			window.setVisible(true);
		});
	}
	
	/**
	 * Parses the given list of strings into a BarChart object.
	 *
	 * @param lines the list of strings
	 * @return generated BarChart object
	 * @throws IllegalArgumentException if the given strings are not valid
	 */
	private static BarChart parse(List<String> lines) {
		if (lines.size() != NUM_LINES) {
			throw new IllegalArgumentException("Not enough lines in a file!");
		}
		Pattern pattern = Pattern.compile("\\s*((-|\\d)+),((-|\\d)+)(\\s*)");
		
		Matcher matcher = pattern.matcher(lines.get(2));
		
		int end = 0;
		
		List<XYValue> values = new ArrayList<>();

		while (matcher.find()) {
			try {
				values.add(new XYValue(
						Integer.parseInt(matcher.group(1)), 
						Integer.parseInt(matcher.group(3))
				));
			} catch(NumberFormatException ex) {
				throw new IllegalArgumentException("Illegal (x,y) values");
			}
				
			if (matcher.start() != end) {
				throw new IllegalArgumentException("Illegal (x,y) values");
			}
			end = matcher.end();
		}
		if (end != lines.get(2).length()) {
			throw new IllegalArgumentException("Illegal (x, y) values");
		}
		
		int yMin, yMax, yDelta;
		
		try {
			yMin = Integer.parseInt(lines.get(3).trim());
			yMax = Integer.parseInt(lines.get(4).trim());
			yDelta = Integer.parseInt(lines.get(5).trim());
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException("Illegal y values");
		}

		Collections.sort(values, (e,f)->{
			if (e.x < f.x) return -1;
			else if (e.x == f.x) return 0;
			return 1;
		});
		
		if (yMin > yMax) {
			int t = yMin;
			yMin = yMax;
			yMax = t;
		}
		
		if (yDelta <= 0) throw new IllegalArgumentException("illegal y delta values");
		
		if ((yMax - yMin) / yDelta > MAX_Y_COORDS) 
			throw new IllegalArgumentException("Too many y coordinates to show");
		
		return new BarChart(values, lines.get(0), lines.get(1), yMin, yMax, yDelta);
	}

}
