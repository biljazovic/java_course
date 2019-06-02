package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Provides a method to execute the given Smart Script script given through a
 * {@link DocumentNode}.
 *
 * @author Bruno Iljazović
 */
public class SmartScriptEngine {

	/** The document node of the Smart Script to execute. */
	private DocumentNode documentNode;

	/** The request context. */
	private RequestContext requestContext;

	/** The stack of variables and its values. */
	private ObjectMultistack multistack = new ObjectMultistack();

	/** The visitor that visits every node in a given document node and executes them. */
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch(IOException ignorable) { }
		}
		
		/**
		 * Gets the value from element.
		 *
		 * @param element the element
		 * @return the value from element
		 */
		private Object getValueFromElement(Element element) {
			if (element instanceof ElementString) {
				return element.asText();
			} else if (element instanceof ElementConstantInteger) {
				return Integer.valueOf(((ElementConstantInteger) element).getValue());
			} else if (element instanceof ElementVariable) {
				return multistack.peek(element.asText()).getValue();
			} else if (element instanceof ElementConstantDouble) {
				return Double.valueOf(((ElementConstantDouble) element).getValue());
			}
			return null; 
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variable = node.getVariable().getName();
			ValueWrapper start = new ValueWrapper(getValueFromElement(node.getStartExpression()));

			multistack.push(variable, start);
			
			ValueWrapper end = new ValueWrapper(getValueFromElement(node.getEndExpression()));
			ValueWrapper step = new ValueWrapper(1);
			if (node.getStepExpression() != null) {
				step = new ValueWrapper(getValueFromElement(node.getStepExpression()));
			}

			for (; start.numCompare(end.getValue()) <= 0; start.add(step.getValue())) {
				for (int i = 0, max = node.numberOfChildren(); i < max; i++) {
					node.getChild(i).accept(this);
				}
			}
			
			multistack.pop(variable);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> objectStack = new Stack<>();
			
			for (Element element : node.getElements()) {
				if (element instanceof ElementOperator || element instanceof ElementFunction) {
					Functions.get(element.asText()).accept(objectStack, requestContext);
				} else {
					objectStack.push(getValueFromElement(element));
				}
			}
			
			for (Object object : objectStack.toArray()) {
				try {
					requestContext.write(object.toString());
				} catch (IOException ignorable) { }
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, max = node.numberOfChildren(); i < max; i++) {
				node.getChild(i).accept(this);
			}
		}
	};

	/**
	 * Instantiates a new Smart Script engine.
	 *
	 * @param documentNode the document node of the Smart Script
	 * @param requestContext the request context
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * Executes the given Smart Script.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
}