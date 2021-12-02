package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
}
