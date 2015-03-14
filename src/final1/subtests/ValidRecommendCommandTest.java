package final1.subtests;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.ErrorRun;
import test.runs.ExactRun;
import test.runs.NoOutputRun;
import test.runs.Run;

/**
 * Performs valid calls to the {@code recommend} command and checks the results.
 * 
 */
public class ValidRecommendCommandTest extends RecommendationSubtest {

	public ValidRecommendCommandTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	@Test
	public void complexTest() {
		runs = new Run[] {
				new ExactRun(
						"recommend S1 7",
						is("chromium:41,debian:78,ub1204lts:1204,ub1210:1210,ub1304:1304,ub1310:1310,ub1404lts:1404,vim:6")),
				new ExactRun("recommend S1 333", is("mso:666666")),
				new ExactRun("recommend S2 222", is("aoo:444,libreoffice:333,oracleoo:555")),
				new ExactRun("recommend S2 111", is("aoo:444,libreoffice:333,ooo:222,oracleoo:555")),
				new ExactRun("recommend S2 555", is("")),
				new ExactRun("recommend S3 222", is("staroffice:111")),
				new ExactRun("recommend S3 555", is("ooo:222,staroffice:111")),
				new ExactRun("recommend S2 4", is("blink:5,webkit:1,webkit2:2")),
				new ExactRun("recommend S3 333", is("ooo:222,staroffice:111")),
				new ExactRun("recommend S3 5", is("khtml:4,kjs:3,webkit:1")),
				new ExactRun("recommend S3 4", is("")),
				new ExactRun("recommend S3 3", is("")),
				new ExactRun("recommend INTERSECTION(S3 4, S3 3)", is("")),
				new ExactRun("recommend UNION(S3 4, S3 3)", is("")),
				new ExactRun("recommend UNION(INTERSECTION(S3 4, S3 3), UNION(S3 4, S3 3))", is("")),
				new ExactRun("recommend INTERSECTION(INTERSECTION(S3 4, S3 3), UNION(S3 4, S3 3))", is("")),
				new ExactRun("recommend S2 69", is("chucknorris:42,evil:666,god:0,hurd:1337")),
				new ExactRun("recommend S2 1337", is("")),
				new ExactRun("recommend S3 1337", is("chucknorris:42,evil:666,god:0,microsoft:69")),
				new ExactRun("recommend INTERSECTION(S2 69, S3 1337)", is("chucknorris:42,evil:666,god:0")),
				new ExactRun("recommend UNION(S2 69, S3 1337)",
						is("chucknorris:42,evil:666,god:0,hurd:1337,microsoft:69")),
				new ExactRun(
						"recommend UNION(UNION(S2 69, S3 1337), UNION(INTERSECTION(INTERSECTION(S3 4, S3 3), UNION(S3 4, S3 3)), UNION(S2 111, UNION(S3 222, UNION(S1 1404, S2 1310)))))",
						is("aoo:444,apt:7,chucknorris:42,debian:78,evil:666,god:0,hurd:1337,libreoffice:333,microsoft:69,ooo:222,oracleoo:555,staroffice:111,ub1204lts:1204,ub1210:1210,ub1304:1304,ub1310:1310,ub1404lts:1404")),
				new ExactRun(
						"recommend INTERSECTION(UNION(S2 69, S3 1337), UNION(INTERSECTION(INTERSECTION(S3 4, S3 3), UNION(S3 4, S3 3)), UNION(S2 111, UNION(S3 222, UNION(S1 1404, S2 1310)))))",
						is("")),
				new NoOutputRun("quit")
		};
		sessionTest(runs, Input.getFile(COMPLEX_INPUT_FILE));
	}

	/**
	 * Asserts correct results if the input file contains semantical duplicates.
	 */
	@Test
	public void duplicatesTest() {
		testAgainstTaskSheet(TASK_SHEET_INPUT_FILE_DUPLICATES);
	}

	@Test
    public void leadingZerosTest() {
      String[] file = new String[] {
          "CentOS5 ( id= 00105) contained-in operatingSystem",
          "centOS6 ( id = 0000106) contained-in OperatingSystem",
          "operatingSystem contains centos7 ( id = 107 )"
      };
      runs = new Run[] {
          new ExactRun("recommend S1 105", is("centos6:106,centos7:107")),
          new ExactRun("recommend UNION(S1 105,S1 107)", is("centos5:105,centos6:106,centos7:107")),
          new NoOutputRun("quit")
      };
      sessionTest(runs, Input.getFile(file));
    }

	/**
	 * Asserts overall correct behaviour of the implementation. This includes several error detection and recovery after
	 * errors.
	 */
	@Test
	public void oneLineTest() {
		runs = new Run[] {
				new ExactRun("recommend S1 1", is("")),
				new ErrorRun("recommend S1 3"),
				new ErrorRun("recommend S4 1"),
				new ExactRun("recommend S2 1", is("")),
				new ExactRun("recommend S2 2", is("b:1")),
				new ExactRun("recommend S3 1", is("a:2")),
				new ExactRun("recommend S3 2", is("")),
				new NoOutputRun("quit")
		};
		sessionTest(runs, Input.getFile(ONE_LINE_INPUT_FILE1));

		runs = new Run[] {
				new ErrorRun("recommend S1 1"),
				new ExactRun("recommend S1 2", is("")),
				new ExactRun("recommend S2 2", is("")),
				new ExactRun("recommend S3 2", is("")),
				new NoOutputRun("quit")
		};
		sessionTest(runs, Input.getFile(ONE_LINE_INPUT_FILE2));
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
	 * Asserts correct results for the example given on the task sheet.
	 */
	@Test
	public void taskSheetExampleTest() {
		testAgainstTaskSheet(TASK_SHEET_INPUT_FILE);
		testAgainstTaskSheet(TASK_SHEET_INPUT_FILE_SPACES); // Not sure if this is really needed
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

  private void testAgainstTaskSheet(String[] input) {
	// the following queries/matchers are taken directly from the task sheet
	runs = new Run[] {
			new ExactRun("recommend S1 105", is("centos6:106,centos7:107")),
			new ExactRun("recommend S3 107", is("centos5:105,centos6:106")),
			new ExactRun("recommend UNION(S1 105,S3 107)", is("centos5:105,centos6:106,centos7:107")),
			new ExactRun("recommend S1 201", is("calc:202,impress:203,libreoffice:200")),
			new ExactRun("recommend UNION(S1 201,INTERSECTION(S1 105,S3 107))",
					is("calc:202,centos6:106,impress:203,libreoffice:200")),
			new NoOutputRun("quit")
	};
	sessionTest(runs, Input.getFile(input));
}
}
