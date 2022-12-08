package y2022.day7;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import utils.InputReader;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * --- Day 7: No Space Left On Device ---
 * You can hear birds chirping and raindrops hitting leaves as the expedition proceeds. Occasionally, you can even hear much louder sounds in the distance; how big do the animals get out here, anyway?
 *
 * The device the Elves gave you has problems with more than just its communication system. You try to run a system update:
 *
 * $ system-update --please --pretty-please-with-sugar-on-top
 * Error: No space left on device
 * Perhaps you can delete some files to make space for the update?
 *
 * You browse around the filesystem to assess the situation and save the resulting terminal output (your puzzle input). For example:
 *
 * $ cd /
 * $ ls
 * dir a
 * 14848514 b.txt
 * 8504156 c.dat
 * dir d
 * $ cd a
 * $ ls
 * dir e
 * 29116 f
 * 2557 g
 * 62596 h.lst
 * $ cd e
 * $ ls
 * 584 i
 * $ cd ..
 * $ cd ..
 * $ cd d
 * $ ls
 * 4060174 j
 * 8033020 d.log
 * 5626152 d.ext
 * 7214296 k
 * The filesystem consists of a tree of files (plain data) and directories (which can contain other directories or files). The outermost directory is called /. You can navigate around the filesystem, moving into or out of directories and listing the contents of the directory you're currently in.
 *
 * Within the terminal output, lines that begin with $ are commands you executed, very much like some modern computers:
 *
 * cd means change directory. This changes which directory is the current directory, but the specific result depends on the argument:
 * cd x moves in one level: it looks in the current directory for the directory named x and makes it the current directory.
 * cd .. moves out one level: it finds the directory that contains the current directory, then makes that directory the current directory.
 * cd / switches the current directory to the outermost directory, /.
 * ls means list. It prints out all of the files and directories immediately contained by the current directory:
 * 123 abc means that the current directory contains a file named abc with size 123.
 * dir xyz means that the current directory contains a directory named xyz.
 * Given the commands and output in the example above, you can determine that the filesystem looks visually like this:
 *
 * - / (dir)
 *   - a (dir)
 *     - e (dir)
 *       - i (file, size=584)
 *     - f (file, size=29116)
 *     - g (file, size=2557)
 *     - h.lst (file, size=62596)
 *   - b.txt (file, size=14848514)
 *   - c.dat (file, size=8504156)
 *   - d (dir)
 *     - j (file, size=4060174)
 *     - d.log (file, size=8033020)
 *     - d.ext (file, size=5626152)
 *     - k (file, size=7214296)
 * Here, there are four directories: / (the outermost directory), a and d (which are in /), and e (which is in a). These directories also contain files of various sizes.
 *
 * Since the disk is full, your first step should probably be to find directories that are good candidates for deletion. To do this, you need to determine the total size of each directory. The total size of a directory is the sum of the sizes of the files it contains, directly or indirectly. (Directories themselves do not count as having any intrinsic size.)
 *
 * The total sizes of the directories above can be found as follows:
 *
 * The total size of directory e is 584 because it contains a single file i of size 584 and no other directories.
 * The directory a has total size 94853 because it contains files f (size 29116), g (size 2557), and h.lst (size 62596), plus file i indirectly (a contains e which contains i).
 * Directory d has total size 24933642.
 * As the outermost directory, / contains every file. Its total size is 48381165, the sum of the size of every file.
 * To begin, find all of the directories with a total size of at most 100000, then calculate the sum of their total sizes. In the example above, these directories are a and e; the sum of their total sizes is 95437 (94853 + 584). (As in this example, this process can count files more than once!)
 *
 * Find all of the directories with a total size of at most 100000. What is the sum of the total sizes of those directories?
 *
 */
public class ConsoleParser {
    public static void main(String[] args) throws FileNotFoundException {
        InputReader input = new InputReader();
        List<String> consoleLines = input.loadStringsFromFile("./src/y2022/day7/ConsoleCommands.txt");

        ConsoleParser consoleParser = new ConsoleParser();
        Map<String, Long> dirSizes = consoleParser.getDirectorySizes(consoleLines);

        // Part 1
        Long runningSize = 0L;
        for (Long size : dirSizes.values()) {
            if (size <= 100000) {
                runningSize += size;
            }
        }
        System.out.println("Part 1: Sum of sizes < 100000 = " + runningSize);

        // Part 2
        Long totalUsedSpace = dirSizes.get("/");
        Long freeSpace = 70_000_000 - totalUsedSpace;
        Long needToFree = Math.abs(freeSpace - 30_000_000);

        // Find the smallest dir we can delete which will leave us with enough free space
        Long smallestDir = Long.MAX_VALUE;
        for (Long size : dirSizes.values()) {
            if (size >= needToFree && size < smallestDir) {
                smallestDir = size;
            }
        }

        System.out.println("Part 2: Total used " + totalUsedSpace);
        System.out.println("Part 2: needToFree " + needToFree);
        System.out.println("Part 2: size of smallest we can delete: " + smallestDir);
    }

    /*
     * I think the easiest way to do this is just have a Map of Directory -> Total Size as we encounter every file.
     * Every file will update the running total for each directory in the hirarchy which contains it.
     *
     * A more complete solution would be to build the actual directory/file Tree, then do an entire tree traversal
     * to get the directory sizes, but I don't think that's needed for part 1, and hopefully not needed for part 2...
     */
    public Map<String, Long> getDirectorySizes(List<String> consoleLines) {
        // We will represent directories by just concatenating the dir tree
        // So directory /a/b will just be /ab (/ representing representing the root directory, which the problems
        // seems to want us to track as well
        Map<String, Long> directorySizes = Maps.newHashMap();

        // We'll use this to track our directory traversal
        Stack<String> dirTree = new Stack<String>();

        // We know the first command is $ cd /, but parse it anyways
        for (String consoleLine : consoleLines) {
            // Handle directory traversal commands
            if (consoleLine.equals("$ cd ..")) {
                dirTree.pop();
            } else if (consoleLine.equals("$ cd /")) {
                dirTree.removeAllElements();
                dirTree.push("/");
            } else if (consoleLine.startsWith("$ cd")) {
                dirTree.push(consoleLine.split(" ")[2]); // The directory
            }

            // Ignore these lines as they don't give us file information or change the working directory
            else if (consoleLine.startsWith("$ ls")) {
                continue;
            } else if (consoleLine.startsWith("dir")) {
                continue;
            }

            else {
                // Everything else should be a file size followed by file name (which we don't care about the name)
                Long fileSize = Long.parseLong(consoleLine.split(" ")[0]);
                List<String> allDirs = getAllDirs(dirTree);

                for (String dir : allDirs) {
                    directorySizes.put(dir, directorySizes.getOrDefault(dir, 0L) + fileSize);
                }
            }
        }

        System.out.println(directorySizes);

        return directorySizes;
    }

    // Return all directory paths in the hierarchy
    // So /a/b/c returns ["/", "/a", "/ab", "/abc"]
    private List<String> getAllDirs(Stack<String> dirTree) {
        List<String> allDirs = Lists.newArrayList();

        for (int i=0; i<dirTree.size(); i++) {
            allDirs.add(dirTree.stream().limit(i+1).collect(Collectors.joining()));
        }

        System.out.println("All dirs: " + allDirs);

        return allDirs;
    }
}
