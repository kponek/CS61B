import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by kevin on 4/13/2017.
 */
public class QuadTree {
    public class QuadNode implements Comparable<QuadNode> {
        public double ullat, ullong, lrlat, lrlong;
        public String filename;

        public QuadNode(double latTop, double longTop, double latBot, double longBot, String name) {
            ullat = latTop;
            ullong = longTop;
            lrlat = latBot;
            lrlong = longBot;
            filename = name;
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
    public QuadTree nw;
    public QuadTree ne;
    public QuadTree sw;
    public QuadTree se;

    public QuadTree(double ullat, double ullong, double lrlat, double lrlong, String filename) {
        /*root = new QuadNode(MapServer.ROOT_ULLAT, MapServer.ROOT_ULLON,
                MapServer.ROOT_LRLAT, MapServer.ROOT_LRLON, null, null);*/
        root = new QuadNode(ullat, ullong, lrlat, lrlong, filename);
        if (root.filename.length() == 7) {
            nw = null;
            ne = null;
            sw = null;
            se = null;
        } else {
            double x = (ullong + lrlong) / 2;
            double y = (ullat + lrlat) / 2;
            nw = new QuadTree(ullong, ullat, x, y, root.filename + "1");
            ne = new QuadTree(x, ullat, lrlong, y, root.filename + "2");
            sw = new QuadTree(ullong, y, x, lrlat, root.filename + "3");
            se = new QuadTree(x, y, lrlong, lrlat, root.filename + "4");
        }
    }

    public boolean intersectsTile(double query_ullat, double query_ullong,
                                  double query_lrlat, double query_lrlong) {
        //checks if bottom right is inside query
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
        return false;
    }

    public boolean lonDPPsmallerThanOrIsLeaf(double queriesLonDPP) {
        //check if it is a leaf
        if (nw == null && ne == null && sw == null && se == null) {
            return true;
        }
        //check if lonDPP is less than or equal to query lonDPP
        return ((root.lrlong - root.ullong) / 256) <= queriesLonDPP;
    }
}