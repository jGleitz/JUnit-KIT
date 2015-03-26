package test.runs;

import org.hamcrest.Matcher;
import static org.hamcrest.CoreMatchers.startsWith;

/**
 * A test run for an error message. This test run succeeds if the tested class calls {@code Terminal.printLine} exactly
 * once, providing a String starting with {@code "Error,"}.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class ErrorRun extends ExactRun {
	private static final Matcher<String> errorMatcher = startsWith("Error,");

	/**
	 * Constructs a test run that shall result in an error message.
	 * 
	 * @param command
	 *            The command to run
	 */
	public ErrorRun(String command) {
		super(command, "an error message", errorMatcher);
	}

	/**
	 * Constructs a test run without a command that shall result in an error message. Use only to test errors before the
	 * first command.
	 */
	public ErrorRun() {
		this(null);
	}

}
