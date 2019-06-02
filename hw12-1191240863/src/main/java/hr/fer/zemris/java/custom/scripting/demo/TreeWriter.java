package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * This program gets the path to the Smart Script file via command line
 * arguments, parses it and produces the approximate original script.
 * 
 * @author Bruno Iljazović
 */
public class TreeWriter {

	/**
	 * Visitor that prints the approximate original script for every node it visits.
	 */
	private static class WriterVisitor implements INodeVisitor {
		
		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node.getText().replace("\\", "\\\\").replace("{", "\\{"));
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.print("{$ FOR ");
			System.out.print(node.getVariable() + " ");
			System.out.print(node.getStartExpression() + " ");
			System.out.print(node.getEndExpression() + " ");
			if (node.getStepExpression() != null) {
				System.out.print(node.getStepExpression() + " ");
			}
			System.out.print("$}");
			for (int i = 0, max = node.numberOfChildren(); i < max; i++) {
				node.getChild(i).accept(this);
			}
			System.out.print("{$END$}");
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.print("{$ = ");
			for (Element element : node.getElements()) {
				System.out.print(element + " ");
			}
			System.out.print("$}");
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, max = node.numberOfChildren(); i < max; i++) {
				node.getChild(i).accept(this);
			}
		}
		
	}
	
	/**
	 * The main method that is called when the program is run.
	 *
	 * @param args
	 *            command line arguments, first and only one should be path to the
	 *            smart script file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
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
		
		WriterVisitor visitor = new WriterVisitor();
		parser.getDocumentNode().accept(visitor);
	}

}
