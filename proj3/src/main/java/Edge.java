/**
 * Created by kevin on 4/18/2017.
 */
public class Edge {
    private String name;
    private Point start;
    private Point end;
    private double length;

    public Edge(Point a, Point b) {
        a.addEdge(b);
        double lonDiff = a.getLon() - b.getLon();
        double latDiff = a.getLat() - b.getLat();
        length = Math.sqrt(lonDiff * lonDiff + latDiff * latDiff);
    }
}
