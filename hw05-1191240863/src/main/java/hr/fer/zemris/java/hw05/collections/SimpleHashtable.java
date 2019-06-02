package hr.fer.zemris.java.hw05.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Collection of objects where keys are mapped to values. A HashTable cannot contain duplicate
 * keys. Items are stored in (key, value) pairs hashed by the key.
 * 
 * <p>Key and value types are parameterized.
 * 
 * <p>Values can be null-references, Keys cannot.
 * 
 * <p>In methods {@link SimpleHashtable#isEmpty} and {@link SimpleHashtable#size} items with values 
 * as null-references are also counted. 
 * 
 * <p>In the Hashtable there are no more than 0.75 * capacityOfTheTable entries at any given moment.
 * 
 * @param <K> key type
 * @param <V> value type
 * 
 * @author Bruno Iljazović
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K,V>> {
	
	/** Size of the table in the default constructor */
	private static final int DEFAULT_CAPACITY = 16;
	
	/** The table of (key, value) pairs. Each element in the table points to the head of the 
	 * linked list which stores elements with the same hashCode MOD capacity*/
	private TableEntry<K, V>[] table;
	
	/** Number of items in the table. */
	private int size;

	/** Maximum number of items in the table. */
	private int limitCapacity;
	
	/** Number of times this Hastable has been changed since construction. */
	private int modificationCount;

	/**
	 * This class represents one (key, value) pair. Key and value types are parameterized.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 */
	public static class TableEntry<K, V> {
		
		/** The key. */
		private K key;
		
		/** The value. */
		private V value;
		
		/**  Reference to the next TableEntry in the list */
		private TableEntry<K, V> next;
		
		/**  Reference to the previous TableEntry in the list */
		private TableEntry<K, V> previous;
		
		/**
		 * Instantiates a new table entry.
		 *
		 * @param key the key
		 * @param value the value
		 * @param next reference to the next entry in the list.
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}

		/**
		 * Instantiates a new table entry.
		 *
		 * @param key the key
		 * @param value the value
		 * @param next reference to the next entry in the list
		 * @param previous reference to the previous entry in the list
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next, TableEntry<K, V> previous) {
			this.key = key;
			this.value = value;
			this.next = next;
			this.previous = previous;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Sets the value.
		 *
		 * @param value the new value
		 */
		public void setValue(V value) {
			this.value = value;
		}

		/**
		 * Gets the key.
		 *
		 * @return the key
		 */
		public K getKey() {
			return key;
		}
		
		@Override
		public String toString() {
			return key.toString() + "=" + value.toString();
		}
	}
	
	/**
	 * Instantiates a new Hashtable with its initial capacity set to 
	 * {@link SimpleHashtable#DEFAULT_CAPACITY}.
	 */
	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Instantiates a new simple hashtable with the given capacity. 
	 *
	 * @param capacity initial capacity
	 * 
	 * @throws IllegalArgumentException if the given capacity is smaller than 1
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity < 1) {
			throw new IllegalArgumentException("capacity must be positive. Was " + capacity + ".");
		}
		
		table = (TableEntry<K, V>[]) new TableEntry[lowestGreaterPowerOfTwo(capacity)];
		
		limitCapacity = (int) Math.ceil(0.75 * table.length);
	}
	
	/**
	 * Returns closest power of two to the given number that is bigger than it.
	 *
	 * @param value the number in question
	 * @return the closest power of two that is bigger than given number
	 * @throws IllegalArgumentException if the given value is negative
	 */
	private static int lowestGreaterPowerOfTwo(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("value must be non-negative. Was " + value + ".");
		}
		if ((value & (value - 1)) == 0) return value;

		while ((value & (value - 1)) > 0) {
			value = value & (value - 1);
		}

		return value << 1;
	}
	
	/**
	 * Adds the entry (key, value) to the given table of entries. If the item with the given key
	 * already exists in the table, new value overwrites the old one.
	 *
	 * @param tempTable table to be modified
	 * @param key key of the entry
	 * @param value value of the entry
	 * @return false, if item with the given key already existed in the table, true otherwise
	 */
	private boolean addToTable(TableEntry<K, V>[] tempTable, K key, V value) {
		int index = indexOf(key, tempTable.length);
		
		if (tempTable[index] == null) {
			tempTable[index] = new TableEntry<K, V>(key, value, null, null);
		}
		else {
			TableEntry<K, V> entry = tempTable[index];
			while (true) {
				if (entry.key.equals(key)) {
					entry.value = value;
					return false;
				}
				if (entry.next == null) break;

				entry = entry.next;
			}
			entry.next = new TableEntry<K, V>(key, value, null, entry);
		}

		return true;
	}
		
	/**
	 * Doubles the capacity of the table and updates {@link SimpleHashtable#limitCapacity}.
	 * Elements from old table are all copied and added with new hashcodes into the new one.
	 */
	@SuppressWarnings("unchecked")
	private void increaseCapacity() {
		TableEntry<K, V>[] newTable = (TableEntry<K,V>[]) new TableEntry[table.length * 2];
		
		for (int i = 0; i < table.length; ++i) {
			for (TableEntry<K, V> entry = table[i]; entry != null; entry = entry.next) {
				addToTable(newTable, entry.key, entry.value);
			}
		}
		
		table = newTable;
		limitCapacity = (int) Math.ceil(0.75 * table.length);
	}
	
	/**
	 * This helper method takes index in the table and reference to the table entry which is removed.
	 *
	 * @param index the index in the table of the given entry
	 * @param entry the entry to be removed
	 * @return true, if successful
	 */
	private boolean removeEntry(int index, TableEntry<K, V> entry) {
		if (entry == null) return false;

		if (entry.previous == null) {
			table[index] = entry.next;
			if (table[index] != null) table[index].previous = null;
		}
		else {
			entry.previous.next = entry.next;
			if (entry.next != null) entry.next.previous = entry.previous;
		}
		
		--size;
		++modificationCount;
		return true;
	}
	
	/**
	 * Index of the entry with the given key if it resides in the table of the given capacity
	 *
	 * @param key the key
	 * @param capacity the capacity of the table
	 * @return the index of the entry with the given key
	 */
	private int indexOf(Object key, int capacity) {
		return Math.abs(key.hashCode()) % capacity;
	}
			
	/**
	 * Gets the entry with the given key. Returns null-reference if this entry doesn't exist
	 * in the table.
	 *
	 * @param key the key
	 * @return the entry with the given key, or null if it doesn't exist
	 */
	private TableEntry<K, V> getEntry(Object key) {
		if (key == null) return null;
		
		for (TableEntry<K, V> entry = table[indexOf(key, table.length)]; entry != null; entry = entry.next) {
			if (entry.key.equals(key)) {
				return entry;
			}
		}
		
		return null;
	}
		
	/**
	 * Adds new entry with the given key and value. If the key is null-reference exception is 
	 * thrown. If after the addition size became equal or greater than 
	 * {@link SimpleHashtable#limitCapacity} capacity is doubled.
	 *
	 * @param key the key must be not-null
	 * @param value the value can be null
	 * 
	 * @throws NullPointerException if the given key is null
	 */
	public void put(K key, V value) {
		if (key == null) {
			throw new NullPointerException("Key cannot be null.");
		}
		
		if (addToTable(table, key, value)) {
			++size;
			++modificationCount;
			if (size >= limitCapacity) {
				increaseCapacity();
			}
		}
	}

	/**
	 * Returns the value of the given key, or null if the key doesn't exist in the table.
	 *
	 * @param key the key
	 * @return the value of the entry with the given key, or null if the key doesn't exist
	 */
	public V get(Object key) {
		TableEntry<K, V> entry = getEntry(key);

		return entry == null ? null : entry.value;
	}
	
	/**
	 * Returns number of entries in the table.
	 *
	 * @return number of entries in the table.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Checks whether this table contains the entry with the given key. Equality is determined
	 * by equals method.
	 *
	 * @param key the key
	 * @return true, iff the table contains entry with the given key
	 */
	public boolean containsKey(Object key) {
		return getEntry(key) != null;
	}
	
	/**
	 * Check whether this table contains the entry with the given value. Equality is determined
	 * by equals method.
	 *
	 * @param value the value
	 * @return true, iff the table contains at least one entry with the given value.
	 */
	public boolean containsValue(Object value) {
		for (int i = 0; i < table.length; ++i) {
			for (TableEntry<K, V> entry = table[i]; entry != null; entry = entry.next) {
				if (entry.value.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Removes the entry with the given key, if it exists in the table.
	 *
	 * @param key the key of the entry to be removed
	 */
	public void remove(Object key) {
		if (key == null) return;
		
		removeEntry(indexOf(key, table.length), getEntry(key));
	}
	
	/**
	 * Checks if the table is empty
	 *
	 * @return true, iff the table is empty
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Empties the table making all its elements null.
	 */
	public void clear() {
		for (int i = 0; i < table.length; ++i) {
			table[i] = null;
		}
		size = 0;
		++modificationCount;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("[");
		
		for (int i = 0; i < table.length; ++i) {
			for (TableEntry<K, V> entry = table[i]; entry != null; entry = entry.next) {
				if (result.length() > 1) {
					result.append(", ");
				}
				result.append(entry.toString());
			}
		}
		
		return result.append("]").toString();
	}

	/**
	 * Iterator class for the SimpleHashtable. It allows the programmer to traverse the table and
	 * remove elements from the table during iteration. If the table is modified during iteration, 
	 * next call of any of this class's methods throws ConcurrentModification exception.
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {
		
		/** The valid modification count. */
		private int validModificationCount;
		
		/** The index. */
		private int index;
		
		/** The current. */
		private TableEntry<K, V> current;
		
		/** The can remove. */
		private boolean canRemove;
		
		/**
		 * Instantiates a new iterator impl.
		 */
		public IteratorImpl() {
			validModificationCount = modificationCount;
			index = -1;
		}
		
		/**
		 * Checks whether the table was modified by any method outside of this class. In that case,
		 * It throws the exception.
		 * 
		 * @throws ConcurrentModificationException if the table was modified outside of this class.
		 */
		private void checkModificationCount() {
			if (modificationCount != validModificationCount) {
				throw new ConcurrentModificationException(
						"Table has been changed since last time this iterator was accessed");
			}
		}
		
		/**
		 * Returns the next index in the table which points to some element.
		 *
		 * @return the next index which points to non-null entry
		 */
		private int nextIndex() {
			for (int i = index + 1; i < table.length; ++i) {
				if (table[i] != null) return i;
			}
			return -1;
		}


		/**
		 * @throws ConcurrentModificationException if the table was modified outside of this class.
		 */
		@Override
		public boolean hasNext() {
			checkModificationCount();

			if ((current == null || current.next == null) && nextIndex() == -1) return false;

			return true;
		}

		/**
		 * @throws IllegalStateException if this method wasn't called once per call of next
		 * @throws ConcurrentModificationException if the table was modified outside of this class.
		 */
		@Override
		public void remove() {
			checkModificationCount();

			if (!canRemove) {
				throw new IllegalStateException("Remove can be called once per call of next().");
			}
			
			removeEntry(index, current);
			
			canRemove = false;
			++validModificationCount;
		}

		/**
		 * @throws NoSuchElementException if the iterator reached the end
		 * @throws ConcurrentModificationException if the table was modified outside of this class.
		 */
		@Override
		public TableEntry<K, V> next() {
			checkModificationCount();
			
			if (current == null || current.next == null) {
				index = nextIndex();
				if (index == -1) {
					throw new NoSuchElementException("No more elements.");
				}
				current = table[index];
			}
			else {
				current = current.next;
			}
				
			canRemove = true;
			return current;
		}
	}

	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
}
