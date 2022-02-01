package y2021.day8;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.IntStream;

/**
 * 1
 * -------
 * 2 |     | 3
 * |  4  |
 * -------
 * 5 |     | 6
 * |     |
 * -------
 * 8
 */
public class Board {
    // Board position -> Letter assignment
    Map<Integer, String> board = new HashMap();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  " + board.get(1) + "  \n");
        sb.append("" + board.get(2) + "   " + board.get(3) + "\n");
        sb.append("  " + board.get(4) + "  \n");
        sb.append("" + board.get(5) + "   " + board.get(6) + "\n");
        sb.append("  " + board.get(7) + "  \n");

        return sb.toString();
    }

    public Board(Map<Integer, String> board) {
        this.board = board;
    }

    // Ok, new try to iteratively create a board by overlapping the set of inputs, narrowing segment letters
    // down to their position...  Attempt 2...
    public Board inferBoard(List<String> signalInputs) {
        // Board position -> Potential signal letters
        Map<Integer, Set<String>> potentialBoard = new HashMap<>();
        IntStream.range(1, 8).forEach(i -> potentialBoard.put(i, new HashSet<>()));

        // This is NOT signal positions, it's a decoded digit mapped to the letters that create that digit
        // We'll use it later to do set subtractions of letters
        Map<Integer, Set<String>> lettersThatMakeUpNumber = new HashMap<>();
        IntStream.range(0, 10).forEach(i -> lettersThatMakeUpNumber.put(i, new HashSet<>()));
        lettersThatMakeUpNumber.get(8).addAll(Lists.newArrayList("a", "b", "c", "d", "e", "f", "g"));

        // Set up the easy ones
        for (String signal : signalInputs) {
            if (signal.length() == 2) {
                // Number 1
                // This is saying position 3 and 6 must be one of the two letters in the signal
                potentialBoard.get(3).addAll(Arrays.asList(signal.split("")));
                potentialBoard.get(6).addAll(Arrays.asList(signal.split("")));
                lettersThatMakeUpNumber.get(1).addAll(Arrays.asList(signal.split("")));
            } else if (signal.length() == 3) {
                // Number 7
                potentialBoard.get(1).addAll(Arrays.asList(signal.split("")));
                potentialBoard.get(3).addAll(Arrays.asList(signal.split("")));
                potentialBoard.get(6).addAll(Arrays.asList(signal.split("")));
                lettersThatMakeUpNumber.get(7).addAll(Arrays.asList(signal.split("")));
            } else if (signal.length() == 4) {
                // Number 4
                potentialBoard.get(2).addAll(Arrays.asList(signal.split("")));
                potentialBoard.get(4).addAll(Arrays.asList(signal.split("")));
                potentialBoard.get(3).addAll(Arrays.asList(signal.split("")));
                potentialBoard.get(6).addAll(Arrays.asList(signal.split("")));
                lettersThatMakeUpNumber.get(4).addAll(Arrays.asList(signal.split("")));
            } else if (signal.length() == 7) {
                // Number 8, which doesn't really give us information at this first pass
                lettersThatMakeUpNumber.get(8).addAll(Arrays.asList(signal.split("")));
            }
        }


        // Handle 7 and 1 overlap
        // Position 1 is 1 potential - 3/6 potential, then 3/6 can remove what's left in 1
        potentialBoard.get(1).removeAll(potentialBoard.get(3));
        potentialBoard.get(3).removeAll(potentialBoard.get(1));
        potentialBoard.get(6).removeAll(potentialBoard.get(1));

        // Handle 4 overlap with 1/3/6
        // Position 2/4 can subtract positions 1, 3/6
        potentialBoard.get(2).removeAll(potentialBoard.get(1));
        potentialBoard.get(2).removeAll(potentialBoard.get(3));
        potentialBoard.get(4).removeAll(potentialBoard.get(1));
        potentialBoard.get(4).removeAll(potentialBoard.get(3));

        // Use the fact that 9 and 4 overlap by 4 (and this is the only number of length 6 that has this overlap
        Set<String> nine = findLettersForNine(signalInputs, lettersThatMakeUpNumber.get(4));
        lettersThatMakeUpNumber.get(9).addAll(nine);

        // Position 5 is now letters for 8 minus letters for 9
        String pos5 = Sets.difference(lettersThatMakeUpNumber.get(8), lettersThatMakeUpNumber.get(9)).stream().findFirst().get();
        potentialBoard.get(5).add(pos5);
        potentialBoard.values().stream().forEach(set -> set.remove(pos5));

        // Letters that make up 6 will be of length 6, not number 9, and will be missing of segments 3/6
        Set<String> lettersForSix = findLettersForSix(signalInputs, nine, potentialBoard.get(3));
        lettersThatMakeUpNumber.get(6).addAll(lettersForSix);

        // Board position 6 is now the letter in common between lettersForOne and lettersForSix
        Set<String> pos6 = Sets.intersection(lettersThatMakeUpNumber.get(1), lettersForSix);
        potentialBoard.put(6, pos6);
        potentialBoard.remove(pos6);

        // At this point we know positions 1,3,5,6 and numbers 1,4,7,8,9,6

        // Number 0 will be the signal of length 6 that is not the signal for 6 or 9
        List<String> signalForZero = signalInputs.stream().filter(s -> s.length() == 6).toList();
        signalForZero.stream().map(s -> Sets.newHashSet(s)).filter(
                ss -> !(Sets.difference(pos6, ss).size() == 0)).filter(
                ss -> !(Sets.difference(lettersThatMakeUpNumber.get(9), ss).size() == 0)
        ).toList();
        lettersThatMakeUpNumber.put(0, Sets.newHashSet(signalForZero));

        // The diff between letters for number 8 and number 0 will be board position
        Set<String> letterforPos4 = Sets.difference(lettersThatMakeUpNumber.get(8), lettersThatMakeUpNumber.get(0));

        // Sanity checking asserts here....
        if (letterforPos4.size() > 1 || Sets.intersection(letterforPos4, potentialBoard.get(4)).size() != 1) {
            throw new RuntimeException("Sanity check at position 4 setting failed");
        }
        potentialBoard.put(4, letterforPos4);

        // We know now every board position but 2 and 7

        
        return new Board(new HashMap<>());
    }

    // Use the fact that 9 and 4 overlap by 4 (and this is the only number of length 6 that has this overlap
    public Set<String> findLettersForNine(List<String> signals, Set<String> lettersForFour) {
        List<String> lengthSix = signals.stream().filter(s -> s.length() == 6).toList();
        for (String signal : lengthSix) {
            Set<String> signalAsSet = new HashSet(Arrays.asList(signal.split("")));
            if (Sets.difference(signalAsSet, lettersForFour).size() == 2) {
                return signalAsSet;
            }
        }

        throw new RuntimeException("Didn't find signal for 9");
    }

    // Use the fact that six and 0 are the last numbers of length 6 left, and six will be missing one of the segments 3/6
    // which we know at this point.
    public Set<String> findLettersForSix(List<String> signals, Set<String> lettersForNine, Set<String> segment36) {
        List<String> lengthSix = signals.stream().filter(s -> s.length() == 6).toList();
        for (String signal : lengthSix) {
            Set<String> signalAsSet = new HashSet(Arrays.asList(signal.split("")));

            // Skip number 9, we've got it
            if (Sets.difference(signalAsSet, lettersForNine).size() == 0) {
                continue;
            }

            if (Sets.union(signalAsSet, segment36).size() > signalAsSet.size()) {
                // Found signal for number 6
                return signalAsSet;
            }
        }

        throw new RuntimeException("Didn't find signal for 6");
    }


    public Integer getDigitsForSignalList(List<String> signals) {
        String finalOutput = "";
        for (String signal : signals) {
            Integer digit = getDigitForBoard(signal);
            finalOutput = finalOutput + digit;
        }

        System.out.println("Input signals: " + Joiner.on(",").join(signals) + ": " + finalOutput);
        return Integer.parseInt(finalOutput);
    }

    public Integer getDigitForBoard(String signal) {
        if (signal.length() == 2) {
            return 1;
        } else if (signal.length() == 3) {
            return 7;
        } else if (signal.length() == 4) {
            return 4;
        } else if (signal.length() == 7) {
            return 8;
        } else if (signal.length() == 6) {
            // length 6 could be 0,6,9
            if (!positionContainsAnyLetter(5, signal)) {
                // If position 5 is empty, it's a 9
                return 9;
            }
            // If position 3 is filled, it's a 0
            if (positionContainsAnyLetter(3, signal)) {
                return 0;
            } else {
                return 6;
            }
        } else if (signal.length() == 5) {
            // length 5 could be 2,3,5
            // If position 2 is filled, it's a 5
            if (positionContainsAnyLetter(2, signal)) {
                return 5;
            }

            // Now that 5 is out of the wya, if position 6 is filled, it's a 3
            if (positionContainsAnyLetter(6, signal)) {
                return 3;
            }

            return 2;
        }

        throw new RuntimeException("Couldn't find the digit for signal: " + signal);

    }

    public Boolean boardValidForAll(List<String> inputs) {
        return inputs.stream().filter(signal -> !boardValidForSingleSignal(signal)).count() == 0;
    }

    // Input: cde -> check if that will make a 7, which length 3 must do
    public Boolean boardValidForSingleSignal(String signal) {

        // Number 1
        if (signal.length() == 2) {
            if (!positionsAllContainAnyLetter(signal, 3, 6)) {
                return false;
            }
        } else if (signal.length() == 3) {
            // Number 7
            if (!positionsAllContainAnyLetter(signal, 1, 3, 6)) {
                return false;
            }
        } else if (signal.length() == 4) {
            // Number 4
            if (!positionsAllContainAnyLetter(signal, 2, 3, 4, 6)) {
                return false;
            }
        } else if (signal.length() == 5) {
            // If this could match any of the possible number configurations, we'll consider it valid
            // Numbers 2, 3, 5
            // Number 2
            if (positionsAllContainAnyLetter(signal, 1, 3, 4, 5, 7)) {
                return true;
            }
            // Number 3
            if (positionsAllContainAnyLetter(signal, 1, 3, 4, 6, 7)) {
                return true;
            }
            // Number 5
            if (positionsAllContainAnyLetter(signal, 1, 2, 4, 6, 7)) {
                return true;
            }
        } else if (signal.length() == 6) {
            // Numbers 0, 6, 9
            // Number 0
            if (positionsAllContainAnyLetter(signal, 1, 2, 3, 5, 6, 7)) {
                return true;
            }
            // Number 6
            if (positionsAllContainAnyLetter(signal, 1, 2, 5, 7, 6, 4)) {
                return true;
            }
            // Number 9
            if (positionsAllContainAnyLetter(signal, 1, 2, 3, 4, 6, 7)) {
                return true;
            }
        } else if (signal.length() == 7) {
            // Number 8
            if (!positionsAllContainAnyLetter(signal, 1, 2, 3, 4, 5, 6, 7)) {
                return false;
            }
        } else {
            throw new RuntimeException("Unexpected signal length");
        }

        return true;
    }

    // If any position doesn't match on of the specified letters, return false
    private Boolean positionsAllContainAnyLetter(String letters, Integer... positions) {
        return Arrays.stream(positions).filter(p -> !positionContainsAnyLetter(p, letters)).count() == 0;
    }

    private Boolean positionContainsAnyLetter(Integer position, String letters) {
        Boolean containsAny = false;

        for (int i = 0; i < letters.length(); i++) {
            if (positionContainsLetter(position, letters.substring(i, i + 1))) {
                return true;
            }
        }

        return false;
    }

    private Boolean positionContainsLetter(Integer position, String letter) {
        return board.get(position).equals(letter);
    }
}
