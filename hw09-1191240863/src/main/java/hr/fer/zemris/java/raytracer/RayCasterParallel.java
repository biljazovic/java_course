package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Simplified ray-tracer for rendering of 3D scenes.
 * <p>
 * Predefined scene is drawn, no user input is required.
 * <p>
 * For lighting, Phong model is used.
 * <p>
 * User can resize the created window and scene will be redrawn each time. But,
 * same view is always drawn, user cannot change the position of the observer or
 * any other components.
 * <p> This program uses multi-threading
 * 
 * @author Bruno Iljazović
 */
public class RayCasterParallel {

	/**
	 * The main method that is called when the program is run.
	 *
	 * @param args the command line arguments, not used here
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(
				getIrayTracerProducer(), 
				new Point3D(10, 0, 0), new Point3D(0, 0, 0), new Point3D(0, 0, 10),
				20, 20
		);
	}
	
	/**
	 * Gets the Object that is capable of producing a scene snapshot using a
	 * ray-casting algorithm.
	 *
	 * @return the scene snapshot producer
	 */
	public static IRayTracerProducer getIrayTracerProducer() {
		return new IRayTracerProducer() {
			
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal,
					double vertical, int width, int height, long requestNo,
					IRayTracerResultObserver observer) {

				System.out.println("Započinjem izračune...");

				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				Point3D zAxis = view.sub(eye).normalize();
				Point3D yAxis = viewUp.normalize()
						.sub(zAxis.scalarMultiply(zAxis.scalarProduct(viewUp.normalize())))
						.normalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();
				Point3D screenCorner = view
						.sub(xAxis.scalarMultiply(horizontal / 2))
						.add(yAxis.scalarMultiply(vertical / 2));
				
				Scene scene = RayTracerViewer.createPredefinedScene();
					
				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(new ForkTrace(height, width, eye, screenCorner, xAxis, yAxis,
						horizontal, vertical, red, green, blue, 0, height - 1, scene));
				pool.shutdown();
				
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}
	
	/**
	 * A recursive resultless task which generates colors of the specific section of
	 * the image.
	 */
	public static class ForkTrace extends RecursiveAction {
		
		/**
		 * serial UID
		 */
		private static final long serialVersionUID = 5415834214898070918L;

		/** The height of the image */
		int height;
		
		/** The width of the image*/
		int width;
		
		/** The eye. */
		Point3D eye;
		
		/** The screen corner. */
		Point3D screenCorner;
		
		/** The x axis. */
		Point3D xAxis;
		
		/** The y axis. */
		Point3D yAxis;
		
		/** The width of the observed space. */
		double horizontal;
		
		/** The height of the observed space. */
		double vertical;
		
		/** The red components of the image. */
		short[] red;
		
		/** The green components of the image. */
		short[] green;
		
		/** The blue components of the image. */
		short[] blue;
		
		/** The minimum y which this method generates colors for. */
		int yMin;
		
		/** The maximum y which this method generates colors for. */
		int yMax;
		
		/** The scene. */
		Scene scene;
		
		/**
		 * maximum difference between yMin and yMax so that this task is split into more
		 * tasks
		 */
		protected static final int threshold = 16;
		
		/**
		 * Instantiates a new fork trace.
		 *
		 * @param height the height of the image
		 * @param width the width of the image
		 * @param eye the eye
		 * @param screenCorner the screen corner
		 * @param xAxis the x axis
		 * @param yAxis the y axis
		 * @param horizontal the width of the observed space
		 * @param vertical the height of the observed space
		 * @param red the red components of the image
		 * @param green the green components of the image
		 * @param blue the blue components of the image
		 * @param yMin the minimum y
		 * @param yMax the maximum y
		 * @param scene the scene
		 */
		public ForkTrace(int height, int width, Point3D eye, Point3D screenCorner, Point3D xAxis,
				Point3D yAxis, double horizontal, double vertical, short[] red, short[] green,
				short[] blue, int yMin, int yMax, Scene scene) {
			this.height = height;
			this.width = width;
			this.eye = eye;
			this.screenCorner = screenCorner;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.yMin = yMin;
			this.yMax = yMax;
			this.scene = scene;
		}

		/* (non-Javadoc)
		 * @see java.util.concurrent.RecursiveAction#compute()
		 */
		@Override
		protected void compute() {
			if (yMax - yMin + 1 <= threshold) {
				computeDirectly();
				return;
			}
			int split = (yMin + yMax) / 2;
			invokeAll(
					new ForkTrace(height, width, eye, screenCorner, xAxis, yAxis, horizontal,
							vertical, red, green, blue, yMin, split, scene),
					new ForkTrace(height, width, eye, screenCorner, xAxis, yAxis, horizontal,
							vertical, red, green, blue, split + 1, yMax, scene)
			);
		}
		
		/**
		 * Computes this task directly, by going through all the assigned pixels in the
		 * image.
		 */
		protected void computeDirectly() {
			int offset = yMin * width;
			short[] rgb = new short[3];

			for (int y = yMin; y <= yMax; y++) {
				for (int x = 0; x < width; x++) {
					Point3D screenPoint = screenCorner
							.add(xAxis.scalarMultiply(horizontal * x / (width - 1.0)))
							.sub(yAxis.scalarMultiply(vertical * y / (height - 1.0)));
					Ray ray = Ray.fromPoints(eye, screenPoint);

					tracer(scene, ray, rgb);

					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];

					offset++;
				}
			}
		}
	}
	
