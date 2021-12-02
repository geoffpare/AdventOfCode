package y2021.day1;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import utils.InputReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * As the submarine drops below the surface of the ocean, it automatically performs a sonar sweep of the nearby sea floor. On a small screen, the sonar sweep report (your puzzle input) appears: each line is a measurement of the sea floor depth as the sweep looks further and further away from the submarine.
 *
 * For example, suppose you had the following report:
 *
 * 199
 * 200
 * 208
 * 210
 * 200
 * 207
 * 240
 * 269
 * 260
 * 263
 * This report indicates that, scanning outward from the submarine, the sonar sweep found depths of 199, 200, 208, 210, and so on.
 *
 * The first order of business is to figure out how quickly the depth increases, just so you know what you're dealing with - you never know if the keys will get carried into deeper water by an ocean current or a fish or something.
 *
 * To do this, count the number of times a depth measurement increases from the previous measurement. (There is no measurement before the first measurement.) In the example above, the changes are as follows:
 *
 * 199 (N/A - no previous measurement)
 * 200 (increased)
 * 208 (increased)
 * 210 (increased)
 * 200 (decreased)
 * 207 (increased)
 * 240 (increased)
 * 269 (increased)
 * 260 (decreased)
 * 263 (increased)
 * In this example, there are 7 measurements that are larger than the previous measurement.
 *
 * How many measurements are larger than the previous measurement?
 *
 * --- Part Two ---
 * Considering every single measurement isn't as useful as you expected: there's just too much noise in the data.
 *
 * Instead, consider sums of a three-measurement sliding window. Again considering the above example:
 *
 * 199  A
 * 200  A B
 * 208  A B C
 * 210    B C D
 * 200  E   C D
 * 207  E F   D
 * 240  E F G
 * 269    F G H
 * 260      G H
 * 263        H
 * Start by comparing the first and second three-measurement windows. The measurements in the first window are marked A (199, 200, 208); their sum is 199 + 200 + 208 = 607. The second window is marked B (200, 208, 210); its sum is 618. The sum of measurements in the second window is larger than the sum of the first, so this first comparison increased.
 *
 * Your goal now is to count the number of times the sum of measurements in this sliding window increases from the previous sum. So, compare A with B, then compare B with C, then C with D, and so on. Stop when there aren't enough measurements left to create a new three-measurement sum.
 *
 * In the above example, the sum of each three-measurement window is as follows:
 *
 * A: 607 (N/A - no previous sum)
 * B: 618 (increased)
 * C: 618 (no change)
 * D: 617 (decreased)
 * E: 647 (increased)
 * F: 716 (increased)
 * G: 769 (increased)
 * H: 792 (increased)
 * In this example, there are 5 sums that are larger than the previous sum.
 *
 * Consider sums of a three-measurement sliding window. How many sums are larger than the previous sum?
 */
public class DepthCounter {
    // Test input
    private List<Integer> getExampleDepthReadings() {
        return Lists.newArrayList(199,200,208,210,200,207,240,269,260,263);
    }

    // Part 1 Solution
    private Integer countDepthIncreases(List<Integer> depths) {
        // Per example, you start counting on the 2nd entry
        Integer previous = depths.get(0);
        Integer numDepthIncrease = 0;

        // Again, i=1 because we skip the first entry
        for (int i=1; i<depths.size(); i++) {
            if (depths.get(i) > previous) {
                numDepthIncrease++;
            }
            previous = depths.get(i);
        }

        return numDepthIncrease;
    }

    // Part 2 Solution - Sliding window of size 3
    // Keep two 3 entry lists representing the previous slilding window and the current sliding window.
    // Take the sum of each list and compare at each iteration to determine if depth increased using same pattern
    // as Solution 1
    private Integer countSlidingWindowIncreases(List<Integer> depths) {
        // Need enough datapoints to establish a sliding window....
        if (depths.size() < 4) {
            return 0;
        }

        // Evicting Queue evicts the FIFO head when full and pushing a new element
        EvictingQueue<Integer> previous = EvictingQueue.create(3);
        EvictingQueue<Integer> current = EvictingQueue.create(3);
        Integer numDepthIncrease = 0;

        // Use i=1 as the start since we seed the previous window with i-1, and current with i
        for (int i=1; i<depths.size(); i++) {
            // We shift the new (and previous) element into the respective windows before comparing
            previous.offer(depths.get(i-1));
            current.offer(depths.get(i));

            // Both lists need to have 3 elements for the comparison to be valid, continue until the windows have been populated
            if (previous.size() != 3 || current.size() != 3) {
                continue;
            }

            if (sumQueue(current) > sumQueue(previous)) {
                numDepthIncrease++;
            }

        }

        return numDepthIncrease;
    }

    private Integer sumList(List<Integer> list) {
        return list.stream().collect(Collectors.summingInt(Integer::intValue));
    }

    private Integer sumQueue(Queue<Integer> queue) {
        return queue.stream().collect(Collectors.summingInt(Integer::intValue));
    }

    public static void main(String[] args) throws FileNotFoundException {
        DepthCounter depthCounter = new DepthCounter();

        InputReader inputs = new InputReader();
        //List<Integer> depthMeasurements = depthCounter.getExampleDepthReadings();
        List<Integer> depthMeasurements = inputs.loadIntegersFromFile("./src/y2021/day1/DepthReadings.txt");

        Integer numIncreases = depthCounter.countDepthIncreases(depthMeasurements);
        Integer numWindowsIncreases = depthCounter.countSlidingWindowIncreases(depthMeasurements);

        System.out.println("Num single increases: " + numIncreases);
        System.out.println("Num sliding increases: " + numWindowsIncreases);
    }
}
