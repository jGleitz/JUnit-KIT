package sheet6.c_bookDatabase;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Test;

import test.InteractiveConsoleTest;

/**
 * /** A test for the Interactive Console (Task C) <br>
 * NOTE: This test is far from being complete and under development!
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 29.01.2015
 */
public class BookDatabaseTest extends InteractiveConsoleTest {
    private static HashMap<String, String[]> filesMap = new HashMap<>();
    private static HashMap<String[], String> reverseFileMap = new HashMap<>();
    private static final String[] taskSheetInputFile = {
            "title=Java_ist_auch_eine_Insel,creator=GalileoComputing",
            "tITle=Grundkurs_Programmieren_iN_JAVA,year=2007",
            "Creator=Ralf_Reussner,year=2006"
    };
    private String[] file;
    private String line;

    /**
     * Fails the test as this test is incomplete an therefore does not grant anything. Remove this method as soon as the
     * test reaches a certain degree of relevance.
     */
    @Test
    public void incomplete() {
        fail("\n\nThis test is under development and incomplete!\n\n");
    }

    /**
     * Tests the program's behaviour for a wrong tolerance command line argument. Asserts that the program prints an
     * error message for:
     * <ul>
     * <li>a tolerance that can not be parsed as a Double
     * <li>a tolerance that can be parsed as a Double, but is not between 0 and 1
     * </ul>
     * 
     */
    @Test
    public void testWrongTolerance() {
        errorTest("quit", "", getFile(taskSheetInputFile));
        errorTest("quit", "xyz", getFile(taskSheetInputFile));
        errorTest("quit", "3", getFile(taskSheetInputFile));
        errorTest("quit", "-1", getFile(taskSheetInputFile));
    }

    /**
     * Tests the program's behaviour for a bad file path argument. Asserts that the program prints an error message for:
     * <ul>
     * <li>a file path that does not point to a file
     * </ul>
     */
    @Test
    public void testBadFilePath() {
        errorTest("quit", "0.3", "");
        errorTest("quit", "0.3", "I sure as hell don't exist!");
    }

    /**
     * Tests the program's behaviour for a bad formed input file. Asserts that the program prints an error message for:
     * <ul>
     * <li>an empty file
     * <li>a file with general bad syntax
     * <li>a file with a malformed attribute name
     * </ul>
     */
    @Test
    public void testBadFile() {
        // empty file
        line = "";
        errorTest("quit", "0.3", getFile(line));

        // random text
        line = "Just some random text";
        errorTest("quit", "0.3", getFile(line));

        // no value
        line = "creator=";
        errorTest("quit", "0.3", getFile(line));

        // false attribute name
        line = "creater=Max_Mustermann";
        errorTest("quit", "0.3", getFile(line));

        // comma at the end
        line = "creator=Max_Mustermann,";
        errorTest("quit", "0.3", getFile(line));

        // space in between
        line = "creator=Max_Mustermann, title=Musterbuch";
        errorTest("quit", "0.3", getFile(line));
    }

    /**
     * Tests the program's behaviour for a bad formed input file. Asserts that the program prints an error message for:
     * <ul>
     * <li>a malformed attribute value
     * </ul>
     * **/
    @Test
    public void testWrongInputFileAttributeValues() {
        List<String> correctValues = new LinkedList<>();
        List<String> falseValues = new LinkedList<>();
        correctValues.add("creator=Max_Mustermann");
        correctValues.add("year=2010");
        correctValues.add("title=Musterbuch");

        // bad character in creator
        line = "creator=Max_Mus$termann";
        errorTest("quit", "0.3", getFile(line));
        falseValues.add(line);

        // bad character in title
        line = "title=Must&erbuch";
        errorTest("quit", "0.3", getFile(line));
        falseValues.add(line);

        // year is not a number
        line = "year=irgendetwas";
        errorTest("quit", "0.3", getFile(line));
        falseValues.add(line);

        // year is a number, but not in range
        line = "year=2020";
        errorTest("quit", "0.3", getFile(line));
        falseValues.add(line);

        // year is a number, but not in range
        line = "year=-100";
        errorTest("quit", "0.3", getFile(line));
        falseValues.add(line);

        // test combinations of good and bad strings
        List<String> goodOnes;
        for (String bad : falseValues) {
            goodOnes = new LinkedList<>();
            for (String good : correctValues) {
                if (!good.contains(bad.split("=")[0])) {
                    goodOnes.add(good);
                }
            }
            errorTest("quit", "0.3", getFile(goodOnes.get(0) + "," + bad));
            errorTest("quit", "0.3", getFile(goodOnes.get(1) + "," + bad));
            errorTest("quit", "0.3", getFile(goodOnes.get(0) + "," + bad + "," + goodOnes.get(1)));
        }
    }

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
    @AfterClass
    public static void removeAllOpenedFiles() {
        for (String fileName : filesMap.keySet()) {
            new File(fileName).delete();
        }
    }

    /**
     * A message giving information about the input file used in a test.
     * 
     * @param commandLineArguments
     *            The command line arguments the main method was called with during the test. The file message will read
     *            the file name in the second argument and output the contents of the file its pointing to.
     * @return A text representing the input file
     */
    private static String fileMessage(String[] commandLineArguments) {
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
    private static String arrayToLines(String[] lines) {
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
    private static String getFile(String... lines) {
        String fileName = UUID.randomUUID().toString();

        if (!reverseFileMap.containsKey(lines)) {
            fileName = UUID.randomUUID().toString() + ".txt";

            BufferedWriter outputWriter = null;
            try {
                outputWriter = new BufferedWriter(new FileWriter(fileName));
                for (int i = 0; i < lines.length; i++) {
                    outputWriter.write(lines[i]);
                    outputWriter.newLine();
                }
                outputWriter.flush();
                outputWriter.close();
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
