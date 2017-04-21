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
        LinkedList<Long> path = new LinkedList<Long>();
        long first = g.closest(stlon, stlat);
        Point firstPoint = g.getNode(first);
        long end = g.closest(destlon, destlat);
        Point endPoint = g.getNode(end);
        double heuristic = g.distance(first, end);
        double dist = 0;
        path.add(first);
        //GraphBuildingHandler x = new GraphBuildingHandler(g);
        Finder f = new Finder(g.nodes);
        return f.stPath(firstPoint, endPoint);
    }

    /*public static void aStar(GraphDB g, Point start, Point end) {
        HashSet<Point> closedSet = new HashSet<>();
        ArrayHeap<Point> openSet = new ArrayHeap<>();
        openSet.insert(start, euclidDist(start, end));
    }*/
}