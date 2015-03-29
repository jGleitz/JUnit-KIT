package test.runs;

import static test.KitMatchers.hasExcactlyThatMuch;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;

/**
 * A test run for exact matching. For each expected call to {@code Terminal.printLine}, one matcher has to be provided.
 * If {@code n} matchers are provided and the tested class fails to call {@code Terminal.printLine} exactly {@code n}
 * times, the run is considered to have failed.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class ExactRun implements Run {
	private String command;
	private List<Matcher<String>> expectedOutputMatchers;
	private String outputDescription;

	/**
	 * Creates a run for the given parameters, without a command. A description of the expected output will
	 * automatically be created based on {@code expectedOutputMatchers}. Use only to test errors before the first
	 * command.
	 * 
	 * @param expectedOutputMatchers
	 *            The matchers for the expected output. One matcher per expected call to {@code Terminal.printLine}.
	 */
	public ExactRun(List<Matcher<String>> expectedOutputMatchers) {
		this(null, expectedOutputMatchers);
	}

	/**
	 * Creates a run for the given parameters. A description of the expected output will automatically be created based
	 * on {@code expectedOutputMatchers}.
	 * 
	 * @param command
	 *            The command to run.
	 * @param expectedOutputMatchers
	 *            The matchers for the expected output. One matcher per expected call to {@code Terminal.printLine}.
	 */
	public ExactRun(String command, List<Matcher<String>> expectedOutputMatchers) {
		this(command, null, expectedOutputMatchers);
	}

	/**
	 * Creates a run for the given parameters. A description of the expected output will automatically be created based
	 * on {@code expectedOutputMatchers}.
	 * 
	 * @param command
	 *            The command to run.
	 * @param expectedOutputMatchers
	 *            The matchers for the expected output. One matcher per expected call to {@code Terminal.printLine}.
	 */
	@SafeVarargs
	public ExactRun(String command, Matcher<String>... expectedOutputMatchers) {
		this(command, Arrays.asList(expectedOutputMatchers));
	}

	/**
	 * Creates a run for the given parameters.
	 * 
	 * @param command
	 *            The command to run.
	 * @param outputDescription
	 *            A description detailing the expected output
	 * @param expectedOutputMatchers
	 *            The matchers for the expected output. One matcher per expected call to {@code Terminal.printLine}.
	 */
	public ExactRun(String command, String outputDescription, List<Matcher<String>> expectedOutputMatchers) {
		this.command = command;
		this.expectedOutputMatchers = new LinkedList<Matcher<String>>(expectedOutputMatchers);
		this.outputDescription = outputDescription;
	}

	/**
	 * Creates a run for the given parameters.
	 * 
	 * @param command
	 *            The command to run.
	 * @param outputDescription
	 *            A description detailing the expected output
	 * @param expectedOutputMatchers
	 *            The matchers for the expected output. One matcher per expected call to {@code Terminal.printLine}.
	 */
	@SafeVarargs
	public ExactRun(String command, String outputDescription, Matcher<String>... expectedOutputMatchers) {
		this(command, outputDescription, Arrays.asList(expectedOutputMatchers));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.runs.Run#match(java.lang.String[])
	 */
	@Override
	public void check(String[] testedClassOutput, String errorMessage) {
		Matcher<String[]> hasCorrectCallNumber = hasExcactlyThatMuch(expectedOutputMatchers.size(), new String[] {
				"calls to Terminal.printLine() at all",
				"call to Terminal.printLine()",
				"calls to Terminal.printLine()"
		}, outputDescription);
		String overallErrorMessage = errorMessage + "\nYour output:\n" + join(testedClassOutput);
		assertThat(overallErrorMessage, testedClassOutput, hasCorrectCallNumber);
		Iterator<Matcher<String>> matcherIterator = expectedOutputMatchers.iterator();
		for (int i = 0; i < testedClassOutput.length; i++) {
			assertThat(errorMessage, testedClassOutput[i], matcherIterator.next());
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
		if (outputDescription != null) {
			return outputDescription;
		} else {
			StringBuilder resultBuilder = new StringBuilder();
			for (Matcher<String> matcher : expectedOutputMatchers) {
				if (resultBuilder.length() > 0) {
					resultBuilder.append("\n");
				}
				resultBuilder.append(matcher);
			}
			return resultBuilder.toString();
		}
	}

	protected String join(String[] stringArray) {
		StringBuilder resultBuilder = new StringBuilder();
		for (String s : stringArray) {
			resultBuilder.append("[");
			resultBuilder.append(s);
			resultBuilder.append("]\n");
		}
		return resultBuilder.toString();
	}
}