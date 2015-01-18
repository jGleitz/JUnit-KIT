package sheet5.b_cities;

import org.junit.Test;

import test.InteractiveConsoleTest;
import test.TestObject;
import test.TestObject.SystemExitStatus;

/**
 * A test for the Interactive Console (Task B) <br>
 * <br>
 * People that checked this test for being correct and complete:
 * <ul>
 * <li>Roman Langrehr</li>
 * <li>Joshua Gleitze</li>
 * </ul>
 * <br>
 * <br>
 * Things that are currently not tested, but should be:
 * <ul>
 * <li>None</li>
 * </ul>
 * 
 * @author Roman Langrehr
 * @author Joshua Gleitze
 * @since 05.01.2015
 * @version 1.0
 *
 */
public class CitiesInteracitveConsoleTest extends InteractiveConsoleTest {

	/**
	 * Tests the {@code quit} command of the interactive console. Asserts that:
	 * <ul>
	 * <li>the program can be terminated by running the command {@code quit}</li>
	 * <li>the program is not terminated by System.exit(x) with x>0</li>
	 * <li>the program does not output anything when being terminated through {@code quit}</li>
	 * </ul>
	 */
	@Test
	public void testConsoleQuit() {
		oneLineTest("quit\n", "", "traverse=pre-order");
	}

	/**
	 * Tests the program without a command line parameter. Asserts that:
	 * <ul>
	 * <li>the program can be terminated by running the command {@code quit}</li>
	 * <li>the program is not terminated by System.exit(x) with x>0</li>
	 * <li>the program does not output anything when being terminated through {@code quit}</li>
	 * </ul>
	 */
	@Test
	public void testNoArgs() {
		oneLineTest("quit\n", "");
	}

	/**
	 * Tests the program with a bad command line parameter. Asserts that:
	 * <ul>
	 * <li>the program terminates
	 * <li>if {@code System.exit(x)} is called, {@code x}>0
	 * <li>an error message is printed
	 * </ul>
	 */
	@Test
	public void testWrongArgs() {
		TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
		errorTest("quit", "blablabla");
		errorTest("quit", "traverse=blablabla");
		errorTest("quit", "traverse=pre-order=blabla");
	}

	/**
	 * Tests the {@code insert} command. Does <span class="strong">not</span> check whether insertions were executed
	 * correctly. Asserts that:
	 * <ul>
	 * <li>Assertion works without error messages or exceptions.
	 * </ul>
	 */
	@Test
	public void simpleInsertTest() {
		String[] commands;

		commands = new String[] {
				"insert Hintertupfingen:3",
				"quit"
		};
		oneLineTest(commands, "", "traverse=pre-order");

		commands = new String[] {
				"insert Hintertupfingen:3",
				"insert Hamburg:5",
				"quit"
		};
		oneLineTest(commands, "", "traverse=pre-order");

		commands = new String[] {
				"insert Hintertupfingen:3",
				"insert Hamburg:5",
				"insert München:6",
				"quit"
		};
		oneLineTest(commands, "", "traverse=pre-order");

		commands = new String[] {
				"insert Hintertupfingen:3",
				"insert Hamburg:5",
				"insert München:6",
				"insert Hintertupfingen:20000",
				"quit"
		};
		oneLineTest(commands, "", "traverse=pre-order");

		commands = new String[] {
				"insert Hintertupfingen:3",
				"insert Hamburg:5",
				"insert München:6",
				"insert Hamburg:0",
				"insert Hintertupfingen:20000",
				"quit"
		};
		oneLineTest(commands, "", "traverse=pre-order");
	}

