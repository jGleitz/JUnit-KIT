package sheet6.c_bookDatabase.subtests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import test.InteractiveConsoleTest;

/**
 * Contains all helper methods and fields to test Sheet 6 Task C.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 31.01.2015
 */
public class BookDatabaseSubTest extends InteractiveConsoleTest {
    private static HashMap<String, String[]> filesMap = new HashMap<>();
    private static HashMap<String[], String> reverseFileMap = new HashMap<>();
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
    protected String consoleMessage(String commands, String[] commandLineArguments) {
        String result = "";
        result += "We ran a session on your interactive console" + getArguments(commandLineArguments)
                + fileMessage(commandLineArguments) + "running the commands \n\n" + commands
                + "\n\n but got unexpected output:\n";
        return result;
    }

    @Override
    protected String getArguments(String[] commandLineArguments) {
        String result = "";
        if (commandLineArguments.length == 2) {
            result = "\n that has been called with the command line arguments [" + commandLineArguments[0] + ", ";
            result += (commandLineArguments[1].contains(".txt")) ? "textFile" : commandLineArguments[1];
            result += "]";
        }
        return result;
    }

    /**
     * Deletes all files that were created during the test run.
     */
    /*
     * Disabled in favor of using file.deleteOnExit(). Hopefully, this will fix bug #11 public static void
     * removeAllOpenedFiles() { for (String fileName : filesMap.keySet()) { new File(fileName).delete(); } }
     */

    /**
     * A message giving information about the input file used in a test.
     * 
     * @param commandLineArguments
     *            The command line arguments the main method was called with during the test. The file message will read
     *            the file name in the second argument and output the contents of the file its pointing to.
     * @return A text representing the input file
     */
    protected static String fileMessage(String[] commandLineArguments) {
        String result = "";
        if (commandLineArguments.length > 1 && filesMap.containsKey(commandLineArguments[1])) {
            result = "\n with the following input file:\n\n" + arrayToLines(filesMap.get(commandLineArguments[1]))
                    + "\n\n";
        }
        return result;
    }

    /**
     * Converts an Array of Strings into a String where each array element is represented as one line.
     * 
     * @param lines
     *            The array to process
     * @return the array as lines.
     */
    protected static String arrayToLines(String[] lines) {
        String result = "";
        for (String line : lines) {
            if (result != "") {
                result += "\n";
            }
            result += line;
        }
        return result;
    }

    /**
     * Returns a path to a file containing the lines given in {@code lines}. Creates the file if it was not created
     * before.
     * 
     * @param lines
     *            The lines to print in the file.
     * @return path to a file containing {@code lines}
     */
    protected static String getFile(String... lines) {
        String fileName;
        if (!reverseFileMap.containsKey(lines)) {
            fileName = UUID.randomUUID().toString() + ".txt";
            File file = new File(fileName);
            BufferedWriter outputWriter = null;
            try {
                outputWriter = new BufferedWriter(new FileWriter(file));
                for (int i = 0; i < lines.length; i++) {
                    outputWriter.write(lines[i]);
                    outputWriter.newLine();
                }
                outputWriter.flush();
                outputWriter.close();
                file.deleteOnExit();
            } catch (IOException e) {
                fail("The test was unable to create a test file. That's a shame!");
            }

            filesMap.put(fileName, lines);
            reverseFileMap.put(lines, fileName);
        } else {
            fileName = reverseFileMap.get(lines);
        }
        return fileName;
    }
}
