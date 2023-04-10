package y2022.day15;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import utils.InputReader;
import utils.Point;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * https://adventofcode.com/2022/day/15
 *
 * --- Day 15: Beacon Exclusion Zone ---
 * You feel the ground rumble again as the distress signal leads you to a large network of subterranean tunnels. You don't have time to search them all, but you don't need to: your pack contains a set of deployable sensors that you imagine were originally built to locate lost Elves.
 *
 * The sensors aren't very powerful, but that's okay; your handheld device indicates that you're close enough to the source of the distress signal to use them. You pull the emergency sensor system out of your pack, hit the big button on top, and the sensors zoom off down the tunnels.
 *
 * Once a sensor finds a spot it thinks will give it a good reading, it attaches itself to a hard surface and begins monitoring for the nearest signal source beacon. Sensors and beacons always exist at integer coordinates. Each sensor knows its own position and can determine the position of a beacon precisely; however, sensors can only lock on to the one beacon closest to the sensor as measured by the Manhattan distance. (There is never a tie where two beacons are the same distance to a sensor.)
 *
 * It doesn't take long for the sensors to report back their positions and closest beacons (your puzzle input). For example:
 *
 * Sensor at x=2, y=18: closest beacon is at x=-2, y=15
 * Sensor at x=9, y=16: closest beacon is at x=10, y=16
 * Sensor at x=13, y=2: closest beacon is at x=15, y=3
 * Sensor at x=12, y=14: closest beacon is at x=10, y=16
 * Sensor at x=10, y=20: closest beacon is at x=10, y=16
 * Sensor at x=14, y=17: closest beacon is at x=10, y=16
 * Sensor at x=8, y=7: closest beacon is at x=2, y=10
 * Sensor at x=2, y=0: closest beacon is at x=2, y=10
 * Sensor at x=0, y=11: closest beacon is at x=2, y=10
 * Sensor at x=20, y=14: closest beacon is at x=25, y=17
 * Sensor at x=17, y=20: closest beacon is at x=21, y=22
 * Sensor at x=16, y=7: closest beacon is at x=15, y=3
 * Sensor at x=14, y=3: closest beacon is at x=15, y=3
 * Sensor at x=20, y=1: closest beacon is at x=15, y=3
 * So, consider the sensor at 2,18; the closest beacon to it is at -2,15. For the sensor at 9,16, the closest beacon to it is at 10,16.
 *
 * Drawing sensors as S and beacons as B, the above arrangement of sensors and beacons looks like this:
 *
 *                1    1    2    2
 *      0    5    0    5    0    5
 *  0 ....S.......................
 *  1 ......................S.....
 *  2 ...............S............
 *  3 ................SB..........
 *  4 ............................
 *  5 ............................
 *  6 ............................
 *  7 ..........S.......S.........
 *  8 ............................
 *  9 ............................
 * 10 ....B.......................
 * 11 ..S.........................
 * 12 ............................
 * 13 ............................
 * 14 ..............S.......S.....
 * 15 B...........................
 * 16 ...........SB...............
 * 17 ................S..........B
 * 18 ....S.......................
 * 19 ............................
 * 20 ............S......S........
 * 21 ............................
 * 22 .......................B....
 * This isn't necessarily a comprehensive map of all beacons in the area, though. Because each sensor only identifies its closest beacon, if a sensor detects a beacon, you know there are no other beacons that close or closer to that sensor. There could still be beacons that just happen to not be the closest beacon to any sensor. Consider the sensor at 8,7:
 *
 *                1    1    2    2
 *      0    5    0    5    0    5
 * -2 ..........#.................
 * -1 .........###................
 *  0 ....S...#####...............
 *  1 .......#######........S.....
 *  2 ......#########S............
 *  3 .....###########SB..........
 *  4 ....#############...........
 *  5 ...###############..........
 *  6 ..#################.........
 *  7 .#########S#######S#........
 *  8 ..#################.........
 *  9 ...###############..........
 * 10 ....B############...........
 * 11 ..S..###########............
 * 12 ......#########.............
 * 13 .......#######..............
 * 14 ........#####.S.......S.....
 * 15 B........###................
 * 16 ..........#SB...............
 * 17 ................S..........B
 * 18 ....S.......................
 * 19 ............................
 * 20 ............S......S........
 * 21 ............................
 * 22 .......................B....
 * This sensor's closest beacon is at 2,10, and so you know there are no beacons that close or closer (in any positions marked #).
 *
 * None of the detected beacons seem to be producing the distress signal, so you'll need to work out where the distress beacon is by working out where it isn't. For now, keep things simple by counting the positions where a beacon cannot possibly be along just a single row.
 *
 * So, suppose you have an arrangement of beacons and sensors like in the example above and, just in the row where y=10, you'd like to count the number of positions a beacon cannot possibly exist. The coverage from all sensors near that row looks like this:
 *
 *                  1    1    2    2
 *        0    5    0    5    0    5
 *  9 ...#########################...
 * 10 ..####B######################..
 * 11 .###S#############.###########.
 * In this example, in the row where y=10, there are 26 positions where a beacon cannot be present.
 *
 * Consult the report from the sensors you just deployed. In the row where y=2000000, how many positions cannot contain a beacon?
 *
 * --- Part Two ---
 * Your handheld device indicates that the distress signal is coming from a beacon nearby. The distress beacon is not detected by any sensor, but the distress beacon must have x and y coordinates each no lower than 0 and no larger than 4000000.
 *
 * To isolate the distress beacon's signal, you need to determine its tuning frequency, which can be found by multiplying its x coordinate by 4000000 and then adding its y coordinate.
 *
 * In the example above, the search space is smaller: instead, the x and y coordinates can each be at most 20. With this reduced search area, there is only a single position that could have a beacon: x=14, y=11. The tuning frequency for this distress beacon is 56000011.
 *
 * Find the only possible position for the distress beacon. What is its tuning frequency?
 *
 */