	/**
	 * Uses the {@code insert} command to insert and values and {@code search} to retrieve them afterwards. Asserts
	 * that:
	 * <ul>
	 * <li>{@code search} outputs {@code insert}ed elements consistently
	 * <li>{@code insert} rewrites elements of existing keys
	 * </ul>
	 */
	@Test
	public void insertSearchTest() {
		String[] commands;
		String expectedResult;
		String[] expectedResults;

		commands = new String[] {
				"insert Hintertupfingen:3",
				"search Hintertupfingen",
				"quit"
		};
		expectedResult = "Hintertupfingen:3";
		oneLineTest(commands, expectedResult, "traverse=pre-order");

		commands = new String[] {
				"insert Pfaffenhofen:1",
				"insert Hintertupfingen:3",
				"search Hintertupfingen",
				"quit"
		};
		expectedResult = "Hintertupfingen:3";
		oneLineTest(commands, expectedResult, "traverse=pre-order");

		commands = new String[] {
				"insert Pfaffenhofen:1",
				"insert Hintertupfingen:3",
				"search Pfaffenhofen",
				"quit"
		};
		expectedResult = "Pfaffenhofen:1";
		oneLineTest(commands, expectedResult, "traverse=pre-order");

		commands = new String[] {
				"insert Pfaffenhofen:1",
				"insert Waldstadt:3",
				"search Waldstadt",
				"quit"
		};
		expectedResult = "Waldstadt:3";
		oneLineTest(commands, expectedResult, "traverse=pre-order");

		commands = new String[] {
				"insert Pfaffenhofen:1",
				"insert Waldstadt:3",
				"search Waldstadt",
				"search Pfaffenhofen",
				"quit"
		};
		expectedResults = new String[] {
				"Waldstadt:3",
				"Pfaffenhofen:1"
		};
		multiLineTest(commands, expectedResults, "traverse=pre-order");

		commands = new String[] {
				"insert Pfaffenhofen:1",
				"insert Waldstadt:3",
				"search Waldstadt",
				"search Pfaffenhofen",
				"insert Pfaffenhofen:25",
				"search Waldstadt",
				"search Pfaffenhofen",
				"insert Waldstadt:36",
				"insert Berlin:50",
				"search Waldstadt",
				"search Pfaffenhofen",
				"search Berlin",
				"quit"
		};
		expectedResults = new String[] {
				"Waldstadt:3",
				"Pfaffenhofen:1",
				"Waldstadt:3",
				"Pfaffenhofen:25",
				"Waldstadt:36",
				"Pfaffenhofen:25",
				"Berlin:50"
		};
		multiLineTest(commands, expectedResults, "traverse=pre-order");
	}
	
	/**
     * Uses the {@code insert} command to insert "complex" (City names can have spaces) values and {@code search} to retrieve them afterwards. Asserts
     * that:
     * <ul>
     * <li>{@code search} outputs {@code insert}ed elements consistently
     * </ul>
     */
    @Test
    public void insertSearchTestComplexNames() {
        String[] commands;
        String expectedResult;

        commands = new String[] {
                "insert New York:75",
                "search New York",
                "quit"
        };
        expectedResult = "New York:75";
        oneLineTest(commands, expectedResult, "traverse=pre-order");
        
        commands = new String[] {
                "insert Bad Sooden-Allendorf:3",
                "search Bad Sooden-Allendorf",
                "quit"
        };
        expectedResult = "Bad Sooden-Allendorf:3";
        oneLineTest(commands, expectedResult, "traverse=pre-order");

    }

	/**
	 * Tests the {@code search} command for non existent elements. Asserts that:
	 * <ul>
	 * <li>{@code search}'s output format is correct for non existent elements.
	 * </ul>
	 */
	@Test
	public void searchNull() {
		String[] commands;
		String expectedResult;
		String[] expectedResults;

		commands = new String[] {
				"insert Pfaffenhofen:1",
				"insert Waldstadt:3",
				"search Freudenstadt",
				"quit"
		};
		expectedResult = "Freudenstadt:null";
		oneLineTest(commands, expectedResult, "traverse=pre-order");

		commands = new String[] {
				"insert Pfaffenhofen:1",
				"insert Waldstadt:3",
				"search Freudenstadt",
				"insert Freudenstadt:66",
				"search Freudenstadt",
				"quit"
		};
		expectedResults = new String[] {
				"Freudenstadt:null",
				"Freudenstadt:66"
		};
		multiLineTest(commands, expectedResults, "traverse=pre-order");
	}

	/**
	 * Tests the {@code search} command on an empty tree. Asserts that:
	 * <ul>
	 * <li>{@code search}'s output format is correct for the non existent element on an empty tree.
	 * </ul>
	 */
	@Test
	public void searchOnEmptyTree() {
		String[] commands;
		String expectedResult;

		commands = new String[] {
				"search Freudenstadt",
				"quit"
		};
		expectedResult = "Freudenstadt:null";
		oneLineTest(commands, expectedResult, "traverse=pre-order");
	}

