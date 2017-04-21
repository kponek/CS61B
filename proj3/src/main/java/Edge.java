/**
 * Created by kevin on 4/18/2017.
 */
public class Edge {
    GraphDB graph;
    private String name;
    private Point start;
    private Point end;
    private double length;


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

    private String getName() {
        return name;
    }

    private Point getStart() {
        return start;
    }

    private Point getEnd() {
        return end;
    }

    private double getLength() {
        return length;
    }
}
