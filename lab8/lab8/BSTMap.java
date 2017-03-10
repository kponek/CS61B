package lab8;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    //idea of Node class taken from BST.java from princeton library
    private class Node {
        private K key;
        private V value;
        private Node left, right;
        private int nodeSize;

        private Node(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.nodeSize = size;
        }
    }

    private Node root;

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        return (get(key) != null);
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node t, K key) {
        if (key == null) {
            return null;
        } else if (t.key == key) {
            return t.value;
        } else if (key.compareTo(t.key) < 0) {
            return get(t.left, key);
        } else {
            return get(t.right, key);
        }
    }

    @Override
    public int size() {
        return size(root);
    }
    private int size(Node t) {
        if (t == null) {
            return 0;
        }
        return t.nodeSize;
    }

    @Override
    public void put(K key, V value) {
        put(root,key,value);
    }

    private Node put(Node t, K key, V val) {
        if (t == null) {
            return new Node(key, val, 1);
        } else if (key.compareTo(t.key) < 0) {
            t.left = put(t.left, key, val);
        } else if (key.compareTo(t.key) > 0){
            t.right = put(t.right, key, val);
        } else {
            t.value = val;
        }
        t.nodeSize = size(t.left) + size(t.right) + 1;
        return t;
    }

    //prints out the Map in order of increasing Key
    public void printInOrder() {

    }

    @Override
    public Set keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }
}
