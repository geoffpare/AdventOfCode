package y2022.day14;

import com.google.common.collect.Lists;
import utils.InputReader;
import utils.Point;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * https://adventofcode.com/2022/day/14
 *
 * --- Day 14: Regolith Reservoir ---
 * The distress signal leads you to a giant waterfall! Actually, hang on - the signal seems like it's coming from the waterfall itself, and that doesn't make any sense. However, you do notice a little path that leads behind the waterfall.
 *
 * Correction: the distress signal leads you behind a giant waterfall! There seems to be a large cave system here, and the signal definitely leads further inside.
 *
 * As you begin to make your way deeper underground, you feel the ground rumble for a moment. Sand begins pouring into the cave! If you don't quickly figure out where the sand is going, you could quickly become trapped!
 *
 * Fortunately, your familiarity with analyzing the path of falling material will come in handy here. You scan a two-dimensional vertical slice of the cave above you (your puzzle input) and discover that it is mostly air with structures made of rock.
 *
 * Your scan traces the path of each solid rock structure and reports the x,y coordinates that form the shape of the path, where x represents distance to the right and y represents distance down. Each path appears as a single line of text in your scan. After the first point of each path, each point indicates the end of a straight horizontal or vertical line to be drawn from the previous point. For example:
 *
 * 498,4 -> 498,6 -> 496,6
 * 503,4 -> 502,4 -> 502,9 -> 494,9
 * This scan means that there are two paths of rock; the first path consists of two straight lines, and the second path consists of three straight lines. (Specifically, the first path consists of a line of rock from 498,4 through 498,6 and another line of rock from 498,6 through 496,6.)
 *
 * The sand is pouring into the cave from point 500,0.
 *
 * Drawing rock as #, air as ., and the source of the sand as +, this becomes:
 *
 * ...
 *
 * Using your scan, simulate the falling sand. How many units of sand come to rest before sand starts flowing into the abyss below?
 *
 */
public class SandDropper {
    public static void main(String[] args) throws FileNotFoundException {
        InputReader reader = new InputReader();
        List<String> inputLines = reader.loadStringsFromFile("./src/y2022/day14/RockInput.txt");

        SandDropper dropper = new SandDropper();
        char[][] part1Grid = dropper.buildGrid(inputLines);

        int grainsDropped = dropper.dropSandUntilSpills(part1Grid);
        System.out.println("Part 1 Total grains dropped " + grainsDropped);

        char[][] part2Grid = dropper.buildGridPart2(inputLines);
        int grainsDroppedBeforeFull = dropper.dropSandUntilFull(part2Grid);
        System.out.println("Part 2 Grains dropped before full: " + grainsDroppedBeforeFull);
    }

    // Returns the number of sand grains dropped before hitting the abyss
    public int dropSandUntilSpills(char[][] sandGrid) {
        int sandDropped = 0;
        boolean peeredIntoTheAbyss = false;

        while (!peeredIntoTheAbyss) {
            sandDropped++;
            peeredIntoTheAbyss = dropSingleGrainPart1(sandGrid);
        }

        return sandDropped-1; // Minus one as the answer doesn't want the grain dropped into the abyss
    }

    // Returns the number of sand grains dropped before becoming full and stopping (Part 2)
    public int dropSandUntilFull(char[][] sandGrid) {
        int sandDropped = 0;
        boolean fullUp = false;

        while (!fullUp) {
            sandDropped++;
            fullUp = dropSingleGrainPart2(sandGrid);
        }

        return sandDropped;
    }


    // Returns true if the sand reached the abyss, false if it stopped
    private boolean dropSingleGrainPart1(char[][] grid) {
        // Sand pours from (500,0)
        Point grain = new Point(500,0);
        boolean stopped = false;

        while (!stopped) {
            System.out.println("Grain at " + grain);

            // Grain has fallen into the abyss, stop moving it
            // Let's define the abyss as Y>=1000
            if (grain.y+1 >= grid[0].length) {
                return true;
            }

            // Can it move down?
            if (grid[grain.x][grain.y+1] == '.') {
                grain.move(0,1); // Move it down 1
                continue;
            }

            // Can't move down, can it move down-left?
            if (grid[grain.x-1][grain.y+1] == '.') {
                grain.move(-1,1);
                continue;
            }

            // Can't move down, or down-left, can it move down-right?
            if (grid[grain.x+1][grain.y+1] == '.') {
                grain.move(1,1);
                continue;
            }

            // Can't move, so update the grid with this grains resting place
            grid[grain.x][grain.y] = 'o';
            System.out.println("Grain came to rest at " + grain);
            stopped = true;
            return false;

        }

        return false;

    }

