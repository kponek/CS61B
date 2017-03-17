package lab9;

import edu.princeton.cs.algs4.SequentialSearchST;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by kevin on 3/16/2017.
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    private int n; //number of key-value pairs
    private int m; //hash table size
    private double factor;
    private SequentialSearchST<K, V>[] map;
    private HashSet keys;

    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    public MyHashMap(int initialSize, double loadFactor) {
        keys = new HashSet();
        m = initialSize;
        map = new SequentialSearchST[m];
        factor = loadFactor;
        for (int i = 0; i < m; i++) {
            map[i] = new SequentialSearchST<K, V>();
        }
    }

    private void resize(int chains) {
        MyHashMap<K, V> temp = new MyHashMap<K, V>(chains);
        //ArrayList<V>[] temp = new ArrayList[2 * m];
        for (int i = 0; i < m; i++) {
            for (K key : map[i].keys()) {
                temp.put(key, map[i].get(key));
            }
        }
        m = temp.m;
        map = temp.map;
    }

    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    public void clear() {
        map = new SequentialSearchST[m];
        for (int i = 0; i < m; i++) {
            map[i] = new SequentialSearchST<K, V>();
        }
        n = 0;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        int i = hash(key);
        return map[i].get(key);
    }

    public int size() {
        return n;
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (n >= factor * m) {
            resize(2 * m);
        }
        int i = hash(key);
        if (!map[i].contains(key)) {
            n++;
        }
        map[i].put(key, value);
        keys.add(key);
    }

    public Set<K> keySet() {
        return keys;
    }

    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public Iterator iterator() {
        return keySet().iterator();
    }
}
