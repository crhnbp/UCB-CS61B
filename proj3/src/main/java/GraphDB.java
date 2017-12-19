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
    private Tries locTrie = new Tries();

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
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
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
        for (Long id : vertices()) {
            if (adj.get(id).size() == 1) {
                adj.remove(id);
            }
        }
    }

    /** Returns an iterable of all vertex IDs in the graph. */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        ArrayList<Long> verticesIter = new ArrayList<>();
        verticesIter.addAll(adj.keySet());
        return verticesIter;
    }

    /** Returns ids of all vertices adjacent to v. */
    Iterable<Long> adjacent(long v) {
        List<Node> adjNodes = adj.get(v);
        List<Long> adjVertices = new ArrayList<>();
        for (int i = 1; i < adjNodes.size(); i++) {
            adjVertices.add(adjNodes.get(i).getID());
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

        for (Long v : vertices()) {
            double nodeLon = getNode(v).Lon;
            double nodeLat = getNode(v).Lat;
            double lonDist = nodeLon - lon;
            double latDist = nodeLat - lat;
            double dist = Math.sqrt(lonDist * lonDist + latDist * latDist);
            if (dist < closet) {
                vertex = v;
                closet = dist;
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

    public class Node implements Comparable<Node> {
        private long ID;
        private double Lon;
        private double Lat;
        double priority;

        public Node(long id, double lon, double lat) {
            this.ID = id;
            this.Lon = lon;
            this.Lat = lat;
            this.priority = 0;
        }

        public long getID() {
            return this.ID;
        }

        public void setPriority(double prt) {
            this.priority = prt;
        }

        public void setLoc(String locName) {
            locTrie.put(locName, this.ID);
        }

        @Override
        public int compareTo(Node n) {
            if (n.priority > this.priority) {
                return -1;
            }
            else if (n.priority < this.priority) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    void addNode(long id, double lon, double lat) {
        ArrayList<Node> adjList = new ArrayList<>();
        adjList.add(new Node(id, lon, lat));
        adj.put(id, adjList);
    }

    Node getNode(long id) {
        return adj.get(id).get(0);
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

    List<String> getLocationsByPrefix(String prefix) {
        return locTrie.keysWithPrefix(cleanString(prefix));
    }

    List<Map<String, Object>> getNodesByLocName(String locName) {
        List<Map<String, Object>> r = new ArrayList<>();
        List<Long> nodesID = locTrie.getNodeByLoc(locName);
        for (long id : nodesID) {
            System.out.println(id);
            Map<String, Object> n = new HashMap<>();
            n.put("lat", lat(id));
            n.put("lon", lon(id));
            n.put("name", locName);
            n.put("id", id);
            r.add(n);
        }
        return r;
    }

    void addWay(ArrayList<Long> way) {
        for (int i = 0; i < way.size() - 1; i++) {
            addEdge(way.get(i), way.get(i+1));
        }
    }

}
