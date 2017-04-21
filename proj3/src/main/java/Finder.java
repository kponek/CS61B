import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Finder {
    private HashMap<Long, Long> prevPoint;
    private Point start;
    private Point target;

    public Finder(HashMap<Long, Point> p) {
        prevPoint = new HashMap<>();
    }

    public LinkedList<Long> stPath(Point s, Point t) {
        start = s;
        target = t;
        PriorityQueue<PQPoint> queue = new PriorityQueue<>();
        PQPoint nextVisit;
        HashMap<Long, Double> dist = new HashMap<>();
        HashSet<Long> visited = new HashSet<>();
        HashMap<Point, Point> prev = new HashMap<>();
        PQPoint startingNode = new PQPoint(start, target, 0);
        queue.add(startingNode);
        dist.put(startingNode.getPoint().getId(), 0.0);
        while (!queue.isEmpty()) {
            nextVisit = queue.remove();
            if (visited.contains(nextVisit.getPoint().getId())) {
                continue;
            }
            visited.add(nextVisit.getPoint().getId());
            if (nextVisit.getPoint().equals(target)) {
                break;
            }
            for (Point p : nextVisit.getPoint().getAdjacentPoints()) {
                PQPoint child = new PQPoint(p, target, 0.0);
                double childDist = dist.get(nextVisit.getPoint().getId())
                        + euclidDist(p, nextVisit.getPoint());
                if (!dist.containsKey(child.getPoint().getId())
                        || dist.get(child.getPoint().getId()) > childDist) {
                    PQPoint trueChild = new PQPoint(p, target, childDist);
                    dist.put(trueChild.getPoint().getId(), childDist);
                    prev.put(trueChild.getPoint(), nextVisit.getPoint());
                    queue.add(trueChild);
                }
            }
        }
        Point currPoint = target;
        Point prevPoint = prev.get(target);
        LinkedList<Long> shortest = new LinkedList<>();
        while (!currPoint.equals(start)) {
            shortest.addFirst(currPoint.getId());
            currPoint = prevPoint;
            prevPoint = prev.get(prevPoint);
        }
        shortest.addFirst(currPoint.getId());
        return shortest;
    }

    public static double euclidDist(Point a, Point b) {
        double lonDiff = (a.getLon() - b.getLon());
        double latDiff = (a.getLat() - b.getLat());
        return Math.sqrt(lonDiff * lonDiff + latDiff * latDiff);
    }

    private HashMap<Long, Long> getPrevPoint() {
        return prevPoint;
    }

    private Point getStart() {
        return start;
    }

    private Point getTarget() {
        return target;
    }
}
