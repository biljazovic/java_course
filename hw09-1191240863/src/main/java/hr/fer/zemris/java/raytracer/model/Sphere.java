package hr.fer.zemris.java.raytracer.model;

/**
 * Represents three-dimensional sphere object. Sphere is set from its center and
 * radius and constants from the Phong reflection model.
 * 
 * @author Bruno Iljazović
 */
public class Sphere extends GraphicalObject {
	
	/** The center. */
	private Point3D center;
	
	/** The radius. */
	private double radius;
	
	/** The red component of the reflection constant. */
	private double krr;
	
	/** The green component of the reflection constant. */
	private double krg;
	
	/** The blue component of the reflection constant. */
	private double krb;
	
	/** The blue component of the diffusion constant. */
	private double kdb;
	
	/** The green component of the diffusion constant. */
	private double kdg;
	
	/** The red component of the diffusion constant. */
	private double kdr;
	
	/** Shininess constant */
	private double krn;

	/* (non-Javadoc)
	 * @see hr.fer.zemris.java.raytracer.model.GraphicalObject#findClosestRayIntersection(hr.fer.zemris.java.raytracer.model.Ray)
	 */
	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D origin = ray.start;
		Point3D centerToOrigin = origin.sub(center);

		double scalarProduct = centerToOrigin.scalarProduct(ray.direction);
		
		double underRoot = scalarProduct * scalarProduct - 
				centerToOrigin.scalarProduct(centerToOrigin) + radius * radius;
		
		if (underRoot < 0) return null;
		
		double root = Math.sqrt(underRoot);
		double distance1 = -scalarProduct - root;
		double distance2 = -scalarProduct + root;
		
		if (distance1 < 0 && distance2 < 0) return null;
		
		double distance = (distance1 < 0) ? distance2 : distance1;
		
		Point3D intersection = origin.add(ray.direction.scalarMultiply(distance));

		return new RayIntersection(
				intersection,
				distance,
				centerToOrigin.norm() >= radius
		) {
			
			@Override
			public Point3D getNormal() {
				return intersection.sub(center).normalize();
			}
			
			@Override
			public double getKrr() {
				return krr;
			}
			
			@Override
			public double getKrn() {
				return krn;
			}
			
			@Override
			public double getKrg() {
				return krg;
			}
			
			@Override
			public double getKrb() {
				return krb;
			}
			
			@Override
			public double getKdr() {
				return kdr;
			}
			
			@Override
			public double getKdg() {
				return kdg;
			}
			
			@Override
			public double getKdb() {
				return kdb;
			}
		};
	}
	
	
	
	/**
	 * Instantiates a new sphere.
	 *
	 * @param center the center
	 * @param radius the radius
	 * @param kdr the red component of the diffusion constant
	 * @param kdg the green component of the diffusion constant
	 * @param kdb the blue component of the diffusion constant
	 * @param krb the blue component of the reflection constant
	 * @param krg the green component of the reflection constant
	 * @param krr the red component of the reflection constant
	 * @param krn shininess constant
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr,
			double krg, double krb, double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.kdb = kdb;
		this.kdg = kdg;
		this.kdr = kdr;
		this.krn = krn;
	}

}
