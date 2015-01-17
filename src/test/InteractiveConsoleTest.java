package test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.hamcrest.Matcher;
import org.junit.Before;

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
     * Tests an interactive console program with multiple commands that should output one line. Calls the main method
     * with optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that the output was
     * exactly {@code expectedOutput + System.lineSeparator()}.
     * 
     * @param commands
     *            The commands to run on the test object.
     * @param expectedOutput
     *            What the test object should print on the console. {@link System#lineSeparator()} will be added to it.
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void oneLineTest(String[] commands, String expectedOutput, String... args0) {
        oneLineTest(join(commands), is(expectedOutput), args0);
    }

    /**
     * Test an interactive console program with one command that should output one line. Calls the main method with
     * optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that the output was
     * exactly {@code expectedOutput + System.lineSeparator()}.
     * 
     * @param command
     *            The command to run on the console. {@code System.lineSeparator()} will be appended to it.
     * @param expectedOutput
     *            What the test object should print on the console. {@link System#lineSeparator()} will be added to it.
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void oneLineTest(String command, String expectedOutput, String... args0) {
        oneLineTest(command, is(expectedOutput), args0);
    }

    /**
     * Tests an interactive console program with multiple commands should that output one line. Calls the main method
     * with optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that the output
     * matches the {@code expectedOutputMatcher}.
     * 
     * @param commands
     *            The commands to run on the test object.
     * @param expectedOutputMatcher
     *            A Matcher that defines what the test object should print on the console. One new line at the end of
     *            the program's output will be ignored.
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void oneLineTest(String[] commands, Matcher<String> expectedOutputMatcher, String... args0) {
        oneLineTest(join(commands), expectedOutputMatcher, args0);
    }

    /**
     * Tests an interactive console program with one command that should output one line. Calls the main method with
     * optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that the output matches
     * the {@code expectedOutputMatcher}.
     * 
     * @param command
     *            The command to run on the console. {@code System.lineSeparator()} will be appended to it.
     * @param expectedOutputMatcher
     *            A Matcher that defines what the test object should print on the console. One new line at the end of
     *            the program's output will be ignored.
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void oneLineTest(String command, Matcher<String> expectedOutputMatcher, String... args0) {
        TestObject.setNextMethodCallInput(command);
        TestObject.runStaticVoid("main", (Object) args0);
        String result = TestObject.getLastMethodOutput();
        assertThat(consoleMessage(command, args0), removeNewLine(result), expectedOutputMatcher);
    }

    /**
     * Tests an interactive console program with one command that output multiple lines. Calls the main method with
     * optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that each output line
     * matches the {@code expectedResults}. Make sure to provide exactly one {@link ExpectedResult} for each output line
     * you expect.<br>
     * <br>
     * Example:<br>
     * Say we expect the main method to output three times {@code "Success!"} and then a line starting with
     * {@code "Error,"}. The call would work like this: <br>
     * 
     * <pre>
     * <code>
     * List<Matcher<String>> expectedResults = new Vector<Matcher<String>>();
     * expectedResults.add(is("Success!"));
     * expectedResults.add(is("Success!"));
     * expectedResults.add(is("Success!"));
     * expectedResults.add(startsWith("Success!"));
     * multiLineTest(commands, expectedResults, args0);
     * </code>
     * </pre>
     * 
     * @param commands
     *            The command to run on the console. {@code System.lineSeparator()} will be appended to it.
     * @param expectedResults
     *            Matchers for every expected output line. Each line of the output will be asserted to match the
     *            corresponding Matcher.
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void multiLineTest(String commands, List<Matcher<String>> expectedResults, String... args0) {
        TestObject.setNextMethodCallInput(commands);
        TestObject.runStaticVoid("main", (Object) args0);
        String result = TestObject.getLastMethodOutput();
        String resultArray[] = result.split("\n");
        String message = "";
        if (resultArray.length != expectedResults.size()) {
            message += consoleMessage(commands, args0);
            message += "Your program's output had ";
            message += (resultArray.length > expectedResults.size()) ? "too much" : "not enough";
            message += " lines. Your output had " + resultArray.length + " lines, expected was "
                    + expectedResults.size();
            message += ".\n" + expectedAndActual(expectedResults, result);
            fail(message);
        }
        String lineErrorMessage;
        Iterator<Matcher<String>> iterator = expectedResults.iterator();
        for (int i = 0; i < expectedResults.size(); i++) {
            lineErrorMessage = "First error at line " + (i + 1) + ":";
            message = consoleMessage(commands, args0) + expectedAndActual(expectedResults, result) + "\n"
                    + lineErrorMessage;
            assertThat(message, resultArray[i], iterator.next());
        }
    }

    private static String expectedAndActual(List<Matcher<String>> expected, String actual) {
        String result = "Expected was:\n\n";
        for (Matcher<String> matcher : expected) {
            result += matcher.toString() + "\n";
        }
        result += "\nYour output was:\n\n" + actual;
        return result;
    }

    /**
     * Tests an interactive console program with multiple commands that should output multiple lines. Calls the main
     * method with optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that each
     * output line matches the {@code expectedResults}. Make sure to provide exactly one {@link ExpectedResult} for each
     * output line you expect.<br>
     * <br>
     * Example:<br>
     * Say we expect the main method to output three times {@code "Success!"}, an empty line afterwards, and then a line
     * starting with {@code "Error,"}. The call would work like this: <br>
     * 
     * <pre>
     * <code>
     * List<Matcher<String>> expectedResults = new Vector<Matcher<String>>();
     * expectedResults.add(is("Success!"));
     * expectedResults.add(is("Success!"));
     * expectedResults.add(is("Success!"));
     * expectedResults.add(startsWith("Success!"));
     * multiLineTest(commands, expectedResults, args0);
     * </code>
     * </pre>
     * 
     * @param commands
     *            The commands to run on the console.
     * @param expectedResults
     *            Matchers for every expected output line. Each line of the output will be asserted to match the
     *            corresponding Matcher in {@code expectedResults}.
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void multiLineTest(String[] commands, List<Matcher<String>> expectedResults, String... args0) {
        multiLineTest(join(commands), expectedResults, args0);
    }

    /**
     * Tests an interactive console program with multiple commands that should output multiple lines. Calls the main
     * method with optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that each
     * output line matches the {@code expectedResults}. Make sure to provide exactly one {@link ExpectedResult} for each
     * output line you expect.
     * 
     * @param commands
     *            The commands to run on the console.
     * @param expectedResults
     *            Strings for every expected output line. Each line of the output will be asserted to be equal to the
     *            corresponding String in {@code expectedResults}.
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void multiLineTest(String[] commands, String[] expectedResults, String... args0) {
        multiLineTest(join(commands), joinAsIsMatchers(expectedResults), args0);
    }

    /**
     * Tests an interactive console program with one command that should output multiple lines. Calls the main method
     * with optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that each output line
     * matches the {@code expectedResults}. Make sure to provide exactly one {@link ExpectedResult} for each output line
     * you expect.
     * 
     * @param command
     *           The command to run on the console. {@code System.lineSeparator()} will be appended to it.
     * @param expectedResults
     *            Strings for every expected output line. Each line of the output will be asserted to be equal to the
     *            corresponding String in {@code expectedResults}.
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void multiLineTest(String command, String[] expectedResults, String... args0) {
        multiLineTest(command, joinAsIsMatchers(expectedResults), args0);
    }

    /**
     * Tests an interactive console program with one command that should output an error message. Calls the main method
     * with optional {@code args0} on the test object and runs the {@code command} on it. Asserts that the output starts
     * with {@code "Error,"}. <br>
     * NOTE: This method does <b>not</b> allow to call {@code System.exit(1)}. If you expect the implemented class to do
     * so, call {@code TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);} before.
     * 
     * @param command
     *            The command to run on the console. {@code System.lineSeparator()} will be appended to it.
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void errorTest(String command, String... args0) {
        String expectedOutputStart = "Error,";
        TestObject.setNextMethodCallInput(command + System.lineSeparator());
        TestObject.runStaticVoid("main", (Object) args0);
        String result = TestObject.getLastMethodOutput();
        assertThat(consoleMessage(command, args0), result, startsWith(expectedOutputStart));
    }

    /**
     * Tests an interactive console program with multiple commands that should output an error message. Calls the main
     * method with optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that the
     * output starts with {@code "Error,"}. <br>
     * NOTE: This method does <b>not</b> allow to call {@code System.exit(1)}. If you expect the implemented class to do
     * so, call {@code TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);} before.
     * 
     * @param commands
     *            The commands to run on the console
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void errorTest(String[] commands, String... args0) {
        errorTest(join(commands), args0);
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

    private static String join(String[] strings) {
        String result = "";
        for (String string : strings) {
            result += string + System.lineSeparator();
        }
        return result;
    }

    private static List<Matcher<String>> joinAsIsMatchers(String[] strings) {
        List<Matcher<String>> result = new Vector<Matcher<String>>();
        for (String s : strings) {
            result.add(is(s));
        }
        return result;
    }

    private static String removeNewLine(String s) {
        String result = s;
        if (s.endsWith(System.lineSeparator())) {
            result = s.substring(0, s.length() - System.lineSeparator().length());
        }
        return result;
    }
}