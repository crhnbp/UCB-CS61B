package lab9;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class MyHashMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private double lF;
    private int M;
    private int N;
    private LinkedList<Item>[] LList;
    private Set<K> kSet;

    private class Item {
        private K key;
        private V val;

        public Item(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }


    public MyHashMap() {
        this(997);
    }

    public MyHashMap(int initialSize) {
        this.M = initialSize;
        this.LList = new LinkedList[initialSize];
        kSet = new HashSet<>();
    }

    public MyHashMap(int initialSize, double loadFactor) {
        this.M = initialSize;
        LList = new LinkedList[initialSize];
        lF = loadFactor;
    }

    /** Compute the hashcode for the key. */
    private int hashCode(K key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    /** Removes all of the mappings from this map. */
    public void clear() {
        LList = new LinkedList[M];
        N = 0;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        int hc = hashCode(key);
        if (LList[hc] == null) {
            return false;
        }
        for (int i = 0; i < LList[hc].size(); i++) {
            if (LList[hc].get(i).key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /** Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        int hc = hashCode(key);
        if (LList[hc] == null) {
            return null;
        }
        for (int i = 0; i < LList[hc].size(); i++) {
            if (LList[hc].get(i).key.equals(key)) {
                return LList[hc].get(i).val;
            }
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    public int size() {
        return N;
    }

    /** Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        Item p = new Item(key, value);
        int hc = hashCode(key);

        if (LList[hc] == null) {
            LList[hc] = new LinkedList<>();
            LList[hc].add(p);
            kSet.add(key);
            N++;
        } else {
            for (int i = 0; i < LList[hc].size(); i++) {
                if (LList[hc].get(i).key.equals(key)) {
                    LList[hc].set(i, p);
                }
            }
        }
    }

    /** Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        return kSet;
    }

    @Override
    public Iterator<K> iterator() {
        return kSet.iterator();
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
}
