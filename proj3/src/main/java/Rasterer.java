import java.util.HashMap;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    // Recommended: QuadTree instance variable. You'll need to make
    //              your own QuadTree since there is no built-in quadtree in Java.
    QuadTree queryTree;

    /** imgRoot is the name of the directory containing the images.
     *  You may not actually need this for your class. */
    public Rasterer(String imgRoot) {
        // YOUR CODE HERE
        Tile rootTile = new Tile(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT,
                MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT, "root.png", 0);
        queryTree = buildQuadTree(new QuadTree(rootTile));
    }

    public class QuadTree {
        private Tile root;

        // X longitude left -> right: bigger
        // Y latitude up -> down: smaller
        private QuadTree NE;
        private QuadTree NW;
        private QuadTree SW;
        private QuadTree SE;

        public QuadTree(Tile rt) {
            this(null, null, null, null, rt);
        }

        public QuadTree(QuadTree nw, QuadTree ne, QuadTree sw, QuadTree se, Tile rt) {
            NW = nw;
            NE = ne;
            SW = sw;
            SE = se;
            root = rt;
        }

        public boolean intersectsTile(double query_ullon, double query_ullat, double query_lrlon, double query_lrlat) {
            return !((root.ullon > query_lrlon) || (query_lrlat > root.ullat)
                    || (query_ullon > root.lrlon) || (root.lrlat > query_ullat));
        }

        public boolean lonDPPsmallerThanOrIsLeaf(double queriesLonDPP) {
            return root.depth == 7 || queriesLonDPP >= (root.lrlon - root.ullon) / MapServer.TILE_SIZE;
        }
    }

    public class Tile {
        private double ullon;
        private double ullat;
        private double lrlon;
        private double lrlat;
        private String img;
        private int depth;

        public Tile(double ullon, double ullat, double lrlon, double lrlat, String img, int depth) {
            this.ullon = ullon;
            this.ullat = ullat;
            this.lrlon = lrlon;
            this.lrlat = lrlat;
            this.img = img;
            this.depth = depth;
        }
    }

    private QuadTree buildQuadTree(QuadTree qt) {
        Tile rootTile = qt.root;

        if (rootTile.depth == 7) {
            return qt;
        }
        /** Build QuadTree from root image */
        else if (rootTile.depth == 0) {
            Tile NW = new Tile(rootTile.ullon, rootTile.ullat, (rootTile.ullon + rootTile.lrlon) / 2, (rootTile.ullat + rootTile.lrlat) / 2, "img/1.png", 1);
            Tile NE = new Tile((rootTile.ullon + rootTile.lrlon) / 2, rootTile.ullat, rootTile.lrlon, (rootTile.ullat + rootTile.lrlat) / 2, "img/2.png", 1);
            Tile SW = new Tile(rootTile.ullon, (rootTile.ullat + rootTile.lrlat) / 2, (rootTile.ullon + rootTile.lrlon) / 2, rootTile.lrlat, "img/3.png", 1);
            Tile SE = new Tile((rootTile.ullon + rootTile.lrlon) / 2, (rootTile.ullat + rootTile.lrlat) / 2, rootTile.lrlon, rootTile.lrlat, "img/4.png", 1);

            QuadTree nwQT = new QuadTree(NW);
            QuadTree neQT = new QuadTree(NE);
            QuadTree swQT = new QuadTree(SW);
            QuadTree seQT = new QuadTree(SE);

            qt.NW = buildQuadTree(nwQT);
            qt.NE = buildQuadTree(neQT);
            qt.SW = buildQuadTree(swQT);
            qt.SE = buildQuadTree(seQT);
        } else {
            /** Build QuadTree from subtrees image */
            String parentImg = rootTile.img.split("\\.")[0];
            int prevDepth = rootTile.depth;

            Tile NW = new Tile(rootTile.ullon, rootTile.ullat, (rootTile.ullon + rootTile.lrlon) / 2, (rootTile.ullat + rootTile.lrlat) / 2, parentImg + "1.png", prevDepth + 1);
            Tile NE = new Tile((rootTile.ullon + rootTile.lrlon) / 2, rootTile.ullat, rootTile.lrlon, (rootTile.ullat + rootTile.lrlat) / 2, parentImg + "2.png", prevDepth + 1);
            Tile SW = new Tile(rootTile.ullon, (rootTile.ullat + rootTile.lrlat) / 2, (rootTile.ullon + rootTile.lrlon) / 2, rootTile.lrlat, parentImg + "3.png", prevDepth + 1);
            Tile SE = new Tile((rootTile.ullon + rootTile.lrlon) / 2, (rootTile.ullat + rootTile.lrlat) / 2, rootTile.lrlon, rootTile.lrlat, parentImg + "4.png", prevDepth + 1);

            QuadTree nwQT = new QuadTree(NW);
            QuadTree neQT = new QuadTree(NE);
            QuadTree swQT = new QuadTree(SW);
            QuadTree seQT = new QuadTree(SE);

            qt.NW = buildQuadTree(nwQT);
            qt.NE = buildQuadTree(neQT);
            qt.SW = buildQuadTree(swQT);
            qt.SE = buildQuadTree(seQT);
        }
        return qt;
    }

    public void query(QuadTree qt, double ullon, double ullat, double lrlon, double lrlat, double LonDPP, Map<String, Object> result, Map<Double, LinkedList<String>> arrange) {
        if (qt == null) {
            return;
        } else {
            if (!qt.intersectsTile(ullon, ullat, lrlon, lrlat)) {
                return;
            } else {
                if (!qt.lonDPPsmallerThanOrIsLeaf(LonDPP)) {
                    query(qt.NW, ullon, ullat, lrlon, lrlat, LonDPP, result, arrange);
                    query(qt.NE, ullon, ullat, lrlon, lrlat, LonDPP, result, arrange);
                    query(qt.SW, ullon, ullat, lrlon, lrlat, LonDPP, result, arrange);
                    query(qt.SE, ullon, ullat, lrlon, lrlat, LonDPP, result, arrange);
                } else {
                    if (qt.root.ullon < (double) result.get("raster_ul_lon")) {
                        result.replace("raster_ul_lon", qt.root.ullon);
                    }
                    if (qt.root.ullat > (double) result.get("raster_ul_lat")) {
                        result.replace("raster_ul_lat", qt.root.ullat);
                    }
                    if (qt.root.lrlon > (double) result.get("raster_lr_lon")) {
                        result.replace("raster_lr_lon", qt.root.lrlon);
                    }
                    if (qt.root.lrlat < (double) result.get("raster_lr_lat")) {
                        result.replace("raster_lr_lat", qt.root.lrlat);
                    }
                    if (qt.root.depth > (int) result.get("depth")) {
                        result.replace("depth", qt.root.depth);
                    }
                    arrangeImg(qt.root.ullat, qt.root.img, arrange);
                }
            }
        }
    }

    /**
     * Arrange the image file name when doing query.
     * @param ullat Upper left latitude of the tile.
     * @param img File name of the tile image.
     * @param arranged A map to keep tiles of same ullat in a linked list.
     */
    private void arrangeImg(double ullat, String img, Map<Double, LinkedList<String>> arranged) {
        if (arranged.containsKey(ullat)) {
            arranged.get(ullat).add(img);
        } else {
            LinkedList<String> ll = new LinkedList<>();
            ll.add(img);
            arranged.put(ullat, ll);
        }
    }

    /**
     * Construct a 2D String array containing the files to display.
     * @param arranged Map of the filename of the corresponding tile image, Key is their
     *                 upper left latitude, Value is a linked list of filenames.
     * @return A 2D String array which represents the tiles that satisfy the query box.
     */
    private String[][] render_grid(Map<Double, LinkedList<String>> arranged) {
        int rowNum = arranged.size();
        String[][] grid = new String[rowNum][];
        int count = 0;
        for (Map.Entry<Double, LinkedList<String>> entry : arranged.entrySet()) {
            int colNum = entry.getValue().size();
            String[] row = new String[colNum];
            for (int i = 0; i < colNum; i++) {
                row[i] = entry.getValue().get(i);
            }
            grid[rowNum - 1 - count] = row;
            count++;
        }
        return grid;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     * </p>
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified:
     * "render_grid"   -> String[][], the files to display
     * "raster_ul_lon" -> Number, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Number, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Number, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Number, the bounding lower right latitude of the rastered image <br>
     * "depth"         -> Number, the 1-indexed quadtree depth of the nodes of the rastered image.
     *                    Can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" -> Boolean, whether the query was able to successfully complete. Don't
     *                    forget to set this to true! <br>
     * @see # REQUIRED_RASTER_REQUEST_PARAMS
     */

    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        results.put("raster_ul_lon", Double.MAX_VALUE);
        results.put("raster_ul_lat", Double.MIN_VALUE);
        results.put("raster_lr_lon", Double.NEGATIVE_INFINITY);
        results.put("raster_lr_lat", Double.MAX_VALUE);
        results.put("depth", 0);

        Map<Double, LinkedList<String>> arrangedImgs = new TreeMap<>();

        Rasterer rstr = new Rasterer("img/");
        rstr.query(rstr.queryTree, params.get("ullon"), params.get("ullat"), params.get("lrlon"),
                params.get("lrlat"), (params.get("lrlon") - params.get("ullon")) / params.get("w"), results, arrangedImgs);

        String[][] rendered_grid = render_grid(arrangedImgs);
        results.put("render_grid", rendered_grid);
        results.put("query_success", true);

        return results;
    }
}
