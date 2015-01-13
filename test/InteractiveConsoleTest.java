package test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.either;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.hamcrest.Matcher;
import org.junit.Before;

import test.ExpectedResult.TestType;
import test.TestObject.SystemExitStatus;

/**
 * This class contains some useful methods for testing with the interactive console.
 * 
 * @author Roman Langrehr
 * @author Joshua Gleitze
 * @since 05.01.2015
 * @version 1.1
 *
 */
public class InteractiveConsoleTest {

    /**
     * A Matcher that matches both an empty string ({@code ""}) or a new line ({@code \n}).
     */
    protected final static Matcher<String> emptyOrNewLine = either(is("")).or(is(System.lineSeparator()));

    /**
     * Test interactive console programs that output one line. Calls the main method with optional {@code args0} on the
     * test object and runs all {@code commands} on it. Asserts that the output was the {@code expectedOutput}.
     * 
     * @param commands
     *            The commands to run on the test object. Every command must be in a single line.
     * @param expectedOutput
     *            What the test object should print on the console. Use {@link System#lineSeparator()} when you expect
     *            the TestObject to start a new line.
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void oneLineTest(String commands, String expectedOutput, String... args0) {
        oneLineTest(commands, is(expectedOutput), args0);
    }

    /**
     * Test interactive console programs that output one line. Calls the main method with optional {@code args0} on the
     * test object and runs all {@code commands} on it. Asserts that the output matches the
     * {@code expectedOutputMatcher}.
     * 
     * @param commands
     *            The commands to run on the test object. Every command must be in a single line
     * @param expectedOutputMatcher
     *            A Matcher that defines what the test object should print on the console. Use
     *            {@link System#lineSeparator()} when you expect the TestObject to start a new line. <br>
     * <br>
     *            Example Matcher, "either empty string or new line":<br>
     *            <code>either(is("")).or(is(System.lineSeparator()))</code>
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void oneLineTest(String commands, Matcher<String> expectedOutputMatcher, String... args0) {
        TestObject.setNextMethodCallInput(commands);
        TestObject.runStatic("main", (Object) args0);
        String result = TestObject.getLastMethodOutput();
        assertThat(consoleMessage(commands, args0), result, expectedOutputMatcher);
    }

    /**
     * Test interactive console programs that output multiple lines. Calls the main method with optional {@code args0}
     * on the test object and runs all {@code commands} on it. Asserts that each output line matches the
     * {@code expectedResults}. Make sure to provide exactly one {@link ExpectedResult} for each output line you expect.<br>
     * <br>
     * Example:<br>
     * Say we expect the main method to output three times {@code "Success!"}, an empty line afterwards, and then a line
     * starting with {@code "Error,"}. The call would work like this: <br>
     * 
     * <pre>
     * {@code
     * ExpectedResult[] expectedResults = new ExpectedResult[] {
     *      new ExpectedResult("Success!", TestType.SAME),
     *      new ExpectedResult("Success!", TestType.SAME),
     *      new ExpectedResult("Success!", TestType.SAME),
     *      new ExpectedResult(System.lineSeparator(), TestType.SAME),
     *      new ExpectedResult("Error,", TestType.STARTS_WITH)
     * }
     * multiLineTest(commands, expectedResults, args0);
     * </pre>
     * 
     * @param commands
     *            The commands to run on the test object. Every command must be in a single line
     * @param expectedResults
     *            An array containing {@link ExpectedResult}s, on {@code ExpectedResult} for every expected output line.
     *            Each line of the output will be asserted to match the corresponding {@code ExpectedResult}.
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void multiLineTest(String commands, ExpectedResult[] expectedResults, String... args0) {
        TestObject.setNextMethodCallInput(commands);
        TestObject.runStatic("main", (Object) args0);
        String result = TestObject.getLastMethodOutput();
        String resultArray[] = result.split("\n");
        String message = "";
        if (resultArray.length != expectedResults.length) {
            message += consoleMessage(commands, args0);
            message += "Your program's output had ";
            message += (resultArray.length > expectedResults.length) ? "too much" : "not enough";
            message += " lines. Your output had " + resultArray.length + " lines, expected was "
                    + expectedResults.length;
            message += ".\nYour output was:\n\n";
            message += result;
            message += "\n\nExpected was:\n\n";
            for (ExpectedResult res : expectedResults) {
                message += res.getTestString();
                message += (res.getTestType() == TestType.STARTS_WITH) ? " ...\n" : "\n";
            }
            fail(message);
        }
        String lineErrorMessage;
        String outputUntilHere = "";
        for (int i = 0; i < expectedResults.length; i++) {
            lineErrorMessage = consoleMessage(commands, args0);
            lineErrorMessage += "First error at line " + (i + 1) + ":";
            expectedResults[i].assertResult(lineErrorMessage + outputUntilHere, resultArray[i]);
            outputUntilHere += resultArray[i] + "\n";
        }
    }

    /**
     * Does the same as {@link #oneLineTest(String, String, String...)}, but it it only checks weather the output begins
     * with "Error," or not. <br>
     * This method does NOT allow to call System.exit(1). If you expect the TestObject to do so, call
     * {@code TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);} before.
     */
    protected void crashTest(String commands, String... args0) {
        String expectedOutputStart = "Error,";
        TestObject.setNextMethodCallInput(commands);
        TestObject.runStatic("main", (Object) args0);
        String result = TestObject.getLastMethodOutput();
        assertThat(consoleMessage(commands, args0), result, startsWith(expectedOutputStart));
    }

    @Before
    public void allowSystemExit0() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_0);
    }

    private static String getArguments(String[] commandLineArguments) {
        if (commandLineArguments == null || commandLineArguments.length == 0) {
            return "";
        }
        return "that has been called with the command line arguments " + Arrays.toString(commandLineArguments);
    }

    private static String consoleMessage(String commands, String[] commandLineArguments) {
        String result = "";
        result += "We ran a session on your interactive console" + getArguments(commandLineArguments)
                + ", running the commands \n\n" + commands + "\n\n but got unexpected output:\n";
        return result;
    }
}