    // Returns true if the sand stopped at (500,0), false otherwise
    // Copy/paste from above, in real life would rework for the new requirements, but meh
    private boolean dropSingleGrainPart2(char[][] grid) {
        // Sand pours from (500,0)
        Point grain = new Point(500,0);
        boolean stopped = false;

        while (!stopped) {
            System.out.println("Grain at " + grain);

            // Can it move down?
            if (grid[grain.x][grain.y+1] == '.') {
                grain.move(0,1); // Move it down 1
                continue;
            }

            // Can't move down, can it move down-left?
            if (grid[grain.x-1][grain.y+1] == '.') {
                grain.move(-1,1);
                continue;
            }

            // Can't move down, or down-left, can it move down-right?
            if (grid[grain.x+1][grain.y+1] == '.') {
                grain.move(1,1);
                continue;
            }

            // Part 2 special case, we can't move, check if we're at 500,0
            if (grain.x == 500 && grain.y == 0) {
                System.out.println("Totally full up");
                return true;
            }

            // Can't move, so update the grid with this grains resting place
            grid[grain.x][grain.y] = 'o';
            System.out.println("Grain came to rest at " + grain);
            stopped = true;
            return false;

        }

        return false;

    }

    private char[][] buildGridPart2(List<String> inputLines) {
        char[][] grid = buildGrid(inputLines);

        // Find the bottom Y, then fill out the grid past that.
        int maxY = 0;
        for (int x=0; x<grid.length; x++) {
            for (int y=0; y<grid[0].length; y++) {
                if (grid[x][y] == '#' && y > maxY) {
                    maxY = y;
                }
            }
        }

        // New floor
        for (int x=0; x<grid.length; x++) {
            grid[x][maxY+2] = '#';
        }

        return grid;
    }

    private char[][] buildGrid(List<String> inputLines) {
        char[][] rockGrid = new char[1000][1000];  // Eyeballing the max grid size from the input

        // Initialize with dots as air
        for (int x=0; x<rockGrid.length; x++) {
            for (int y=0; y<rockGrid[0].length; y++) {
                rockGrid[x][y] = '.';
            }
        }

        for (String line : inputLines) {
            List<Point> pointsInLine = Lists.newArrayList();

            for (String point : line.split(" -> ")) {
                int x = Integer.parseInt(point.split(",")[0]);
                int y = Integer.parseInt(point.split(",")[1]);
                pointsInLine.add(new Point(x,y));
            }

            // Fill in grid with rocks from previous point to current point, starting a 1 so there is a previous
            for (int i=1; i<pointsInLine.size(); i++) {
                Point pStart = pointsInLine.get(i-1);
                Point pEnd = pointsInLine.get(i);

                // Vertical
                if (pStart.x == pEnd.x) {
                    // Iterate inclusive of the endpoint itself
                    for (int y=0; y<=Math.abs(pStart.y - pEnd.y); y++) {
                        int yFrom = Math.min(pStart.y, pEnd.y);
                        System.out.println("Inserting Rock: (" + pStart.x + "," + (yFrom+y) + ")");
                        rockGrid[pStart.x][yFrom+y] = '#';
                    }
                }

                // Horizontal
                else if (pStart.y == pEnd.y) {
                    // Iterate inclusive of the endpoint itself
                    for (int x=0; x<=Math.abs(pStart.x - pEnd.x); x++) {
                        int xFrom = Math.min(pStart.x, pEnd.x);
                        System.out.println("Inserting Rock: (" + (xFrom+x) + "," + pStart.y + ")");
                        rockGrid[xFrom+x][pStart.y] = '#';
                    }
                }
            }

        }

        return rockGrid;
    }
}
