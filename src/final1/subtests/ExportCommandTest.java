package final1.subtests;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import test.Input;
import test.KitMatchers;
import test.SystemExitStatus;
import test.runs.LineRun;
import test.runs.NoOutputRun;
import test.runs.Run;

/**
 * Performs calls to the {@code export} command and checks the results.
 */
public class ExportCommandTest extends RecommendationSubtest {

	public ExportCommandTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * Asserts correct results for the example given on the task sheet.
	 */
	@Test
	public void taskSheetExampleTest() {
		testAgainstTaskSheet(TASK_SHEET_INPUT_FILE);
		testAgainstTaskSheet(TASK_SHEET_INPUT_FILE_SPACES); // why not...
	}

	/**
	 * Asserts correct results if the input file contains semantical duplicates.
	 */
	@Test
	public void duplicatesTest() {
		testAgainstTaskSheet(TASK_SHEET_INPUT_FILE_DUPLICATES);
	}

	private void testAgainstTaskSheet(String[] input) {
		// expectedLines are taken from the task sheet, optionalLines not
		String[] expectedLines = new String[] {
				"libreoffice -> impress [label=haspart]",
				"writer -> libreoffice [label=partof]",
				"centos7 -> operatingsystem [label=containedin]",
				"centos6 -> operatingsystem [label=containedin]",
				"centos5 -> operatingsystem [label=containedin]",
				"calc -> officesuite [label=containedin]",
				"impress -> libreoffice [label=partof]",
				"officesuite -> impress [label=contains]",
				"libreoffice -> writer [label=haspart]",
				"software -> operatingsystem [label=contains]",
				"officesuite -> libreoffice [label=contains]",
				"impress -> officesuite [label=containedin]",
				"calc -> libreoffice [label=partof]",
				"software -> officesuite [label=contains]",
				"centos6 -> centos7 [label=predecessorof]",
				"officesuite -> calc [label=contains]",
				"operatingsystem -> centos5 [label=contains]",
				"centos5 -> centos6 [label=predecessorof]",
				"operatingsystem -> centos6 [label=contains]",
				"libreoffice -> calc [label=haspart]",
				"officesuite -> writer [label=contains]",
				"libreoffice -> officesuite [label=containedin]",
				"operatingsystem -> centos7 [label=contains]",
				"writer -> officesuite [label=containedin]",
				"centos7 -> centos6 [label=successorof]",
				"officesuite -> software [label=containedin]",
				"operatingsystem -> software [label=containedin]",
				"centos6 -> centos5 [label=successorof]",
				"software [shape=box]",
				"officesuite [shape=box]",
				"operatingsystem [shape=box]"
		};
		String[] optionalLines = new String[] {
				"centos5",
				"centos6",
				"centos7",
				"writer",
				"calc",
				"impress",
				"libreoffice"
		};
		testExport(input, expectedLines, optionalLines);
	}

	private void testExport(String[] input, String[] expectedLines, String[] optionalLines) {
		List<Matcher<String>> expectedResults = new ArrayList<>();
		for (String line : expectedLines) {
			expectedResults.add(leadingSpaces(line));
		}
		List<Matcher<String>> optionalResults = new ArrayList<>();
		for (String line : optionalLines) {
			optionalResults.add(leadingSpaces(line));
		}

		runs = new Run[] {
				new ExportRun(expectedResults, optionalResults),
				new NoOutputRun("quit")
		};
		sessionTest(runs, Input.getFile(input));
	}

	/**
	 * Returns a matcher that accepts all strings that are {@code text} preceded with an arbitrary number of spaces
	 * (including none).
	 */
	private static Matcher<String> leadingSpaces(final String expectedText) {
		return new BaseMatcher<String>() {

			@Override
			public boolean matches(final Object testObject) {
				String text = (String) testObject;
				// it's unclear whether other whitespace (\s) is allowed as well
				return text.matches(" *" + Pattern.quote(expectedText));
			}

			@Override
			public void describeTo(Description description) {
				description.appendText(" *" + expectedText);
			}

			@Override
			public void describeMismatch(final Object item, final Description description) {
				description.appendText("a string '" + expectedText + "' preceded with an arbitrary number of spaces");
			}
		};
	}

	class ExportRun extends LineRun {
		private List<Matcher<String>> expectedResults;
		private Matcher<String> expectedMatcher;
		private List<Matcher<String>> optionalResults;
		private Matcher<String> optionalMatcher;

		/**
		 * Creates a test run that merges all call to {@code Terminal.printLine}. The run succeeds if the merged output
		 * matches {@code expectedResults} line by line.
		 * 
		 * @param command
		 *            The command to run
		 * @param expectedResults
		 *            The matchers describing the desired output
		 */
		public ExportRun(List<Matcher<String>> expectedResults, List<Matcher<String>> optionalResults) {
			super("export");
			this.expectedResults = expectedResults;
			this.optionalResults = optionalResults;
			this.expectedMatcher = KitMatchers.anyOfRemaining(expectedResults);
			this.optionalMatcher = KitMatchers.anyOfRemaining(optionalResults);
		}

		@Override
		public void check(String[] testedClassOutput, String errorMessage) {
			String[] outputLines = mergedOutputLines(testedClassOutput);

			if (outputLines.length != 0) {
				assertThat("First error at line 0: ", outputLines[0], is("digraph {"));
				for (int i = 1; i < outputLines.length - 1; i++) {
					String appendix = "\n First error at line " + i + ":";
					assertThat(errorMessage + appendix, outputLines[i], anyOf(expectedMatcher, optionalMatcher));
				}
				assertThat("First error at last line: ", outputLines[outputLines.length - 1], is("}"));
				if (!expectedResults.isEmpty()) {
					fail("Did not contain these obligatory lines: " + expectedResults);
				}
			} else {
				fail(errorMessage + "No calls to Terminal.printLine() were made!");
			}
		}

		@Override
		public String getExpectedDescription() {
			StringBuilder resultBuilder = new StringBuilder();
			for (Matcher<String> matcher : expectedResults) {
				if (resultBuilder.length() > 0) {
					resultBuilder.append("\n");
				}
				resultBuilder.append(matcher);
			}
			resultBuilder.append("\nand optional lines:\n");
			for (Matcher<String> matcher : optionalResults) {
				if (resultBuilder.length() > 0) {
					resultBuilder.append("\n");
				}
				resultBuilder.append(matcher);
			}
			return resultBuilder.toString();
		}
	}

}
