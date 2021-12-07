package y2021.day4;

import java.util.List;

/**
 * 2d bingo board as list of lists instead of 2d array
 */
public class BingoBoard {
    private List<List<Integer>> board;  // int[row][colum]
    private Integer numRows;
    private Integer numColumns;

    public BingoBoard(List<List<Integer>> board) {
        this.board = board;
        this.numRows = board.size();
        this.numColumns = board.get(0).size();
    }

    public Boolean doesBoardWin(List<Integer> calledNumbers) {
        // First check if any row completes
        for (int r=0; r<numRows; r++) {

            Boolean rowCompleted = true;  // Assume a win unless any number doesn't match
            for (int c=0; c<numColumns; c++) {
                if (!calledNumbers.contains(board.get(r).get(c))) {
                    rowCompleted = false;
                    break;
                }
            }

            // A row was completed
            if (rowCompleted) {
                return true;
            }
        }

        // Now check if a column is completed
        for (int c=0; c<numColumns; c++) {

            Boolean columnCompleted = true;
            for (int r=0; r<numRows; r++) {
                if (!calledNumbers.contains(board.get(r).get(c))) {
                    columnCompleted = false;
                    break;
                }
            }

            if (columnCompleted) {
                return true;
            }
        }

        return false;
    }

    // Simply check each number in the board against the called numbers, and add to the total if it wasn't called
    public Integer sumUncalledNumbers(List<Integer> calledNums) {
        Integer sumUncalled = 0;

        for (int r=0; r<numRows; r++) {
            for (int c=0; c<numColumns; c++) {
                if (!calledNums.contains(board.get(r).get(c))) {
                    sumUncalled+=board.get(r).get(c);
                }
            }
        }

        return sumUncalled;
    }
}
