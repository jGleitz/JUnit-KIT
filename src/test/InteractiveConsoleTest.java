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

/**
 * Contains some useful methods for testing with the interactive console. Extend this class to write interactive console
 * tests. <h2>Checking the system exit status</h2> There are two ways of asserting the tested class' system exit status.
 * <p>
 * <h6>Using {@link TestObject#allowSystemExit(SystemExitStatus) Preferably by overriding
 *
 * {@link #defaultSystemExitStatus()}. This was the old way, but is now Deprecated.
 * <h6>The new way</h6> The new way of asserting the system exit status has two big advantages:
 * <ul>
 * <li>Error messages are shown for failed tests if more than the exit status was wrong
 * <li>It differentiates between an <i>expected</i> and an <i>allowed</i> system exit status.
 * </ul>
 * To use the new way, call {@link #setAllowedSystemExitStatus(SystemExitStatus)} or
 * {@link #setExpectedSystemStatus(SystemExitStatus)} before running one of the test methods. Typically, a test class
 * does this once in its constructor.
 *
 * @author Roman Langrehr
 * @author Joshua Gleitze
 * @since 05.01.2015
 * @version 1.2
 *
 */
public abstract class InteractiveConsoleTest {
	/**
	 * A test is terminated after 5 seconds. This assures that a test fails if the tested class fails to terminate. Some
	 * users may mistake a non terminating test as being successful.
	 */
	@Rule
	public Timeout globalTimeout = new Timeout(5000); // 5 seconds max per method tested
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
	 * You can use this field to put one expected result Matcher in it.
	 */
	protected Matcher<String> expectedResultMatcher;
	/**
	 * You can use this field to put an expected result Matcher array in it.
	 */
	protected List<Matcher<String>> expectedResultMatchers;
	/**
	 * You can use this field to put an expected result String array in it.
	 */
	protected String[] expectedResults;
	private SystemExitStatus allowedExitStatus;
	private SystemExitStatus expectedExitStatus;
	private boolean newSystemExitStatusCeckInited;

	private SystemExitStatus oldDefault;

	/**
	 * Adds the "quit" command to the given {@code commands}.
	 *
	 * @param commands
	 *            The commands to add {@code quit} to.
	 * @return A copy of {@code commands} with {@code "quit"} added to its end
	 */
	public static String[] addQuit(String... commands) {
		String[] allCommands = new String[commands.length + 1];
		for (int i = 0; i < commands.length; i++) {
			allCommands[i] = commands[i];
		}
		allCommands[commands.length] = "quit";
		return allCommands;
	}

	protected static String joinOnePerLine(String[] strings) {
		String result = "";
		for (String string : strings) {
			result += (result != "") ? System.lineSeparator() : "";
			result += string;
		}
		return result;
	}

	private static String expectedAndActual(List<Matcher<String>> expected, String[] actual) {
		String result = "Expected was:\n\n";
		for (Matcher<String> matcher : expected) {
			result += matcher.toString() + "\n";
		}
		result += "\nYour output was:\n\n" + joinOnePerLine(actual);
		return result;
	}

	/**
	 * Gets called before each test run. Sets the allowed system exit status to {@link SystemExitStatus#WITH_0}.
	 * Override this method if you wish to have another default system exit status.
	 * <p>
	 * <i>Deprecated. Please use {@link #setAllowedSystemExitStatus(SystemExitStatus)} and
	 * {@link #setExpectedSystemStatus(SystemExitStatus)}.
	 */
	@Deprecated
	@Before
	public void defaultSystemExitStatus() {
		TestObject.allowSystemExit(SystemExitStatus.WITH_0);
	}

