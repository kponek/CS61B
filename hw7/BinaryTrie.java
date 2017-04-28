import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 4/28/2017.
 */
public class BinaryTrie implements Serializable {
    private Node root;

    //this Node class is inspired by Huffman.java from princeton library
    private class Node implements Comparable<Node> {
        private char character;
        private int frequency;
        private Node left;
        private Node right;

        Node(char ch, int freq, Node left, Node right) {
            character = ch;
            frequency = freq;
            this.left = left;
            this.right = right;
        }

        private boolean isLeaf() {
            return left == null && right == null;
        }

        public int compareTo(Node comp) {
            return this.frequency - comp.frequency;
        }
    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        MinPQ<Node> pq = new MinPQ<>();
        for (Character c : frequencyTable.keySet()) {
            pq.insert(new Node(c, frequencyTable.get(c), null, null));
        }
        while (pq.size() > 1) {
            Node left = null;
            Node right = null;
            if (pq.size() != 0) {
                left = pq.delMin();
            }
            if (pq.size() != 0) {
                right = pq.delMin();
            }

            Node parent = new Node('\0', left.frequency + right.frequency, left, right);
            pq.insert(parent);
        }
        root = pq.delMin();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node cur = root;
        String match = "";
        for (int i = 0; i < querySequence.length(); i++) {
            match += querySequence.bitAt(i);
            if (querySequence.bitAt(i) == 0) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
            if (cur.character != ('\0')) {
                return new Match(new BitSequence(match), cur.character);
            }
        }
        return null;
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> searchBits = new HashMap<>();
        buildHelper(root, "", searchBits);
        return searchBits;

    }

    private void buildHelper(Node n, String bits, Map<Character, BitSequence> searchBits) {
        if (n.character != '\0') {
            searchBits.put(n.character, new BitSequence(bits));
        } else {
            if (n.left != null) {
                buildHelper(n.left, bits + "0", searchBits);
            }
            if (n.right != null) {
                buildHelper(n.right, bits + "1", searchBits);
            }
        }
    }
}
