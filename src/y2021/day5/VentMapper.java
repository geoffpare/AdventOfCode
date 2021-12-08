package y2021.day5;

import utils.InputReader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * --- Day 5: Hydrothermal Venture ---
 * You come across a field of hydrothermal vents on the ocean floor! These vents constantly produce large, opaque clouds, so it would be best to avoid them if possible.
 *
 * They tend to form in lines; the submarine helpfully produces a list of nearby lines of vents (your puzzle input) for you to review. For example:
 *
 * 0,9 -> 5,9
 * 8,0 -> 0,8
 * 9,4 -> 3,4
 * 2,2 -> 2,1
 * 7,0 -> 7,4
 * 6,4 -> 2,0
 * 0,9 -> 2,9
 * 3,4 -> 1,4
 * 0,0 -> 8,8
 * 5,5 -> 8,2
 * Each line of vents is given as a line segment in the format x1,y1 -> x2,y2 where x1,y1 are the coordinates of one end the line segment and x2,y2 are the coordinates of the other end. These line segments include the points at both ends. In other words:
 *
 * An entry like 1,1 -> 1,3 covers points 1,1, 1,2, and 1,3.
 * An entry like 9,7 -> 7,7 covers points 9,7, 8,7, and 7,7.
 * For now, only consider horizontal and vertical lines: lines where either x1 = x2 or y1 = y2.
 *
 * So, the horizontal and vertical lines from the above list would produce the following diagram:
 *
 * .......1..
 * ..1....1..
 * ..1....1..
 * .......1..
 * .112111211
 * ..........
 * ..........
 * ..........
 * ..........
 * 222111....
 * In this diagram, the top left corner is 0,0 and the bottom right corner is 9,9. Each position is shown as the number of lines which cover that point or . if no line covers that point. The top-left pair of 1s, for example, comes from 2,2 -> 2,1; the very bottom row is formed by the overlapping lines 0,9 -> 5,9 and 0,9 -> 2,9.
 *
 * To avoid the most dangerous areas, you need to determine the number of points where at least two lines overlap. In the above example, this is anywhere in the diagram with a 2 or larger - a total of 5 points.
 *
 * Consider only horizontal and vertical lines. At how many points do at least two lines overlap?
 *
 * NOTE: This person uses X as the vertical, I'm not sure why, but I'll follow along in case it matters for part 2...
 */
public class VentMapper {
    Map<Point, Integer> pointCounter = new HashMap<>();

    /**
     * Since we're just counting points, I'm not going to bother with a sparse matrix, I'm just going to do a Map(Point) -> Count
     *
     * Parse input into line segments
     * For each segment, traverse incrementing the cooresponding point count in the matrix
     */
    public static void main(String[] args) throws FileNotFoundException {
        InputReader inputReader = new InputReader();
        VentMapper ventMapper = new VentMapper();

        //List<String> rawSegments = inputReader.loadStringsFromFile("./src/y2021/day5/SampleVentData.txt");
        List<String> rawSegments = inputReader.loadStringsFromFile("./src/y2021/day5/VentData.txt");
        List<LineSegment> lineSegments = ventMapper.parseSegments(rawSegments);

        for (LineSegment seg : lineSegments) {
            ventMapper.addAllPoints(seg);
        }

        System.out.println("Part 1 overlaps: " + ventMapper.countOverlaps());

    }

    public void addAllPoints(LineSegment segment) {
        if (segment.isHorizontal()) {  // x's equal
            IntStream.range(
                    Math.min(segment.y1, segment.y2),
                    Math.max(segment.y1, segment.y2) +1 )  // We need the +1 because .range is exclusive for the end
                    .forEach(y -> addPoint(segment.x1, y));
        }

        if (segment.isVertical()) {  // y's equal
            IntStream.range(
                            Math.min(segment.x1, segment.x2),
                            Math.max(segment.x1, segment.x2) +1 )  // We need the +1 because .range is exclusive for the end
                    .forEach(x -> addPoint(x, segment.y1));
        }

        if (segment.isDiagonal()) {
            Integer pointDistance = Math.abs(segment.x1 - segment.x2); //Size of loop
            Integer xIncrement = (segment.x1 - segment.x2) < 0 ? +1 : -1;
            Integer yIncrement = (segment.y1 - segment.y2) < 0 ? +1 : -1;

            for (int x=0, y=0; Math.abs(x)<=pointDistance; x+=xIncrement, y+=yIncrement) {
                addPoint(segment.x1 + x, segment.y1 + y);
            }
        }
    }

    public void addPoint(Integer x, Integer y) {
        Point point = new Point(x,y);
        Integer count = pointCounter.getOrDefault(point, 0);
        pointCounter.put(point, count+1);
    }

    public Integer countOverlaps() {
        return Math.toIntExact(pointCounter.values().stream().filter(c -> c >1).count());
    }

    public List<LineSegment> parseSegments(List<String> segInputs) {
        List<LineSegment> segs = new ArrayList<>();

        for (String seg : segInputs) {
            String[] parts = seg.split("->");
            String[] p1 = parts[0].split(",");
            String[] p2 = parts[1].split(",");
            LineSegment parsedSeg = new LineSegment(
                    Integer.parseInt(p1[0].trim()), Integer.parseInt(p1[1].trim()),
                    Integer.parseInt(p2[0].trim()), Integer.parseInt(p2[1].trim())
            );

            System.out.println("Read: " + seg);
            System.out.println("Parsed: " + parsedSeg.toString());

            segs.add(parsedSeg);
        }

        return segs;
    }
}
