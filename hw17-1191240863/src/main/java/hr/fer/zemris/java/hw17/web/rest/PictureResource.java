package hr.fer.zemris.java.hw17.web.rest;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

import hr.fer.zemris.java.hw17.model.Picture;
import hr.fer.zemris.java.hw17.model.PicturesDB;

/**
 * Handles all picture related requests.
 * 
 * @author Bruno Iljazović
 */
@Path("/pictures")
public class PictureResource {
	
	/**
	 * Gets the list of tags in JSON format.
	 *
	 * @return the tag list in json format
	 */
	@Path("/tag")
	@GET
	@Produces("application/json")
	public Response getTagList() {
		String[] tags = PicturesDB.getTags();

		Gson gson = new Gson();
		String jsonText = gson.toJson(tags);
		
		return Response.ok(jsonText).build();
	}
	
	/**
	 * Gets the list of pictures with the given tag, in JSOn format.
	 *
	 * @param tag the tag
	 * @return the list of pictures with the given tag
	 */
	@Path("/tag/{tag}")
	@GET
	@Produces("application/json")
	public Response getByTag(@PathParam("tag") String tag) {
		List<Picture> picturesList = PicturesDB.getByTag(tag);
		Picture[] pictures = picturesList.toArray(new Picture[picturesList.size()]);
		
		Gson gson = new Gson();
		String jsonText = gson.toJson(pictures);
		
		return Response.ok(jsonText).build();
	}
	
	/**
	 * Gets the single thumbnail of the picture with the given file name
	 *
	 * @param fileName the file name
	 * @return the thumbnail of the wanted image
	 */
	@Path("/thumbnail/{fileName}")
	@GET
	@Produces("image/png")
	public Response getThumbnail(@PathParam("fileName") String fileName) {
		byte[] picture = null;
		try {
			picture = PicturesDB.getThumbnail(fileName);
		} catch (IOException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(picture).build();
	}
	
	/**
	 * Gets the full-sized picture with the given file name.
	 *
	 * @param fileName the file name
	 * @return the picture
	 */
	@Path("/picture/{fileName}")
	@GET
	@Produces("image/png")
	public Response getPicture(@PathParam("fileName") String fileName) {
		byte[] picture = null;
		try {
			picture = PicturesDB.getPicture(fileName);
		} catch (IOException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(picture).build();
	}
}
