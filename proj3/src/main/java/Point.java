import java.util.HashSet;
import java.util.Set;

/**
 * Created by kevin on 4/18/2017.
 */
public class Point {
    private long id;
    private double lat;
    private double lon;
    private Set<Point> adjacentPoints;
    private Set<Long> adjacentIds;
    private String name;

    public Point(long ident, double latitude, double longitude) {
        id = ident;
        lat = latitude;
        lon = longitude;
        adjacentPoints = new HashSet<>();
        adjacentIds = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public Set<Point> getAdjacentPoints() {
        return adjacentPoints;
    }

    public Set<Long> getAdjacentIds() {
        return adjacentIds;
    }

    public String getName() {
        return name;
    }

    public void addEdge(Point a) {
        Edge connection = new Edge(a, this);
        adjacentPoints.add(a);
        a.adjacentPoints.add(this);
        adjacentIds.add(a.getId());
        a.adjacentIds.add(this.id);
    }

    public void setName(String n) {
        name = n;
    }
}
