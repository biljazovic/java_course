package hr.fer.zemris.java.hw03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * This program tests SmartScriptParser. It takes exactly one argument from command line which should
 * be valid filepath and parses the text from the file with SmartScriptParser. It then generates
 * possible original text back from the parser and outputs it on standard output. 
 * 
 * <p> It also generates new parser from the generated original textbody and compares the structures of 
 * the first and second parser (using {@link Node#equals} method) They should be same.
 * 
 * @author bruno
 */
public class SmartScriptTester {


	/**
	 * Method that is called when a program is ran.
	 *
	 * @param args the arguments from the command line. There should be only one of them.
	 */
	public static void main(String[] args) throws IOException{
		if (args.length != 1) {
			System.out.println("Program should get single command line argument that is path to the test file.");
			System.exit(-1);
		}
		String docBody = null;
		try {
			docBody = new String( 
					Files.readAllBytes(Paths.get(args[0])), 
					StandardCharsets.UTF_8
			);
		} catch (InvalidPathException | NoSuchFileException ex) {
			System.out.println("Invalid filepath!");
			System.exit(-1);
		}

		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		} 
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println(originalDocumentBody); 
		
		SmartScriptParser parser2 = null;
		try {
			parser2 = new SmartScriptParser(originalDocumentBody);
		} catch(SmartScriptParserException ex) {
			System.out.println(ex.getMessage());
			System.exit(-1);
		}
		if (parser2.getDocumentNode().equals(parser.getDocumentNode())) {
			System.out.println("Test sucess!");
		}
		else {
			System.out.println("Failed Test!");
		}
	}
	
	/**
	 * String element.
	 *
	 * @param element the element
	 * @return the string
	 */
	private static String stringElement(Element element) {
		if (element instanceof ElementFunction) {
			return "@" + element.asText();
		}
		else if (element instanceof ElementString) {
			return "\"" + element.asText().replace("\\", "\\\\").replace("\"", "\\\"")
					.replace("\n", "\\n").replace("\t", "\\t").replace("\r", "\\t") + "\"";
		}
		else {
			return element.asText();
		}
	}

	/**
	 * Performs depth-first search on a tree rooted at given node.
	 * Returns StringBuilder which represents document body that generated given tree.
	 * 
	 * @param node Root of a tree structure of Nodes
	 * @return original document body
	 */
	private static StringBuilder dfs(Node node) {
		StringBuilder ret = new StringBuilder();
		
		if (node instanceof TextNode) {
			ret.append(((TextNode) node).getText().replace("\\", "\\\\").replace("{", "\\{"));
		}
		else if (node instanceof ForLoopNode) {
			ForLoopNode alias = (ForLoopNode) node;
			ret.append("{$ FOR ");
			ret.append(stringElement(alias.getVariable()) + " ");
			ret.append(stringElement(alias.getStartExpression()) + " ");
			ret.append(stringElement(alias.getEndExpression()) + " ");
			if (alias.getStepExpression() != null) {
				ret.append(stringElement(alias.getStepExpression()) + " ");
			}
			ret.append("$}");
			for (int i = 0, max = node.numberOfChildren(); i < max; ++i) {
				ret.append(dfs(node.getChild(i)));
			}
			ret.append("{$END$}");
		}
		else if (node instanceof EchoNode) {
			ret.append("{$ = ");
			Element[] elements = ((EchoNode) node).getElements();
			for (Element element : elements) {
				ret.append(stringElement(element) + " " );
			}
			ret.append("$}");
		}
		else if (node instanceof DocumentNode) {
			for (int i = 0, max = node.numberOfChildren(); i < max; ++i) {
				ret.append(dfs(node.getChild(i)));
			}
		}
		
		return ret;
	}

	/**
	 * Generates original document text from an instance of DocumentNode.
	 *
	 * @param doc the doc
	 * @return the string
	 */
	public static String createOriginalDocumentBody(DocumentNode doc) {
		return dfs(doc).toString();
	}
}
