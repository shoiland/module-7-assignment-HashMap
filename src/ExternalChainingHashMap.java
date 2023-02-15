import java.util.NoSuchElementException;
import java.lang.Math;
/**
 * Your implementation of a ExternalChainingHashMap.
 */
public class ExternalChainingHashMap<K, V> {

    /*
     * The initial capacity of the ExternalChainingHashMap when created with the
     * default constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    /*
     * The max load factor of the ExternalChainingHashMap.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final double MAX_LOAD_FACTOR = 0.67;

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private ExternalChainingMapEntry<K, V>[] table;
    private int size;

    /**
     * Constructs a new ExternalChainingHashMap with an initial capacity of INITIAL_CAPACITY.
     */
    public ExternalChainingHashMap() {
        //DO NOT MODIFY THIS METHOD!
        table = (ExternalChainingMapEntry<K, V>[]) new ExternalChainingMapEntry[INITIAL_CAPACITY];
    }

    /**
     * Adds the given key-value pair to the map. If an entry in the map
     * already has this key, replace the entry's value with the new one
     * passed in.
     *
     * In the case of a collision, use external chaining as your resolution
     * strategy. Add new entries to the front of an existing chain, but don't
     * forget to check the entire chain for duplicate keys first.
     *
     * If you find a duplicate key, then replace the entry's value with the new
     * one passed in. When replacing the old value, replace it at that position
     * in the chain, not by creating a new entry and adding it to the front.
     *
     * Before actually adding any data to the HashMap, you should check to
     * see if the table would violate the max load factor if the data was
     * added. Resize if the load factor (LF) is greater than max LF (it is
     * okay if the load factor is equal to max LF). For example, let's say
     * the table is of length 5 and the current size is 3 (LF = 0.6). For
     * this example, assume that no elements are removed in between steps.
     * If another entry is attempted to be added, before doing anything else,
     * you should check whether (3 + 1) / 5 = 0.8 is larger than the max LF.
     * It is, so you would trigger a resize before you even attempt to add
     * the data or figure out if it's a duplicate. Be careful to consider the
     * differences between integer and double division when calculating load
     * factor.
     *
     * When regrowing, resize the length of the backing table to
     * (2 * old length) + 1. You should use the resizeBackingTable method to do so.
     *
     * @param key   The key to add.
     * @param value The value to add.
     * @return null if the key was not already in the map. If it was in the
     *         map, return the old value associated with it.
     * @throws java.lang.IllegalArgumentException If key or value is null.
     */
    public V put(K key, V value) {
        // WRITE YOUR CODE HERE (DO NOT MODIFY METHOD HEADER)!
        if (key == null || value == null){
            throw new IllegalArgumentException();
        }
        float currentLoadFactor = (float)(this.size + 1) / table.length;
        if (currentLoadFactor > MAX_LOAD_FACTOR){
            resizeBackingTable(2 * table.length + 1);
        }
        int hash = key.hashCode();
        int compressed = Math.abs(hash % table.length);
        if (table[compressed] == null){
            ExternalChainingMapEntry<K, V> newEntry = new ExternalChainingMapEntry<>(key, value);
            table[compressed] = newEntry;
            this.size++;
        } else {
            V valueDuplicate = loopThroughSSL(table[compressed], key, value);
            if (valueDuplicate != null){
                return valueDuplicate;
            }
            ExternalChainingMapEntry<K, V> newEntry = new ExternalChainingMapEntry<>(key, value);
            ExternalChainingMapEntry<K, V> temp = table[compressed];
            table[compressed] = newEntry;
            newEntry.setNext(temp);
            this.size++;
        }
        return null;
    }

    private V loopThroughSSL(ExternalChainingMapEntry<K, V> current, K key, V value){
        V tempValue;
        if (current.getKey().equals(key)){
            tempValue = current.getValue();
            current.setValue(value);
        } else if (current.getNext() == null){
            return null;
        } else {
            tempValue = loopThroughSSL(current.getNext(), key, value);
        }
        return tempValue;
    }

    /**
     * Removes the entry with a matching key from the map.
     *
     * @param key The key to remove.
     * @return The value associated with the key.
     * @throws java.lang.IllegalArgumentException If key is null.
     * @throws java.util.NoSuchElementException   If the key is not in the map.
     */
    public V remove(K key) {
        V foundValue = null;
        if (key == null){
            throw new IllegalArgumentException();
        }
        //Get Key into correct format
        int hashKey = key.hashCode();
        int compressionKey = Math.abs(hashKey % table.length);
        //Check if the array is empty at that location
        if (table[compressionKey] == null){
            throw new NoSuchElementException();
        }
        //It is not empty. Check if the location is the product and if so set to node next
        if (table[compressionKey].getKey().equals(key)){
            foundValue = table[compressionKey].getValue();
            table[compressionKey] = table[compressionKey].getNext();
            this.size--;
            return foundValue;
        }
        //It isn't in the first location, so start to search through the linked list
        ExternalChainingMapEntry<K, V> current = table[compressionKey];
        while(current.getNext() != null){
            if (current.getNext().getKey().equals(key)){
                foundValue = current.getNext().getValue();
                current.setNext(current.getNext().getNext());
                this.size--;
                return foundValue;
            }
            current = current.getNext();
        }
        throw new NoSuchElementException();
    }


    /**
     * Helper method stub for resizing the backing table to length.
     *
     * This method should be called in put() if the backing table needs to
     * be resized.
     *
     * You should iterate over the old table in order of increasing index and
     * add entries to the new table in the order in which they are traversed.
     *
     * Since resizing the backing table is working with the non-duplicate
     * data already in the table, you won't need to explicitly check for
     * duplicates.
     *
     * Hint: You cannot just simply copy the entries over to the new table.
     *
     * @param Length The new length of the backing table.
     */
    private void resizeBackingTable(int length) {
        // WRITE YOUR CODE HERE (DO NOT MODIFY METHOD HEADER)!
        ExternalChainingMapEntry<K, V>[] newTable = (ExternalChainingMapEntry<K, V>[]) new ExternalChainingMapEntry[length];
        for (int i = 0; i < table.length; i++){
            if (table[i] != null){
                ExternalChainingMapEntry<K, V> currentEntry = table[i];
                resizingHelper(currentEntry, newTable);
                table[i] = null;
            }
        }
        table = newTable;
    }

    private void resizingHelper(ExternalChainingMapEntry<K, V> entry, ExternalChainingMapEntry<K, V>[] newTable) {
        K currentKey = entry.getKey();
        int hashcode = currentKey.hashCode();
        int compressed = Math.abs(hashcode % newTable.length);
        ExternalChainingMapEntry<K, V> next = entry.getNext();
        if (newTable[compressed] == null){
            entry.setNext(null);
            newTable[compressed] = entry;
        } else {
            ExternalChainingMapEntry<K, V> temp = newTable[compressed];
            entry.setNext(null);
            newTable[compressed] = entry;
            entry.setNext(temp);
        }
        if (next != null) {
            resizingHelper(next, newTable);
        }
    }
    /**
     * Returns the table of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return The table of the map.
     */
    public ExternalChainingMapEntry<K, V>[] getTable() {
        // DO NOT MODIFY THIS METHOD!
        return table;
    }

    /**
     * Returns the size of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return The size of the map.
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}