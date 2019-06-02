package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * This program starts the GUI that shows two lists of numbers. At the start,
 * both lists show only number 1. Clicking on the button, a smallest prime
 * number that isn't on the lists gets added to the both lists. 
 * 
 * @author Bruno Iljazović
 */
public class PrimDemo extends JFrame {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates new Prime Demo.
	 */
	public PrimDemo() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Prime Lists");
		setLocation(100, 100);
		setSize(700, 500);
		initGUI();
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		PrimListModel model = new PrimListModel();
		
		JList<Integer> list1 = new JList<>(model);
		JList<Integer> list2 = new JList<>(model);
		
		JPanel lists = new JPanel(new GridLayout(1, 0));
		lists.add(new JScrollPane(list1));
		lists.add(new JScrollPane(list2));
		
		JButton next = new JButton("next");
		next.addActionListener(e -> model.next());
		
		cp.add(lists, BorderLayout.CENTER);
		cp.add(next, BorderLayout.PAGE_END);
	}

	/**
	 * This method is called when the program is run.
	 *
	 * @param args the command line arguments, not used here.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			PrimDemo window = new PrimDemo();
			window.setVisible(true);
		});
	}
}