public class BeaconSearch {
    public static void main(String[] args) throws FileNotFoundException {
        InputReader inputReader = new InputReader();
        List<String> inputLines = inputReader.loadStringsFromFile("./src/y2022/day15/SampleBeaconInput.txt");

        BeaconSearch searcher = new BeaconSearch();
        Map<Point, Point> sensorBeacons = searcher.parseSensorBeaconPairs(inputLines);

        // Part 1
        //int numOverlaps = searcher.countSensorOverlapWithRow(sensorBeacons, 20); // Sample
        //int numOverlaps = searcher.countSensorOverlapWithRow(sensorBeacons, 2_000_000);

        // Part 2
        long tuningFreq = searcher.findTuningFreq(sensorBeacons, 20);  // Sample
        System.out.println("Tuning frequency: " + tuningFreq);
    }

    public long findTuningFreq(Map<Point, Point> sensorBeacons, int maxGrid) {
        long tuningFreq = 0;

        Point foundPoint = null;

        for (int i=0; i<=20; i++) {
            System.out.println("\n\nRow: " + i);
            foundPoint = findEmptySpaceInLine(sensorBeacons, i, maxGrid);
            if (foundPoint != null) {
                break;
            }
        }

        if (foundPoint == null) {
            throw new RuntimeException("Point not found");
        }

        tuningFreq = foundPoint.x * 4_000_000;

        return tuningFreq;
    }

    // Part 2 implementation
    // Go row by row similar to part 1, trying to find a single point that is uncovered
    // Return that point, or null if not found
    private Point findEmptySpaceInLine(Map<Point, Point> sensorBeacons, int row, int maxGrid) {
        Point foundPoint = null;
        int numOverlaps = 0;
        Set<Integer> rowPositionNoBeacon = Sets.newHashSet();

        for (Map.Entry<Point, Point> sensorBeacon : sensorBeacons.entrySet()) {
            rowPositionNoBeacon.addAll(findOverlapRowWithMax(sensorBeacon.getKey(), sensorBeacon.getValue(), row, maxGrid));
        }

        System.out.println("All Overlaps: " + rowPositionNoBeacon.stream().sorted().toList().toString());
        numOverlaps = rowPositionNoBeacon.size();
        System.out.println("Num overlaps: " + numOverlaps);

        return foundPoint;
    }

