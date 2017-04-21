import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Finder {
    HashMap<Long, Point> nodes;
    HashMap<Point, HashSet<Edge>> edges;
    HashMap<Long, Double> distTo;
    HashMap<Long, Long> prevPoint;
    Point start;
    Point target;


    public Finder(HashMap<Long, Point> p) {
        this.nodes = p;
        distTo = new HashMap<>();
        prevPoint = new HashMap<>();
        target = null;
    }

    public LinkedList<Long> stPath(Point startt, Point targett) {
        start = startt;
        target = targett;
        HashSet<Long> visited = new HashSet<>();
        HashMap<Long, Double> dist = new HashMap<>();
        HashMap<Point, Point> prev = new HashMap<>();
        PriorityQueue<PQPoint> fringe = new PriorityQueue<>();
        PQPoint startingNode = new PQPoint(startt, targett, 0.0);
        dist.put(startingNode.pt.getId(), 0.0);
        fringe.add(startingNode);
        PQPoint node;
        while (!fringe.isEmpty()) {
            node = fringe.remove();
            if (visited.contains(node.pt.getId())) {
                continue;
            }
            visited.add(node.pt.getId());
            if (node.pt.equals(targett)) {
                break;
            }
            for (Point p : node.pt.getAdjacentPoints()) {
                PQPoint child = new PQPoint(p, targett, 0.0);
                if (!dist.containsKey(child.pt.getId()) || dist.get(child.pt.getId()) > dist.get(node.pt.getId()) + euclidDist(p, node.pt)) {
                    double childTrueDistance = dist.get(node.pt.getId()) + euclidDist(p, node.pt);
                    PQPoint trueChild = new PQPoint(p, targett, childTrueDistance);
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

    public static double euclidDist(Point a, Point b) {
        double lonDist = (a.getLon() - b.getLon());
        double latDist = (a.getLat() - b.getLat());
        return Math.sqrt(Math.pow(lonDist, 2) + Math.pow(latDist, 2));
    }
}
