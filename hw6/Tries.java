import java.util.ArrayList;
import java.util.List;

public class Tries {
    private static final int R = 256;
    private Node root = new Node();

    private static class Node {
        private boolean exists;
        private Node[] next;

        public Node() {
            next = new Node[R];
            exists = false;
        }
    }

    public void put(String key) {
        put(root, key, 0);
    }

    private Node put(Node n, String key, int d) {
        if (n == null) {
            n = new Node();
        }

        if (d == key.length()) {
            n.exists = true;
            return n;
        }

        char c = key.charAt(d);
        n.next[c] = put(n.next[c], key, d+1);
        return n;
    }

    public List<String> keysWithPrefix(String pre) {
        List<String> keys = new ArrayList<>();
        Node x = root;
        for (int i = 0; i < pre.length(); i++) {
            char c = pre.charAt(i);
            x = x.next[c];
        }
        collect(x, pre, keys);
        return keys;
    }

    public boolean keysThatMatch(String key) {
        Node x = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            x = x.next[c];
        }
        return x.exists;
    }

    private void collect(Node n, String pre, List<String> l) {
        if (n == null) {return;}
        if (n.exists) {
            l.add(pre);
        }
        for (char c = 0; c < R; c++) {
            collect(n.next[c], pre + c, l);
        }
    }
}
