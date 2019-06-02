package hr.fer.zemris.java.fractals;

import java.util.concurrent.Callable;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Represents a single task to be executed by {@link Newton.IFractalProducerImpl}
 * 
 * @author Bruno Iljazović
 */
public class NewtonJob implements Callable<Void> {
	
	/** The real minimum. */
	private double reMin;
	
	/** The real maximum. */
	private double reMax;
	
	/** The imaginary minimum. */
	private double imMin;
	
	/** The imaginary maximum. */
	private double imMax;
	
	/** The width of the image. */
	private int width;
	
	/** The height of the image. */
	private int height;
	
	/** The minimal y. */
	private int yMin;
	
	/** The maximal y. */
	private int yMax;
	
	/** The data where the results of the computation are put in. */
	private short[] data;
	
	/** The convergence threshold.  */
	private double convergenceThreshold;
	
	/** The root threshold.*/
	private double rootThreshold;
	
	/** The maximum number of iterations in Newton's method. */
	private int maxIter;
	
	/** The polynomial. */
	private ComplexRootedPolynomial polynomial;
	
	/** The derived polynomial. */
	private ComplexPolynomial derived;

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		for (int y = yMin; y <= yMax; ++y) {
			for (int x = 0; x < width; ++x) {
				Complex c = new Complex(
						x * (reMax - reMin) / (width - 1.0) + reMin,
						(height - 1.0 - y) * (imMax - imMin) / (height - 1.0) + imMin
				);

				int iter = 0;
				Complex z0 = c;
				double module;

				do {
					Complex fraction = polynomial.apply(z0).divide(derived.apply(z0));
					Complex z1 = z0.sub(fraction);
					module = z1.sub(z0).module();
					z0 = z1;
					++iter;
				} while (module > convergenceThreshold && iter < maxIter);

				short index = (short) polynomial.indexOfClosestRootFor(z0, rootThreshold);

				int offset = y * width + x;
				if (index != -1) {
					data[offset] = index;
				} else {
					data[offset] = 0;
				}
			}
		}
		return null;
	}

	/**
	 * Instantiates a new newton job.
	 *
	 * @param reMin the minimum real component
	 * @param reMax the maximum real component
	 * @param imMin the minimum imaginary component
	 * @param imMax the maximum imaginary component
	 * @param width the width of the image
	 * @param height the height of the image
	 * @param yMin the minimum y
	 * @param yMax the maximum y
	 * @param data the data where the results of computation are put in.
	 * @param polynomial the polynomial
	 * @param convergenceThreshold the convergence threshold
	 * @param maxIter identifier
	 * @param rootThreshold the root threshold
	 */
	public NewtonJob(double reMin, double reMax, double imMin, double imMax, int width, int height,
			int yMin, int yMax, short[] data, ComplexRootedPolynomial polynomial, 
			double convergenceThreshold, int maxIter, double rootThreshold) {
		this.reMin = reMin;
		this.reMax = reMax;
		this.imMin = imMin;
		this.imMax = imMax;
		this.width = width;
		this.height = height;
		this.yMin = yMin;
		this.yMax = yMax;
		this.data = data;
		this.polynomial = polynomial;
		this.derived = polynomial.toComplexPolynom().derive();
		this.convergenceThreshold = convergenceThreshold;
		this.maxIter = maxIter;
		this.rootThreshold = rootThreshold;
	}
	
}