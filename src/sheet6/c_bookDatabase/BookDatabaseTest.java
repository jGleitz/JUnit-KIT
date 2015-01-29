package sheet6.c_bookDatabase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
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
import test.TestObject;
import test.TestObject.SystemExitStatus;

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
    private String[] file;
    private String line;

    /*
     * PREDEFINED BOOK INPUT FILES
     */
    /**
     * The input file that was given as valid on the task sheet.
     */
    private static final String[] taskSheetInputFile = {
            "title=Java_ist_auch_eine_Insel,creator=GalileoComputing",
            "tITle=Grundkurs_Programmieren_iN_JAVA,year=2007",
            "Creator=Ralf_Reussner,year=2006"
    };

    /**
     * A very simple input file, intended not to produce a parsing error (to focus on other things). Therefore it only
     * contains lower case characters between a and z and all attributes.
     */
    private static final String[] simpleValidFile = {
        "title=musterbuch,creator=mustermann,year=2000"
    };

    /*
     * ERROR TESTS
     */

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
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);

        // no tolerance
        errorTest("quit", "", getFile(simpleValidFile));
        // not a number
        errorTest("quit", "xyz", getFile(simpleValidFile));
        // not in range
        errorTest("quit", "3", getFile(simpleValidFile));
        // not in range
        errorTest("quit", "-1", getFile(simpleValidFile));
        // not a correct number
        errorTest("quit", "0.5fd", getFile(simpleValidFile));
    }

    /**
     * Tests the program's behaviour for a bad file path argument. Asserts that the program prints an error message for:
     * <ul>
     * <li>a file path that does not point to a file
     * </ul>
     */
    @Test
    public void testBadFilePath() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);

        // no path
        errorTest("quit", "0.3", "");
        // wrong path
        errorTest("quit", "0.3", "I sure as hell don't exist!");
    }

    /**
     * Tests the program's behaviour for a wrong number of command line arguments. Asserts that the program prints an
     * error message for:
     * <ul>
     * <li>a wrong amount of command line arguments.
     * </ul>
     */
    @Test
    public void testWrongCommandLineArgumentNumber() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);

        // no argument
        errorTest("quit");
        // one empty argument
        errorTest("quit", "");
        // one valid argument
        errorTest("quit", "0.5");
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
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);

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

        // dublicate attribute keyword
        line = "title=java,creator=reussner,title=java,year=2005";
        errorTest("quit", "0.3", getFile(line));

        // maybe a regex will fail here
        line = "creator=reussner,year=2014,tl=test";
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
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);

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
        line = "year=2016";
        errorTest("quit", "0.3", getFile(line));
        falseValues.add(line);

        // year is a number, but not in range
        line = "year=-1";
        errorTest("quit", "0.3", getFile(line));
        falseValues.add(line);

        // year is a number, is in range, but not an Integer.
        line = "year=2014.2";
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

    /*
     * CORRECT ARGUMENTS
     */
    /**
     * Starts program with two valid command line arguments Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}=0
     * <li>no error message is printed
     * </ul>
     */
    @Test
    public void testCorrectCommandLineArguments() {
        // lower range for tolerance
        oneLineTest("quit", "", "0", getFile(simpleValidFile));
        // upper range for tolerance
        oneLineTest("quit", "", "1", getFile(simpleValidFile));
        // longer but valid tolerance
        oneLineTest("quit", "", "0.22456", getFile(simpleValidFile));
        // tolerance in float notation
        oneLineTest("quit", "", "0.5f", getFile(simpleValidFile));
        // tolerance in double notation
        oneLineTest("quit", "", "0.5d", getFile(simpleValidFile));
    }

    /**
     * Simulates the Praktomat's public test. Asserts that:
     * <ul>
     * <li>the program outputs the expected search responses.
     * <li>an error message is printed for the last search command (key isbn not allowed)
     * </ul>
     */
    @Test
    public void publicPraktomatTest() {
        file = new String[] {
                "creator=galileocomputing,title=java_ist_auch_eine_insel",
                "title=grundkursprogrammieren_in_java,year=2007",
                "creator=ralf_reussner,year=2006"
        };
        commands = new String[] {
                "search creator=ralf_reussner",
                "search year=2006",
                "search AND(creator=ralf_reussner,year=2006)",
                "search OR(creator=ralf_reussner,year=2006)",
                "search isbn=12345",
                "quit"
        };
        // @formatter:off
        expectedResultMatchers = getMatchers(
                is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"),
                is("creator=unknown,title=grundkursprogrammieren_in_java,year=2007,false"),
                is("creator=ralf_reussner,title=unknown,year=2006,true"),
                
                is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"),
                is("creator=unknown,title=grundkursprogrammieren_in_java,year=2007,true"),
                is("creator=ralf_reussner,title=unknown,year=2006,true"),
                
                is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"),
                is("creator=unknown,title=grundkursprogrammieren_in_java,year=2007,false"),
                is("creator=ralf_reussner,title=unknown,year=2006,true"),
                
                is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"),
                is("creator=unknown,title=grundkursprogrammieren_in_java,year=2007,true"),
                is("creator=ralf_reussner,title=unknown,year=2006,true"),
                
                startsWith("Error,")
         );
        // @formatter:on

        multiLineTest(commands, expectedResultMatchers, "0.5", getFile(file));
    }

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
