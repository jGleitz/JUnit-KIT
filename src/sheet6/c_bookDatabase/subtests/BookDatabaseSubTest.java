package sheet6.c_bookDatabase.subtests;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import test.Input;
import test.InteractiveConsoleTest;

/**
 * Contains all helper methods and fields to test Sheet 6 Task C.
 *
 * @author Joshua Gleitze
 * @version 1.0
 * @since 31.01.2015
 */
public class BookDatabaseSubTest extends InteractiveConsoleTest {
    /**
     * You can put a test input file in here
     */
    protected String[] file;
    /**
     * You can put a one lined test input file in here
     */
    protected String line;

    /*
     * PREDEFINED BOOK INPUT FILES
     */
    /**
     * The input file that was given as valid on the task sheet.
     */
    protected static final String[] taskSheetInputFile = {
        "title=Java_ist_auch_eine_Insel,creator=GalileoComputing",
        "tITle=Grundkurs_Programmieren_iN_JAVA,year=2007",
        "Creator=Ralf_Reussner,year=2006"
    };

    /**
     * A very simple input file, intended not to produce a parsing error (to focus on other things). Therefore it only
     * contains lower case characters between a and z and all attributes.
     */
    protected static final String[] simpleValidFile = {
        "title=musterbuch,creator=mustermann,year=2000"
    };

    /*
     * HELPER METHODS
     */

    @Override
    protected String consoleMessage(String[] commands, String[] commandLineArguments) {
        String result = "";
        result += "We ran a session on your interactive console" + getArguments(commandLineArguments)
                + Input.fileMessage(commandLineArguments[1]) + "running the commands \n\n" + joinOnePerLine(commands)
                + "\n\n but got unexpected output:\n";
        return result;
    }

    @Override
    protected String getArguments(String[] commandLineArguments) {
        String result = "";
        if (commandLineArguments.length == 2) {
            result = "\n that has been called with the command line arguments [" + commandLineArguments[0] + ", ";
            result += (Input.isFile(commandLineArguments[1])) ? "textFile" : commandLineArguments[1];
            result += "]";
        }
        return result;
    }

    protected List<String> shuffleCase(List<String> lines) {
        List<String> shuffled = new LinkedList<>();
        for (String line : lines) {
            shuffled.add(shuffleCaseLine(line));
        }
        return shuffled;
    }

    protected String[] shuffleCase(String[] lines) {
        String[] shuffled = new String[lines.length];
        for (int i = 0; i < lines.length; i++) {
            shuffled[i] = shuffleCaseLine(lines[i]);
        }
        return shuffled;
    }

    private String shuffleCaseLine(String line) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (char c : line.toCharArray()) {
            builder.append((random.nextInt(2) != 0) ? Character.toUpperCase(c) : Character.toLowerCase(c));
        }
        return builder.toString();
    }

    protected String shuffleLine(String line) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (char c : line.toCharArray()) {
            while (random.nextInt(2) != 0) {
                builder.append(" ");
            }
            builder.append((random.nextInt(2) != 0) ? Character.toUpperCase(c) : Character.toLowerCase(c));
        }
        while (random.nextInt(2) != 0) {
            builder.append(" ");
        }
        return builder.toString();
    }

    protected String[] shuffle(String[] lines) {
        String[] shuffled = new String[lines.length];
        for (int i = 0; i < lines.length; i++) {
            shuffled[i] = shuffleLine(lines[i]);
        }
        return shuffled;
    }

}
