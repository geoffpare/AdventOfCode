package y2023.day1;

import utils.InputReader;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * --- Day 1: Trebuchet?! ---
 * Something is wrong with global snow production, and you've been selected to take a look. The Elves have even given you a map; on it, they've used stars to mark the top fifty locations that are likely to be having problems.
 * <p>
 * You've been doing this long enough to know that to restore snow operations, you need to check all fifty stars by December 25th.
 * <p>
 * Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 * <p>
 * You try to ask why they can't just use a weather machine ("not powerful enough") and where they're even sending you ("the sky") and why your map looks mostly blank ("you sure ask a lot of questions") and hang on did you just say the sky ("of course, where do you think snow comes from") when you realize that the Elves are already loading you into a trebuchet ("please hold still, we need to strap you in").
 * <p>
 * As they're making the final adjustments, they discover that their calibration document (your puzzle input) has been amended by a very young Elf who was apparently just excited to show off her art skills. Consequently, the Elves are having trouble reading the values on the document.
 * <p>
 * The newly-improved calibration document consists of lines of text; each line originally contained a specific calibration value that the Elves now need to recover. On each line, the calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number.
 * <p>
 * For example:
 * <p>
 * 1abc2
 * pqr3stu8vwx
 * a1b2c3d4e5f
 * treb7uchet
 * In this example, the calibration values of these four lines are 12, 38, 15, and 77. Adding these together produces 142.
 * <p>
 * Consider your entire calibration document. What is the sum of all of the calibration values?
 *
 * --- Part Two ---
 * Your calculation isn't quite right. It looks like some of the digits are actually spelled out with letters: one, two, three, four, five, six, seven, eight, and nine also count as valid "digits".
 *
 * Equipped with this new information, you now need to find the real first and last digit on each line. For example:
 *
 * two1nine
 * eightwothree
 * abcone2threexyz
 * xtwone3four
 * 4nineeightseven2
 * zoneight234
 * 7pqrstsixteen
 * In this example, the calibration values are 29, 83, 13, 24, 42, 14, and 76. Adding these together produces 281.
 *
 * What is the sum of all of the calibration values?
 */
public class Calibrator {
    private static final Map<String, String> wordToDigit = new HashMap<String, String>();
    static {
        // wordToDigit.put("zero", "0");  Zero isn't explicitly listed as allowed
        wordToDigit.put("one", "1");
        wordToDigit.put("two", "2");
        wordToDigit.put("three", "3");
        wordToDigit.put("four", "4");
        wordToDigit.put("five", "5");
        wordToDigit.put("six", "6");
        wordToDigit.put("seven", "7");
        wordToDigit.put("eight", "8");
        wordToDigit.put("nine", "9");
    }

    public static void main(String[] args) throws FileNotFoundException {

        InputReader reader = new InputReader();
        //List<String> inputLines = reader.loadStringsFromFile("./src/y2023/day1/PartTwoSampleCalValues.txt");
        List<String> inputLines = reader.loadStringsFromFile("./src/y2023/day1/CalibrationValues.txt");

        Calibrator cal = new Calibrator();

        // Part 1
        int sumCalValues = 0;
        for (String line : inputLines) {
            String cValue = cal.getDigitBasedValue(line);
            sumCalValues += Integer.valueOf(cValue);
        }

        System.out.println("Part 1 value: " + sumCalValues);


        // Part 2
        int sumDigitCalValues = 0;
        for (String line : inputLines) {
            String cValue = cal.getCalibrationValue(line);
            sumDigitCalValues += Integer.valueOf(cValue);
        }

        System.out.println("Part 2 value: " + sumDigitCalValues);
    }

    // Finds the first digit, the last digit, combines them together and returns that combined number
    private String getDigitBasedValue(String inputLine) {
        String firstDigit = new Scanner(inputLine).findInLine("\\d");
        String lastDigit = new Scanner(new StringBuilder(inputLine).reverse().toString()).findInLine("\\d");
        String cValue = firstDigit + lastDigit;

        System.out.println("Found cvalue: " + cValue);
        return cValue;
    }

    // Finds the first digit, the last digit, combines them together and returns that combined number
    // Unlike the above method, this allows for english words to be "digits" based on the part 2 definition
    private String getCalibrationValue(String inputLine) {
        String wordRegex = "one|two|three|four|five|six|seven|eight|nine|\\d";
        String firstDigit = new Scanner(inputLine).findInLine(wordRegex);

        // Reversing doesn't work here because the words get reversed....
        // Search the string beginning to end for matches, stopping at the last match
        // Relying on just matcher.find doesn't work for overlapping words, like "twone",
        // if it occurs as the last match in the line, hence running it over and over from the next character
        Pattern stringPattern = Pattern.compile(wordRegex);
        Matcher matcher = stringPattern.matcher(inputLine);
        String lastDigit = null;
        for (int i=0; i<inputLine.length(); i++) {
            if (matcher.find(i)) {
                lastDigit = matcher.group();
            }
        }

        String cValue = convertWordToDigit(firstDigit) + convertWordToDigit(lastDigit);

        System.out.println("Found first: " + firstDigit + ", last: " + lastDigit + ", cvalue: " + cValue);
        return cValue;
    }

    private String convertWordToDigit(String word) {
        if (wordToDigit.containsKey(word)) {
            return wordToDigit.get(word);
        }
        // It was already a digit rather than a word
        return word;
    }

}
