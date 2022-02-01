package y2021.day8;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void validateSampleBoardSolution() {
        Map<Integer, String> setup = new HashMap<>() {{
            put(1, "d");
            put(2, "e");
            put(3, "a");
            put(4, "f");
            put(5, "g");
            put(6, "b");
            put(7, "c");
        }};

        List<String> signals = Lists.newArrayList(
                "ab",
                "acedgfb",
                "cdfbe",
                "gcdfa",
                "fbcad",
                "dab",
                "cefabd",
                "cdfgeb",
                "eafb",
                "cagedb"
        );

        Board board = new Board(setup);

        assertTrue(board.boardValidForAll(signals));

        List<String> outputSignals = Lists.newArrayList(
                "cdfeb",
                "fcadb",
                "cdfeb",
                "cdbaf"
        );

        String finalOutput = "";
        for (String signal : outputSignals) {
            Integer digit = board.getDigitForBoard(signal);
            System.out.println("Digit for signal: " + signal + ": " + digit);
            finalOutput = finalOutput + digit;
        }

        assertEquals(finalOutput, "5353");
    }

    @Test
    public void ensureSampleOnlyMatchesOneBoard() {
        List<String> signals = Lists.newArrayList(
                "ab",
                "acedgfb",
                "cdfbe",
                "gcdfa",
                "fbcad",
                "dab",
                "cefabd",
                "cdfgeb",
                "eafb",
                "cagedb"
        );

        SignalPatterns patterns = new SignalPatterns();
        List<Board> allPossibleBoards = patterns.generateAllPossibleBoards();
        List<Board> validBoards = allPossibleBoards.stream().filter(b -> b.boardValidForAll(signals)).toList();

        for (Board b : validBoards) {
            System.out.println(b.toString());
        }

        assertEquals(1, validBoards.size());
    }

    @Test
    public void ensureFailedBoardSolutionFails() {
        Map<Integer, String> setup = new HashMap<>() {{
            put(1, "d");
            put(2, "a");  // a in position 2 will fail the ab combination below
            put(3, "e");
            put(4, "f");
            put(5, "g");
            put(6, "b");
            put(7, "c");
        }};

        List<String> signals = Lists.newArrayList(
                "ab",
                "acedgfb",
                "cdfbe",
                "gcdfa",
                "fbcad",
                "dab",
                "cefabd",
                "cdfgeb",
                "eafb",
                "cagedb"
        );

        Board board = new Board(setup);

        assertFalse(board.boardValidForAll(signals));
    }

    @Test
    public void ensureGenerateAllBoards() {
        SignalPatterns patterns = new SignalPatterns();
        List<Board> boards = patterns.generateAllPossibleBoards();

        assertEquals(boards.size(), 5040);
    }


}
