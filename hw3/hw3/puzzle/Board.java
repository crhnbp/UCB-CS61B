package hw3.puzzle;

import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;

public class Board implements WorldState {
    private ArrayList<Integer> Bd;
    private int size;
    private int BLANK = 0;

    /** Constructs a board from an N-by-N array of tiles where tiles[i][j] = tile at row i, column j */
    public Board(int[][] tiles) {
        Bd = new ArrayList<>();
        size = tiles[0].length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Bd.add(tiles[i][j]);
            }
        }
    }

    /** Returns value of tile at row i, column j (or 0 if blank) */
    public int tileAt(int i, int j) {
        if (i < 0 || i > size - 1 || j < 0 || j > size - 1) {
            throw new IndexOutOfBoundsException("ERROR: index out of bounds");
        }
        return Bd.get(i * size + j);
    }

    /** Returns the board size N */
    public int size() {
        return size;
    }

    @Override
    /** Returns the neighbors of the current board
     *  Code from: http://joshh.ug/neighbors.html */
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;

    }

    /** Returns the Hamming estimate */
    public int hamming() {
        int count = 0;
        for (int i = 0; i < size * size - 1; i++) {
            if (Bd.get(i) != i + 1) {
                count++;
            }
        }
        return count;
    }

    private int[] conv2D(int n) {
        int[] pos = new int[2];
        pos[0] = n / size;
        pos[1] = n % size;
        return pos;
    }

    private int compDist(int[] curr, int[] goal) {
        int dist = 0;
        dist += Math.abs(curr[0] - goal[0]);
        dist += Math.abs(curr[1] - goal[1]);
        return dist;
    }

    /** Returns the Manhattan estimate */
    public int manhattan() {
        int count = 0;
        for (int i = 0; i < size * size - 1; i++) {
            int[] currPos = conv2D(Bd.indexOf(i + 1));
            int[] goalPos = conv2D(i);
            count += compDist(currPos, goalPos);
        }
        return count;
    }

    @Override
    /** Estimated distance to goal. */
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    /** Returns true if is this board the goal board */
    public boolean isGoal() {
        return hamming() == 0;
    }

    @Override
    /** Returns true if this board's tile values are the same position as y's */
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }

        Board bd1 = (Board) y;

        if (this.size != bd1.size) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (Bd.get(i * size + j) != bd1.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
