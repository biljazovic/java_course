package hr.fer.zemris.java.hw16.jvdraw.objects;

/**
 * Visitor for {@link GeometricalObject}.
 * 
 * @author Bruno Iljazović
 */
public interface GeometricalObjectVisitor {

	/**
	 * Visits line.
	 *
	 * @param line the line
	 */
	public abstract void visit(Line line);

	/**
	 * Visits circle.
	 *
	 * @param circle the circle
	 */
	public abstract void visit(Circle circle);

	/**
	 * Visits filled circle.
	 *
	 * @param filledCircle the filled circle
	 */
	public abstract void visit(FilledCircle filledCircle);

}
