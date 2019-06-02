package hr.fer.zemris.java.hw17.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletContextEvent;

/**
 * Provides methods for accessing the pictures. At the start of application, 
 * {@link PicturesDB#initialise(ServletContextEvent)} should be called.
 * <p>Thumbnails are generated from original pictures only when they are first accessed.
 * 
 * @author Bruno Iljazović
 */
public class PicturesDB {
	
	/** The directory with all the pictures. */
	private static final String picturesDir = "WEB-INF/slike";
	
	/** The directory with pictures in smaller format. */
	private static final String thumbnailDir = "WEB-INF/thumbnails";
	
	/** The file with a list of all the pictures and their tags and descriptions. */
	private static final String pictureListFile = "WEB-INF/opisnik.txt";
	
	/** The width of thumbnail images. */
	private static final int THUMBNAIL_WIDTH = 150;

	/** The height of thumbnail iamges. */
	private static final int THUMBNAIL_HEIGHT = 150;
	
	/** The path to the pictures directory. */
	private static Path picturesDirPath;

	/** The path to the thumbnail directory. */
	private static Path thumbnailDirPath;
	
	/**
	 * Initialises the pictures database.
	 *
	 * @param sce servlet context event
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void initialise(ServletContextEvent sce) throws IOException {
		picturesDirPath = Paths.get(sce.getServletContext().getRealPath(picturesDir));
		thumbnailDirPath = Paths.get(sce.getServletContext().getRealPath(thumbnailDir));
		Path pictureList = Paths.get(sce.getServletContext().getRealPath(pictureListFile));

		if (!Files.isDirectory(picturesDirPath) || !Files.isRegularFile(pictureList)) {
			return;
		}
		
		List<String> lines = Files.readAllLines(pictureList);
		
		if (lines.size() % 3 != 0) {
			return;
		}

		for (int i = 0; i < lines.size() / 3; i++) {
			System.out.println(i);
			String[] rawTags = lines.get(3*i + 2).split(",");
			Set<String> tags = new HashSet<>();
			for (String tag : rawTags) {
				tag = tag.trim();
				if (tag.isEmpty()) continue;
				tags.add(tag.toLowerCase());
			}
			Picture picture = new Picture(
					lines.get(3*i).trim(), 
					lines.get(3*i+1), 
					tags.toArray(new String[tags.size()])
			);
			
			pictures.add(picture);
			for (String tag : tags) {
				if (picturesByTags.get(tag) == null) {
					picturesByTags.put(tag, new ArrayList<>());
				}
				picturesByTags.get(tag).add(picture);
			}
		}
	}
	
	/** Pictures by tags. */
	private static Map<String, List<Picture>> picturesByTags = new HashMap<>();
	
	/** List of pictures. */
	private static List<Picture> pictures = new ArrayList<>();
	
	/**
	 * Gets all pictures with the given tag.
	 *
	 * @param tag the tag
	 * @return the by tag
	 */
	public static List<Picture> getByTag(String tag) {
		List<Picture> result = picturesByTags.get(tag);
		return result == null ? new ArrayList<>() : result;
	}
	
	/**
	 * Gets the array of tags.
	 *
	 * @return the tags
	 */
	public static String[] getTags() {
		return picturesByTags.keySet().toArray(new String[picturesByTags.size()]);
	}
	
	/**
	 * Gets the thumbnail of the picture with the given file name.
	 *
	 * @param fileName the file name of the picture.
	 * @return the thumbnail of the wanted picture
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] getThumbnail(String fileName) throws IOException {
		if (!Files.isDirectory(thumbnailDirPath)) {
			Files.createDirectory(thumbnailDirPath);
		}

		Path thumbnailPath = thumbnailDirPath.resolve(fileName);

		if (!Files.exists(thumbnailPath)) {
			Path picturePath = picturesDirPath.resolve(fileName);
			BufferedImage img = ImageIO.read(picturePath.toFile());
			BufferedImage scaledImg = new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, img.getType());
			Graphics2D g2d = scaledImg.createGraphics();
			g2d.drawImage(img, 0, 0, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, null);
			g2d.dispose();
			ImageIO.write(scaledImg, "jpg", thumbnailPath.toFile());
		}

		return Files.readAllBytes(thumbnailPath);
	}
	
	/**
	 * Gets the full-sized picture with the given file name.
	 *
	 * @param fileName the file name
	 * @return the picture
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] getPicture(String fileName) throws IOException {
		Path picturePath = picturesDirPath.resolve(fileName);
		
		return Files.readAllBytes(picturePath);
	}
}
