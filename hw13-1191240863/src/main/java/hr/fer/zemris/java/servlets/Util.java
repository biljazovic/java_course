package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides utility functions that read some data from the provided files.
 * 
 * @author Bruno Iljazović
 */
public class Util {
	
	/**
	 * Reads the definitions file. Each line in the file should be of this format:
	 * <pre>ID  NAME_OF_THE_BAND  LINK_TO_THE_REPRESENTATIVE_SONG</pre>
	 * where the elements are separated by a tab. If the file is invalid, null is
	 * returned. Otherwise, map of (ID, {@link Band}) is returned with name and song
	 * link written in the Band objects. Points value is kept at null.
	 * <p>Ids should be unique.
	 *
	 * @param fileName
	 *           	path to definitions file
	 * @return the map of (ID, Band) entries
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Map<Integer,Band> readDefinitions(String fileName) throws IOException {
		
		Map<Integer,Band> bands = new HashMap<>();

		for (String line : Files.readAllLines(Paths.get(fileName))) {
			line = line.trim();
			if (line.isEmpty()) continue;
			String[] lineSplit = line.split("\\t");
			if (lineSplit.length != 3) {
				return null;
			}

			try {
				Integer id = Integer.parseInt(lineSplit[0].trim());
				Band band = new Band(
						lineSplit[1].trim(),
						lineSplit[2].trim(),
						null
				);
				if (bands.containsKey(id)) {
					return null;
				}
				bands.put(id, band);
			} catch(NumberFormatException ex) {
				return null;
			}
		}
		
		return bands;
	}
	
	/**
	 * Reads the result file. Each line in the file should be of the following
	 * format:
	 * <pre> ID  POINTS </pre>
	 * where the elements are separated by tabs.If the file is invalid, empty map is
	 * returned. Otherwise, map of (ID, {@link Band}) pairs is returned with points
	 * written in the points membe of Band objects. Other members are kept at null.
	 * <p>
	 * Ids should be unique.
	 *
	 * @param fileName
	 *            path to results file
	 * @return the map of (ID, Band) entries
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Map<Integer,Band> readResults(String fileName) throws IOException {
		
		Map<Integer,Band> results = new HashMap<>();
		
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(fileName));
		} catch(NoSuchFileException ex) {
			return new HashMap<>();
		}
		
		for (String line : lines) {
			line = line.trim();
			if (line.isEmpty()) continue;
			String[] lineSplit = line.split("\\t");
			if (lineSplit.length != 2) {
				return new HashMap<>();
			}
			
			try {
				Integer id = Integer.parseInt(lineSplit[0].trim());
				Band result = new Band(
						null, 
						null,
						Integer.parseInt(lineSplit[1].trim())
				);
				if (results.containsKey(id)) {
					return new HashMap<>();
				}
				results.put(id, result);
			} catch(NumberFormatException ex) {
				return new HashMap<>();
			}
		}
		
		return results;
	}
	
	/**
	 * Read definitions and results.
	 *
	 * @param definitionFile the definition file
	 * @param resultFile the result file
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Map<Integer, Band> readDefinitionsAndResults(String definitionFile,
			String resultFile) throws IOException {
		Map<Integer, Band> definitions = readDefinitions(definitionFile);
		Map<Integer, Band> results = readResults(resultFile);
		
		if (definitions == null) return null;
		
		for (Map.Entry<Integer, Band> entry : definitions.entrySet()) {
			Band result = results.get(entry.getKey());
			Integer points = result == null ? 0 : result.points;
			entry.getValue().points = points;
		}
		
		return definitions;
	}
	
	/**
	 * String and a number. Natural ordering is by natural ordering of numbers.
	 */
	public static class StrAndInt implements Comparable<StrAndInt> {
		
		/** The string. */
		private String string;
		
		/** The number. */
		private Integer number;

		/**
		 * Instantiates a new string and number pair
		 *
		 * @param string the string
		 * @param number the number
		 */
		public StrAndInt(String string, Integer number) {
			this.string = string;
			this.number = number;
		}

		/**
		 * Gets the string.
		 *
		 * @return the string
		 */
		public String getString() {
			return string;
		}

		/**
		 * Gets the number.
		 *
		 * @return the number
		 */
		public Integer getNumber() {
			return number;
		}

		@Override
		public int compareTo(StrAndInt o) {
			return Integer.compare(number, o.number);
		}
	}
	
	/**
	 * Data for a band.
	 */
	public static class Band {
		
		/** The name. */
		private String name;
		
		/** The song link. */
		private String songLink;
		
		/** The points. */
		private Integer points;

		/**
		 * Instantiates a new band.
		 *
		 * @param name
		 *            the name
		 * @param songLink
		 *            the song link
		 * @param points
		 *            the points
		 */
		public Band(String name, String songLink, Integer points) {
			super();
			this.name = name;
			this.songLink = songLink;
			this.points = points;
		}

		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Gets the song link.
		 *
		 * @return the song link
		 */
		public String getSongLink() {
			return songLink;
		}

		/**
		 * Gets the points.
		 *
		 * @return the points
		 */
		public Integer getPoints() {
			return points;
		}
	}
}
