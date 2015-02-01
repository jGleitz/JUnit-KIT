package test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.Timeout;

import test.TestObject.SystemExitStatus;

/**
 * Contains some useful methods for testing with the interactive console. Extend this class to write interactive console
 * tests.
 * 
 * @author Roman Langrehr
 * @author Joshua Gleitze
 * @since 05.01.2015
 * @version 1.1.1
 *
 */
public abstract class InteractiveConsoleTest {
	/**
	 * You can use this field to put one command in it.
	 */
	protected String command;
	/**
	 * You can use this field to put an array of commands in it.
	 */
	protected String[] commands;
	/**
	 * You can use this field to put one expected result string it it.
	 */
	protected String expectedResult;
	/**
	 * You can use this field to put an expected result String array in it.
	 */
	protected String[] expectedResults;
	/**
	 * You can use this field to put one expected result Matcher in it.
	 */
	protected Matcher<String> expectedResultMatcher;
	/**
	 * You can use this field to put an expected result Matcher array in it.
	 */
	protected List<Matcher<String>> expectedResultMatchers;

	/**
	 * A test is terminated after 5 seconds. This assures that a test fails if the tested class fails to terminate. Some
	 * users may mistake a non terminating test as being successful.
	 */
	@Rule
	public Timeout globalTimeout = new Timeout(5000); // 5 seconds max per method tested

	/**
	 * Tests an interactive console program with multiple commands that should output one line. Calls the main method
	 * with optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that the output was
	 * exactly {@code expectedOutput}. One new line at the end of the output will be ignored.
	 * 
	 * @param commands
	 *            The commands to run on the test object.
	 * @param expectedOutput
	 *            What the test object should print on the console. One new line at the end of the program's output will
	 *            be ignored.
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	protected void oneLineTest(String[] commands, String expectedOutput, String... args0) {
		oneLineTest(join(commands), is(expectedOutput), args0);
	}

	/**
	 * Test an interactive console program with one command that should output one line. Calls the main method with
	 * optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that the output was
	 * exactly {@code expectedOutput}. One new line at the end of the output will be ignored.
	 * 
	 * @param command
	 *            The command to run on the console. {@code System.lineSeparator()} will be appended to it.
	 * @param expectedOutput
	 *            What the test object should print on the console. One new line at the end of the program's output will
	 *            be ignored.
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	protected void oneLineTest(String command, String expectedOutput, String... args0) {
		oneLineTest(command, is(expectedOutput), args0);
	}

	/**
	 * Tests an interactive console program with multiple commands should that output one line. Calls the main method
	 * with optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that the output
	 * matches the {@code expectedOutputMatcher}. One new line at the end of the output will be ignored.
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
	 * the {@code expectedOutputMatcher}. One new line at the end of the output will be ignored.
	 * 
	 * @param command
	 *            The command to run on the console.
	 * @param expectedOutputMatcher
	 *            A Matcher that defines what the test object should print on the console. One new line at the end of
	 *            the program's output will be ignored.
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	protected void oneLineTest(String command, Matcher<String> expectedOutputMatcher, String... args0) {
		TestObject.resetClass();
		TestObject.setNextMethodCallInput(command);
		TestObject.runStaticVoid("main", (Object) args0);
		String result = TestObject.getLastMethodOutput();
		assertThat(consoleMessage(command, args0), removeNewLine(result), expectedOutputMatcher);
	}

	/**
	 * Tests an interactive console program with one command that output multiple lines. Calls the main method with
	 * optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that each output line
	 * matches the {@code expectedResults}. Make sure to provide exactly one {@link Matcher} for each output line you
	 * expect.<br>
	 * <br>
	 * Example:<br>
	 * Say we expect the main method to output three times {@code "Success!"} and then a line starting with
	 * {@code "Error,"}. The call would work like this: <br>
	 * 
	 * <pre>
	 * <code>
	 * {@code
	 * List<Matcher<String>> expectedResults = new Vector<Matcher<String>>();
	 * expectedResults.add(is("Success!"));
	 * expectedResults.add(is("Success!"));
	 * expectedResults.add(is("Success!"));
	 * expectedResults.add(startsWith("Success!"));
	 * multiLineTest(commands, expectedResults, args0);
	 * }
	 * </code>
	 * </pre>
	 * 
	 * @param commands
	 *            The command to run on the console.
	 * @param expectedResults
	 *            Matchers for every expected output line. Each line of the output will be asserted to match the
	 *            corresponding Matcher.
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	protected void multiLineTest(String commands, List<Matcher<String>> expectedResults, String... args0) {
		TestObject.resetClass();
		TestObject.setNextMethodCallInput(commands);
		TestObject.runStaticVoid("main", (Object) args0);
		String result = TestObject.getLastMethodOutput();
		String resultArray[] = result.split(System.lineSeparator());
		String message = "";
		if (resultArray.length < expectedResults.size()) {
			message += consoleMessage(commands, args0);
			message += "Your program's output had not enough lines. Your output had ";
			message += resultArray.length + " lines, expected were " + expectedResults.size();
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
	 * output line matches the {@code expectedResults}. Make sure to provide exactly one {@link Matcher} for each output
	 * line you expect.<br>
	 * <br>
	 * Example:<br>
	 * Say we expect the main method to output three times {@code "Success!"}, an empty line afterwards, and then a line
	 * starting with {@code "Error,"}. The call would work like this: <br>
	 * 
	 * <pre>
	 * <code>
	 * {@code
	 * List<Matcher<String>> expectedResults = new Vector<Matcher<String>>();
	 * expectedResults.add(is("Success!"));
	 * expectedResults.add(is("Success!"));
	 * expectedResults.add(is("Success!"));
	 * expectedResults.add(startsWith("Success!"));
	 * multiLineTest(commands, expectedResults, args0);
	 * }
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
	 * output line matches the {@code expectedResults}. Make sure to provide exactly one {@link Matcher} for each output
	 * line you expect.
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
	 * matches the {@code expectedResults}. Make sure to provide exactly one {@link Matcher} for each output line you
	 * expect.
	 * 
	 * @param command
	 *            The command to run on the console.
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
	 *            The command to run on the console.
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	protected void errorTest(String command, String... args0) {
		String expectedOutputStart = "Error,";
		TestObject.resetClass();
		TestObject.setNextMethodCallInput(command);
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

	/**
	 * Constructs a List of matchers to be used for the {@link #multiLineTest} methods. This method can be used to write
	 * down expected output nicely, one Matcher per line. For example:
	 * 
	 * <pre>
	 * <code>
	 * 	// @formatter:off
	 * 	expectedResultMatchers = getMatchers(
	 * 		is("Hintertupfingen:3"),
	 * 		startsWith("Error,"),
	 * 		is("Hintertupfingen:3")
	 * 	);
	 * 	// @formatter:on
	 * </code>
	 * </pre>
	 * 
	 * @param matchers
	 *            the matchers to construct the list.
	 * @return a list containing {@code matchers}
	 */
	@SafeVarargs
	protected final List<Matcher<String>> getMatchers(Matcher<String>... matchers) {
		List<Matcher<String>> result = new LinkedList<Matcher<String>>();
		for (Matcher<String> m : matchers) {
			result.add(m);
		}
		return result;
	}

