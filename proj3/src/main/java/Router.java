import com.sun.corba.se.impl.orbutil.graph.Graph;

import java.util.*;

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
        long first = g.closest(stlon, stlat);
        Point firstPoint = g.getNode(first);
        long end = g.closest(destlon, destlat);
        Point endPoint = g.getNode(end);
        Finder f = new Finder(g.getNodes());
        return f.stPath(firstPoint, endPoint);
    }
}