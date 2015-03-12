package sheet6.c_bookDatabase.subtests;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.TestObject;

/**
 * Asserts the program's checking of the command line arguments. The program should print error messages for bad formed
 * command line arguments. Meanwhile, correct ones should be read in without printing anything. Note that the program's
 * ability to actually parse the input file is asserted in {@link InputFileParsingTest}.
 *
 * @author Joshua Gleitze
 * @version 1.0
 * @since 30.01.2015
 */
public class CommandLineArgumentsTest extends BookDatabaseSubTest {

    /*
     * (non-Javadoc)
     *
     * @see sheet6.c_bookDatabase.subtests.BookDatabaseSubTest#consoleMessage(java.lang.String[], java.lang.String[])
     */
    @Override
    protected String consoleMessage(String[] commands, String[] commandLineArguments) {
        String result = "";
        result += "We called your interactive console" + getArguments(commandLineArguments)

                + "\n\n but got unexpected output:\n";
        return result;
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
        TestObject.allowSystemExit(SystemExitStatus.ALL);

        // no tolerance
        errorTest("quit", "", Input.getFile(simpleValidFile));
        // not a number
        errorTest("quit", "xyz", Input.getFile(simpleValidFile));
        // not in range
        errorTest("quit", "3", Input.getFile(simpleValidFile));
        // not in range
        errorTest("quit", "-1", Input.getFile(simpleValidFile));
        // not a correct number
        errorTest("quit", "0.5fd", Input.getFile(simpleValidFile));
    }

    /**
     * Tests the program's behaviour for a bad file path argument. Asserts that the program prints an error message for:
     * <ul>
     * <li>a file path that does not point to a file
     * </ul>
     */
    @Test
    public void testBadFilePath() {
        TestObject.allowSystemExit(SystemExitStatus.ALL);

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
        TestObject.allowSystemExit(SystemExitStatus.ALL);

        // no argument
        errorTest("quit");
        // one empty argument
        errorTest("quit", "");
        // one valid argument
        errorTest("quit", "0.5");
    }

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
        noOutputTest("quit", "0", Input.getFile(simpleValidFile));
        // upper range for tolerance
        noOutputTest("quit", "1", Input.getFile(simpleValidFile));
        // longer but valid tolerance
        noOutputTest("quit", "0.22456", Input.getFile(simpleValidFile));
        // tolerance in float notation
        noOutputTest("quit", "0.5f", Input.getFile(simpleValidFile));
        // tolerance in double notation
        noOutputTest("quit", "0.5d", Input.getFile(simpleValidFile));
    }

}
