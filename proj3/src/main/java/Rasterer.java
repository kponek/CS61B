import java.util.*;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    // Recommended: QuadTree instance variable. You'll need to make
    //              your own QuadTree since there is no built-in quadtree in Java.
    private QuadTree tree;
    private ArrayList<QuadTree> grid;
    private String img;

    /**
     * imgRoot is the name of the directory containing the images.
     * You may not actually need this for your class.
     */
    public Rasterer(String imgRoot) {
        tree = new QuadTree(MapServer.ROOT_ULLAT, MapServer.ROOT_ULLON, MapServer.ROOT_LRLAT, MapServer.ROOT_LRLON, imgRoot);
        grid = new ArrayList<>();
        img = imgRoot;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
     * <li>Has dimensions of at least w by h, where w and h are the user viewport width
     * and height.</li>
     * <li>The tiles collected must cover the most longitudinal distance per pixel
     * (LonDPP) possible, while still covering less than or equal to the amount of
     * longitudinal distance per pixel in the query box for the user viewport size. </li>
     * <li>Contains all tiles that intersect the query bounding box that fulfill the
     * above condition.</li>
     * <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     * </p>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified:
     * "render_grid"   -> String[][], the files to display
     * "raster_ul_lon" -> Number, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Number, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Number, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Number, the bounding lower right latitude of the rastered image <br>
     * "depth"         -> Number, the 1-indexed quadtree depth of the nodes of the rastered image.
     * Can also be interpreted as the length of the numbers in the image
     * string. <br>
     * "query_success" -> Boolean, whether the query was able to successfully complete. Don't
     * forget to set this to true! <br>
     * @see #REQUIRED_RASTER_REQUEST_PARAMS
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //testQuadTree(tree);
        System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        /*double ullat = params.get("ullat");
        double ullon = params.get("ullon");
        double lrlat = params.get("lrlat");
        double lrlon = params.get("lrlon");
        double width = params.get("w");
        double queryLonDPP = (lrlon - ullon) / width;*/
        matches(tree, params);
        /*System.out.println(grid.size());
        for (QuadTree x : grid) {
            System.out.print(x.root.getFilename() + " ");
        }*/
        int rows = numRows();
        int cols = grid.size() / rows;
        QuadTree[][] map = new QuadTree[rows][cols];
        String[][] images = new String[rows][cols];
        //add items to map, where each item is on the right row but needs to be in the right column still
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                map[r][c] = grid.remove(grid.size() - 1);
                System.out.println("(" + r + "," + c + "): " + map[r][c].root.getFilename());
            }
        }
        for (int i = 0; i < rows; i++) {
            String[] imgs = sortRow(map[i]);
            /*for (int j = 0; j < imgs.length; j++) {
                images[i][j] = imgs[j];
            }*/
            System.out.println("sort row " + i + ": " + Arrays.toString(imgs));
            System.arraycopy(imgs, 0, images[i], 0, imgs.length);
        }
        results.put("render_grid", images);
        results.put("raster_ul_lon", map[0][0].root.getUllong());
        results.put("raster_ul_lat", map[0][0].root.getUllat());
        results.put("raster_lr_lon", map[rows - 1][cols - 1].root.getLrlong());
        results.put("raster_lr_lat", map[rows - 1][cols - 1].root.getLrlat());
        results.put("depth", (map[0][0].root.getFilename() + "").length());
        results.put("query_success", true);
        return results;
    }

    public void testQuadTree(QuadTree t) {
        System.out.println(t.root.getFilename());
        for (QuadTree c : t.children) {
            if (c != null)
                testQuadTree(c);
        }
    }

    public void matches(QuadTree qt, Map<String, Double> params) {
        //System.out.println(qt.root.getFilename());
        //System.out.println("ullat: " + qt.root.getUllat() + " ullon: " + qt.root.getUllong() + " lrlat: " + qt.root.getLrlat() + " lrlon: " + qt.root.getLrlong());
        if (qt.root.getFilename().equals("img/")) {
            for (QuadTree c : qt.children) {
                matches(c, params);
            }
        } else if (!qt.intersectsTile(params.get("ullat"), params.get("ullon"), params.get("lrlat"), params.get("lrlon"))) {
            return;
        } else if (qt.lonDPPsmallerThanOrIsLeaf((params.get("lrlon") - params.get("ullon")) / params.get("w")) || qt.root.getFilename().length() >= 11) {
            //System.out.println("query londpp: " + (params.get("lrlon") - params.get("ullon")) / params.get("w"));
            //System.out.println("tile londpp: " + qt.getLonDPP());
            if (qt.intersectsTile(params.get("ullat"), params.get("ullon"), params.get("lrlat"), params.get("lrlon"))) {
                //System.out.println("londpp smaller and intersect");
                grid.add(qt);
            }
        } else {
            //System.out.println("uh");
            for (QuadTree c : qt.children) {
                if (c.intersectsTile(params.get("ullat"), params.get("ullon"), params.get("lrlat"), params.get("lrlon"))) {
                    matches(c, params);
                }
            }
        }
    }

    private int numRows() {
        /*ArrayList<QuadTree> g = (ArrayList<QuadTree>) grid.clone();
        ArrayList<QuadTree> newGrid = new ArrayList<>();
        ArrayList<Double> latRows = new ArrayList<>();
        while (g.size() > 0) {
            double minLat = g.get(0).root.getUllat();
            if (!latRows.contains(minLat)) {
                latRows.add(minLat);
            }
            int index = 0;
            for (int i = 0; i < g.size(); i++) {
                if (g.get(i).root.getUllat() < minLat) {
                    index = 0;
                    minLat = g.get(i).root.getUllat();
                    if (!latRows.contains(minLat)) {
                        latRows.add(minLat);
                    }
                }
            }
            newGrid.add(g.remove(index));
        }
        grid = newGrid;
        return latRows.size();*/
        ArrayList<QuadTree> newGrid = new ArrayList<>();
        ArrayList<Double> lats = new ArrayList<>();
        for (QuadTree q : grid) {
            if (!lats.contains(q.root.getUllat())) {
                lats.add(q.root.getUllat());
            }
        }
        Collections.sort(lats);
        for (double d : lats) {
            for (QuadTree q : grid) {
                if (q.root.getUllat() == d) {
                    newGrid.add(q);
                }
            }
        }
        grid = newGrid;
        return lats.size();
    }

    private String[] sortRow(QuadTree[] tree) {
        ArrayList<String> images = new ArrayList<>();
        ArrayList<Double> lonRows = new ArrayList<>();
        for (QuadTree child : tree) {
            lonRows.add(child.root.getUllong());
        }
        Collections.sort(lonRows);
        for (double d : lonRows) {
            for (QuadTree q : tree) {
                if (q.root.getUllong() == d) {
                    images.add(q.root.getFilename() + ".png");
                }
            }
        }
        String[] imageArr = new String[images.size()];
        for (int i = 0; i < images.size(); i++) {
            imageArr[i] = images.get(i);
        }
        return imageArr;
    }
}
