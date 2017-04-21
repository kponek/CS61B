public class PQPoint implements Comparable {
    Point pt;
    double priority;
    PQPoint prevNode;
    double dist;
    private Point targett;

    public PQPoint(Point ptt, Point end, double distt) {
        this.pt = ptt;
        this.dist = distt;
        this.targett = end;
        this.priority = Finder.euclidDist(ptt, end) + distt;
    }

    @Override
    public int compareTo(Object o) {
        double difference = this.getPriority() - ((PQPoint) o).getPriority();
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
            return (this.pt.getId() == ((PQPoint) o).pt.getId());
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void setPriority(double distance) {
        this.priority = Finder.euclidDist(pt, this.targett) + distance;
    }
}