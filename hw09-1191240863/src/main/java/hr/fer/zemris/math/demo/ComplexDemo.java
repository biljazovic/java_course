package hr.fer.zemris.math.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hr.fer.zemris.math.Complex;

/**
 * Demo program to test the {@link Complex#parse(String)} method. Input 'done'
 * when done
 *
 * @author Bruno Iljazović
 */
public class ComplexDemo {
	
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.print("Enter complex number: ");
			String line = reader.readLine();
			if (line.trim().equals("done")) break;
			try {
				System.out.println(Complex.parse(line));
			} catch(IllegalArgumentException ex) {
				System.out.println(ex);
			}
		}
	}
}
