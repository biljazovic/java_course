package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Represents a list of prime numbers (and 1).
 * At the start only 1 is present in the list.
 * 
 * @author Bruno Iljazović
 */
public class PrimListModel implements ListModel<Integer> {
	
	/** The elements of the list. */
	private List<Integer> elements;
	
	/** The listeners. */
	private List<ListDataListener> listeners;

	/**
	 * Instantiates a new prime list model.
	 */
	public PrimListModel() {
		elements = new ArrayList<>();
		elements.add(Integer.valueOf(1));
		
		listeners = new ArrayList<>();
	}
	
	/**
	 * Checks if the given number is prime.
	 *
	 * @param number the number to be checked
	 * @return true, iff the number is prime
	 */
	private boolean isPrime(int number) {
		for (int i = 2; i * i <= number; i++) {
			if (number % i == 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Generates next prime number, adds it to the end of the list and notifies the
	 * listeners of the change.
	 */
	public void next() {
		int prime = elements.get(elements.size()-1);
		for (prime++; !isPrime(prime); prime++);

		elements.add(Integer.valueOf(prime));
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 
				elements.size(), elements.size());
		for (ListDataListener listener : listeners) {
			listener.intervalAdded(event);
		}
	}

	@Override
	public int getSize() {
		return elements.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return elements.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}
	
}
