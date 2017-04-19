/**
 * Created by kevin on 4/18/2017.
 */
public class Edge {
    private String name;

    public Edge(Point a, Point b) {
        a.addEdge(b);
    }
}
