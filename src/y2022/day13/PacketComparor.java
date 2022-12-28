package y2022.day13;

import com.google.common.collect.Lists;
import utils.InputReader;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

/**
 * --- Day 13: Distress Signal ---
 * You climb the hill and again try contacting the Elves. However, you instead receive a signal you weren't expecting: a distress signal.
 *
 * Your handheld device must still not be working properly; the packets from the distress signal got decoded out of order. You'll need to re-order the list of received packets (your puzzle input) to decode the message.
 *
 * Your list consists of pairs of packets; pairs are separated by a blank line. You need to identify how many pairs of packets are in the right order.
 *
 * For example:
 *
 * [1,1,3,1,1]
 * [1,1,5,1,1]
 *
 * [[1],[2,3,4]]
 * [[1],4]
 *
 * [9]
 * [[8,7,6]]
 *
 * [[4,4],4,4]
 * [[4,4],4,4,4]
 *
 * [7,7,7,7]
 * [7,7,7]
 *
 * []
 * [3]
 *
 * [[[]]]
 * [[]]
 *
 * [1,[2,[3,[4,[5,6,7]]]],8,9]
 * [1,[2,[3,[4,[5,6,0]]]],8,9]
 * Packet data consists of lists and integers. Each list starts with [, ends with ], and contains zero or more comma-separated values (either integers or other lists). Each packet is always a list and appears on its own line.
 *
 * When comparing two values, the first value is called left and the second value is called right. Then:
 *
 * If both values are integers, the lower integer should come first. If the left integer is lower than the right integer, the inputs are in the right order. If the left integer is higher than the right integer, the inputs are not in the right order. Otherwise, the inputs are the same integer; continue checking the next part of the input.
 * If both values are lists, compare the first value of each list, then the second value, and so on. If the left list runs out of items first, the inputs are in the right order. If the right list runs out of items first, the inputs are not in the right order. If the lists are the same length and no comparison makes a decision about the order, continue checking the next part of the input.
 * If exactly one value is an integer, convert the integer to a list which contains that integer as its only value, then retry the comparison. For example, if comparing [0,0,0] and 2, convert the right value to [2] (a list containing 2); the result is then found by instead comparing [0,0,0] and [2].
 * Using these rules, you can determine which of the pairs in the example are in the right order:
 *
 * == Pair 1 ==
 * - Compare [1,1,3,1,1] vs [1,1,5,1,1]
 *   - Compare 1 vs 1
 *   - Compare 1 vs 1
 *   - Compare 3 vs 5
 *     - Left side is smaller, so inputs are in the right order
 *
 * == Pair 2 ==
 * - Compare [[1],[2,3,4]] vs [[1],4]
 *   - Compare [1] vs [1]
 *     - Compare 1 vs 1
 *   - Compare [2,3,4] vs 4
 *     - Mixed types; convert right to [4] and retry comparison
 *     - Compare [2,3,4] vs [4]
 *       - Compare 2 vs 4
 *         - Left side is smaller, so inputs are in the right order
 *
 * == Pair 3 ==
 * - Compare [9] vs [[8,7,6]]
 *   - Compare 9 vs [8,7,6]
 *     - Mixed types; convert left to [9] and retry comparison
 *     - Compare [9] vs [8,7,6]
 *       - Compare 9 vs 8
 *         - Right side is smaller, so inputs are not in the right order
 *
 * == Pair 4 ==
 * - Compare [[4,4],4,4] vs [[4,4],4,4,4]
 *   - Compare [4,4] vs [4,4]
 *     - Compare 4 vs 4
 *     - Compare 4 vs 4
 *   - Compare 4 vs 4
 *   - Compare 4 vs 4
 *   - Left side ran out of items, so inputs are in the right order
 *
 * == Pair 5 ==
 * - Compare [7,7,7,7] vs [7,7,7]
 *   - Compare 7 vs 7
 *   - Compare 7 vs 7
 *   - Compare 7 vs 7
 *   - Right side ran out of items, so inputs are not in the right order
 *
 * == Pair 6 ==
 * - Compare [] vs [3]
 *   - Left side ran out of items, so inputs are in the right order
 *
 * == Pair 7 ==
 * - Compare [[[]]] vs [[]]
 *   - Compare [[]] vs []
 *     - Right side ran out of items, so inputs are not in the right order
 *
 * == Pair 8 ==
 * - Compare [1,[2,[3,[4,[5,6,7]]]],8,9] vs [1,[2,[3,[4,[5,6,0]]]],8,9]
 *   - Compare 1 vs 1
 *   - Compare [2,[3,[4,[5,6,7]]]] vs [2,[3,[4,[5,6,0]]]]
 *     - Compare 2 vs 2
 *     - Compare [3,[4,[5,6,7]]] vs [3,[4,[5,6,0]]]
 *       - Compare 3 vs 3
 *       - Compare [4,[5,6,7]] vs [4,[5,6,0]]
 *         - Compare 4 vs 4
 *         - Compare [5,6,7] vs [5,6,0]
 *           - Compare 5 vs 5
 *           - Compare 6 vs 6
 *           - Compare 7 vs 0
 *             - Right side is smaller, so inputs are not in the right order
 * What are the indices of the pairs that are already in the right order? (The first pair has index 1, the second pair has index 2, and so on.) In the above example, the pairs in the right order are 1, 2, 4, and 6; the sum of these indices is 13.
 *
 * Determine which pairs of packets are already in the right order. What is the sum of the indices of those pairs?
 *
 * --- Part Two ---
 * Now, you just need to put all of the packets in the right order. Disregard the blank lines in your list of received packets.
 *
 * The distress signal protocol also requires that you include two additional divider packets:
 *
 * [[2]]
 * [[6]]
 * Using the same rules as before, organize all packets - the ones in your list of received packets as well as the two divider packets - into the correct order.
 *
 * For the example above, the result of putting the packets in the correct order is:
 *
 * []
 * [[]]
 * [[[]]]
 * [1,1,3,1,1]
 * [1,1,5,1,1]
 * [[1],[2,3,4]]
 * [1,[2,[3,[4,[5,6,0]]]],8,9]
 * [1,[2,[3,[4,[5,6,7]]]],8,9]
 * [[1],4]
 * [[2]]
 * [3]
 * [[4,4],4,4]
 * [[4,4],4,4,4]
 * [[6]]
 * [7,7,7]
 * [7,7,7,7]
 * [[8,7,6]]
 * [9]
 * Afterward, locate the divider packets. To find the decoder key for this distress signal, you need to determine the indices of the two divider packets and multiply them together. (The first packet is at index 1, the second packet is at index 2, and so on.) In this example, the divider packets are 10th and 14th, and so the decoder key is 140.
 *
 * Organize all of the packets into the correct order. What is the decoder key for the distress signal?
 */
