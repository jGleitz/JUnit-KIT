package final1.subtests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

import test.Input;
import test.SystemExitStatus;

/**
 * Performs valid calls to the {@code recommend} command and checks the results.
 * 
 */
public class ValidRecommendCommandTest extends RecommendationSubtest {

	public ValidRecommendCommandTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * Asserts correct results for the example given on the task sheet.
	 */
	@Test
	public void taskSheetExampleTest() {
		testAgainstTaskSheet(TASK_SHEET_INPUT_FILE);
		testAgainstTaskSheet(TASK_SHEET_INPUT_FILE_SPACES); // Not sure if this is really needed
	}

	/**
	 * Asserts correct results if the input file contains semantical duplicates.
	 */
	@Test
	public void duplicatesTest() {
		testAgainstTaskSheet(TASK_SHEET_INPUT_FILE_DUPLICATES);
	}

	private void testAgainstTaskSheet(String[] input) {
		// the following queries/matchers are taken directly from the task sheet
		String[] queries = new String[] {
				"recommend S1 105",
				"recommend S3 107",
				"recommend UNION(S1 105,S3 107)",
				"recommend S1 201",
				"recommend UNION(S1 201,INTERSECTION(S1 105,S3 107))",
		};
		// @formatter:off
        List<Matcher<String>> matchers = getMatchers(
            is("centos6:106,centos7:107"),
            is("centos5:105,centos6:106"),
            is("centos5:105,centos6:106,centos7:107"),
            is("calc:202,impress:203,libreoffice:200"),
            is("calc:202,centos6:106,impress:203,libreoffice:200")
        );
        // @formatter:on
		multiLineTest(addQuit(queries), matchers, Input.getFile(input));
	}

	/**
	 * Asserts correct results for different variations of spaces in the recommend command.
	 */
	@Test
	public void spacesTest() {
		String[] variants = {
				"recommend UNION(S1 201,INTERSECTION(S1 105,S3 107))",
				"recommend  UNION  (S1 201,INTERSECTION(S1 105,S3 107))",
				"recommend UNION  (S1 201,INTERSECTION(S1 105,S3 107))",
				"recommend UNION(S1  201,INTERSECTION(S1 105,S3 107))",
				"recommend UNION(S1 201  ,INTERSECTION(S1 105,S3 107))",
				"recommend UNION(S1 201,  INTERSECTION(S1 105,S3 107))",
				"recommend UNION(S1 201,INTERSECTION  (S1 105,S3 107))",
				"recommend UNION(S1 201,INTERSECTION(  S1 105,S3 107))",
				"recommend UNION(S1 201,INTERSECTION(S1 105   ,S3 107))",
				"recommend UNION(S1 201,INTERSECTION(S1   105,  S3 107))",
				"recommend UNION(S1 201,INTERSECTION(S1 105,S3  107))",
				"recommend UNION(S1 201,INTERSECTION(S1 105,S3 107  ))",
				"recommend UNION(S1 201,INTERSECTION(S1 105,S3 107)  )",
				"recommend UNION(S1 201,INTERSECTION(S1 105,S3 107))  ",
				"recommend        UNION      (     S1     201    ,    INTERSECTION    (    S1    105   ,   S3    107    )    )     "
		};
		for (String variant : variants) {
			oneLineTest(addQuit(variant), "calc:202,centos6:106,impress:203,libreoffice:200",
				Input.getFile(TASK_SHEET_INPUT_FILE));
		}
	}

	/**
	 * Asserts overall correct behaviour of the implementation. This includes several error detection and recovery after
	 * errors.
	 */
	@Test
	public void oneLineTest() {
		String[] queries = new String[] {
				"recommend S1 1",
				"recommend S1 3",
				"recommend S4 1",
				"recommend S2 1",
				"recommend S2 2",
				"redbutton",
				"recommend S3 1",
				"recommend S3 2"
		};
		// @formatter:off
		List<Matcher<String>> matchers = getMatchers(
			is(""),
			startsWith("Error,"),
			startsWith("Error,"),
			is(""),
			is("b:1"),
			startsWith("Error,"),
			is("a:2"),
			is("")
		);
		// @formatter:on
		// edges: new String[] { "b:1-[successor-of]->a", "a-[predecessor-of]->b" }
		multiLineTest(addQuit(queries), matchers, Input.getFile(ONE_LINE_INPUT_FILE1));

		queries = new String[] {
				"recommend S1 1",
				"recommend S1 2",
				"recommend S2 2",
				"recommend S3 2",
		};
		// @formatter:off
        matchers = getMatchers(
            startsWith("Error,"),
            is(""),
            is(""),
            is("")
        );
        // @formatter:on
		multiLineTest(addQuit(queries), matchers, Input.getFile(ONE_LINE_INPUT_FILE2));
	}

