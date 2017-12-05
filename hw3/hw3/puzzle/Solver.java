package hw3.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;

public class Solver {

    private ArrayList<WorldState> Solution;

    /** Constructor which solves the puzzle, computing everything necessary
     * for moves() and solution() to not have to solve the problem again.
     * Solves the puzzle using the A* algorithm.
     * Assumes a solution exists. */
    public Solver(WorldState initial) {
        MinPQ<SNode> WSPQ = new MinPQ<>();
        Solution = new ArrayList<>();
        WSPQ.insert(new SNode(initial, 0, null));

        while (!WSPQ.min().getWS().isGoal()) {
            SNode minPrior = WSPQ.delMin();
            for (WorldState ws : minPrior.getWS().neighbors()) {
                if (minPrior.getPrev() == null) {
                    WSPQ.insert(new SNode(ws, minPrior.getMove() + 1, minPrior));
                } else if (!(ws.equals(minPrior.getPrev().getWS()))) {
                    WSPQ.insert(new SNode(ws, minPrior.getMove() + 1, minPrior));
                }
            }
        }

        SNode s = WSPQ.min();
        while (s != null) {
            Solution.add(0, s.getWS());
            s = s.getPrev();
        }
    }

    /** Returns the minimum number of moves to solve the puzzle
     * starting at the initial WorldState. */
    public int moves() {
        return Solution.size() - 1;
    }

    /** Returns a sequence of WorldStates from the initial WorldState to the solution. */
    public Iterable<WorldState> solution() {
        return Solution;
    }

    public class SNode implements Comparable<SNode>{
        private WorldState ws;
        private int move;
        private SNode prev;

        public SNode(WorldState w, int mv, SNode sn) {
            ws = w;
            move = mv;
            prev = sn;
        }

        public WorldState getWS() {
            return ws;
        }

        public int getMove() {
            return move;
        }

        public SNode getPrev() {
            return prev;
        }

        @Override
        public int compareTo(SNode sn) {
            if (sn.move + sn.ws.estimatedDistanceToGoal() > move +ws.estimatedDistanceToGoal()) {
                return -1;
            }
            else if (sn.move + sn.ws.estimatedDistanceToGoal() < move + ws.estimatedDistanceToGoal()) {
                return 1;
            } else {
                return 0;
            }
        }

    }
}
