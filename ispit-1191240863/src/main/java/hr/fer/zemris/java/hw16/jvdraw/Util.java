package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Point;
import java.util.List;

/**
 * Provides some utility functions.
 * 
 * @author Bruno Iljazović
 */
public class Util {
	
	/**
	 * Parses the positive integer, or if the given string is invalid throws an
	 * exception.
	 *
	 * @param str
	 *            the str
	 * @return the integer
	 * @throws NumberFormatException if the given string doesn't represent positive integer.
	 */
	public static Integer parsePositiveInteger(String str) {
		Integer coordInt = null;
		coordInt = Integer.parseInt(str);
		if (coordInt < 0) throw new NumberFormatException();
		return coordInt;
	}
	
	public static double distanceSq(Point a, Point b) {
		return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
	}
	
	private static double vectorProduct(Point a, Point b, Point c) {
		return (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
	}
	
	public static boolean isConvex(List<Point> points) {
		if (points == null || points.isEmpty()) return false;
		if (points.size() < 3) return true;
		int i = 0, n = points.size();
		while (vectorProduct(points.get(i), points.get((i+1)%n), points.get((i+2)%n)) == 0) {
			i = (i+1)%n;
			if (i == 0) {
				return true;
			}
		}
		boolean plus = vectorProduct(points.get(i), points.get((i+1)%n), points.get((i+2)%n)) > 0;
		
		for (int j = (i+1)%n; j != i; j = (j+1)%n) {
			double vpr = vectorProduct(points.get(j), points.get((j+1)%n), points.get((j+2)%n));
			if (vpr > 0 != plus) return false;
		}
		
		return true;
	}

}
