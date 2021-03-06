import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

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
        tree = new QuadTree(MapServer.ROOT_ULLAT, MapServer.ROOT_ULLON,
                MapServer.ROOT_LRLAT, MapServer.ROOT_LRLON, imgRoot);
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
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
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
        //System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        matches(tree, params);
        /*System.out.println(grid.size());
        for (QuadTree x : grid) {
            System.out.print(x.root.getFilename() + " ");
        }*/
        int rows = numRows().size();
        //to protect against Arithmetic Exception: divide by zero
        if (rows == 0) {
            results.put("render_grid", this.img + ".png");
            results.put("raster_ul_lon", MapServer.ROOT_ULLON);
            results.put("raster_ul_lat", MapServer.ROOT_ULLAT);
            results.put("raster_lr_lon", MapServer.ROOT_LRLON);
            results.put("raster_lr_lat", MapServer.ROOT_LRLAT);
            results.put("depth", 0);
            results.put("query_success", true);
            return results;
        }
        int cols = grid.size() / rows;
        QuadTree[][] map = new QuadTree[rows][cols];
        String[][] images = new String[rows][cols];
        //add items to map, where each item is on the right
        // row but needs to be in the right column still
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                map[r][c] = grid.remove(grid.size() - 1);
                //System.out.println("(" + r + "," + c + "): " + map[r][c].root.getFilename());
            }
        }
        for (int i = 0; i < rows; i++) {
            Map<String, Object> arrs = sortRow(map[i]);
            String[] imgs = (String[]) arrs.get("images");
            QuadTree[] query = (QuadTree[]) arrs.get("query");
            /*for (int j = 0; j < imgs.length; j++) {
                images[i][j] = imgs[j];
            }*/
            //System.out.println("sort row " + i + ": " + Arrays.toString(imgs));
            System.arraycopy(imgs, 0, images[i], 0, imgs.length);
            System.arraycopy(query, 0, map[i], 0, query.length);
        }
        //testing and seeing all files
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(images[i][j] + " ");
            }
            System.out.println();
        }
        results.put("render_grid", images);
        results.put("raster_ul_lon", map[0][0].root.getUllong());
        results.put("raster_ul_lat", map[0][0].root.getUllat());
        results.put("raster_lr_lon", map[rows - 1][cols - 1].root.getLrlong());
        results.put("raster_lr_lat", map[rows - 1][cols - 1].root.getLrlat());
        results.put("depth", (map[0][0].root.getFilename() + "").length() - 4);
        results.put("query_success", true);
        return results;
    }

    public void testQuadTree(QuadTree t) {
        //if (t.root.getUllong() == -122.2998046875) {
        System.out.println(t.root.getFilename());
        //}
        for (QuadTree c : t.children) {
            if (c != null) {
                testQuadTree(c);
            }
        }
    }

    public void matches(QuadTree qt, Map<String, Double> params) {
        //System.out.println(qt.root.getFilename());
        //System.out.println("ullat: " + qt.root.getUllat() + " ullon: " + qt.root.getUllong()
        // + " lrlat: " + qt.root.getLrlat() + " lrlon: " + qt.root.getLrlong());
        if (qt.root.getFilename().equals("img/")) {
            for (QuadTree c : qt.children) {
                matches(c, params);
            }
        } else if (!qt.intersectsTile(params.get("ullat"), params.get("ullon"),
                params.get("lrlat"), params.get("lrlon"))) {
            return;
        } else if (qt.lonDPPsmallerThanOrIsLeaf((params.get("lrlon")
                - params.get("ullon"))
                / params.get("w")) || qt.root.getFilename().length() >= 11) {
            //System.out.println("query londpp: "
            // + (params.get("lrlon")
            // - params.get("ullon")) / params.get("w"));
            //System.out.println("tile londpp: " + qt.getLonDPP());
            //if (qt.intersectsTile(params.get("ullat"), params.get("ullon"),
            // params.get("lrlat"), params.get("lrlon"))) {
            //System.out.println("londpp smaller and intersect");
            grid.add(qt);
        } else {
            //System.out.println("uh");
            for (QuadTree c : qt.children) {
                //if (c.intersectsTile(params.get("ullat"), params.get("ullon"),
                // params.get("lrlat"), params.get("lrlon"))) {
                matches(c, params);
                //}
            }
        }
    }

    private ArrayList<Double> numRows() {
        ArrayList<Double> latRows = new ArrayList<>();
        ArrayList<QuadTree> newGrid = new ArrayList<>();
        for (QuadTree q : grid) {
            if (!latRows.contains(q.root.getUllat())) {
                latRows.add(q.root.getUllat());
            }
        }
        Collections.sort(latRows);
        for (double d : latRows) {
            for (QuadTree q : grid) {
                if (q.root.getUllat() == d) {
                    newGrid.add(q);
                }
            }
        }
        grid = newGrid;
        return latRows;
    }

    private Map<String, Object> sortRow(QuadTree[] quadTrees) {
        ArrayList<String> images = new ArrayList<>();
        ArrayList<Double> lonRows = new ArrayList<>();
        ArrayList<QuadTree> newTree = new ArrayList<>();
        Map<String, Object> ret = new HashMap<>();
        for (QuadTree child : quadTrees) {
            lonRows.add(child.root.getUllong());
        }
        Collections.sort(lonRows);
        for (double d : lonRows) {
            for (int i = 0; i < quadTrees.length; i++) {
                if (quadTrees[i].root.getUllong() == d) {
                    images.add(quadTrees[i].root.getFilename() + ".png");
                    newTree.add(quadTrees[i]);
                }
            }
        }
        String[] imageArr = new String[images.size()];
        QuadTree[] treeArr = new QuadTree[images.size()];
        for (int i = 0; i < images.size(); i++) {
            imageArr[i] = images.get(i);
            treeArr[i] = newTree.get(i);
        }
        ret.put("images", imageArr);
        ret.put("query", treeArr);
        return ret;
    }
}