	/**
	 * Asserts that product ID 0 can be handled correctly.
	 */
	@Test
	public void zeroIdTest() {
		multiLineTest(addQuit(new String[] {
				"recommend S1 0",
				"recommend S1 1",
				"recommend S2 0"
		}), new String[] {
				"c:1",
				"b:0",
				""
		}, Input.getFile(ZERO_ID_INPUT_FILE));
	}

	@Test
	public void complexTest() {
		String[][] commandResultArray = new String[][] {
				{
						"S1 7",
						"chromium:41,debian:78,ub1204lts:1204,ub1210:1210,ub1304:1304,ub1310:1310,ub1404lts:1404,vim:6"
				},
				{
						"S1 333",
						"mso:666666"
				},
				{
						"S2 222",
						"aoo:444,libreoffice:333,oracleoo:555"
				},
				{
						"S2 111",
						"aoo:444,libreoffice:333,ooo:222,oracleoo:555"
				},
				{
						"S2 555",
						""
				},
				{
						"S3 222",
						"staroffice:111"
				},
				{
						"S3 555",
						"ooo:222,staroffice:111"
				},
				{
						"S2 4",
						"blink:5,webkit:1,webkit2:2"
				},
				{
						"S3 333",
						"ooo:222,staroffice:111"
				},
				{
						"S3 5",
						"khtml:4,kjs:3,webkit:1"
				},
				{
						"S3 4",
						""
				},
				{
						"S3 3",
						""
				},
				{
						"INTERSECTION(S3 4, S3 3)",
						""
				},
				{
						"UNION(S3 4, S3 3)",
						""
				},
				{
						"UNION(INTERSECTION(S3 4, S3 3), UNION(S3 4, S3 3))",
						""
				},
				{
						"INTERSECTION(INTERSECTION(S3 4, S3 3), UNION(S3 4, S3 3))",
						""
				},
				{
						"S2 69",
						"chucknorris:42,evil:666,god:0,hurd:1337"
				},
				{
						"S2 1337",
						""
				},
				{
						"S3 1337",
						"chucknorris:42,evil:666,god:0,microsoft:69"
				},
				{
						"INTERSECTION(S2 69, S3 1337)",
						"chucknorris:42,evil:666,god:0"
				},
				{
						"UNION(S2 69, S3 1337)",
						"chucknorris:42,evil:666,god:0,hurd:1337,microsoft:69"
				},
				{
						"UNION(UNION(S2 69, S3 1337), UNION(INTERSECTION(INTERSECTION(S3 4, S3 3), UNION(S3 4, S3 3)), UNION(S2 111, UNION(S3 222, UNION(S1 1404, S2 1310)))))",
						"aoo:444,apt:7,chucknorris:42,debian:78,evil:666,god:0,hurd:1337,libreoffice:333,microsoft:69,ooo:222,oracleoo:555,staroffice:111,ub1204lts:1204,ub1210:1210,ub1304:1304,ub1310:1310,ub1404lts:1404"
				},
				{
						"INTERSECTION(UNION(S2 69, S3 1337), UNION(INTERSECTION(INTERSECTION(S3 4, S3 3), UNION(S3 4, S3 3)), UNION(S2 111, UNION(S3 222, UNION(S1 1404, S2 1310)))))",
						""
				}
		};

		String queries[] = new String[commandResultArray.length];
		List<Matcher<String>> matchers = new ArrayList<>();
		for (int i = 0; i < commandResultArray.length; i++) {
			queries[i] = "recommend " + commandResultArray[i][0];
			matchers.add(is(commandResultArray[i][1]));
		}
		multiLineTest(addQuit(queries), matchers, Input.getFile(COMPLEX_INPUT_FILE));
	}

}
