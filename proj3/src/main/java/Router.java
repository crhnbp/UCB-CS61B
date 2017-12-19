import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest, 
     * where the longs are node IDs.
     */
    public static LinkedList<Long> shortestPath(GraphDB g, double stlon, double stlat, double destlon, double destlat) {
        /** Data structures: fringe (priority queue), best (two hash maps) */
        PriorityQueue<GraphDB.Node> fringe = new PriorityQueue<>();
        LinkedList<Long> path = new LinkedList<>();
        HashMap<Long, Double> distTo = new HashMap<>();
        HashMap<Long, Long> edgeTo = new HashMap<>();

        Iterable<Long> allVertices = g.vertices();
          for (Long vertex : allVertices) {
            distTo.put(vertex, Double.MAX_VALUE);
        }

        /** Start by adding the source to fringe. Initialize best. */
        Long start = g.closest(stlon, stlat);
        GraphDB.Node startNode = g.getNode(start);    // Start Node
        startNode.setPriority(0);
        distTo.put(start, 0.0);
        fringe.add(startNode);

        Long dest = g.closest(destlon, destlat);    // End Node
        path.add(dest);

        /** A* algorithm */
        while (fringe.peek().getID() != dest) {
            long curr = fringe.poll().getID();
            for (long adj : g.adjacent(curr)) {
                double distAdj = distTo.get(curr) + g.distance(curr, adj);
                if (distAdj < distTo.get(adj)) {
                    distTo.put(adj, distAdj);
                    edgeTo.put(adj, curr);
                    GraphDB.Node adjNode = g.getNode(adj);
                    adjNode.setPriority(distAdj + g.distance(adj, dest));
                    if (fringe.contains(adjNode)) {
                        fringe.remove(adjNode);
                        fringe.add(adjNode);
                    } else {
                        fringe.add(adjNode);
                    }
                }
            }
        }

        /** Path to goal */
        for (Long edge = edgeTo.get(dest); edge != null; edge = edgeTo.get(edge)) {
            path.addFirst(edge);
        }
        return path;
    }
}
