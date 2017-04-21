
/**
 * Created by kevin on 4/13/2017.
 */
public class QuadTree {
    public class QuadNode implements Comparable<QuadNode> {
        private double ullat, ullong, lrlat, lrlong;
        private String filename;

        public QuadNode(double latTop, double longTop,
                        double latBot, double longBot, String name) {
            ullat = latTop;
            ullong = longTop;
            lrlat = latBot;
            lrlong = longBot;
            filename = name;
        }

        public double getUllat() {
            return ullat;
        }

        public double getUllong() {
            return ullong;
        }

        public double getLrlat() {
            return lrlat;
        }

        public double getLrlong() {
            return lrlong;
        }

        public String getFilename() {
            return filename;
        }

        @Override
        public int compareTo(QuadNode n) {
            if (Double.compare(ullat, n.ullat) != 0) {
                return Double.compare(ullat, n.ullat);
            }
            return Double.compare(ullong, n.ullong);
        }
    }

    public QuadNode root;
    public QuadTree[] children;

    public QuadTree(double ullat, double ullong,
                    double lrlat, double lrlong, String filename) {
        root = new QuadNode(ullat, ullong, lrlat, lrlong, filename);
        children = new QuadTree[4];
        if (root.filename.length() == 11) {//img/ + 7 levels
            children[0] = null;
            children[1] = null;
            children[2] = null;
            children[3] = null;
        } else {
            double x = (ullong + lrlong) / 2;
            double y = (ullat + lrlat) / 2;
            children[0] = new QuadTree(ullat, ullong, y, x, root.filename + "1");
            children[1] = new QuadTree(ullat, x, y, lrlong, root.filename + "2");
            children[2] = new QuadTree(y, ullong, lrlat, x, root.filename + "3");
            children[3] = new QuadTree(y, x, lrlat, lrlong, root.filename + "4");
        }
    }

    public boolean intersectsTile(double query_ullat, double query_ullong,
                                  double query_lrlat, double query_lrlong) {
        /*//checks if bottom right is inside query
        if (root.lrlat <= query_ullat && root.lrlong >= query_ullong
                && root.lrlat >= query_lrlat && root.lrlong <= query_lrlong) {
            return true;
        }
        //checks if upper left is inside query
        else if (root.ullat <= query_ullat && root.ullong >= query_ullong
                && root.ullat >= query_lrlat && root.ullong <= query_lrlong) {
            return true;
        }
        //checks if upper right is inside query
        else if (root.ullong <= query_lrlong && root.lrlat >= query_lrlat
                && root.ullong >= query_ullong && root.lrlat <= query_ullat) {
            return true;
        }
        //checks if lower left is inside query
        else if (root.lrlong <= query_lrlong && root.ullat >= query_lrlat
                && root.lrlong >= query_ullong && root.ullat <= query_ullat) {
            return true;
        }
        //checks if image overlaps query
        else if (root.ullong <= query_ullong && root.ullat >= query_ullat
                && root.lrlong >= query_lrlong && root.lrlat <= query_lrlat) {
            return true;
        }
        return false;*/
        return root.ullong <= query_lrlong && root.lrlong >= query_ullong
                && root.ullat >= query_lrlat && root.lrlat <= query_ullat;
    }

    public boolean lonDPPsmallerThanOrIsLeaf(double queriesLonDPP) {
        //check if it is a leaf
        if (root.getFilename().length() == 11) {
            return true;
        }
        //check if lonDPP is less than or equal to query lonDPP
        return getLonDPP() <= queriesLonDPP;
    }

    public double getLonDPP() {
        return (root.lrlong - root.ullong) / 256;
    }

}