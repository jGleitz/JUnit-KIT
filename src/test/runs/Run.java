package test.runs;

/**
 * Describes one command that is to be run on the interactive console as well as its expected output. Provides a method
 * to check whether the output the tested class printed fulfils the needs defined by the run.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public interface Run {

	/**
	 * Checks whether the output of the tested class does fulfil the needs defined by this run. Throws an exception if
	 * it doesn't.
	 * 
	 * @param testedClassOutput
	 *            The output of the tested class for this run. One String represents one call to
	 *            {@code Terminal.printLine}.
	 * @param errorMessage
	 *            The message that will be reported to {@link org.junit.Assert#fail(String)} or
	 *            {@link org.junit.Assert#assertThat(String, Object, org.hamcrest.Matcher)}
	 * @throws RunFailedException
	 *             If {@code testedClassOutput} does not fulfil this test run's expected output.
	 */
	public void check(String[] testedClassOutput, String errorMessage);

	/**
	 * @return The command that is to be run on the interactive console for this test run.
	 */
	public String getCommand();

	/**
	 * @return A String describing what this test run expects the tested class to print.
	 */
	public String getExpectedDescription();
}
