package hr.fer.zemris.java.custom.collections;

/**
 * Collection of objects where keys are mapped to values. A dictionary cannot contain duplicate
 * keys. Items are stored as (key, value) pairs. 
 * <p>Values can be null-references.
 * 
 * <p>Values are accessed and changed by providing respective keys.
 * 
 * <p>In methods {@link Dictionary#isEmpty} and {@link Dictionary#size} items with values as null-references are also 
 * counted. 
 * 
 * @author Bruno Iljazović
 */
public class Dictionary {
	
	/**
	 * This helper class represents one pair (key, value).
	 */
	private static class Item {

		/** The key. Can't be null-reference. */
		private Object key;
		
		/** The value. Can be null-reference */
		private Object value;
		
		/**
		 * Instantiates a new item by giving key and value.
		 *
		 * @param key the key
		 * @param value the value
		 */
		public Item(Object key, Object value) {
			this.key = key;
			this.value = value;
		}
	}
	
	/** Array of all the items in the dictionary. Only contains Objects of type {@link Item} */
	private ArrayIndexedCollection items;
	
	/**
	 * Instantiates a new empty dictionary. 
	 */
	public Dictionary() {
		items = new ArrayIndexedCollection();
	}
	
	/**
	 * Checks if the dictionary is empty. 
	 *
	 * @return true iff the dictionary has no items.
	 */
	public boolean isEmpty() {
		return items.isEmpty();
	}
	
	/**
	 * Returns the number of items in the dictionary. Items with null-references for value are also
	 * counted.
	 *
	 * @return the number of items in the dictionary.
	 */
	public int size() {
		return items.size();
	}
	
	/**
	 * Empties the dictionary.
	 */
	public void clear() {
		items.clear();
	}
	
	/**
	 * Inserts new item in the dictionary. If the key already exists in the dictionary, overwrites
	 * the old value with the new one.
	 *
	 * @param key The key. Can't be null-reference.
	 * @param value The value. Can be null-reference.
	 * 
	 * @throws IllegalArgumentException if the key was null.
	 */
	public void put(Object key, Object value) {
		if (key == null) throw new IllegalArgumentException("Key can't be null-reference");
		
		for (int i = 0, size = size(); i < size; ++i) {
			Item item = (Item) items.get(i);
			if (item.key.equals(key)) {
				item.value = value;
				return;
			}
		}
		items.add(new Item(key, value));
	}
	
	/**
	 * Gets the value associated with given key. If the item with the given key doesn't exist in 
	 * the dictionary returns null.
	 *
	 * @param key Key of the wanted value.
	 * 
	 * @return the value of the given key, or null if the key doesn't exist.
	 */
	public Object get(Object key) {
		for (int i = 0, size = size(); i < size; ++i) {
			Item item = (Item) items.get(i);
			if (item.key.equals(key)) {
				return item.value;
			}
		}
		return null;
	}
}
