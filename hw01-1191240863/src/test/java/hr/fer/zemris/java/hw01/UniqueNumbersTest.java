package hr.fer.zemris.java.hw01;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Bruno Iljazović
 *
 */
public class UniqueNumbersTest {

	@Test
	public void emptyTreeSize() {
		UniqueNumbers.TreeNode root = null;

		int expected = 0;

		int actual = UniqueNumbers.treeSize(root);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void nonEmptyTreeSize() {
		UniqueNumbers.TreeNode root = null;
		root = UniqueNumbers.addNode(root, 12);
		root = UniqueNumbers.addNode(root, 12);
		root = UniqueNumbers.addNode(root, 20);

		int expected = 2;

		int actual = UniqueNumbers.treeSize(root);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void containsValueTest() {
		UniqueNumbers.TreeNode root = null;
		root = UniqueNumbers.addNode(root, 20);
		root = UniqueNumbers.addNode(root, 20);
		root = UniqueNumbers.addNode(root, 12);

		int value1 = 12;
		int value2 = 10;
		boolean expected1 = true;
		boolean expected2 = false;

		boolean actual1 = UniqueNumbers.containsValue(root, value1);
		boolean actual2 = UniqueNumbers.containsValue(root, value2);

		Assert.assertEquals(expected1, actual1);
		Assert.assertEquals(expected2, actual2);
	}

	@Test
	public void inorderTraversal() {
		UniqueNumbers.TreeNode root = null;
		root = UniqueNumbers.addNode(root, 20);
		root = UniqueNumbers.addNode(root, 121);
		root = UniqueNumbers.addNode(root, 30);
		root = UniqueNumbers.addNode(root, 5);
		root = UniqueNumbers.addNode(root, 76);

		int[] expected = {5, 20, 30, 76, 121};

		int[] actual = new int[UniqueNumbers.treeSize(root)];
		UniqueNumbers.inorderTraversal(root, actual, 0);

		Assert.assertArrayEquals(expected, actual);
	}
}