	/**
	 * Checks the exit status of a tested method. Has to be called after call to a {@link TestObject} run method.
	 *
	 * @param commands
	 *            The commands that were run on the interactive console.
	 * @param args0
	 *            The arguments the main method was called with in the test run.
	 * @throws IllegalStateException
	 *             If {@link #initSystemExitStatusCheck()} has not been called before the test run.
	 */
	protected final void checkSystemExitStatus(String[] commands, String[] args0) {
		if (!newSystemExitStatusCeckInited) {
			throw new IllegalStateException("The new way of system exit status checking has not been inited yet!");
		}
		if ((allowedExitStatus != null) || (expectedExitStatus != null)) {
			SystemExitStatus actualStatus = TestObject.getLastMethodsSystemExitStatus();
			if (expectedExitStatus != null) {
				assertThat(consoleMessage(commands, args0) + "\nWrong system exit status!", actualStatus,
					is(expectedExitStatus));
			} else if ((actualStatus != SystemExitStatus.NONE) && (allowedExitStatus != SystemExitStatus.ALL)) {
				assertThat(consoleMessage(commands, args0) + "\nWrong system exit status!", actualStatus,
					is(allowedExitStatus));
			}
			TestObject.allowSystemExit(oldDefault);
		}
		newSystemExitStatusCeckInited = false;
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
	protected String consoleMessage(String[] commands, String[] commandLineArguments) {
		String result = "";
		result += "We ran a session on your interactive console" + getArguments(commandLineArguments)
				+ ", running the commands \n\n" + joinOnePerLine(commands) + "\n\n but got unexpected output:\n";
		return result;
	}

	/**
	 * Tests an interactive console program with one command that should output an error message. Calls the main method
	 * with optional {@code args0} on the test object and runs the {@code command} on it. Asserts that the tested class
	 * called {@code Terminal.printLine} at least once and the output starts with {@code "Error,"}. <br>
	 * NOTE: This method does <b>not</b> allow to call {@code System.exit(1)}. If you expect the implemented class to do
	 * so, call {@code TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);} before.
	 *
	 * @param command
	 *            The command to run on the console.
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	protected void errorTest(String command, String... args0) {
		errorTest(wrapInArray(command), args0);
	}

	/**
	 * Tests an interactive console program with multiple commands that should output an error message. Calls the main
	 * method with optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that the
	 * tested class called {@code Terminal.printLine} at least once and the output starts with {@code "Error,"}.<br>
	 * NOTE: This method does <b>not</b> allow to call {@code System.exit(1)}. If you expect the implemented class to do
	 * so, call {@code TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);} before.
	 *
	 * @param commands
	 *            The commands to run on the console
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	protected void errorTest(String[] commands, String... args0) {
		initSystemExitStatusCheck();
		String expectedOutputStart = "Error,";
		TestObject.resetClass();
		TestObject.setNextMethodCallInput(commands);
		TestObject.runStaticVoid("main", (Object) args0);
		String[] result = TestObject.getLastMethodsOutput();
		if (result.length == 0) {
			fail(consoleMessage(commands, args0)
					+ "\n Expected an error message, but your code never called Terminal.printLine!");
		}
		assertThat(consoleMessage(commands, args0), result[0], startsWith(expectedOutputStart));
		checkSystemExitStatus(commands, args0);
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
		if ((commandLineArguments == null) || (commandLineArguments.length == 0)) {
			return "";
		}
		return "that has been called with the command line arguments " + Arrays.toString(commandLineArguments);
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
	 * Initiates the new way of system exit status checking. Has to be called before the first call to a
	 * {@link TestObject} run method.
	 *
	 * @throws IllegalStateException
	 *             If this method has already been called without a subsequent call to
	 *             {@link #checkSystemExitStatus(String[])}.
	 */
	protected final void initSystemExitStatusCheck() {
		if (newSystemExitStatusCeckInited) {
			throw new IllegalStateException("The new way of system exit status checking may only be inited once. Call "
					+ "checkSystemExitStatus before initing again!");
		}
		if ((allowedExitStatus != null) || (expectedExitStatus != null)) {
			oldDefault = TestObject.getAllowedSystemExitStatus();
			TestObject.allowSystemExit(SystemExitStatus.ALL);
		}
		newSystemExitStatusCeckInited = true;
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
	 * @param command
	 *            The command to run on the console.
	 * @param expectedResults
	 *            Matchers for every expected output line. Each line of the output will be asserted to match the
	 *            corresponding Matcher.
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	protected void multiLineTest(String command, List<Matcher<String>> expectedResults, String... args0) {
		multiLineTest(wrapInArray(command), expectedResults, args0);
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
		initSystemExitStatusCheck();
		TestObject.resetClass();
		TestObject.setNextMethodCallInput(commands);
		TestObject.runStaticVoid("main", (Object) args0);
		String[] result = TestObject.getLastMethodsOutput();
		String message = "";
		if (result.length != expectedResults.size()) {
			message += consoleMessage(commands, args0);
			message += "Your program's output had ";
			message += (result.length < expectedResults.size()) ? "not enough" : "too many";
			message += " lines (<=> calls to Terminal.printLine()).\nYour output had ";
			message += result.length + " lines, expected were " + expectedResults.size();
			message += ".\n" + expectedAndActual(expectedResults, result);
			fail(message);
		}
		String lineErrorMessage;
		Iterator<Matcher<String>> iterator = expectedResults.iterator();
		for (int i = 0; i < expectedResults.size(); i++) {
			lineErrorMessage = "First error at line " + (i + 1) + ":";
			message = consoleMessage(commands, args0) + expectedAndActual(expectedResults, result) + "\n"
					+ lineErrorMessage;
			assertThat(message, result[i], iterator.next());
		}
		checkSystemExitStatus(commands, args0);
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
		multiLineTest(commands, joinAsIsMatchers(expectedResults), args0);
	}

	/**
	 * Tests an interactive console program with one command that should not output anything. Calls the main method with
	 * optional {@code args0} on the test object and runs {@code command} on it. Asserts that the tested class never
	 * called {@code Terminal.printLine}.
	 *
	 * @param command
	 *            The command to run on the test object.
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	protected void noOutputTest(String command, String... args0) {
		noOutputTest(wrapInArray(command), args0);
	}

	/**
	 * Tests an interactive console program with multiple commands that should not output anything. Calls the main
	 * method with optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that the
	 * tested class never called {@code Terminal.printLine}.
	 *
	 * @param commands
	 *            The commands to run on the test object.
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	protected void noOutputTest(String[] commands, String... args0) {
		initSystemExitStatusCheck();
		TestObject.resetClass();
		TestObject.setNextMethodCallInput(commands);
		TestObject.runStaticVoid("main", (Object) args0);

		String[] result = TestObject.getLastMethodsOutput();
		if (result.length != 0) {
			fail(consoleMessage(commands, args0)
					+ "\n Your code called Terminal.printLine, while it was expected not to output anything! The output was:\n"
					+ joinOnePerLine(result));
		}
		checkSystemExitStatus(commands, args0);
	}

	/**
	 * Tests an interactive console program with one command that should output one line. Calls the main method with
	 * optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that the tested class only
	 * called {@code Terminal.printLine} once and the output matches the {@code expectedOutputMatcher}.
	 *
	 * @param command
	 *            The command to run on the console.
	 * @param expectedOutputMatcher
	 *            A Matcher that defines what the test object should print on the console.
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	protected void oneLineTest(String command, Matcher<String> expectedOutputMatcher, String... args0) {
		oneLineTest(wrapInArray(command), expectedOutputMatcher, args0);
	}

	/**
	 * Test an interactive console program with one command that should output one line. Calls the main method with
	 * optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that the tested class
	 * called {@code Terminal.printLine} only once and the output was exactly {@code expectedOutput}.
	 *
	 * @param command
	 *            The command to run on the console.
	 * @param expectedOutput
	 *            What the test object should print on the console.
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	protected void oneLineTest(String command, String expectedOutput, String... args0) {
		oneLineTest(command, is(expectedOutput), args0);
	}

	/**
	 * Tests an interactive console program with multiple commands that should output one line. Calls the main method
	 * with optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that the tested class
	 * called {@code Terminal.printLine} only once and the output matches the {@code expectedOutputMatcher}.
	 *
	 * @param commands
	 *            The commands to run on the test object.
	 * @param expectedOutputMatcher
	 *            A Matcher that defines what the test object should print on the console.
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	protected void oneLineTest(String[] commands, Matcher<String> expectedOutputMatcher, String... args0) {
		initSystemExitStatusCheck();
		TestObject.resetClass();
		TestObject.setNextMethodCallInput(commands);
		TestObject.runStaticVoid("main", (Object) args0);
		String[] result = TestObject.getLastMethodsOutput();
		if (result.length == 0) {
			fail(consoleMessage(commands, args0) + "\n Your code never called Terminal.printLine!");
		} else if (result.length > 1) {
			fail(consoleMessage(commands, args0) + "\n Your code called Terminal.printLine more than once!");
		}
		assertThat(consoleMessage(commands, args0), result[0], expectedOutputMatcher);
		checkSystemExitStatus(commands, args0);
	}

	/**
	 * Tests an interactive console program with multiple commands that should output one line. Calls the main method
	 * with optional {@code args0} on the test object and runs all {@code commands} on it. Asserts that the tested class
	 * called {@code Terminal.printLine} only once and the output was exactly {@code expectedOutput}.
	 *
	 * @param commands
	 *            The commands to run on the test object.
	 * @param expectedOutput
	 *            What the test object should print on the console.
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	protected void oneLineTest(String[] commands, String expectedOutput, String... args0) {
		oneLineTest(commands, is(expectedOutput), args0);
	}

	/**
	 * If the tested class is allowed but not required to call {@code System.exit} with a certain status, set it through
	 * this method. The default status is {@code null}, which disables the new system exit status checking in favour of
	 * the deprecated way through {@link #defaultSystemExitStatus()}. The setting stays until the next call to this
	 * method.
	 * <p>
	 * NOTE: This setting only takes effect if {@link #setExpectedSystemStatus(SystemExitStatus)} was called with
	 * {@code null}!
	 *
	 * @param status
	 *            The {@code x} the tested class may {@code System.exit} with. {@code null} to disable the new way of
	 *            system exit status checking.
	 */
	protected final void setAllowedSystemExitStatus(SystemExitStatus status) {
		allowedExitStatus = status;
	}

	/**
	 * If you expect a certain system exit status (which means the tested class has to call {@code System.exit}, instead
	 * of just being allowed to), set is through this method. The default status is {@code null}, which means no
	 * restriction on the method's system exit status. The setting stays until the next call to this method.
	 * <p>
	 * NOTE: This setting overrides {@link #setAllowedSystemExitStatus(SystemExitStatus)}: The allowed system exit
	 * status is ignored as long as the expected status is not {@code null}.
	 *
	 * @param status
	 *            The {@code x} the tested class has to call {@code System.exit} with. Set to {@code null} if the tested
	 *            class does not necessary have to call {@code System.exit}.
	 */
	protected final void setExpectedSystemStatus(SystemExitStatus status) {
		expectedExitStatus = status;
	}

	/**
	 * @param s
	 *            A String.
	 * @return An Array containing {@code s} as only element.
	 */
	protected String[] wrapInArray(String s) {
		return new String[] {
			s
		};
	}

	private List<Matcher<String>> joinAsIsMatchers(String[] strings) {
		List<Matcher<String>> result = new Vector<Matcher<String>>();
		for (String s : strings) {
			result.add(is(s));
		}
		return result;
	}
}