public class PacketComparor {
    public static void main(String[] args) throws FileNotFoundException {
        InputReader reader = new InputReader();
        //List<String> input = reader.loadStringsFromFile("./src/y2022/day13/SamplePackets.txt");
        List<String> input = reader.loadStringsFromFile("./src/y2022/day13/PacketInput.txt");
        //List<String> input = reader.loadStringsFromFile("./src/y2022/day13/TestPackets.txt");

        PacketComparor packetComparor = new PacketComparor();
        List<PacketPair> pairs = packetComparor.getPacketPairs(input);

        int sumIndexInOrder = 0;
        for (PacketPair p : pairs) {
            if (p.isCorrectOrder()) {
                sumIndexInOrder += p.getPosition();
            }
        }

        System.out.println("Part 1: Sum of indices: " + sumIndexInOrder);

        // Part 2
        List<Packet> packetList = packetComparor.getPackets(input);
        System.out.println("Sorting packet list...");
        Collections.sort(packetList);

        int sepIndex1 = 0;
        int sepIndex2 = 0;

        for (int i=0; i<packetList.size(); i++) {
            String ps = packetList.get(i).getItem().toString();
            System.out.println(ps);

            if (ps.equals("[[2,]]")) {
                sepIndex1 = i+1;
            }

            if (ps.equals("[[6,]]")) {
                sepIndex2 = i+1;
            }
        }

        System.out.println("Sep indexes: [" + sepIndex1 + "," + sepIndex2 + "]");
        System.out.println("Part 2 Decoder: " + sepIndex1 * sepIndex2);
    }

