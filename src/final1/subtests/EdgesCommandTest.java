package final1.subtests;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

import test.Input;
import test.KitMatchers;
import test.SystemExitStatus;
import test.runs.LineRun;
import test.runs.NoOutputRun;
import test.runs.Run;

/**
 * Performs calls to the {@code edges} command and checks the results.
 */
public class EdgesCommandTest extends RecommendationSubtest {

	public EdgesCommandTest() {
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
		testEdges(new String[] {
				"A contains B",
				"A contains B"
		}, new String[] {
				"a-[contains]->b",
				"b-[contained-in]->a"
		});

		testAgainstTaskSheet(TASK_SHEET_INPUT_FILE_DUPLICATES);
	}

	/**
	 * Asserts correct results for multiple edges from one node to another
	 */
	@Test
	public void multipleEdgesTest() {
		// r = 1: omit contains/contained-in for simplicity
		for (int r = 1; r < 3; r++) {
			for (int s = r * 2 + 2; s < 6; s++) {
				testEdges(new String[] {
						relation(r * 2, 1, 2),
						relation(s, 1, 2),
				}, new String[] {
						"a:1-[" + RELATIONS[(r) % 6][(r + 1) % 2] + "]->b:2",
						"b:2-[" + RELATIONS[(r) % 6][(r) % 2] + "]->a:1",
						"a:1-[" + RELATIONS[(s / 2) % 6][s % 2] + "]->b:2",
						"b:2-[" + RELATIONS[(s / 2) % 6][(s + 1) % 2] + "]->a:1",
				});

				testEdges(new String[] {
						reverse(r * 2, 1, 2),
						relation(s, 1, 2),
				}, new String[] {
						"a:1-[" + RELATIONS[(r) % 6][r % 2] + "]->b:2",
						"b:2-[" + RELATIONS[(r) % 6][(r + 1) % 2] + "]->a:1",
						"a:1-[" + RELATIONS[(s / 2) % 6][s % 2] + "]->b:2",
						"b:2-[" + RELATIONS[(s / 2) % 6][(s + 1) % 2] + "]->a:1",
				});
			}
		}
	}

	/**
	 * Asserts correct results for simple one line input files.
	 */
	@Test
	public void oneLineTest() {
		testEdges(ONE_LINE_INPUT_FILE1, new String[] {
				"b:1-[successor-of]->a:2",
				"a:2-[predecessor-of]->b:1"
		});

		testEdges(ONE_LINE_INPUT_FILE2, new String[] {
				"a-[contains]->b:2",
				"b:2-[contained-in]->a"
		});
	}

	/**
	 * Asserts that product ID 0 can be handled correctly.
	 */
	@Test
	public void zeroIdTest() {
		testEdges(ZERO_ID_INPUT_FILE, new String[] {
				"a-[contains]->b:0",
				"b:0-[contained-in]->a",
				"a-[contains]->c:1",
				"c:1-[contained-in]->a"
		});
	}

	private void testAgainstTaskSheet(String[] input) {
		// the following queries/matchers are NOT taken from the task sheet (only the first 11 are)
		String[] nodes = new String[] {
				"calc:202-[contained-in]->officesuite",
				"calc:202-[part-of]->libreoffice:200",
				"centos5:105-[contained-in]->operatingsystem",
				"centos5:105-[predecessor-of]->centos6:106",
				"centos6:106-[contained-in]->operatingsystem",
				"centos6:106-[predecessor-of]->centos7:107",
				"centos6:106-[successor-of]->centos5:105",
				"centos7:107-[contained-in]->operatingsystem",
				"centos7:107-[successor-of]->centos6:106",
				"impress:203-[contained-in]->officesuite",
				"impress:203-[part-of]->libreoffice:200",
				"writer:201-[part-of]->libreoffice:200",
				"writer:201-[contained-in]->officesuite",
				"libreoffice:200-[contained-in]->officesuite",
				"libreoffice:200-[has-part]->writer:201",
				"libreoffice:200-[has-part]->calc:202",
				"libreoffice:200-[has-part]->impress:203",
				"officesuite-[contained-in]->software",
				"officesuite-[contains]->writer:201",
				"officesuite-[contains]->libreoffice:200",
				"officesuite-[contains]->impress:203",
				"officesuite-[contains]->calc:202",
				"software-[contains]->officesuite",
				"software-[contains]->operatingsystem",
				"operatingsystem-[contained-in]->software",
				"operatingsystem-[contains]->centos7:107",
				"operatingsystem-[contains]->centos6:106",
				"operatingsystem-[contains]->centos5:105"
		};
		testEdges(input, nodes);
	}

	private void testEdges(String[] input, String[] expectedLines) {
		List<Matcher<String>> matchers = joinAsIsMatchers(expectedLines);
		runs = new Run[] {
				new LineRun("edges", KitMatchers.inAnyOrder(matchers)),
				new NoOutputRun("quit")
		};
		sessionTest(runs, Input.getFile(input));
	}

}
