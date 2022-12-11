package y2022.day10;

import com.google.common.collect.Lists;
import utils.InputReader;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * --- Day 10: Cathode-Ray Tube ---
 * You avoid the ropes, plunge into the river, and swim to shore.
 *
 * The Elves yell something about meeting back up with them upriver, but the river is too loud to tell exactly what they're saying. They finish crossing the bridge and disappear from view.
 *
 * Situations like this must be why the Elves prioritized getting the communication system on your handheld device working. You pull it out of your pack, but the amount of water slowly draining from a big crack in its screen tells you it probably won't be of much immediate use.
 *
 * Unless, that is, you can design a replacement for the device's video system! It seems to be some kind of cathode-ray tube screen and simple CPU that are both driven by a precise clock circuit. The clock circuit ticks at a constant rate; each tick is called a cycle.
 *
 * Start by figuring out the signal being sent by the CPU. The CPU has a single register, X, which starts with the value 1. It supports only two instructions:
 *
 * addx V takes two cycles to complete. After two cycles, the X register is increased by the value V. (V can be negative.)
 * noop takes one cycle to complete. It has no other effect.
 * The CPU uses these instructions in a program (your puzzle input) to, somehow, tell the screen what to draw.
 *
 * Consider the following small program:
 *
 * noop
 * addx 3
 * addx -5
 * Execution of this program proceeds as follows:
 *
 * At the start of the first cycle, the noop instruction begins execution. During the first cycle, X is 1. After the first cycle, the noop instruction finishes execution, doing nothing.
 * At the start of the second cycle, the addx 3 instruction begins execution. During the second cycle, X is still 1.
 * During the third cycle, X is still 1. After the third cycle, the addx 3 instruction finishes execution, setting X to 4.
 * At the start of the fourth cycle, the addx -5 instruction begins execution. During the fourth cycle, X is still 4.
 * During the fifth cycle, X is still 4. After the fifth cycle, the addx -5 instruction finishes execution, setting X to -1.
 * Maybe you can learn something by looking at the value of the X register throughout execution. For now, consider the signal strength (the cycle number multiplied by the value of the X register) during the 20th cycle and every 40 cycles after that (that is, during the 20th, 60th, 100th, 140th, 180th, and 220th cycles).
 *
 * The interesting signal strengths can be determined as follows:
 *
 * During the 20th cycle, register X has the value 21, so the signal strength is 20 * 21 = 420. (The 20th cycle occurs in the middle of the second addx -1, so the value of register X is the starting value, 1, plus all of the other addx values up to that point: 1 + 15 - 11 + 6 - 3 + 5 - 1 - 8 + 13 + 4 = 21.)
 * During the 60th cycle, register X has the value 19, so the signal strength is 60 * 19 = 1140.
 * During the 100th cycle, register X has the value 18, so the signal strength is 100 * 18 = 1800.
 * During the 140th cycle, register X has the value 21, so the signal strength is 140 * 21 = 2940.
 * During the 180th cycle, register X has the value 16, so the signal strength is 180 * 16 = 2880.
 * During the 220th cycle, register X has the value 18, so the signal strength is 220 * 18 = 3960.
 * The sum of these signal strengths is 13140.
 *
 * Find the signal strength during the 20th, 60th, 100th, 140th, 180th, and 220th cycles. What is the sum of these six signal strengths?
 *
 * --- Part Two ---
 * It seems like the X register controls the horizontal position of a sprite. Specifically, the sprite is 3 pixels wide, and the X register sets the horizontal position of the middle of that sprite. (In this system, there is no such thing as "vertical position": if the sprite's horizontal position puts its pixels where the CRT is currently drawing, then those pixels will be drawn.)
 *
 * You count the pixels on the CRT: 40 wide and 6 high. This CRT screen draws the top row of pixels left-to-right, then the row below that, and so on. The left-most pixel in each row is in position 0, and the right-most pixel in each row is in position 39.
 *
 * Like the CPU, the CRT is tied closely to the clock circuit: the CRT draws a single pixel during each cycle. Representing each pixel of the screen as a #, here are the cycles during which the first and last pixel in each row are drawn:
 *
 * Cycle   1 -> ######################################## <- Cycle  40
 * Cycle  41 -> ######################################## <- Cycle  80
 * Cycle  81 -> ######################################## <- Cycle 120
 * Cycle 121 -> ######################################## <- Cycle 160
 * Cycle 161 -> ######################################## <- Cycle 200
 * Cycle 201 -> ######################################## <- Cycle 240
 * So, by carefully timing the CPU instructions and the CRT drawing operations, you should be able to determine whether the sprite is visible the instant each pixel is drawn. If the sprite is positioned such that one of its three pixels is the pixel currently being drawn, the screen produces a lit pixel (#); otherwise, the screen leaves the pixel dark (.).
 *
 * The first few pixels from the larger example above are drawn as follows:
 *
 * Allowing the program to run to completion causes the CRT to produce the following image:
 *
 * ##..##..##..##..##..##..##..##..##..##..
 * ###...###...###...###...###...###...###.
 * ####....####....####....####....####....
 * #####.....#####.....#####.....#####.....
 * ######......######......######......####
 * #######.......#######.......#######.....
 * Render the image given by your program. What eight capital letters appear on your CRT?
 */
public class CPU {
    List<Integer> valueDuringCycle = Lists.newArrayList();

    public static void main(String[] args) throws FileNotFoundException {
        InputReader inputReader = new InputReader();
        List<String> instructions = inputReader.loadStringsFromFile("./src/y2022/day10/Instructions.txt");
        //List<String> instructions = inputReader.loadStringsFromFile("./src/y2022/day10/SampleInstructions.txt");

        CPU cpu = new CPU(instructions);
        Integer signal = cpu.estimateSignalStrength();
        System.out.println("Sum of signal strengths: " + signal);

        cpu.drawCRT();
    }

    public CPU(List<String> instructions) {
        // X starts at 1, so after cycle 1, the value is 1
        valueDuringCycle.add(1);
        valueDuringCycle.add(1);

        int lastValue = 1;

        for (String instruction : instructions) {
            String command = instruction.split(" ")[0];
            if (command.equals("noop")) {
                valueDuringCycle.add(lastValue);
            } else if (command.equals("addx")) {
                int amount = Integer.parseInt(instruction.split(" ")[1]);
                valueDuringCycle.add(lastValue);
                lastValue += amount;
                valueDuringCycle.add(lastValue);
            }
        }

        // Draws RFKZCPEF
    }

    public void drawCRT() {
        for (int cycle=1; cycle<=240; cycle++) {
            int column = (cycle - 1) % 40;
            int register = valueDuringCycle.get(cycle);

            if (Math.abs(register-column) <= 1) {
                System.out.print("#");
            } else {
                System.out.print(".");
            }

            if (cycle % 40 == 0) {
                System.out.println("");
            }
        }
    }

    public Integer estimateSignalStrength() {
        // Building a new list here just as a debugging aid
        List<Integer> signalSamples = Lists.newArrayList();
        signalSamples.add(valueDuringCycle.get(20)*20);
        signalSamples.add(valueDuringCycle.get(60)*60);
        signalSamples.add(valueDuringCycle.get(100)*100);
        signalSamples.add(valueDuringCycle.get(140)*140);
        signalSamples.add(valueDuringCycle.get(180)*180);
        signalSamples.add(valueDuringCycle.get(220)*220);

        System.out.println("Samples: " + signalSamples);

        return signalSamples.stream().reduce(0, Integer::sum);
    }

}