    // Part 2 has max grid
    private Set<Integer> findOverlapRowWithMax(Point sensor, Point beacon, int row, int maxGrid) {
        Set<Integer> overlaps = Sets.newHashSet();

        int gridDistance = Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);  // Taxicab distance
        int yDistance = Math.abs(sensor.y - row);
        int xDistance = gridDistance - yDistance;  // Is this right???  TODO


        System.out.println("Sensor " + sensor + ", beacon " + beacon + ", gridDistance=" + gridDistance + ", yDistance=" +
                yDistance + ", xDistance=" + xDistance);


        // Short cut if there's no insection with the row at all
        if (yDistance > gridDistance) {
            return overlaps;
        }

        // Generate overlaps with row, cap at max grid
        // Not sure if adding branches here helps or hurts processing time at this point...
        for (int i=0; i<=xDistance; i++) {
            int newpx = sensor.x + i;
            if (newpx <= maxGrid && newpx >=0){
                overlaps.add(newpx);
            }
            int newnx = sensor.x - i;
            if (newnx >= 0  && newnx <= maxGrid) {
                overlaps.add(newnx);
            }
        }

        System.out.println("Capped Overlaps: " + overlaps.stream().sorted().toList().toString());

        return overlaps;
    }

    public Map<Point, Point> parseSensorBeaconPairs(List<String> inputLines) {
        Map<Point, Point> pairs = Maps.newHashMap();

        for (String line : inputLines) {
            Point sensor = getPointFromString(line.split(":")[0]);
            Point beacon = getPointFromString(line.split(":")[1]);
            pairs.put(sensor, beacon);
        }

        return pairs;
    }

    // Sensor at x=2, y=18: closest beacon is at x=-2, y=15
    // Expecting to receive result of a split on the ":"
    //    Sensor at x=2, y=18 or  closest beacon is at x=-2, y=15
    private Point getPointFromString(String s) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(s);

        matcher.find();
        int x = Integer.parseInt(matcher.group());
        matcher.find();
        int y = Integer.parseInt(matcher.group());

        Point p = new Point(x,y);
        System.out.println("Created point: " + p);

        return p;
    }

    // Part 1 implementation
    public int countSensorOverlapWithRow(Map<Point, Point> sensorBeacons, int row) {
        int numOverlaps = 0;
        Set<Integer> rowPositionNoBeacon = Sets.newHashSet();

        for (Map.Entry<Point, Point> sensorBeacon : sensorBeacons.entrySet()) {
            rowPositionNoBeacon.addAll(findOverlapRow(sensorBeacon.getKey(), sensorBeacon.getValue(), row));
        }

        System.out.println("\nAll Overlaps: " + rowPositionNoBeacon.stream().sorted().toList().toString());
        numOverlaps = rowPositionNoBeacon.size();
        System.out.println("Num overlaps before removing: " + numOverlaps);

        // Remember the row can contain beacons, so iterate a second time to remove them
        for (Point beacon : sensorBeacons.values()) {
            if (beacon.y == row) {
                rowPositionNoBeacon.remove(beacon.x);
            }
        }

        System.out.println("\nAll Overlaps after removing beacons: " + rowPositionNoBeacon.stream().sorted().toList().toString());
        numOverlaps = rowPositionNoBeacon.size();
        System.out.println("Num overlaps after removing: " + numOverlaps);

        return numOverlaps;
    }

    // Part 1 implementation
    private Set<Integer> findOverlapRow(Point sensor, Point beacon, int row) {
        Set<Integer> overlaps = Sets.newHashSet();

        int gridDistance = Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);  // Taxicab distance
        int yDistance = Math.abs(sensor.y - row);
        int xDistance = gridDistance - yDistance;

        System.out.println("Sensor " + sensor + ", beacon " + beacon + ", gridDistance=" + gridDistance + ", yDistance=" +
                yDistance + ", xDistance=" + xDistance);

        // Short cut if there's no insection with the row at all
        if (yDistance > gridDistance) {
            return overlaps;
        }

        // Generate overlaps with row
        for (int i=0; i<=xDistance; i++) {
            overlaps.add(sensor.x + i);
            overlaps.add(sensor.x - i);
        }

        System.out.println("Overlaps: " + overlaps.stream().sorted().toList().toString());

        return overlaps;
    }

}
