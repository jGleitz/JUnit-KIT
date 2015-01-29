package sheet6.c_bookDatabase;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
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

    @Test
    public void incomplete() {
        fail("\n\nThis test is under development and incomplete!\n\n");
    }

    /**
     * Tests the program's behaviour for a wrong tolerance command line argument. Asserts that:
     * <ul>
     * <li>The program prints an error message for a tolerance that can not be parsed as a Double
     * <li>The program prints an error message for a tolerance that can be parsed as a Double, but is not between 0 and
     * 1
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
     * Tests the program's behaviour for a bad file path argument. Assert that:
     * <ul>
     * <li>The program prints an error message for a file path that does not point to a file
     * </ul>
     */
    @Test
    public void testBadFilePath() {
        errorTest("quit", "0.3", "");
        errorTest("quit", "0.3", "I sure as hell don't exist!");
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

    @AfterClass
    public static void removeAllOpenedFiles() {
        for (String fileName : filesMap.keySet()) {
            new File(fileName).delete();
        }
    }

    private static String fileMessage(String[] commandLineArguments) {
        String result = "";
        if (commandLineArguments.length > 1 && filesMap.containsKey(commandLineArguments[1])) {
            result = "\n with the following input file:\n\n" + arrayToLines(filesMap.get(commandLineArguments[1]))
                    + "\n\n";
        }
        return result;
    }

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

    private static String getFile(String[] lines) {
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