    // Part 2
    public List<Packet> getPackets(List<String> input) {
        List<Packet> packets = Lists.newArrayList();

        for (int i=0; i<input.size(); i+=3) {
            Packet pl = new Packet(parsePacket(input.get(i)));
            Packet pr = new Packet(parsePacket(input.get(i+1)));

            packets.add(pl);
            packets.add(pr);
        }

        // Also add two separator packets
        packets.add(new Packet(parsePacket("[[2]]")));
        packets.add(new Packet(parsePacket("[[6]]")));

        System.out.println("\n\n===============================================");

        for (Packet p : packets) {
            System.out.println("Packet: " + p.getItem());
        }

        return packets;
    }

    // Part 1
    public List<PacketPair> getPacketPairs(List<String> input) {
        List<PacketPair> pairs = Lists.newArrayList();

        int pairNum = 1;
        for (int i=0; i<input.size(); i+=3) {
            Item left = parsePacket(input.get(i));
            System.out.println("Parsed Left: " + left.toString());
            Item right = parsePacket(input.get(i+1));
            System.out.println("Parsed right: " + right.toString());

            PacketPair pair = new PacketPair(left, right, pairNum);
            pairs.add(pair);
            pairNum++;
        }

        System.out.println("\n\n===============================================");

        for (PacketPair p : pairs) {
            System.out.println("Pair " + p.getPosition());
            System.out.println("Left: " + p.getLeft());
            System.out.println("Right:" + p.getRight());
            System.out.println("Is correct order: " + p.isCorrectOrder());
        }

        return pairs;
    }

    // Call this recursively?
    private Item parsePacket(String line) {
        List<Item> items = parseList(line);
        return items.get(0); // Do to how we parse, we have already wrapped the list of items, unwrap here
    }

    private List<Item> parseList(String line) {
        // Parse until we find a [ and recurse, or find a ] and can return our list
        List<Item> items = Lists.newArrayList();

        int i=0;  // Our current position scanning the string
        while (i<line.length()) {
            System.out.println("Iteration i=" + i + ", line=" + line);

            if (line.charAt(i) == ']') {
                System.out.println("End of list, returning");
                return items;

            } else if (line.charAt(i) == '[') {
                // Recurse to parse the new sublist, than move our pointer forward
                String withinBrackets = getStringWithinBrackets(line.substring(i, line.length()));
                Item item = Item.newListItem(parseList(withinBrackets.substring(1, withinBrackets.length()))); // Start at pos 1 because we've already parsed the open bracket
                // Advance i to the close of the list we just parsed
                System.out.println("Advancing i from: " + i);
                //i = line.indexOf("]", i) + 1;
                i = i + withinBrackets.length();
                System.out.println("Advanced i to: " + i);
                items.add(item);

            } else if (line.charAt(i) == ',') {
                // Simple move past the comma
                i+=1;

            } else if (line.substring(i,i+2).matches("10")) {
                // It's absolutely dirty that the entirety of the sample only uses 0-9, but you have to find the surprise
                // that 10 is valid by digging into your individual input.  Dirty.
                Integer v = Integer.parseInt(line.substring(i,i+2));
                System.out.println("Parsed int: " + v);
                items.add(Item.newValueItem(v));
                i+=2;
            }

            else if (line.substring(i,i+1).matches("[0-9]")) {
                Integer v = Integer.parseInt(line.substring(i,i+1));
                System.out.println("Parsed int: " + v);
                items.add(Item.newValueItem(v));
                i+=1;
            }
        }

        return items;
    }

    // Returns the substring corresponding to the matching brackets starting at the beginning of s, brackets inclusive
    // eg [a[b,c],d],e]  returns [a[b,c],d]
    private String getStringWithinBrackets(String s) {
        int numOpen = 0;
        for (int i=0; i<s.length(); i++) {
            if (s.charAt(i) == '[') {
                numOpen++;
            } else if (s.charAt(i) == ']') {
                numOpen--;
            }

            if (numOpen == 0) {
                return s.substring(0, i+1);
            }
        }

        throw new RuntimeException("String " + s + " wasn't balanced");
    }
}
