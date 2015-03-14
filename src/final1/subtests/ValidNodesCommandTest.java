package final1.subtests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.ExactRun;
import test.runs.NoOutputRun;
import test.runs.Run;

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
	 * Asserts correct results if the input file contains semantical duplicates.
	 */
	@Test
	public void duplicatesTest() {
		testAgainstTaskSheet(TASK_SHEET_INPUT_FILE_DUPLICATES);
	}

	/**
	 * Asserts correct results for simple one line input files.
	 */
	@Test
	public void oneLineTest() {
		runs = new Run[] {
				new ExactRun("nodes", is("a:2,b:1")),
				new NoOutputRun("quit")
		};
		sessionTest(runs, Input.getFile(ONE_LINE_INPUT_FILE1));

		runs = new Run[] {
				new ExactRun("nodes", is("a,b:2")),
				new NoOutputRun("quit")
		};
		sessionTest(runs, Input.getFile(ONE_LINE_INPUT_FILE2));
	}

	/**
	 * Asserts that product ID 0 can be handled correctly.
	 */
	@Test
	public void zeroIdTest() {
		oneLineTest(addQuit("nodes"), "a,b:0,c:1", Input.getFile(ZERO_ID_INPUT_FILE));
	}

	private void testAgainstTaskSheet(String[] input) {
		// the following queries/matchers are taken directly from the task sheet
		runs = new Run[] {
				new ExactRun(
						"nodes",
						is("calc:202,centos5:105,centos6:106,centos7:107,impress:203,libreoffice:200,officesuite,operatingsystem,software,writer:201")),
				new NoOutputRun("quit")
		};
		sessionTest(runs, Input.getFile(input));
	}

	@Test
	public void incomplete() {
		fail("This test is still in the development state and therefore incomplete!");
	}

}