	/**
	 * Helper method that looks at what does given ray intersect in the scene. If the
	 * ray doesn't intersect anything, that pixel will be colored black. Otherwise,
	 * using Phong model of lighting, color of the seen object is generated.
	 *
	 * @param scene
	 *            the scene which gives access to all the objects and lights
	 * @param ray
	 *            from the observer to the point on the image
	 * @param rgb
	 *            this is where the result is stored after computation
	 */
	private static void tracer(Scene scene, Ray ray, short[] rgb) {
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;
		
		RayIntersection closest = findClosestIntersection(scene, ray);
		
		if (closest == null) {
			return;
		}

		rgb[0] = 15;
		rgb[1] = 15;
		rgb[2] = 15;
		
		final double treshold = 1E-3;
		
		for (LightSource lightSource : scene.getLights()) {
			Ray lightRay = Ray.fromPoints(lightSource.getPoint(), closest.getPoint());
			RayIntersection lightClosest = findClosestIntersection(scene, lightRay);

			if (lightClosest == null) continue;

			if (lightClosest.getDistance() < lightSource.getPoint()
					.sub(closest.getPoint()).norm() - treshold) {
				continue;
			}
			
			Point3D incoming = lightSource.getPoint().sub(closest.getPoint()).normalize();
			Point3D normal = closest.getNormal();
			double ln = Math.max(incoming.scalarProduct(normal), 0);
			
			Point3D reflected = normal.scalarMultiply(2 * normal.scalarProduct(incoming))
					.sub(incoming).normalize();
			Point3D toEye = ray.start.sub(closest.getPoint()).normalize();
			double rv = Math.max(toEye.scalarProduct(reflected), 0);
			double powrv = Math.pow(rv, closest.getKrn());

			rgb[0] += (closest.getKrr() * powrv + closest.getKdr() * ln) * lightSource.getR();
			rgb[1] += (closest.getKrg() * powrv + closest.getKdg() * ln) * lightSource.getG();
			rgb[2] += (closest.getKrb() * powrv + closest.getKdb() * ln) * lightSource.getB();
		}
	}

	/**
	 * Finds closest intersection to the objects from the given seen. Intersection
	 * holds the information of the point of intersection, distance to the observer
	 * and whether the intersection is outer or not.
	 *
	 * @param scene
	 *            the scene which provides the objects
	 * @param ray
	 *            the ray of vision
	 * @return closest intersection of the given ray and objects from the given
	 *         scene, or null if this intersection doesn't exist.
	 */
	private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		double distanceMin = -1.0;
		RayIntersection result = null;

		for (GraphicalObject graphicalObject : scene.getObjects()) {
			RayIntersection intersection = graphicalObject.findClosestRayIntersection(ray);
			if (intersection == null) continue;
			if (distanceMin == -1.0 || intersection.getDistance() < distanceMin) {
				distanceMin = intersection.getDistance();
				result = intersection;
			}
		}

		return result;
	}
}
