package y2022.day8;

import utils.InputReader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * --- Day 8: Treetop Tree House ---
 * The expedition comes across a peculiar patch of tall trees all planted carefully in a grid. The Elves explain that a previous expedition planted these trees as a reforestation effort. Now, they're curious if this would be a good location for a tree house.
 *
 * First, determine whether there is enough tree cover here to keep a tree house hidden. To do this, you need to count the number of trees that are visible from outside the grid when looking directly along a row or column.
 *
 * The Elves have already launched a quadcopter to generate a map with the height of each tree (your puzzle input). For example:
 *
 * 30373
 * 25512
 * 65332
 * 33549
 * 35390
 *
 * Each tree is represented as a single digit whose value is its height, where 0 is the shortest and 9 is the tallest.
 *
 * A tree is visible if all of the other trees between it and an edge of the grid are shorter than it. Only consider trees in the same row or column; that is, only look up, down, left, or right from any given tree.
 *
 * All of the trees around the edge of the grid are visible - since they are already on the edge, there are no trees to block the view. In this example, that only leaves the interior nine trees to consider:
 *
 * The top-left 5 is visible from the left and top. (It isn't visible from the right or bottom since other trees of height 5 are in the way.)
 * The top-middle 5 is visible from the top and right.
 * The top-right 1 is not visible from any direction; for it to be visible, there would need to only be trees of height 0 between it and an edge.
 * The left-middle 5 is visible, but only from the right.
 * The center 3 is not visible from any direction; for it to be visible, there would need to be only trees of at most height 2 between it and an edge.
 * The right-middle 3 is visible from the right.
 * In the bottom row, the middle 5 is visible, but the 3 and 4 are not.
 * With 16 trees visible on the edge and another 5 visible in the interior, a total of 21 trees are visible in this arrangement.
 *
 * Consider your map; how many trees are visible from outside the grid?
 *
 * --- Part Two ---
 * Content with the amount of tree cover available, the Elves just need to know the best spot to build their tree house: they would like to be able to see a lot of trees.
 *
 * To measure the viewing distance from a given tree, look up, down, left, and right from that tree; stop if you reach an edge or at the first tree that is the same height or taller than the tree under consideration. (If a tree is right on the edge, at least one of its viewing distances will be zero.)
 *
 * The Elves don't care about distant trees taller than those found by the rules above; the proposed tree house has large eaves to keep it dry, so they wouldn't be able to see higher than the tree house anyway.
 *
 * In the example above, consider the middle 5 in the second row:
 *
 * 30373
 * 25512
 * 65332
 * 33549
 * 35390
 * Looking up, its view is not blocked; it can see 1 tree (of height 3).
 * Looking left, its view is blocked immediately; it can see only 1 tree (of height 5, right next to it).
 * Looking right, its view is not blocked; it can see 2 trees.
 * Looking down, its view is blocked eventually; it can see 2 trees (one of height 3, then the tree of height 5 that blocks its view).
 * A tree's scenic score is found by multiplying together its viewing distance in each of the four directions. For this tree, this is 4 (found by multiplying 1 * 1 * 2 * 2).
 *
 * However, you can do even better: consider the tree of height 5 in the middle of the fourth row:
 *
 * 30373
 * 25512
 * 65332
 * 33549
 * 35390
 * Looking up, its view is blocked at 2 trees (by another tree with a height of 5).
 * Looking left, its view is not blocked; it can see 2 trees.
 * Looking down, its view is also not blocked; it can see 1 tree.
 * Looking right, its view is blocked at 2 trees (by a massive tree of height 9).
 * This tree's scenic score is 8 (2 * 2 * 1 * 2); this is the ideal spot for the tree house.
 *
 * Consider each tree on your map. What is the highest scenic score possible for any tree?
 *
 *
 */
public class TreeCounter {
    public static void main(String[] args) throws FileNotFoundException {
        InputReader input = new InputReader();
        List<List<Integer>> grid = input.readIntegerGridFromFile("./src/y2022/day8/TreeGrid.txt");
        
        TreeCounter counter = new TreeCounter();

        // Part 1
        List<List<Integer>> visTrees = counter.buildVisibleTreeGrid(grid);
        Integer numVisTrees = counter.countVisibleTrees(visTrees);

        System.out.println("Number of visible trees: " + numVisTrees);

        // Part 2
        List<List<Integer>> treeScores = counter.buildTreeScoreGrid(grid);
        Integer maxViewScore = counter.getMaxViewScore(treeScores);
        System.out.println("Max tree view score: " + maxViewScore);
    }

    private List<List<Integer>> buildTreeScoreGrid(List<List<Integer>> treeGrid) {
        Integer numColumns = treeGrid.get(0).size();
        Integer numRows = treeGrid.size();

        List<List<Integer>> treeScores = createZeroGrid(numRows, numColumns);

        for (int r=0; r<numRows; r++) {
            for (int c=0; c<numColumns; c++) {
                treeScores.get(r).set(c, scoreLocation(treeGrid, r, c));
            }
        }

        return treeScores;
    }

    public Integer getMaxViewScore(List<List<Integer>> viewScores) {
        Integer numColumns = viewScores.get(0).size();
        Integer numRows = viewScores.size();
        Integer maxScore = 0;

        for (int r=0; r<numRows; r++) {
            for (int c=0; c<numColumns; c++) {
                if (viewScores.get(r).get(c) > maxScore) {
                    maxScore = viewScores.get(r).get(c);
                }
            }
        }

        return maxScore;
    }

    private Integer scoreLocation(List<List<Integer>> treeGrid, Integer row, Integer column) {
        Integer numColumns = treeGrid.get(0).size();
        Integer numRows = treeGrid.size();
        Integer totalScore = 1;
        Integer tempScore = 0;
        Integer myHeight = treeGrid.get(row).get(column);

        // Look down start at row+1
        tempScore = 0;
        for (int r=row+1; r<numRows; r++) {
            Integer currentHeight = treeGrid.get(r).get(column);
            tempScore+=1;
            if (currentHeight >= myHeight) {
                break;
            }
        }
        totalScore *= tempScore;

        // Look up start at row-1
        tempScore = 0;
        for (int r=row-1; r>=0; r--) {
            Integer currentHeight = treeGrid.get(r).get(column);
            tempScore+=1;
            if (currentHeight >= myHeight) {
                break;
            }
        }
        totalScore *= tempScore;

        // Look right start at column+1
        tempScore = 0;
        for (int c=column+1; c<numColumns; c++) {
            Integer currentHeight = treeGrid.get(row).get(c);
            tempScore += 1;
            if (currentHeight >= myHeight) {
                break;
            }
        }
        totalScore *= tempScore;

        // Look left at column-1
        tempScore = 0;
        for (int c=column-1; c>=0; c--) {
            Integer currentHeight = treeGrid.get(row).get(c);
            tempScore += 1;
            if (currentHeight >= myHeight) {
                break;
            }
        }
        totalScore *= tempScore;

        System.out.println("Score for (row,column, score): (" + row + "," + column + "," + totalScore + ")");

        return totalScore;
    }

    public Integer countVisibleTrees(List<List<Integer>> visGrid) {
        Integer numColumns = visGrid.get(0).size();
        Integer numRows = visGrid.size();
        Integer numVisTrees = 0;

        for (int r=0; r<numRows; r++) {
            for (int c=0; c<numColumns; c++) {
                numVisTrees += visGrid.get(r).get(c);
            }
        }

        return numVisTrees;
    }

    public List<List<Integer>> buildVisibleTreeGrid(List<List<Integer>> treeGrid) {
        Integer numColumns = treeGrid.get(0).size();
        Integer numRows = treeGrid.size();

        // A mirror grid which marks if a tree is visible.  1 is visible, 0 is not visible
        List<List<Integer>> visGrid = createZeroGrid(numRows, numColumns);

        outputGrid(treeGrid);
        System.out.println("\nInitial visGrid");
        outputGrid(visGrid);

        markEdgesVisible(visGrid);

        System.out.println("\nEdges Marked visGrid");
        outputGrid(visGrid);

        // Go across each row
        //   Left to right
        for (int r=0; r<numRows; r++) {
            Integer tallestTree = 0;
            for (int c=0; c<numColumns; c++) {
                if (treeGrid.get(r).get(c) > tallestTree) {
                    visGrid.get(r).set(c, 1); // Mark tree visible
                    tallestTree = treeGrid.get(r).get(c);
                }
            }
        }

        System.out.println("\nAfter left to right visGrid");
        outputGrid(visGrid);

        // Go across each row
        //   Right to Left
        for (int r=0; r<numRows; r++) {
            Integer tallestTree = 0;
            for (int c=numColumns-1; c>=0; c--) {
                if (treeGrid.get(r).get(c) > tallestTree) {
                    visGrid.get(r).set(c, 1); // Mark tree visible
                    tallestTree = treeGrid.get(r).get(c);
                }
            }
        }

        System.out.println("\nAfter right to left visGrid");
        outputGrid(visGrid);

        // Go across each column
        //   Top to bottom
        for (int c=0; c<numColumns; c++) {
            Integer tallestTree = 0;
            for (int r=0; r<numRows; r++) {
                if (treeGrid.get(r).get(c) > tallestTree) {
                    visGrid.get(r).set(c, 1); // Mark tree visible
                    tallestTree = treeGrid.get(r).get(c);
                }
            }
        }

        System.out.println("\nAfter top to bottom visGrid");
        outputGrid(visGrid);

        // Go across each column
        //   Bottom to top
        for (int c=0; c<numColumns; c++) {
            Integer tallestTree = 0;
            for (int r=numRows-1; r>=0; r--) {
                if (treeGrid.get(r).get(c) > tallestTree) {
                    visGrid.get(r).set(c, 1); // Mark tree visible
                    tallestTree = treeGrid.get(r).get(c);
                }
            }
        }

        System.out.println("\nAfter bottom to top visGrid");
        outputGrid(visGrid);

        return visGrid;
    }

    private void markEdgesVisible(List<List<Integer>> visGrid) {
        // First row and last row
        for (int c=0; c<visGrid.get(0).size(); c++) {
            visGrid.get(0).set(c, 1);
            visGrid.get(visGrid.size()-1).set(c, 1);
        }

        // First column and last column
        for (int r=0; r<visGrid.size(); r++) {
            visGrid.get(r).set(0, 1);
            visGrid.get(r).set(visGrid.get(0).size()-1, 1);
        }
    }

    public List<List<Integer>> createZeroGrid(Integer rows, Integer columns) {
        List<List<Integer>> grid = new ArrayList<>(rows);

        for (int r=0; r<rows; r++) {
            List<Integer> columnList = new ArrayList<>(columns);
            for (int i=0; i<columns; i++) {
                columnList.add(0);
            }
            grid.add(columnList);
        }

        return grid;
    }

    private void outputGrid(List<List<Integer>> grid) {
        for (List<Integer> line : grid) {
            for (Integer i : line) {
                System.out.print(i);
            }
            System.out.println("");
        }
    }
}
