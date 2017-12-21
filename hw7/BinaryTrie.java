import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
    private Node Trie;
    Map<Character, BitSequence> lookupTable;

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        Trie = buildTrie(frequencyTable);
        lookupTable = new HashMap<>();
    }

    private static class Node implements Serializable, Comparable<Node> {
        private final char ch;
        private int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public int compareTo(Node n) {
            return this.freq - n.freq;
        }
    }

    private Node buildTrie(Map<Character, Integer> frequencyTable) {
        MinPQ<Node> pq = new MinPQ<>();
        for (char c : frequencyTable.keySet()) {
            int f = frequencyTable.get(c);
            pq.insert(new Node(c, f, null, null));
        }

        while (pq.size() > 1) {
            Node left = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }

        return pq.delMin();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node curr = Trie;
        for (int i = 0; i < querySequence.length(); i++) {
            if (curr.isLeaf()) {
                return new Match(querySequence.firstNBits(i), curr.ch);
            } else {
                int bit = querySequence.bitAt(i);
                if (bit == 0) {
                    curr = curr.left;
                } else {
                    curr = curr.right;
                }
            }
        }
        return new Match(querySequence, curr.ch);
    }

    public Map<Character, BitSequence> buildLookupTable() {
        buildLookupTable(lookupTable, Trie, "");
        return lookupTable;
    }

    private void buildLookupTable(Map<Character, BitSequence> lookupTable, Node x, String s) {
        if (!x.isLeaf()) {
            buildLookupTable(lookupTable, x.left, s + '0');
            buildLookupTable(lookupTable, x.right, s + '1');
        } else {
            lookupTable.put(x.ch, new BitSequence(s));
        }
    }
}
