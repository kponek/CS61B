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
        long end = g.closest(stlon, stlat);
        double heuristic = g.distance(first, end);
        path.add(first);
        return path;
    }
    /*HashMap<Long, Point> vertices;
    HashMap<Point, HashSet<Edge>> edges;
    HashMap<Long, Double> distTo;
    HashMap<Long, Long> prev;
    Point start;
    Point target;
    private class SearchNode implements Comparable {
        Point pt;
        double priority;
        SearchNode prevNode;
        double dist;
        private Point targett;

        public SearchNode(Point ptt, Point end, double distt) {
            this.pt = ptt;
            this.dist = distt;
            this.targett = end;
            this.priority = RouteGraph.euclidDist(ptt, end) + distt;
        }
        @Override
        public int compareTo(Object o) {
            double difference = this.getPriority() - ((SearchNode) o).getPriority();
            if (difference < 0) {
                return -1;
            } else if (difference > 0) {
                return 1;
            } else {
                return 0;
            }
        }
        public double getPriority() {
            return this.priority;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || !o.getClass().equals(this.getClass())) {
                return false;
            } else {
                return (this.pt.id == ((SearchNode) o).pt.id);
            }
        }
        @Override
        public int hashCode() {
            return super.hashCode();
        }
        public void setPriority(double distance) {
            this.priority = GraphDB.distance(pt, this.targett) + distance;
        }
    }

    public LinkedList<Long> findAndSetRoute(double startLon, double startLat,
                                            double endLon, double endLat) {
        Point startt = this.findNearestPointTo(startLon, startLat);
        Point end = this.findNearestPointTo(endLon, endLat);
        this.start = startt;
        this.target = end;
        return this.stPath(startt, end);
    }
    public RouteGraph(HashMap<Long, Point> verticess, HashMap<Point, HashSet<Edge>> edgess) {
        this.vertices = verticess;
        this.edges = edgess;
        distTo = new HashMap<>();
        prevPoint = new HashMap<>();
        target = null;
    }
    public LinkedList<Long> stPath(Point s, Point t) {
        HashSet<Long> visited = new HashSet<>();
        HashMap<Long, Double> dist = new HashMap<>();
        HashMap<Point, Point> prev = new HashMap<>();
        PriorityQueue<SearchNode> fringe = new PriorityQueue<>();
        SearchNode startingNode = new SearchNode(s, t, 0.0);
        dist.put(startingNode.pt.id, 0.0);
        fringe.add(startingNode);
        SearchNode node;
        while (!fringe.isEmpty()) {
            node = fringe.remove();
            if (visited.contains(node.pt.getId())) {
                continue;
            }
            visited.add(node.pt.getId());
            if (node.pt.equals(t)) {
                break;
            }
            for (Edge e : edges.get(node.pt)) {
                Point newPt = e.end;
                SearchNode child = new SearchNode(newPt, t, 0.0);
                if (!dist.containsKey(child.pt.id) || dist.get(child.pt.id) > dist.get(node.pt.id) + e.length) {
                    double childTrueDistance = dist.get(node.pt.id) + e.length;
                    SearchNode trueChild = new SearchNode(newPt, t, childTrueDistance);
                    dist.put(trueChild.pt.id, childTrueDistance);
                    fringe.add(trueChild);
                    prev.put(trueChild.pt, node.pt);
                }
            }
        }*/
}
