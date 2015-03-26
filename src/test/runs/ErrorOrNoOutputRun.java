package test.runs;

import static org.junit.Assert.assertThat;
import static test.KitMatchers.suits;
import test.SystemExitStatus;
import test.TestObject;

/**
 * A test run that should result in either an error message or no output at all.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class ErrorOrNoOutputRun implements Run {
	private String command;
	private boolean checkSystemExitStatus;

	/**
	 * Constructs a test run for the given {@code command} that shall result in either an error message or no output at
	 * all.
	 * 
	 * @param command
	 *            The command to run
	 * @param mustTerminate
	 *            {@code true} if the program has to terminate after the run. Enables system exit status checking.
	 */
	public ErrorOrNoOutputRun(String command, boolean mustTerminate) {
		this.command = command;
		this.checkSystemExitStatus = mustTerminate;
	}

	/**
	 * Constructs a test run without a command that shall result in either an error message or no output at all. Use
	 * only to test errors before the first command.
	 * 
	 * @param mustTerminate
	 *            {@code true} if the program has to terminate after the run. Enables system exit status checking.
	 */
	public ErrorOrNoOutputRun(boolean mustTerminate) {
		this(null, mustTerminate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.runs.Run#check(java.lang.String[], java.lang.String)
	 */
	@Override
	public void check(String[] testedClassOutput, String errorMessage) {
		if (testedClassOutput.length > 0) {
			new ErrorRun(command).check(testedClassOutput, errorMessage);
			if (checkSystemExitStatus) {
				assertThat(errorMessage + "\nYour class printed an error message. This requires System.exit(1)!",
					TestObject.getLastMethodsSystemExitStatus(), suits(SystemExitStatus.EXACTLY.status(1), true));
			}
		} else {
			new NoOutputRun(command).check(testedClassOutput, errorMessage);
			if (checkSystemExitStatus) {
				assertThat(errorMessage
						+ "\nYour class did not print an error message. It may only call System.exit(0)!",
					TestObject.getLastMethodsSystemExitStatus(), suits(SystemExitStatus.WITH_0, false));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.runs.Run#getCommand()
	 */
	@Override
	public String getCommand() {
		return command;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.runs.Run#getExpectedDescription()
	 */
	@Override
	public String getExpectedDescription() {
		return "Either an error message or no output at all";
	}
}
