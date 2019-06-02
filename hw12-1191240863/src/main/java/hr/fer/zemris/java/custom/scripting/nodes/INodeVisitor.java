package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Visits a {@link Node} and does something with it.
 * 
 * @author Bruno Iljazović
 */
public interface INodeVisitor {

	/**
	 * Visit text node.
	 *
	 * @param node the node
	 */
	public void visitTextNode(TextNode node);

	/**
	 * Visit FOR loop node.
	 *
	 * @param node the node
	 */
	public void visitForLoopNode(ForLoopNode node);

	/**
	 * Visit echo node.
	 *
	 * @param node the node
	 */
	public void visitEchoNode(EchoNode node);

	/**
	 * Visit document node.
	 *
	 * @param node the node
	 */
	public void visitDocumentNode(DocumentNode node);
}