package test.runs;

import static test.KitMatchers.hasExcactlyThatMuchLines;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.Matcher;

/**
 * A test run for matching the method's whole output for one command line by line. All calls to
 * {@code Terminal.printLine} will be merged into one String, each call separated by {@code \n}, and can then be matched
 * line by line.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class LineRun implements Run {
	private String command;
	private List<Matcher<String>> expectedResults;

	/**
	 * Creates a test run that merges all call to {@code Terminal.printLine}. The run succeeds if the merged output
	 * matches {@code expectedResults} line by line.
	 * 
	 * @param command
	 *            The command to run
	 * @param expectedResults
	 *            The matchers describing the desired output
	 */
	public LineRun(String command, List<Matcher<String>> expectedResults) {
		this.command = command;
		this.expectedResults = expectedResults;
	}

	/**
	 * Creates a test run that merges all call to {@code Terminal.printLine}. The run succeeds if the merged output
	 * matches {@code expectedResults} line by line.
	 * 
	 * @param command
	 *            The command to run
	 * @param expectedResults
	 *            The matchers describing the desired output
	 */
	@SafeVarargs
	public LineRun(String command, Matcher<String>... expectedResults) {
		this(command, Arrays.asList(expectedResults));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.runs.Run#getCommand()
	 */
	@Override
	public String getCommand() {
		return this.command;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.runs.Run#check(java.lang.String[], java.lang.String)
	 */
	@Override
	public void check(String[] testedClassOutput, String errorMessage) {
		StringBuilder mergedOutputBuilder = new StringBuilder();
		for (String output : testedClassOutput) {
			mergedOutputBuilder.append(output.replace("\r", ""));
			mergedOutputBuilder.append("\n");
		}
		Iterator<Matcher<String>> expectedIterator = expectedResults.iterator();
		String[] outputLines = mergedOutputBuilder.toString().split("\n");
		assertThat(errorMessage, outputLines, hasExcactlyThatMuchLines(expectedResults.size(), lineCountMessage()));
		for (int i = 0; i < outputLines.length; i++) {
			String appendix = "\n First error at line " + i + ":";
			assertThat(errorMessage + appendix, outputLines[i], expectedIterator.next());
		}
	}

	private String lineCountMessage() {
		return "exactly " + ((expectedResults.size() == 1) ? "line" : "lines");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.runs.Run#getExpectedDescription()
	 */
	@Override
	public String getExpectedDescription() {
		StringBuilder resultBuilder = new StringBuilder();
		for (Matcher<String> matcher : expectedResults) {
			if (resultBuilder.length() > 0) {
				resultBuilder.append("\n");
			}
			resultBuilder.append(matcher);
		}
		return resultBuilder.toString();
	}
}
