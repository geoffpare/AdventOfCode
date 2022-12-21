package y2022.day12;

import com.google.common.collect.Lists;
import utils.InputReader;
import utils.Point;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * --- Day 12: Hill Climbing Algorithm ---
 * You try contacting the Elves using your handheld device, but the river you're following must be too low to get a decent signal.
 *
 * You ask the device for a heightmap of the surrounding area (your puzzle input). The heightmap shows the local area from above broken into a grid; the elevation of each square of the grid is given by a single lowercase letter, where a is the lowest elevation, b is the next-lowest, and so on up to the highest elevation, z.
 *
 * Also included on the heightmap are marks for your current position (S) and the location that should get the best signal (E). Your current position (S) has elevation a, and the location that should get the best signal (E) has elevation z.
 *
 * You'd like to reach E, but to save energy, you should do it in as few steps as possible. During each step, you can move exactly one square up, down, left, or right. To avoid needing to get out your climbing gear, the elevation of the destination square can be at most one higher than the elevation of your current square; that is, if your current elevation is m, you could step to elevation n, but not to elevation o. (This also means that the elevation of the destination square can be much lower than the elevation of your current square.)
 *
 * For example:
 *
 * Sabqponm
 * abcryxxl
 * accszExk
 * acctuvwj
 * abdefghi
 * Here, you start in the top-left corner; your goal is near the middle. You could start by moving down or right, but eventually you'll need to head toward the e at the bottom. From there, you can spiral around to the goal:
 *
 * v..v<<<<
 * >v.vv<<^
 * .>vv>E^^
 * ..v>>>^^
 * ..>>>>>^
 * In the above diagram, the symbols indicate whether the path exits each square moving up (^), down (v), left (<), or right (>). The location that should get the best signal is still E, and . marks unvisited squares.
 *
 * This path reaches the goal in 31 steps, the fewest possible.
 *
 * What is the fewest steps required to move from your current position to the location that should get the best signal?
 *
 * --- Part Two ---
 * As you walk up the hill, you suspect that the Elves will want to turn this into a hiking trail. The beginning isn't very scenic, though; perhaps you can find a better starting point.
 *
 * To maximize exercise while hiking, the trail should start as low as possible: elevation a. The goal is still the square marked E. However, the trail should still be direct, taking the fewest steps to reach its goal. So, you'll need to find the shortest path from any square at elevation a to the square marked E.
 *
 * Again consider the example from above:
 *
 * Sabqponm
 * abcryxxl
 * accszExk
 * acctuvwj
 * abdefghi
 * Now, there are six choices for starting position (five marked a, plus the square marked S that counts as being at elevation a). If you start at the bottom-left square, you can reach the goal most quickly:
 *
 * ...v<<<<
 * ...vv<<^
 * ...v>E^^
 * .>v>>>^^
 * >^>>>>>^
 * This path reaches the goal in only 29 steps, the fewest possible.
 *
 * What is the fewest steps required to move starting from any square with elevation a to the location that should get the best signal?
 *
 */
public class MapSearch {
    Graph graph = new Graph();
    Point startPoint;
    Point endPoint;

    public static void main(String[] args) throws FileNotFoundException {
        InputReader inputReader = new InputReader();
        //List<String> input = inputReader.loadStringsFromFile("./src/y2022/day12/SampleMap.txt");
        List<String> input = inputReader.loadStringsFromFile("./src/y2022/day12/FullMap.txt");
        MapSearch searcher = new MapSearch();

        char[][] heightMap = searcher.getHeightMap(input);

        // Part 1
        Point startPoint = searcher.buildGraph(heightMap);
        searcher.outputShortestPathForStartingPosition(startPoint);

        // Part 2
        List<Point> potentialStartingPositions = searcher.getPotentialStartingPositions(heightMap);
        System.out.println("Num starting points: " + potentialStartingPositions.size());

        // We need to rebuild the graph before each search cycle to reset the dijkstra distances set on the nodes
        // We're just going to brute force part 2, but in hindsight we could just do the Dijkstra search seeding it with
        // all the starting positions at weight 8 and starting all of those in the unsettled list.  But I'm not bothering
        // to update now this is running.
        Integer shortestPath = Integer.MAX_VALUE;
        int runNumber = 1;
        long startTimeMillis = System.currentTimeMillis();
        for (Point start : potentialStartingPositions) {
            System.out.println("Starting run number: " + runNumber + " at time: " + ((System.currentTimeMillis() - startTimeMillis) / 1000));
            searcher.buildGraph(heightMap);
            Integer pathLength = searcher.outputShortestPathForStartingPosition(start);
            if (pathLength < shortestPath) {
                shortestPath = pathLength;
            }
            runNumber++;
        }

        System.out.println("Part 2: Shortest shortest hiking path: " + shortestPath);
    }

