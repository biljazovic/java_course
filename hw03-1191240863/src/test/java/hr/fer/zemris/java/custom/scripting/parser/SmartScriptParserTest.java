package hr.fer.zemris.java.custom.scripting.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

public class SmartScriptParserTest {

	@Test
	public void testGeneralStructureOfNodes() {
		String docBody = loader("test1.txt");
		
		SmartScriptParser parser = new SmartScriptParser(docBody);
		
		DocumentNode actual = parser.getDocumentNode();
		
		DocumentNode expected = new DocumentNode();
		expected.addChildNode(new TextNode());

		ForLoopNode for1 = new ForLoopNode();
		for1.addChildNode(new TextNode());
		for1.addChildNode(new EchoNode());
		for1.addChildNode(new TextNode());
		
		ForLoopNode for2 = new ForLoopNode();
		for2.addChildNode(new TextNode());
		
		for1.addChildNode(for2);
		
		for1.addChildNode(new TextNode());

		expected.addChildNode(for1);
		expected.addChildNode(new TextNode());

		Assert.assertEquals(expected, actual);
	}
	

	@Test
	public void testGeneralStructure2() {
		String docBody = loader("test7.txt");
		
		SmartScriptParser parser = new SmartScriptParser(docBody);
		
		DocumentNode actual = parser.getDocumentNode();
		
		DocumentNode expected = new DocumentNode();
		expected.addChildNode(new TextNode());
		expected.addChildNode(new ForLoopNode());
		expected.addChildNode(new TextNode());
		
		ForLoopNode for1 = new ForLoopNode();
		for1.addChildNode(new TextNode());
		for1.addChildNode(new EchoNode());
		for1.addChildNode(new TextNode());

		expected.addChildNode(for1);
		expected.addChildNode(new TextNode());

		Assert.assertEquals(expected, actual);
	}
	

	@Test(expected = SmartScriptParserException.class)
	public void testUnclosedTag() {
		String docBody = loader("test2.txt");
		
		SmartScriptParser parser = new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void testMissingEndTag() {
		String docBody = loader("test3.txt");
		
		SmartScriptParser parser = new SmartScriptParser(docBody);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testLexerException() {
		String docBody = loader("test4.txt");
		
		SmartScriptParser parser = new SmartScriptParser(docBody);
	}
	
	@Test(expected = SmartScriptParserException.class)
	public void testTooManyEndTags() {
		String docBody = loader("test5.txt");
		
		SmartScriptParser parser = new SmartScriptParser(docBody);
	}

	@Test(expected = SmartScriptParserException.class)
	public void testInvalidForTag() {
		String docBody = loader("test6.txt");
		
		SmartScriptParser parser = new SmartScriptParser(docBody);
	}	
	
	private String loader(String filename) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename)) {
			byte[] buffer = new byte[1024];
			while (true) {
				int read = is.read(buffer);
				if (read < 1)
					break;
				bos.write(buffer, 0, read);
			}
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			return null;
		}
	}

}
