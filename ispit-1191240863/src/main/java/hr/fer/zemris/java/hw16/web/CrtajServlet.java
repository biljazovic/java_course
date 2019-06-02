package hr.fer.zemris.java.hw16.web;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw16.jvdraw.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw16.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.drawing.DrawingModelImpl;
import hr.fer.zemris.java.hw16.jvdraw.drawing.GeometricalObjectPainter;
import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;
import hr.fer.zemris.java.hw16.jvdraw.objects.Polygon;

@WebServlet("/crtaj")
public class CrtajServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String jvd = req.getParameter("jvd");
		
		DrawingModel drawingModel = new DrawingModelImpl();
		List<GeometricalObject> objects = null;
		try {
			objects = parseJVDFile(jvd);
		} catch(IllegalArgumentException ex) {
			//req.getRequestDispatcher("/error.html").forward(req, resp);
			req.setAttribute("errorMessage", ex.getMessage());
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		for (GeometricalObject o : objects) {
			drawingModel.add(o);
		}

		GeometricalObjectBBCalculator bbcalc = new GeometricalObjectBBCalculator();
		for (int i = 0; i < drawingModel.getSize(); i++) {
			drawingModel.getObject(i).accept(bbcalc);
		}
		Rectangle box = bbcalc.getBoundingBox();
		BufferedImage image = new BufferedImage(box.width, box.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();
		g.translate(-box.x, -box.y);
		GeometricalObjectPainter painter = new GeometricalObjectPainter(g);
		for (int i = 0; i < drawingModel.getSize(); i++) {
			drawingModel.getObject(i).accept(painter);
		}
		g.dispose();
		
		resp.setContentType("image/png");
		OutputStream out = resp.getOutputStream();
		ImageIO.write(image, "png", out);
		out.close();
	}

	private List<GeometricalObject> parseJVDFile(String jvd) {
		List<GeometricalObject> objects = new ArrayList<>();
		
		for (String line : jvd.split("\n")) {
			line = line.trim();
			if (line.isEmpty()) continue;
			String[] lineArr = line.split("[ ]+");
			lineArr[0] = lineArr[0].toUpperCase();
			switch(lineArr[0]) {
			case "LINE":
				if (lineArr.length != 8) throw new IllegalArgumentException("Invalid number of values.");
				objects.add(Line.fromJVDEntry(lineArr));
				break;
			case "CIRCLE":
				if (lineArr.length != 7) throw new IllegalArgumentException("Invalid number of values.");
				objects.add(Circle.fromJVDEntry(lineArr));
				break;
			case "FCIRCLE":
				if (lineArr.length != 10) throw new IllegalArgumentException("Invalid number of values.");
				objects.add(FilledCircle.fromJVDEntry(lineArr));
				break;
			case "FPOLY":
				int n = lineArr.length - 7;
				if (n <= 0 || n % 2 > 0) throw new IllegalArgumentException("Invalid number of values.");
				objects.add(Polygon.fromJVDEntry(lineArr));
				break;
			default:
				throw new IllegalArgumentException("Invalid name of object");
			}
		}
		return objects;
	}
}
