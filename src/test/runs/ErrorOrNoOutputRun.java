package test.runs;

/**
 * A test run that should result in either an error message or no output at all.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class ErrorOrNoOutputRun implements Run {
	private String command;

	/**
	 * Constructs a test run for the given {@code command} that shall result in either an error message or no output at
	 * all.
	 */
	public ErrorOrNoOutputRun(String command) {
		this.command = command;
	}

	/**
	 * Constructs a test run without a command that shall result in either an error message or no output at all. Use
	 * only to test errors before the first command.
	 */
	public ErrorOrNoOutputRun() {
		this(null);
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
		} else {
			new NoOutputRun(command).check(testedClassOutput, errorMessage);
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
