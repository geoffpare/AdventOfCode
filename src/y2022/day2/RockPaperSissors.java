package y2022.day2;

import com.google.common.collect.Lists;
import utils.InputReader;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * --- Day 2: Rock Paper Scissors ---
 * The Elves begin to set up camp on the beach. To decide whose tent gets to be closest to the snack storage, a giant Rock Paper Scissors tournament is already in progress.
 *
 * Rock Paper Scissors is a game between two players. Each game contains many rounds; in each round, the players each simultaneously choose one of Rock, Paper, or Scissors using a hand shape. Then, a winner for that round is selected: Rock defeats Scissors, Scissors defeats Paper, and Paper defeats Rock. If both players choose the same shape, the round instead ends in a draw.
 *
 * Appreciative of your help yesterday, one Elf gives you an encrypted strategy guide (your puzzle input) that they say will be sure to help you win. "The first column is what your opponent is going to play: A for Rock, B for Paper, and C for Scissors. The second column--" Suddenly, the Elf is called away to help with someone's tent.
 *
 * The second column, you reason, must be what you should play in response: X for Rock, Y for Paper, and Z for Scissors. Winning every time would be suspicious, so the responses must have been carefully chosen.
 *
 * The winner of the whole tournament is the player with the highest score. Your total score is the sum of your scores for each round. The score for a single round is the score for the shape you selected (1 for Rock, 2 for Paper, and 3 for Scissors) plus the score for the outcome of the round (0 if you lost, 3 if the round was a draw, and 6 if you won).
 *
 * Since you can't be sure if the Elf is trying to help you or trick you, you should calculate the score you would get if you were to follow the strategy guide.
 *
 * For example, suppose you were given the following strategy guide:
 *
 * A Y
 * B X
 * C Z
 *
 * This strategy guide predicts and recommends the following:
 *
 * In the first round, your opponent will choose Rock (A), and you should choose Paper (Y). This ends in a win for you with a score of 8 (2 because you chose Paper + 6 because you won).
 * In the second round, your opponent will choose Paper (B), and you should choose Rock (X). This ends in a loss for you with a score of 1 (1 + 0).
 * The third round is a draw with both players choosing Scissors, giving you a score of 3 + 3 = 6.
 * In this example, if you were to follow the strategy guide, you would get a total score of 15 (8 + 1 + 6).
 *
 * What would your total score be if everything goes exactly according to your strategy guide?
 *
 * --- Part Two ---
 * The Elf finishes helping with the tent and sneaks back over to you. "Anyway, the second column says how the round needs to end: X means you need to lose, Y means you need to end the round in a draw, and Z means you need to win. Good luck!"
 *
 * The total score is still calculated in the same way, but now you need to figure out what shape to choose so the round ends as indicated. The example above now goes like this:
 *
 * In the first round, your opponent will choose Rock (A), and you need the round to end in a draw (Y), so you also choose Rock. This gives you a score of 1 + 3 = 4.
 * In the second round, your opponent will choose Paper (B), and you choose Rock so you lose (X) with a score of 1 + 0 = 1.
 * In the third round, you will defeat your opponent's Scissors with Rock for a score of 1 + 6 = 7.
 * Now that you're correctly decrypting the ultra top secret strategy guide, you would get a total score of 12.
 *
 * Following the Elf's instructions for the second column, what would your total score be if everything goes exactly according to your strategy guide?
 */
public record RockPaperSissors() {
    private static Map<String, Integer> roundScore = new HashMap<String, Integer>() {{
        // You win, 6 points for win + what you threw
        put("A Y", 6 + 2);
        put("B Z", 6 + 3);
        put("C X", 6 + 1);

        // Draw, 3 points for tie + what you threw
        put("A X", 3 + 1);
        put("B Y", 3 + 2);
        put("C Z", 3 + 3);

        // You lose
        put("A Z", 0 + 3);
        put("B X", 0 + 1);
        put("C Y", 0 + 2);
    }};

    // Map of coded string => how the round needs to be played for part 2
    // We can then plug in what we should play into the scoring map above
    private static Map<String, String> codeToPlay = new HashMap<String, String>() {{
        // You need to win
        put("A Z", "A Y");
        put("B Z", "B Z");
        put("C Z", "C X");

        // You need to draw
        put("A Y", "A X");
        put("B Y", "B Y");
        put("C Y", "C Z");

        // You need to lose
        put("A X", "A Z");
        put("B X", "B X");
        put("C X", "C Y");
    }};

    public static void main(String[] args) throws FileNotFoundException {
        RockPaperSissors rps = new RockPaperSissors();
        rps.doPart1();
        rps.doPart2();
    }

    public void doPart1() throws FileNotFoundException {
        InputReader input = new InputReader();
        List<String> rounds = input.loadStringsFromFile("./src/y2022/day2/RPSInput.txt");

        Integer result = scoreSeries(rounds);
        System.out.println("Part 1 Score: " + result);
    }

    public void doPart2() throws FileNotFoundException {
        InputReader input = new InputReader();
        List<String> codedRounds = input.loadStringsFromFile("./src/y2022/day2/RPSInput.txt");
        List<String> decodedRounds = decodeRounds(codedRounds);

        Integer result = scoreSeries(decodedRounds);
        System.out.println("Part 2 Score: " + result);
    }

    private List<String> decodeRounds(List<String> codedRounds) {
        List<String> rounds = Lists.newArrayList();

        for (String codedRound : codedRounds) {
            rounds.add(codeToPlay.get(codedRound));
        }

        return rounds;
    }

    private Integer scoreSeries(List<String> rounds) {
        Integer totalScore = 0;

        for (String round : rounds) {
            Integer score = scoreRound(round);
            System.out.println("Round: " + round + " Score: " + score);
            totalScore += scoreRound(round);
        }

        return totalScore;
    }

    private Integer scoreRound(String round) {
        return roundScore.get(round);
    }
}
