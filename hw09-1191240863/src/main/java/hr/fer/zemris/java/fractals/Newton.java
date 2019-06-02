package hr.fer.zemris.java.fractals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * This program draws Newton-Raphson iteration-based fractals. User should input
 * at least two roots of the polynomial of the form 'a + ib'.
 * 
 * @author Bruno Iljazović
 */
public class Newton {

	/**
	 * The main method that is called when the program is run.
	 *
	 * @param args the command line arguments, not used here
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when "
				+ "done.");
		
		List<Complex> roots = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			roots.clear();
			int count = 1;
			String line;
			while (true) {
				System.out.printf("Root %d>", count);
				line = reader.readLine();
				if (line.trim().equals("done")) break;
				Complex c = null;
				try {
					c = Complex.parse(line);
				} catch(IllegalArgumentException ex) {
					System.out.println(ex.getMessage());
					continue;
				}
				
				roots.add(c);
				++count;
			}
			if (count < 3) {
				System.out.println("You have to enter at least two roots.");
				continue;
			}
			break;
		}
		
		ComplexRootedPolynomial polynomial = new ComplexRootedPolynomial(
				roots.toArray(new Complex[roots.size()]));

		FractalViewer.show(new IFractalProducerImpl(polynomial));
	}
	
	/** The pool of tasks to execute. */
	static ExecutorService pool = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors(), new ThreadFactory() {

				@Override
				public Thread newThread(Runnable r) {
					Thread worker = new Thread(r);
					worker.setDaemon(true);
					return worker;
				}
			}
	);
	
	/**
	 * This class provides method {@link IFractalProducerImpl#produce} which generates 
	 * data needed for generating fractal images. 
	 */
	public static class IFractalProducerImpl implements IFractalProducer {
		
		/** The polynomial in Newton's method  */
		ComplexRootedPolynomial polynomial;

		/**
		 * Instantiates a new fractal producer
		 *
		 * @param polynomial the polynomial in Newton's method
		 */
		public IFractalProducerImpl(ComplexRootedPolynomial polynomial) {
			this.polynomial = polynomial;
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width,
				int height, long requestNo, IFractalResultObserver observer) {
			final double convergenceTreshold = 1E-3;
			final double rootTreshold = 1E-3;
			final int maxIter = 16 * 16 * 16;
			short[] data = new short[width * height];
			final int numOfPieces = 8;
			int pieceHeight = height / numOfPieces;
			
			List<Future<Void>> results = new ArrayList<>();
			
			for (int i = 0; i < numOfPieces; ++i) {
				int yMin = i * pieceHeight;
				int yMax = (i + 1) * pieceHeight - 1;
				if (i == numOfPieces - 1) {
					yMax = height - 1;
				}
				results.add(pool.submit(new NewtonJob(reMin, reMax, imMin, imMax, width, height, yMin,
						yMax, data, polynomial, convergenceTreshold, maxIter, rootTreshold)));
			}
			
			for (Future<Void> job : results) {
				try {
					job.get();
				} catch(InterruptedException | ExecutionException e) {
				}
			}
			
			observer.acceptResult(data, (short)(polynomial.order() + 1), requestNo);
			
		}
		
	}

}
