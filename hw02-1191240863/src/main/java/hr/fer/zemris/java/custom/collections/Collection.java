package hr.fer.zemris.java.custom.collections;

/**
 * This class is a 'template' class for collections of objects. 
 * Provides implementation for {@code isEmpty} and {@code addAll}
 * methods, while others are meant to be overridden, if needed, in 
 * derived classes.
 * 
 * <p>Since it is intended as a template, it has no storage capabilities.
 * 
 * @author Bruno Iljazović
 */
public class Collection {

    /**
     * Default constructor.
     */
	protected Collection() {
	}
	
	/**
	 * @return {@code true} if the collection is empty, {@code false}
	 * otherwise.
	 */
	public boolean isEmpty() {
		if (this.size() == 0) return true;
		return false;
	}
	
	/**
	 * Returns number of elements in the collection.
	 * 
	 * @return number of elements in the collection
	 */
	public int size() {
		return 0;
	}
	
	/**
	 * Adds new element to the collection
	 * 
	 * @param value object which is to be added
	 */
	public void add(Object value) {
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
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Returns true only if a collection contains given value and removes one
	 * occurrence of it. Existence is determined by {@code equals} method, same
	 * as in {@link #contains}.
	 * 
	 * @param value object which is to be removed
	 * 
	 * @return {@code true} if the element existed in the collection, {@code false}
	 * otherwise.
	 *
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Allocates new array and fills it with elements from the collection.
	 * 
	 * <p>Array size is equal to the size of the collection.
	 * 
	 * <p>If the collection is empty, returns empty array.
	 * 
	 * @return Array of elements from collection.
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Calls method {@code process} from given instance of {@link Processor}
	 * for each element of the collection in the undefined order.
	 * 
	 * @param processor instance of a {@code Processor} class
	 */
	public void forEach(Processor processor) {
	}
	
	/**
	 * Adds all elements from given collection to this collection.
	 * 
	 * @param other collection from which elements should be copied.
	 * 
	 * @throws IllegalArgumentException If the collection is calling addAll on itself
	 */
	public void addAll(Collection other) {
		if (other == null) {
			throw new NullPointerException();
		}
		if (other == this) {
			throw new IllegalArgumentException("addAll can't be called on itself");
		}

		class AddingProcessor extends Processor {

			@Override
			public void process(Object value) {
				Collection.this.add(value);
			}
		}

		other.forEach(new AddingProcessor());
	}
	
	/**
	 * Empties the collection.
	 */
	public void clear() {
	}
}