    // Simply return all points which have a height of 'a' as potential starts
    public List<Point> getPotentialStartingPositions(char[][] heightMap) {
        int numRows = heightMap.length;
        int numColumns = heightMap[0].length;
        List<Point> starts = Lists.newLinkedList();

        for (int r=0; r<numRows; r++) {
            for (int c=0; c<numColumns; c++) {
                if (heightMap[r][c] == 'a') {
                    startPoint = new Point(r, c);
                    System.out.println("Potential Start point: (" + r + "," + c + ")");
                    starts.add(startPoint);
                }

            }
        }

        return starts;
    }

    public Integer outputShortestPathForStartingPosition(Point startPoint) {
        Integer shortestPath = graph.calculateShortestPathWeightFromSource(graph.getNode(startPoint), graph.getNode(endPoint));
        System.out.println("Shortest Path: " + shortestPath);
        return shortestPath;
    }

    // Builds the class graph member, and replaces S and E with their correct heights in the height map
    // Returns the starting point that was marked as 'S'
    public Point buildGraph(char[][] heightMap) {
        Graph graph = new Graph();
        Point startPoint = new Point(-1,-1);
        int numRows = heightMap.length;
        int numColumns = heightMap[0].length;

        // Find start and end nodes, remember them, than replace their letters with the correct heights in the height map
        for (int r=0; r<numRows; r++) {
            for (int c=0; c<numColumns; c++) {
                if (heightMap[r][c] == 'S') {
                    startPoint = new Point(r,c);
                    heightMap[r][c] = 'a';
                    //System.out.println("Start point: (" + r + "," + c + ")");
                } else if (heightMap[r][c] == 'E') {
                    endPoint = new Point(r,c);
                    heightMap[r][c] = 'z';
                    //System.out.println("End point: (" + r + "," + c + ")");
                }
            }
        }

        buildAdjacentNodes(heightMap);

        return startPoint;
    }

    // Add adjacent nodes to our graph if they are one height away from our node
    private void buildAdjacentNodes(char[][] heightMap) {
        int numRows = heightMap.length;
        int numColumns = heightMap[0].length;

        // write this
        // For each position in the heightmap
        for (int r=0; r<numRows; r++) {
            for (int c=0; c<numColumns; c++) {
                Point point = new Point(r,c);
                SearchNode nodeUnderConsideration;
                if (graph.nodeExists(point)) {
                    nodeUnderConsideration = graph.getNode(point);
                } else {
                    nodeUnderConsideration = new SearchNode(point);
                }

                //System.out.println(("For Node: " + point));

                // Check each adjacent position for traversability, add the adjacent node if it is
                for (int rt=-1; rt<=1; rt++) {
                    for (int ct=-1; ct<=1; ct++) {

                        // We are saying diagonals aren't traversable for now (only up down left right allowed), or -1,0 1,0 0,-1, 0,1 allowed
                        if (Math.abs(rt) + Math.abs(ct) == 2) {
                            continue;
                        }

                        if (isPointTraversable(r + rt, c + ct, point, heightMap)) {
                            Point toPoint = new Point(r + rt, c + ct);
                            //System.out.println("  Adding adjacency: " + toPoint);
                            if (graph.nodeExists(toPoint)) {
                                // All edges weight of 1 for now
                                nodeUnderConsideration.addAdjacency(graph.getNode(toPoint), 1);
                            } else {
                                SearchNode newNode = new SearchNode(toPoint);
                                graph.addNode(newNode);  // We need to add this to the graph as soon as we encounter it as an adjacency
                                nodeUnderConsideration.addAdjacency(newNode, 1);
                            }
                        }
                    }
                }

                graph.addNode(nodeUnderConsideration);

            }
        }
    }

    private boolean isPointTraversable(int r, int c, Point fromPoint, char[][] heightMap) {
        // Don't traverse off the map
        if (r<0 || c<0) {
            return false;
        }

        // Don't traverse off the map
        if (r>heightMap.length-1 || c>heightMap[0].length-1) {
            return false;
        }

        // Don't traverse to self
        if (r == fromPoint.x && c == fromPoint.y) {
            return false;
        }

        // This point is safely adjacent, is it within 1 height?
        if (Math.abs(heightMap[r][c] - heightMap[fromPoint.x][fromPoint.y]) <= 1 ) {
            return true;
        }

        // Apparently we can traverse down any amount of height
        if (heightMap[fromPoint.x][fromPoint.y] - heightMap[r][c] >= 0) {
            return true;
        }

        return false;
    }

    public char[][] getHeightMap(List<String> mapInput) {
        // [row][column]
        int numRows = mapInput.size();
        int numColumns = mapInput.get(0).length();
        char[][] heights = new char[mapInput.size()][mapInput.get(0).length()];

        for (int r=0; r<numRows; r++) {
            for (int c=0; c<numColumns; c++) {
                heights[r][c] = mapInput.get(r).charAt(c);
            }
        }

        return heights;
    }
}
