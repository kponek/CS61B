public class PQPoint implements Comparable {
    private Point point;
    private double priority;
    private double distance;
    private Point end;

    public PQPoint(Point p, Point e, double d) {
        point = p;
        distance = d;
        end = e;
        this.priority = Finder.euclidDist(p, end) + distance;
    }

    @Override
    public int compareTo(Object o) {
        double difference = priority - ((PQPoint) o).getPriority();
        if (difference == 0) {
            return 0;
        } else if (difference > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(this.getClass())) {
            return false;
        } else {
            return (point.getId() == ((PQPoint) o).point.getId());
        }
    }


    public Point getPoint() {
        return point;
    }

    public double getPriority() {
        return priority;
    }

    public double getDistance() {
        return distance;
    }

    public Point getEnd() {
        return end;
    }
}