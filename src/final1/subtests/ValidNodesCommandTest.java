package final1.subtests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.fail;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

import test.Input;
import test.SystemExitStatus;

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
	 * Asserts overall correct behaviour of the implementation. This includes several error detection and recovery after
	 * errors.
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

	private void testAgainstTaskSheet(String[] input) {
		//@formatter:off

        // the following queries/matchers are taken directly from the task sheet
		runs = new Run[] {
			new ExactRun("nodes", is("calc:202,centos5:105,centos6:106,centos7:107,impress:203,libreoffice:200,officesuite,operatingsystem,software,writer:201")),
			new ExactRun("recommend S1 105", is("centos6:106,centos7:107")),
			new ExactRun("recommend S3 107", is("centos5:105,centos6:106")),
			new ExactRun("recommend UNION(S1 105,S3 107)", is("centos5:105,centos6:106,centos7:107")),
			new ExactRun("recommend S1 201", is("calc:202,impress:203,libreoffice:200")),
			new ExactRun("recommend UNION(S1 201,INTERSECTION(S1 105,S3 107))", is("calc:202,centos6:106,impress:203,libreoffice:200")),
			new NoOutputRun("quit")
		};
		//@formatter:on
		sessionTest(runs, Input.getFile(input));
	}

	@Test
	public void incomplete() {
		fail("This test is still in the development state and therefore incomplete!");
	}

}
