package lab8;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    //idea of Node class taken from BST.java from princeton library
    private class Node {
        private K key;
        private V value;
        private Node left, right;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }

        private void printInOrder() {
            this.left.printInOrder();
            System.out.print(this.key + " ");
            this.right.printInOrder();
        }
    }

    private Node root;
    private int size = 0;

    @Override
    public void clear() {
        size = 0;
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
        if (t == null) {
            return null;
        } else if (t.key.compareTo(key) == 0) {
            return t.value;
        } else if (key.compareTo(t.key) < 0) {
            return get(t.left, key);
        } else {
            return get(t.right, key);
        }
    }

    @Override
    public int size() {
        return size;
    }

    private int size(Node t) {
        if (t == null) {
            return 0;
        }
        return 0;
    }

    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private Node put(Node t, K key, V val) {
        if (t == null) {
            size++;
            t = new Node(key, val);
        } else if (key.compareTo(t.key) < 0) {
            t.left = put(t.left, key, val);
        } else if (key.compareTo(t.key) > 0) {
            t.right = put(t.right, key, val);
        } else {
            t.value = val;
        }
        return t;
    }

    //prints out the Map in order of increasing Key
    public void printInOrder() {
        root.printInOrder();
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
