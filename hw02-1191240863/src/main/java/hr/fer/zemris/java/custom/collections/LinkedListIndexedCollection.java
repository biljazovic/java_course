package hr.fer.zemris.java.custom.collections;

/**
 * Linked List-backed collection of objects. Storage of duplicate elements
 * is allowed, while storage of null references is not.
 * 
 * <p>Since it is implemented with linked list, does not provide constant
 * random access of elements.
 *
 * @author Bruno Iljazović
 */
public class LinkedListIndexedCollection extends Collection {
	

	/**
	 * Helper class which represents one node in the linked list.
	 * It has references to the neighboring nodes, and reference 
	 * to the object stored in the node.
	 *
	 */
	private static class ListNode {
		
		ListNode next;
		ListNode prev;
		Object value;

		/**
		 * Instantiates a new list node.
		 *
		 * @param value The value that is to be stored in node.
		 */
		public ListNode(Object value) {
			this.value = value;
		}
	}

	/** Number of elements in the collection. */
	private int size;
	
	/** First node in the collection. */
	private ListNode first;
	
	/** Last node in the collection. */
	private ListNode last;

	/**
	 * Instantiates a new linked list indexed collection which has no elements.
	 */
	public LinkedListIndexedCollection() {
		first = last = null; 
		size = 0;
	}

	/**
	 * Instantiates a new linked list indexed collection and copies elements
	 * from the given collection to this one.
	 *
	 * @param other Collection from which elements are copied from.
	 */
	public LinkedListIndexedCollection(Collection other) {
		if (other == null) {
			throw new NullPointerException("other was null.");
		}
		this.addAll(other);
	}


	/**
	 * Returns number of elements in the collection.
	 * 
	 * @return Number of elements in the collection.
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Adds new element to the end of the collection.
	 * 
	 * @param value Element which is to be added.
	 * 
	 * @throws NullPointerException If the given new element is null-reference.
	 */
	@Override
	public void add(Object value) {
		if (value == null) {
			throw new NullPointerException("value was null.");
		}
		ListNode newNode = new ListNode(value);
		if (first == null) {
			first = last = newNode;
		}
		else {
			newNode.prev = last;
			last.next = newNode;
			last = newNode;
		}
		++size;
	}

	/**
	 * Returns the node at the given index. It is implemented so that it iterates from 
	 * first or last depending on which node is closest.
	 *
	 * @param index The position of the node to be returned.
	 * 
	 * @return The ListNode at the given index.
	 */
	private ListNode getNode(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("index was " + index + ", but size was " + size + ".");
		}

		ListNode resultNode = null;

		if (index < size / 2) {
			resultNode = first;
			for (int i = 0; i < index; ++i) {
				resultNode = resultNode.next;
			}
		}
		else {
			resultNode = last;
			for (int i = 0; i < size - index - 1; ++i) {
				resultNode = resultNode.prev;
			}
		}
		
		return resultNode;
	}

	/**
	 * Returns the element at the given index. Behaves like {@link getNode}
	 * regarding the complexity.
	 *
	 * @param index The position of the element to be returned.
	 * 
	 * @return The element at the given index.
	 */
	public Object get(int index) {
		return getNode(index).value;
	}
	
	/**
	 * If the given object exists in the collection, position of its first
	 * occurrence is returned. Otherwise, -1 is returned. Existence is 
	 * determined by {@code equals} method, same as in {@link #contains}
	 * 
	 * <p> Average complexity of this method is O(n).
	 * 
	 * @param value The element to search for.
	 * @return index Index of the element if found, -1 otherwise.
	 */
	public int indexOf(Object value) {
		if (value == null) {
			return -1;
		}

		ListNode node = first;
		int index = 0;

		do {
			if (node.value.equals(value)) {
				return index;
			}
			++index;
			node = node.next;
		} while (node != null);

		return -1;
	}

	/**
	 * Checks if an element is already in the collection, determined by 
	 * {@code equals} method
	 * 
	 * @param value the element to search
	 * 
	 * @return {@code true} if {@code value} is in the collection, 
	 * {@code false} otherwise
	 */
	@Override
	public boolean contains(Object value) {
		if (indexOf(value) == -1) {
			return false;
		}
		return true;
	}

	/**
	 * Returns true only if a collection contains given value and removes one
	 * occurrence of it. Existence is determined by {@code equals} method, same
	 * as in {@link #contains}.
	 * 
	 * @param value element which is to be removed
	 * 
	 * @return {@code true} if the element existed in the collection, {@code false}
	 * otherwise.
	 */	
	@Override
	public boolean remove(Object value) {
		int indexOfValue = indexOf(value);

		if (indexOfValue == -1) {
			return false;
		}
		remove(indexOfValue);

		return true;
	}

	/**
	 * Inserts element at the given position, increasing the collection's size 
	 * by one.
	 * 
	 * <p> Shifts all elements with index >= position by one.
	 * 
	 * <p> Average complexity of this method is O(n).
	 * 
	 * @param value element which is to be added
	 * @param position index at which the new element should be
	 * inserted
	 * 
     * @throws IndexOutOfBoundsException if the position is not in [0, size] range.
     * @throws NullPointerException If the value to be inserted was null-reference.
     */
	public void insert(Object value, int position) {
		if (value == null) {
			throw new NullPointerException("value was null.");
		}
		if (position == size) { 
			add(value);
			return;
		}
		ListNode node = getNode(position); //getNode throws IndexOutOfBoundsException
		ListNode newNode = new ListNode(value);
		
		newNode.prev = node.prev;
		newNode.next = node;
		if (node.prev != null) {
			node.prev.next = newNode;
		}
		else {
			first = newNode;
		}
		node.prev = newNode;

		++size;
	}
		

	/**
	 * Removes an element at the given index. Elements after the given index are 
	 * shifted by one.
	 * 
	 * @param index Index of the element which is to be removed.
	 * 
	 * @throws IndexOutOfBoundsException If the index is not in [0, size-1] range.
	 */
	public void remove(int index) {
		ListNode node = getNode(index); //getNode throws IndexOutOfBoundsException
		
		if (node.prev != null) {
			node.prev.next = node.next;
		}
		if (node.next != null) {
			node.next.prev = node.prev;
		}

		if (index == size - 1) {
			last = node.prev;
		}
		if (index == 0) {
			first = node.next;
		}
		
		--size;
	}

	/**
	 * Allocates new array and fills it with elements from the collection.
	 * 
	 * <p>Array size is equal to the size of the collection
	 * 
	 * <p>If the collection is empty, returns empty array.
	 * 
	 * @return Array of elements from collection.
	 */
	@Override
	public Object[] toArray() {
		Object[] result = new Object[size];
		ListNode node = first;
		int index = 0;

		while (node != null) {
			result[index++] = node.value;
			node = node.next;
		}
		
		return result;
	}

	/**
	 * Calls method {@code process} from given instance of {@link Processor}
	 * for each element of the collection.
	 * 
	 * @param processor instance of a {@code Processor} class
	 */
	@Override
	public void forEach(Processor processor) {
		ListNode node = first;
		while (node != null) {
			processor.process(node.value);
			node = node.next;
		}
	}

	/**
	 * Empties the collection.
	 */
	@Override
	public void clear() {
		size = 0;
		first = last = null;
	}
}

