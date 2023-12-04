package y2023.day2;

import com.google.common.collect.Lists;
import utils.InputReader;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * https://adventofcode.com/2023/day/2
 * --- Day 2: Cube Conundrum ---
 * You're launched high into the atmosphere! The apex of your trajectory just barely reaches the surface of a large island floating in the sky. You gently land in a fluffy pile of leaves. It's quite cold, but you don't see much snow. An Elf runs over to greet you.
 *
 * The Elf explains that you've arrived at Snow Island and apologizes for the lack of snow. He'll be happy to explain the situation, but it's a bit of a walk, so you have some time. They don't get many visitors up here; would you like to play a game in the meantime?
 *
 * As you walk, the Elf shows you a small bag and some cubes which are either red, green, or blue. Each time you play this game, he will hide a secret number of cubes of each color in the bag, and your goal is to figure out information about the number of cubes.
 *
 * To get information, once a bag has been loaded with cubes, the Elf will reach into the bag, grab a handful of random cubes, show them to you, and then put them back in the bag. He'll do this a few times per game.
 *
 * You play several games and record the information from each game (your puzzle input). Each game is listed with its ID number (like the 11 in Game 11: ...) followed by a semicolon-separated list of subsets of cubes that were revealed from the bag (like 3 red, 5 green, 4 blue).
 *
 * For example, the record of a few games might look like this:
 *
 * Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
 * Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
 * Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
 * Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
 * Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
 * In game 1, three sets of cubes are revealed from the bag (and then put back again). The first set is 3 blue cubes and 4 red cubes; the second set is 1 red cube, 2 green cubes, and 6 blue cubes; the third set is only 2 green cubes.
 *
 * The Elf would first like to know which games would have been possible if the bag contained only 12 red cubes, 13 green cubes, and 14 blue cubes?
 *
 * In the example above, games 1, 2, and 5 would have been possible if the bag had been loaded with that configuration. However, game 3 would have been impossible because at one point the Elf showed you 20 red cubes at once; similarly, game 4 would also have been impossible because the Elf showed you 15 blue cubes at once. If you add up the IDs of the games that would have been possible, you get 8.
 *
 * Determine which games would have been possible if the bag had been loaded with only 12 red cubes, 13 green cubes, and 14 blue cubes. What is the sum of the IDs of those games?
 */
public class CubeCounter {
    public static void main(String[] args) throws FileNotFoundException {
        InputReader reader = new InputReader();
        List<String> inputLines = reader.loadStringsFromFile("./src/y2023/day2/CubeGameInput.txt");
        //List<String> inputLines = reader.loadStringsFromFile("./src/y2023/day2/SampleBagInput.txt");

        CubeCounter counter = new CubeCounter();

        // [GameID => ["color" => maxCubesSeenInGame]]
        Map<Integer, Map<String, Integer>> maxColorInGame = counter.getMaxColorsInGames(inputLines);

        List<Integer> validGameIds = counter.getIdsOfValidGamesPart1(maxColorInGame);
        int sumValidGameIds = validGameIds.stream().mapToInt(Integer::valueOf).sum();

        System.out.println("Part 1 sum of valid games: " + sumValidGameIds);

        int sumPowerCube = counter.getSumCubePowerNumber(maxColorInGame);
        System.out.println("Part 2 sum of maxes: " + sumPowerCube);
    }

    // Part 2, multiply the max colors in each game
    private int getSumCubePowerNumber(Map<Integer, Map<String, Integer>> maxColorInGame) {
        int sumCubePower = 0;
        for (Integer gameId : maxColorInGame.keySet()) {
            Map<String, Integer> gameColor = maxColorInGame.get(gameId);
            int gamePower = gameColor.get("red") * gameColor.get("blue") * gameColor.get("green");
            System.out.println("Game ID: " + gameId + ", Power: " + gamePower);
            sumCubePower += gamePower;
        }

        return sumCubePower;
    }

    // Part 1 hardcoded max values of only 12 red cubes, 13 green cubes, and 14 blue cubes
    private List<Integer> getIdsOfValidGamesPart1(Map<Integer, Map<String, Integer>> maxColorInGames) {
        List<Integer> validGameIds = Lists.newArrayList();

        for (Integer gameId : maxColorInGames.keySet()) {
            Map<String, Integer> game = maxColorInGames.get(gameId);
            if (game.get("red") <= 12 && game.get("blue") <= 14 && game.get("green") <= 13) {
                validGameIds.add(gameId);
                System.out.println("Valid game: " + gameId);
            }
        }

        return validGameIds;
    }

    private Map<Integer, Map<String, Integer>> getMaxColorsInGames(List<String> gameInputs) {
        Map<Integer, Map<String, Integer>> maxColorInGame = new HashMap<>();

        for (String gameInput : gameInputs) {
            String gameId = new Scanner(gameInput).findInLine("\\d+");
            System.out.println("For gameID: " + gameId);

            maxColorInGame.put(Integer.valueOf(gameId), getMaxColorSingleGame(gameInput));
        }


        return maxColorInGame;
    }

    // "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
    private Map<String, Integer> getMaxColorSingleGame(String input) {
        // We only care about the max pull of a color ever seen
        Map<String, Integer> maxColors = new HashMap<>();
        maxColors.put("red", 0);
        maxColors.put("green", 0);
        maxColors.put("blue", 0);

        // Remove the gameId, in part 1 we don't care about the different between ; and ,
        String allPulls = input.split(":")[1].replaceAll(";", ",");

        // Array of individual pulls ("8 green")
        List<String> pulls = Arrays.stream(allPulls.split(",")).toList();
        String regex = "\\d+ red|blue|green";

        for (String pull : pulls) {
            Integer firstDigit = Integer.parseInt(new Scanner(pull).findInLine("\\d+"));
            String color = new Scanner(pull).findInLine("red|blue|green");

            System.out.println("Found: " + firstDigit + " " + color);

            if (firstDigit > maxColors.get(color)) {
                maxColors.put(color, firstDigit);
            }
        }

        System.out.println(maxColors);
        return maxColors;
    }
}
