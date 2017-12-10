import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    private HashMap<Long, List<Node>> adj = new HashMap<>();    // adjacency lists

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
            //adj = new HashMap<>();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        //clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TODO: Your code here.
        adj.clear();
    }

    /** Returns an iterable of all vertex IDs in the graph. */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return adj.keySet();
        //return new ArrayList<Long>();
    }

    /** Returns ids of all vertices adjacent to v. */
    Iterable<Long> adjacent(long v) {
        List<Node> adjNodes = adj.get(v);
        List<Long> adjVertices = new ArrayList<>();
        for (Node nd : adjNodes) {
            adjVertices.add(nd.ID);
        }
        return adjVertices;
    }

    /** Returns the Euclidean distance between vertices v and w, where Euclidean distance
     *  is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ). */
    double distance(long v, long w) {
        double vlon = adj.get(v).get(0).Lon;
        double vlat = adj.get(v).get(0).Lat;
        double wlon = adj.get(w).get(0).Lon;
        double wlat = adj.get(w).get(0).Lat;
        double lonDist = vlon - wlon;
        double latDist = vlat - wlat;
        return Math.sqrt(lonDist * lonDist + latDist * latDist);
    }

    /** Returns the vertex id closest to the given longitude and latitude. */
    long closest(double lon, double lat) {
        double closet = Double.MAX_VALUE;
        long vertex = 0;

        for (HashMap.Entry<Long, List<Node>> entry : adj.entrySet()) {
            double nodeLon = entry.getValue().get(0).Lon;
            double nodeLat = entry.getValue().get(0).Lat;
            double lonDist = nodeLon - lon;
            double latDist = nodeLat - lat;
            if (Math.sqrt(lonDist * lonDist + latDist * latDist) < closet) {
                vertex = entry.getKey();
            }
        }
        return vertex;
    }

    /** Longitude of vertex v. */
    double lon(long v) {
        return adj.get(v).get(0).Lon;
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return adj.get(v).get(0).Lat;
    }

    public class Node {
        private long ID;
        private double Lon;
        private double Lat;

        public Node(long id, double lon, double lat) {
            this.ID = id;
            this.Lon = lon;
            this.Lat = lat;
        }
    }

    void addNode(long id, double lon, double lat) {
        ArrayList<Node> adjList = new ArrayList<>();
        adjList.add(new Node(id, lon, lat));
        adj.put(id, adjList);
    }


    void addEdge(long id1, long id2) {
        double lon1 = adj.get(id1).get(0).Lon;
        double lat1 = adj.get(id1).get(0).Lat;
        double lon2 = adj.get(id2).get(0).Lon;
        double lat2 = adj.get(id2).get(0).Lat;
        adj.get(id1).add(new Node(id2, lon2, lat2));
        adj.get(id2).add(new Node(id1, lon1, lat1));
    }

    void removeEdge(long id1, long id2) {
        for (Node nd : adj.get(id1)) {
            if (nd.ID == id2) {
                adj.get(id1).remove(nd);
            }
        }

        for (Node nd : adj.get(id2)) {
            if (nd.ID == id1) {
                adj.get(id2).remove(nd);
            }
        }
    }

    void addWay(ArrayList<Long> way) {
        for (int i = 0; i < way.size() - 1; i++) {
            addEdge(way.get(i), way.get(i+1));
        }
    }

}
