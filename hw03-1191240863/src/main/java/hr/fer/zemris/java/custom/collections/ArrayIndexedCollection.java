package hr.fer.zemris.java.custom.collections;

/**
 * Re-sizable array-backed collection of objects. Storage of duplicate
 * elements is allowed, while storage of null references is not.
 * 
 * <p>Provides nearly constant random access, adding new elements to the 
 * back and removing from the back.
 * 
 * @author Bruno Iljazović
 */
public class ArrayIndexedCollection extends Collection {

	/**
	 * Number of elements currently in the collection.
	 */
	private int size;
	
	/**
	 * Current capacity of the collection
	 */
	private int capacity;
	
	/**
	 * Array in which elements are stored, its size is determined by
	 * {@link capacity}
	 */
	private Object[] elements;
	
	/**
	 * Constructor which sets the capacity to the given value and allocates 
	 * array {@code elements} with null-references.
	 * 
	 * @param initialCapacity wanted capacity of the collection.
	 * 
	 * @throws IllegalArgumentException If the given capacity is non-positive.
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException("initialCapacity should be positive. Was " + initialCapacity + ".");
		}
		capacity = initialCapacity;
		elements = new Object[initialCapacity];
	}

	/**
	 * Default constructor. Initial capacity is set to 16.
	 */
	public ArrayIndexedCollection() {
		this(16);
	}

	/**
	 * Constructor which copies the elements from given other collection and sets the
	 * capacity to the size of that collection.
	 * 
	 * <p> If the other collection's size is less than given {@code initalCapacity},
	 * capacity is set to initialCapacity instead.
	 * 
	 * @param other Collection from which elements are copied from.
	 * @param initialCapacity Minimum capacity of the collection.
	 * 
	 * @throws NullPointerException If the other collection was null-reference.
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		if (other == null) {
			throw new NullPointerException("other was null.");
		}
		//this(Math.max(initialCapacity, other.size()));
		this.capacity = Math.max(initialCapacity, other.size());
		this.elements = new Object[this.capacity];
		this.addAll(other);
	}

	/**
	 * Constructor which copies the elements from given other collection.
	 * 
	 * <p> Minimum capacity of the newly created collection is 16.
	 * 
	 * @param other Collection from which elements are copied from.
	 * 
	 * @throws NullPointerException If the other collection was null-reference.
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, 16);
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
	 * Doubles the capacity of the collection and reallocates the array with new
	 * length.
	 */
	private void increaseCapacity() {
		capacity = 2 * capacity;
		elements = java.util.Arrays.copyOf(elements, capacity);
	}
	
	/**
	 * Adds new element to the end of the collection. If the {@code elements} array 
	 * is full {@link #increaseCapacity} is called.
	 * 
	 * <p> Average complexity of this method is O(1).
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
		if (size == capacity) {
			increaseCapacity();
		}
		this.elements[size++] = value;
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
	 * If the given object exists in the collection, position of its first
	 * occurrence in the array {@code elements} is returned. 
	 * Otherwise, -1 is returned. Existence is determined by {@code equals} 
	 * method, same as in {@link #contains}
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
		for (int i = 0; i < size; ++i) {
			if (value.equals(elements[i])) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Removes an element at the given index in the array {@code elements}. 
	 * Elements after the given index are shifted to the left by one.
	 * 
	 * @param index Index of the element which is to be removed.
	 * 
	 * @throws IndexOutOfBoundsException If the index is not in [0, size-1] range.
	 */
	public void remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("index was " + index + ", but size was " + size + ".");
		}
		for (int i = index; i < size - 1; ++i) {
			elements[i] = elements[i+1];
		}
		elements[--size] = null;
	}
	
	/**
	 * Inserts element at the given position, increasing the collection's size 
	 * by one. If the array is full, calls {@link increaseCapacity}.
	 * 
	 * <p> Shifts all elements with index >= position by one to the right.
	 * 
	 * <p> Average complexity of this method is O(n).
	 * 
	 * @param value element which is to be added
	 * @param position index in the array at which the new element should be
	 * inserted
	 * 
     * @throws IndexOutOfBoundsException if the position is not in [0, size] range.
     * @throws NullPointerException If the value to be inserted was null-reference.
     */
	public void insert(Object value, int position) {
		if (value == null) {
			throw new NullPointerException("value was null.");
		}
		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException("position was " + position + ", but size was " + size + ".");
		}
		if (size == capacity) {
			increaseCapacity();
		}
		for (int i = size; i > position; --i) {
			elements[i] = elements[i-1];
		}
		elements[position] = value;
		++size;
	}
	
	/**
	 * Empties the collection. Array {@code elements} is left at current capacity, but now all
	 * elements of the array are null-references.
	 */
	@Override
	public void clear() {
		while (size > 0) {
			elements[--size] = null;
		}
	}
	
    /**
     * Returns element at given index from array {@code elements}
     * 
     * <p> Average complexity of this method is O(1).
     * 
     * @param index index of the element. Should be between 0 and size - 1
     * 
     * @return element at given index
     * 
     * @throws IndexOutOfBoundsException if the index is not in [0, size-1] range.
     */
	public Object get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("index was " + index + ", but size was " + size + ".");
		}
		return elements[index];
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
		System.arraycopy(elements, 0, result, 0, size);
		return result;
	}
	
	/**
	 * Calls method {@code process} from given instance of {@link Processor}
	 * for each element of the collection in the order of increasing indices.
	 * 
	 * @param processor instance of a {@code Processor} class
	 */
	@Override
	public void forEach(Processor processor) {
		for (int i = 0; i < size; ++i) {
			processor.process(elements[i]);
		}
	}
}