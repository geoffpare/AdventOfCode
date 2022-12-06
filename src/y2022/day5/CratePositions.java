package y2022.day5;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Stack;

/**
 *     [D]
 * [N] [C]
 * [Z] [M] [P]
 *  1   2   3
 *
 *  Represented as a List of Stacks, the List is the stack of crates left to right, each stack in the list
 *  is the crates in that stack bottom to top (so we can push/pop the top item)
 */
public class CratePositions {
    List<Stack<String>> crateStacks = Lists.newArrayList();

    public CratePositions(Integer numStacks, List<String> startingPositions) {
        // Initialize the correct amount of empty stacks
        for (int i=0; i<numStacks; i++) {
            crateStacks.add(new Stack<String>());
        }

        // Build the stacks one at a time, push crates bottom to top, than each stack left to right
        // Left to right loop
        for (int i=0; i<numStacks; i++) {
            // Bottom to top loop
            for (int j=startingPositions.size()-1; j>=0; j--) {
                // Grab the specific letter, the input strings aren't padded, so pad it so we don't worry about
                // substring array out of bounds issues.
                String paddedCrate = Strings.padEnd(startingPositions.get(j), numStacks*4-1, ' ');
                String crate = paddedCrate.substring(i*4+1, i*4+2);

                if (crate.isBlank()) {
                    // No more crates on this stack, move to the right and build the next stack
                    break;
                }

                crateStacks.get(i).push(crate);
            }
        }

        printStacks();
    }

    public void executeMoves9000(List<List<Integer>> moves) {
        for (List<Integer> move : moves) {
            executeMove9000(move);
        }
    }

    // move 1 from 2 to 1 in the list format (1,2,1)
    public void executeMove9000(List<Integer> move) {
        System.out.println("Executing move: " + move.toString());

        for (int i=0; i<move.get(0); i++) {
            // Remember to subtract 1 to the indicies because the moves are indexed starting at 1 (not 0 like our List)
            String crate = crateStacks.get(move.get(1)-1).pop();
            crateStacks.get(move.get(2)-1).push(crate);
        }

        printStacks();
    }

    public void executeMoves9001(List<List<Integer>> moves) {
        for (List<Integer> move : moves) {
            executeMove9001(move);
        }
    }

    // move 1 from 2 to 1 in the list format (1,2,1)
    public void executeMove9001(List<Integer> move) {
        System.out.println("Executing move: " + move.toString());

        Deque<String> cratesToMove = Lists.newLinkedList();

        for (int i=0; i<move.get(0); i++) {
            // Remember to subtract 1 to the indicies because the moves are indexed starting at 1 (not 0 like our List)
            String crate = crateStacks.get(move.get(1)-1).pop();
            cratesToMove.addFirst(crate);
        }
        crateStacks.get(move.get(2)-1).addAll(cratesToMove);
        printStacks();
    }

    // Prints the current stack state using the input format
    public void printStacks() {
        List<String> output = Lists.newArrayList();

        // Bottom to top loop
        for (int j=getMaxStackHeight(crateStacks)-1; j>=0; j--) {
            String rowOutput = "";
            for (Stack stack : crateStacks) {
                rowOutput = rowOutput + formatCrate(stack, j);
            }
            output.add(rowOutput);
        }

        output.add("  1   2   3   4   5   6   7   8   9 ");

        for (String line : output) {
            System.out.println(line);
        }
    }

    public String getCratesOnTop() {
        StringBuilder sb = new StringBuilder();

        for (Stack<String> stack : crateStacks) {
            sb.append(stack.peek());
        }

        return sb.toString();
    }

    private String formatCrate(Stack<String> stack, Integer height) {
        String output = "";

        if (height < stack.size()) {
            output = output + " [" + stack.get(height) + "]";
        } else {
            output = output + "    ";
        }

        return output;
    }

    private Integer getMaxStackHeight(List<Stack<String>> stacks) {
        return stacks.stream().max(Comparator.comparingInt(Stack::size)).get().size();
    }
}
