package test.runs;

import static test.KitMatchers.hasExcactlyThatMuch;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

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
	 * Creates a test run that merges all calls to {@code Terminal.printLine}. The run succeeds if the merged output
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
	 * Creates a test run that merges all calls to {@code Terminal.printLine}. The run succeeds if the merged output
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

	/**
	 * Creates a test run without a command that merges all calls to {@code Terminal.printLine}. The run succeeds if the
	 * merged output matches {@code expectedResults} line by line. Use only to test errors before the first command.
	 * 
	 * @param expectedResults
	 *            The matchers describing the desired output
	 */
	public LineRun(List<Matcher<String>> expectedResults) {
		this(null, expectedResults);
	}

	/**
	 * Creates a test run without a command that merges all calls to {@code Terminal.printLine}. The run succeeds if the
	 * merged output matches {@code expectedResults} line by line. Use only to test errors before the first command.
	 * 
	 * @param expectedResults
	 *            The matchers describing the desired output
	 */
	@SafeVarargs
	public LineRun(Matcher<String>... expectedResults) {
		this(null, expectedResults);
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
		Iterator<Matcher<String>> expectedIterator = expectedResults.iterator();
		String[] outputLines = mergedOutputLines(testedClassOutput);
		String overallErrorMessage = errorMessage + "\nYour output:\n" + mergedOutput(testedClassOutput);
		assertThat(overallErrorMessage, outputLines, hasExcactlyThatMuch(expectedResults.size(), new String[] {
				"lines",
				"line",
				"lines"
		}));
		for (int count = 0; count < outputLines.length; count++) {
			String appendix = "\n First error at line " + count + ":";
			assertThat(errorMessage + appendix, outputLines[count], expectedIterator.next());
		}
	}

	/**
	 * @param output
	 *            The output to merge.
	 * @return All output joined with {@code \n}. Explicitly uses {@code \n} for concatenation and removes potentially
	 *         contained {@code \r}.
	 */
	protected String mergedOutput(String[] output) {

		StringBuilder mergedOutputBuilder = new StringBuilder();
		for (String call : output) {
			mergedOutputBuilder.append(call.replace("\r", ""));
			mergedOutputBuilder.append("\n");
		}
		return mergedOutputBuilder.toString();
	}

	/**
	 * The joined output merged by {@link #mergedOutput(String[])} split at {@code \n}. Unlike
	 * {@link String#split(String)}, this method guarantees one array element per occurrence of {@code \n}.
	 * 
	 * @param output
	 *            The output to process
	 * @return One array element per line found in any of {@code output}'s elements.
	 */
	protected String[] mergedOutputLines(String[] output) {
		Scanner outputScanner = new Scanner("\n" + mergedOutput(output));
		outputScanner.useDelimiter("\n");
		List<String> outputList = new ArrayList<>();
		while (outputScanner.hasNext()) {
			outputList.add(outputScanner.next());
		}
		outputScanner.close();
		return outputList.toArray(new String[outputList.size()]);
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
