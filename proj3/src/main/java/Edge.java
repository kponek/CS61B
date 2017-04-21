/**
 * Created by kevin on 4/18/2017.
 */
public class Edge {
    private String name;
    private Point start;
    private Point end;
    private double length;
    GraphDB graph;

    public Edge(Point a, Point b) {
        /*if (a.getId() == 3347105714L || b.getId() == 3347105714L) {
            System.out.println("poop");
        }*/
        start = a;
        end = b;
        a.addEdge(b);
        double lonDiff = a.getLon() - b.getLon();
        double latDiff = a.getLat() - b.getLat();
        length = Math.sqrt(lonDiff * lonDiff + latDiff * latDiff);
    }

    public double getLength() {
        return length;
    }

    public Point getEnd() {
        return end;
    }

    public Point getStart() {
        return start;
    }
}