	@Test
	public void emptyPreOrderTest() {
		oneLineTest("info\nquit\n", "", "traverse=pre-order");
	}

	@Test
	public void emptyLevelOrderTest() {
		oneLineTest("info\nquit\n", "", "traverse=level-order");
	}

	@Test
	public void emptyInOrderTest() {
		oneLineTest("info\nquit\n", "", "traverse=in-order");
	}

	@Test
	public void preOrderTest() {
		oneLineTest("insert Hamburg:10\ninfo\nquit\n", "Hamburg:10", "traverse=pre-order");
	}

	@Test
	public void preOrderTest2() {
		oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninfo\nquit\n", "Hamburg:10,Muenchen:30",
				"traverse=pre-order");
	}

	@Test
	public void preOrderTest3() {
		oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninfo\nquit\n",
				"Hamburg:10,Aachen:9,Muenchen:30", "traverse=pre-order");
	}

	@Test
	public void preOrderTest4() {
		oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninsert Zuerich:3\ninfo\nquit\n",
				"Hamburg:10,Aachen:9,Muenchen:30,Zuerich:3", "traverse=pre-order");
	}

	@Test
	public void inOrderTest() {
		oneLineTest("insert Hamburg:10\ninfo\nquit\n", "Hamburg:10", "traverse=in-order");
	}

	@Test
	public void inOrderTest2() {
		oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninfo\nquit\n", "Hamburg:10,Muenchen:30",
				"traverse=in-order");
	}

	@Test
	public void inOrderTest3() {
		oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninfo\nquit\n",
				"Aachen:9,Hamburg:10,Muenchen:30", "traverse=in-order");
	}

	@Test
	public void inOrderTest4() {
		oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninsert Zuerich:3\ninfo\nquit\n",
				"Aachen:9,Hamburg:10,Muenchen:30,Zuerich:3", "traverse=in-order");
	}

	@Test
	public void inOrderTestWithoutParameter() {
		oneLineTest("insert Hamburg:10\ninfo\nquit\n", "Hamburg:10");
	}

	@Test
	public void inOrderTestWithoutParameter2() {
		oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninfo\nquit\n", "Hamburg:10,Muenchen:30");
	}

	@Test
	public void inOrderTestWithoutParameter3() {
		oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninfo\nquit\n",
				"Aachen:9,Hamburg:10,Muenchen:30");
	}

	@Test
	public void inOrderTestWithoutParameter4() {
		oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninsert Zuerich:3\ninfo\nquit\n",
				"Aachen:9,Hamburg:10,Muenchen:30,Zuerich:3");
	}

	@Test
	public void levelOrderTest() {
		oneLineTest("insert Hamburg:10\ninfo\nquit\n", "Hamburg:10", "traverse=level-order");
	}

	@Test
	public void levelOrderTest2() {
		oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninfo\nquit\n", "Hamburg:10,Muenchen:30",
				"traverse=level-order");
	}

	@Test
	public void levelOrderTest3() {
		oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninfo\nquit\n",
				"Hamburg:10,Aachen:9,Muenchen:30", "traverse=level-order");
	}

	@Test
	public void levelOrderTest4() {
		oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninsert Zuerich:3\ninfo\nquit\n",
				"Hamburg:10,Aachen:9,Muenchen:30,Zuerich:3", "traverse=level-order");
	}

	@Test
	public void levelOrderTest5() {
		oneLineTest(
				"insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninsert Zuerich:3\ninsert Aaachen:3\ninfo\nquit\n",
				"Hamburg:10,Aachen:9,Muenchen:30,Aaachen:3,Zuerich:3", "traverse=level-order");
	}

	@Test
	public void preOrderTest5() {
		oneLineTest(
				"insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninsert Zuerich:3\ninsert Aaachen:3\ninfo\nquit\n",
				"Hamburg:10,Aachen:9,Aaachen:3,Muenchen:30,Zuerich:3", "traverse=pre-order");
	}
}