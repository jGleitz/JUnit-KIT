package final1.subtests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.ErrorRun;
import test.runs.ExactRun;
import test.runs.NoOutputRun;

/**
 * Performs valid calls to the {@code nodes} command and checks the results.
 *
 * @author Joshua Gleitze
 * @author Martin Loeper
 * @version 1.1
 */
public class ValidNodesCommandTest extends RecommendationSubtest {

	public ValidNodesCommandTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * Asserts correct results for the example given on the task sheet.
	 */
	@Test
	public void taskSheetExampleTest() {
		testAgainstTaskSheet(TASK_SHEET_INPUT_FILE);
	}

	/**
	 * Asserts correct results if the input file contains spaces.
	 */
	@Test
	public void spacesTest() {
		testAgainstTaskSheet(TASK_SHEET_INPUT_FILE_SPACES);
	}

	/**
	 * Asserts correct results if the input file contains semantically dublicates.
	 */
	@Test
	public void duplicatesTest() {
		testAgainstTaskSheet(TASK_SHEET_INPUT_FILE_DUPLICATES);
	}

	/**
	 * Asserts overall correct behaviour of the implementation. This includes several error detection and recovery after
	 * errors.
	 */
	@Test
	public void oneLineTest() {
		// @formatter:off
		runs = getRuns(
			new ExactRun("nodes", is("a:2,b:1")),
			new ExactRun("recommend S1 1", is("")),
			new ErrorRun("recommend S1 3"),
			new ErrorRun("recommend S4 1"),
			new ExactRun("recommend S2 1", is("")),
			new ExactRun("recommend S2 2", is("b:1")),
			new ErrorRun("redbutton"),
			new ExactRun("recommend S3 1", is("a:2")),
			new ExactRun("recommend S3 2", is("")),
			new NoOutputRun("quit")
		);
		// @formatter:on
		// edges: new String[] { "b:1-[successor-of]->a", "a-[predecessor-of]->b" }
		sessionTest(runs, Input.getFile(ONE_LINE_INPUT_FILE1));

		// @formatter:off
		runs = getRuns(
			new ExactRun("nodes", is("a,b:2")),
			new ErrorRun("recommend S1 1"),
			new ExactRun("recommend S1 2", is("")),
			new ExactRun("recommend S2 1", is("")),
			new ExactRun("recommend S3 1", is("")),
			new NoOutputRun("quit")
		);
		// @formatter:on
		sessionTest(runs, Input.getFile(ONE_LINE_INPUT_FILE2));
	}

	private void testAgainstTaskSheet(String[] input) {
		//@formatter:off

        // the following queries/matchers are taken directly from the task sheet
		runs = getRuns(
			new ExactRun("nodes", is("calc:202,centos5:105,centos6:106,centos7:107,impress:203,libreoffice:200,officesuite,operatingsystem,software,writer:201")),
			new ExactRun("recommend S1 105", is("centos6:106,centos7:107")),
			new ExactRun("recommend S3 107", is("centos5:105,centos6:106")),
			new ExactRun("recommend UNION(S1 105,S3 107)", is("centos5:105,centos6:106,centos7:107")),
			new ExactRun("recommend S1 201", is("calc:202,impress:203,libreoffice:200")),
			new ExactRun("recommend UNION(S1 201,INTERSECTION(S1 105,S3 107))", is("calc:202,centos6:106,impress:203,libreoffice:200")),
			new NoOutputRun("quit")
		);
		//@formatter:on
		sessionTest(runs, Input.getFile(input));
	}

	@Test
	public void incomplete() {
		fail("This test is still in the development state and therefore incomplete!");
	}
}
