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

    /*public void aStar(GraphDB g, long) {
        HashSet<Point> visited = new HashSet<>();
        PriorityQueue<Point>
    }*/
    HashMap<Long, Point> vertices;
    HashMap<Point, HashSet<Edge>> edges;
    HashMap<Long, Double> distTo;
    HashMap<Long, Long> prevPoint;
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
            this.priority = Router.euclidDist(ptt, end) + distt;
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
                return (this.pt.getId() == ((SearchNode) o).pt.getId());
            }
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        public void setPriority(double distance) {
            this.priority = Router.euclidDist(pt, this.targett) + distance;
        }
    }

    public Router(HashMap<Long, Point> verticess, HashMap<Point, HashSet<Edge>> edgess) {
        this.vertices = verticess;
        this.edges = edgess;
        distTo = new HashMap<>();
        prevPoint = new HashMap<>();
        target = null;
    }

    public LinkedList<Long> findAndSetRoute(double startLon, double startLat,
                                            double endLon, double endLat) {
        Point startt = this.findNearestPointTo(startLon, startLat);
        Point end = this.findNearestPointTo(endLon, endLat);
        this.start = startt;
        this.target = end;
        return this.stPath(startt, end);
    }

    public LinkedList<Long> stPath(Point startt, Point targett) {
        HashSet<Long> visited = new HashSet<>();
        HashMap<Long, Double> dist = new HashMap<>();
        HashMap<Point, Point> prev = new HashMap<>();
        PriorityQueue<SearchNode> fringe = new PriorityQueue<>();
        SearchNode startingNode = new SearchNode(startt, targett, 0.0);
        dist.put(startingNode.pt.getId(), 0.0);
        fringe.add(startingNode);
        SearchNode node;
        while (!fringe.isEmpty()) {
            node = fringe.remove();
            if (visited.contains(node.pt.getId())) {
                continue;
            }
            visited.add(node.pt.getId());
            if (node.pt.equals(targett)) {
                break;
            }
            for (Edge e : edges.get(node.pt)) {
                Point newPt = e.getEnd();
                SearchNode child = new SearchNode(newPt, targett, 0.0);
                if (!dist.containsKey(child.pt.getId()) || dist.get(child.pt.getId()) > dist.get(node.pt.getId()) + e.getLength()) {
                    double childTrueDistance = dist.get(node.pt.getId()) + e.getLength();
                    SearchNode trueChild = new SearchNode(newPt, targett, childTrueDistance);
                    dist.put(trueChild.pt.getId(), childTrueDistance);
                    fringe.add(trueChild);
                    prev.put(trueChild.pt, node.pt);
                }
            }
        }
        Point currPoint = targett;
        Point prevPoint = prev.get(targett);
        LinkedList<Long> soln = new LinkedList<>();
        while (!currPoint.equals(start)) {
            soln.addFirst(currPoint.getId());
            currPoint = prevPoint;
            prevPoint = prev.get(prevPoint);
        }
        soln.addFirst(currPoint.getId());
        return soln;
    }

    public void initializeFringe(ArrayHeap<Point> fringee, HashMap<Long, Double> distToo,
                                 HashMap<Long, Long> prevPointt) {
        for (Point pt : vertices.values()) {
            if (pt.getId() != start.getId()) {
                fringee.insert(pt, 999999.99);
                distToo.put(pt.getId(), 999999.99);
                prevPointt.put(pt.getId(), null);
            }
        }
    }

    public LinkedList<Long> createSoln(SearchNode pt) {
        LinkedList<Long> soln = new LinkedList<Long>();
        while (pt != null) {
            soln.addLast(pt.pt.getId());
            pt = pt.prevNode;
        }
        return soln;
    }

    public static double euclidDist(Point a, Point b) {
        double lonDist = (a.getLon() - b.getLon());
        double latDist = (a.getLat() - b.getLat());
        return Math.sqrt(Math.pow(lonDist, 2) + Math.pow(latDist, 2));
    }

    public Point findNearestPointTo(double lon, double lat) {
        Point targetPoint = new Point(0, lon, lat);
        Point minPt = null;
        double minDist = 99999.9;
        for (Point vertex : this.edges.keySet()) {
            double currDist = this.euclidDist(vertex, targetPoint);
            if (minPt == null || currDist < minDist) {
                minPt = vertex;
                minDist = currDist;
            }
        }
        return minPt;
    }
}