	/**
	 * Gets called before each test run. Sets the allowed system exit status to
	 * {@link TestObject.SystemExitStatus#WITH_0}. Override this method if you which to have another default system exit
	 * status.
	 */
	@Before
	public void defaultSystemExitStatus() {
		TestObject.allowSystemExit(SystemExitStatus.WITH_0);
	}

	/**
	 * A representation of command line arguments. Returns {@code that has been called with the command line arguments},
	 * concatenated with a list representation of {@code commandLineArguments}. Returns an empty String if
	 * {@code commandLineArguments} is {@code null} or empty.
	 * 
	 * @param commandLineArguments
	 *            the cli arguments to process
	 * @return a String represention {@code commandLineArguments}
	 */
	protected String getArguments(String[] commandLineArguments) {
		if (commandLineArguments == null || commandLineArguments.length == 0) {
			return "";
		}
		return "that has been called with the command line arguments " + Arrays.toString(commandLineArguments);
	}

	/**
	 * The message that should be printed at the start of an error message. Override this method to print your own
	 * message.
	 * 
	 * @param commands
	 *            the commands that were run on the interactive console
	 * @param commandLineArguments
	 *            the command line arguments the console was called with
	 * @return a String representing the session
	 */
	protected String consoleMessage(String commands, String[] commandLineArguments) {
		String result = "";
		result += "We ran a session on your interactive console" + getArguments(commandLineArguments)
				+ ", running the commands \n\n" + commands + "\n\n but got unexpected output:\n";
		return result;
	}

	private String join(String[] strings) {
		String result = "";
		for (String string : strings) {
			result += string + System.lineSeparator();
		}
		return result;
	}

	private List<Matcher<String>> joinAsIsMatchers(String[] strings) {
		List<Matcher<String>> result = new Vector<Matcher<String>>();
		for (String s : strings) {
			result.add(is(s));
		}
		return result;
	}

	private String removeNewLine(String s) {
		String result = s;
		if (s.endsWith(System.lineSeparator())) {
			result = s.substring(0, s.length() - System.lineSeparator().length());
		}
		return result;
	}

  /**
   * Adds the "quit" command to a simple one-line command
   */
  public static String[] addQuit(String... commands) {
    String[] allCommands = new String[commands.length + 1];
    for (int i = 0; i < commands.length; i++) {
      allCommands[i] = commands[i];
    }
    return allCommands;
  }
}