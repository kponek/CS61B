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

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     *
     * @param dbPath Path to the XML file to be parsed.
     */
    Map<Long, Point> nodes = new HashMap<>();
    private Map<Point, HashSet<Edge>> edges;
    private HashMap<String, Point> nodeNames = new HashMap<>();

    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
            //my code
            //nodes = gbh.getNodes();
            //edges = gbh.getEdges();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Iterable<Long> v = vertices();
        for (long l : v) {
            int adjCount = 0;
            for (long a : adjacent(l)) {
                adjCount++;
            }
            if (adjCount == 0) {
                //removeNode(l);
                nodes.remove(l);
            }
        }
        /*Iterator v = vertices().iterator();
        while (v.hasNext()) {
            Iterable<Long> adj = adjacent((Long) v.next());
            if (adj instanceof Collection && ((Collection<?>) adj).size() == 0) {
                v.remove();
            } else {
                int count = 0;
                Iterator iterator = adj.iterator();
                while (iterator.hasNext()) {
                    iterator.next();
                    count++;
                }
                if (count == 0) {
                    v.remove();
                }
            }
        }*/
        /*for (long l : v) {
            int adjCount = 0;
            for (long a : adjacent(l)) {
                adjCount++;
            }
            if (adjCount == 0) {
                //removeNode(l);
                nodes.remove(l);
            }
        }*/
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     */
    Iterable<Long> vertices() {
        return nodes.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     */
    Iterable<Long> adjacent(long v) {
        return nodes.get(v).getAdjacentIds();
    }

    /**
     * Returns the distance in units of longitude between vertices v and w.
     */
    double distance(long v, long w) {
        double lonDiff = lon(v) - lon(w);
        double latDiff = lat(v) - lat(w);
        return Math.sqrt(lonDiff * lonDiff + latDiff * latDiff);
    }

    /**
     * Returns the vertex id closest to the given longitude and latitude.
     */
    long closest(double lon, double lat) {
        double minDist = Double.MAX_VALUE;
        Point minPoint = null;
        if (nodes == null) {
            return 0;
        }
        for (Point p : nodes.values()) {
            double lonDiff = p.getLon() - lon;
            double latDiff = p.getLat() - lat;
            double dist = Math.sqrt(lonDiff * lonDiff + latDiff * latDiff);
            if (dist < minDist) {
                minDist = dist;
                minPoint = p;
            }
        }
        return minPoint.getId();
    }

    /**
     * Longitude of vertex v.
     */
    double lon(long v) {
        return nodes.get(v).getLon();
    }

    /**
     * Latitude of vertex v.
     */
    double lat(long v) {
        return nodes.get(v).getLat();
    }

    public void addNode(long id, double lat, double lon) {
        Point n = new Point(id, lat, lon);
        nodes.put(id, n);
    }

    public void addNodeName(Point n, String name) {
        nodeNames.put(name, n);
    }

    public void addEdge(Point a, Point b) {
        //implement
    }

    public void addWay(ArrayList<Long> ids) {
        //implement
    }

    public void removeNode(long id) {
        nodes.remove(id);
    }

    public Point getNode(long id) {
        return nodes.get(id);
    }
}