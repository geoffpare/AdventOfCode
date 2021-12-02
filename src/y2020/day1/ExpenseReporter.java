package y2020.day1;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Before you leave, the Elves in accounting just need you to fix your expense report (your puzzle input); apparently, something isn't quite adding up.
 *
 * Specifically, they need you to find the two entries that sum to 2020 and then multiply those two numbers together.
 *
 * For example, suppose your expense report contained the following:
 *
 * 1721
 * 979
 * 366
 * 299
 * 675
 * 1456
 * In this list, the two entries that sum to 2020 are 1721 and 299. Multiplying them together produces 1721 * 299 = 514579, so the correct answer is 514579.
 *
 * Of course, your expense report is much larger. Find the two entries that sum to 2020; what do you get if you multiply them together?
 */
public class ExpenseReporter {
    private static Integer EXPENSE_MATCH = 2020; // Magic number that expenses must add up to

    // Dummy test input
    private List<Integer> getExampleInput1() {
        return Lists.newArrayList(1721, 979, 366, 299, 675, 1456);
    }

    private List<Integer> loadExpensesFromFile() throws FileNotFoundException {
        List expenses = new ArrayList<Integer>();

        File file = new File("./src/y2020/day1/ExpenseReports.txt");
        Scanner scanner = new Scanner(file);

        while(scanner.hasNextInt())
        {
            expenses.add(scanner.nextInt());
        }

        return expenses;
    }

    /*
     * I dislike the streams version of this and would probably just unravel the loops to make both what the function is doing
     * and the runtime more obvious.  O(n**2)
     * NOTE: This will print duplicates (the match in both directions), but the solution doesn't care about that.
     */
    private void printTwo(List<Integer> expenses) {
        // Iterate over each expense, for each, search the list for an entry to sum to 2020.  If found, multiply and print our result
        expenses.stream().forEach(expense -> {
            expenses.stream()
                    .filter(otherExpense -> (otherExpense + expense) == EXPENSE_MATCH)
                    .forEach(otherExpense -> System.out.println("Found Two: " + (expense * otherExpense)));
        });
    }

    // Screw it, triple loop, following what I think I should have just done with the find2 anyways.  Clear O(n**3) with no waste)
    // And lol, first execution did have an "off by one letter" typo bug, hence why not to do this....
    private void printThree(List<Integer> expenses) {
        for (int i=0; i<expenses.size(); i++) {
            for (int j=i+1; j<expenses.size(); j++) {
                for (int k=j+1; k<expenses.size(); k++) {
                    if ( (expenses.get(i) + expenses.get(j) + expenses.get(k)) == EXPENSE_MATCH ) {
                        System.out.println(String.format("Found 3: %d, %d, %d, %d",
                                expenses.get(i), expenses.get(j), expenses.get(k), (expenses.get(i) * expenses.get(j) * expenses.get(k))));
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        ExpenseReporter reporter = new ExpenseReporter();
        //List<Integer> expenses = reporter.getExampleInput1();
        List<Integer> expenses = reporter.loadExpensesFromFile();

        reporter.printTwo(expenses);
        reporter.printThree(expenses);

        System.out.println("All done day 1!");
    }
}
