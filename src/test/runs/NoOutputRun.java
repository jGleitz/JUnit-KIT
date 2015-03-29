package test.runs;

import java.util.LinkedList;

import org.hamcrest.Matcher;

/**
 * A test run that may not output anything at all.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class NoOutputRun extends ExactRun {

	/**
	 * Constructs a test run for the given command that fails if {@code Terminal.printLine} is ever called by the tested
	 * class.
	 */
	public NoOutputRun(String command) {
		super(command, "no output at all", new LinkedList<Matcher<String>>());
	}

	/**
	 * Constructs a test run without a command that fails if {@code Terminal.printLine} is ever called by the tested
	 * class. Use only to test errors before the first command.
	 */
	public NoOutputRun() {
		this(null);
	}
}
