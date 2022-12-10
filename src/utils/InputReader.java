package utils;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class InputReader {
    // Expecting 1 Integer per line, no sanitization
    public List<Integer> loadIntegersFromFile(String fileName) throws FileNotFoundException {
        List<Integer> integers = new ArrayList<Integer>();

        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        while(scanner.hasNextInt())
        {
            integers.add(scanner.nextInt());
        }

        return integers;
    }

    // Expecting 1 String per line, no sanitization
    public List<String> loadStringsFromFile(String fileName) throws FileNotFoundException {
        List<String> strings = new ArrayList<String>();

        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        while(scanner.hasNextLine())
        {
            strings.add(scanner.nextLine());
        }

        return strings;
    }

    // Return just the first line
    public String loadStringFromFile(String fileName) throws FileNotFoundException {
        return loadStringsFromFile(fileName).get(0);
    }

    // Expecting 1 line of comma seperated integers
    public List<Integer> loadCommaSeperatedIntegersFromFile(String fileName) throws FileNotFoundException {
        String numString = loadStringsFromFile(fileName).get(0);

        return Arrays.stream(numString.split(",")).map((s) -> Integer.parseInt(s)).collect(Collectors.toList());
    }

    // Example below would result in a 2d array that's 2x2
    // 12
    // 23
    public List<List<Integer>> readIntegerGridFromFile(String fileName) throws FileNotFoundException {
        List<List<Integer>> grid = new ArrayList<>();
        List<String> gridLines = loadStringsFromFile(fileName);

        for (String line : gridLines) {
            List<Integer> li = Lists.newArrayList();
            for (String num : line.split("")) {
                li.add(Integer.parseInt(num));
            }
            grid.add(li);
        }

        return grid;
    }

}
