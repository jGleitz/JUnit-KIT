package sheet5.b_cities;

import org.junit.Test;

import test.InteractiveConsoleTest;
import test.SystemExitStatus;
import test.TestObject;

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
 * @author childen
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
		command = "quit";
		oneLineTest(command, "", "traverse=pre-order");
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
		command = "quit";
		oneLineTest(command, "");
		oneLineTest(command, "");
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
		command = "quit";
		TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
		errorTest(command, "blablabla");
		errorTest(command, "traverse=blablabla");
		errorTest(command, "traverse=pre-order=blabla");
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
	 * Uses the {@code insert} command to insert "complex" (City names can have spaces) values and {@code search} to
	 * retrieve them afterwards. Asserts that:
	 * <ul>
	 * <li>{@code search} outputs {@code insert}ed elements consistently
	 * </ul>
	 */
	@Test
	public void insertSearchTestComplexNames() {
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
		
		commands = new String[] {
				"insert Ein langer Stadtname mit vielen    Leerzeichen:3",
				"search Ein langer Stadtname mit vielen    Leerzeichen",
				"quit"
		};
		expectedResult = "Ein langer Stadtname mit vielen    Leerzeichen:3";
		oneLineTest(commands, expectedResult, "traverse=pre-order");
		
		commands = new String[] {
				"insert Sonderzeichen üäö#*§$%&/()=?:3",
				"search Sonderzeichen üäö#*§$%&/()=?",
				"quit"
		};
		expectedResult = "Sonderzeichen üäö#*§$%&/()=?:3";
		oneLineTest(commands, expectedResult, "traverse=pre-order");
		
		commands = new String[] {
				"insert  :5",
				"search  ",
				"quit"
		};
		expectedResult = " :5";
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
	 * <li>the above is true for all valid traverse type arguments.
	 * </ul>
	 */
	@Test
	public void searchOnEmptyTree() {
		commands = new String[] {
				"search Freudenstadt",
				"quit"
		};
		expectedResult = "Freudenstadt:null";
		oneLineTest(commands, expectedResult, "traverse=pre-order");
		oneLineTest(commands, expectedResult, "traverse=in-order");
		oneLineTest(commands, expectedResult, "traverse=level-order");
		oneLineTest(commands, expectedResult);
	}

	/**
	 * Runs the {@code info} command on an empty tree. Asserts that:
	 * <ul>
	 * <li>there is no output
	 * <li>the above is true for all valid traverse type arguments.
	 * </ul>
	 */
	@Test
	public void emptyInfoTest() {
		commands = new String[] {
				"info",
				"quit"
		};
		oneLineTest(commands, "");
		oneLineTest(commands, "", "traverse=pre-order");
		oneLineTest(commands, "", "traverse=level-order");
		oneLineTest(commands, "", "traverse=in-order");
	}

	/**
	 * Runs the {@code info} command on different filled trees initialised with pre-order traversal. Asserts that:
	 * <ul>
	 * <li>elements are printed in the correct syntax and order.
	 * </ul>
	 */
	@Test
	public void preOrderTest() {
		commands = new String[] {
				"insert Hamburg:10",
				"info",
				"quit"
		};
		expectedResult = "Hamburg:10";
		oneLineTest(commands, expectedResult, "traverse=pre-order");

		commands = new String[] {
				"insert Hamburg:10",
				"insert Muenchen:30",
				"info",
				"quit"
		};
		expectedResult = "Hamburg:10,Muenchen:30";
		oneLineTest(commands, expectedResult, "traverse=pre-order");

		commands = new String[] {
				"insert Hamburg:10",
				"insert Muenchen:30",
				"insert Aachen:9",
				"info",
				"quit"
		};
		expectedResult = "Hamburg:10,Aachen:9,Muenchen:30";
		oneLineTest(commands, expectedResult, "traverse=pre-order");

		commands = new String[] {
				"insert Hamburg:10",
				"insert Muenchen:30",
				"insert Aachen:9",
				"insert Zuerich:3",
				"info",
				"quit"
		};
		expectedResult = "Hamburg:10,Aachen:9,Muenchen:30,Zuerich:3";
		oneLineTest(commands, expectedResult, "traverse=pre-order");

		commands = new String[] {
				"insert Hamburg:10",
				"insert Muenchen:30",
				"insert Aachen:9",
				"insert Zuerich:3",
				"insert Aaachen:3",
				"info",
				"quit"
		};
		expectedResult = "Hamburg:10,Aachen:9,Aaachen:3,Muenchen:30,Zuerich:3";
		oneLineTest(commands, expectedResult, "traverse=pre-order");
	}

	/**
	 * Runs the {@code info} command on different filled trees initialised with in-order traversal. Asserts that:
	 * <ul>
	 * <li>elements are printed in the correct syntax and order.
	 * <li>the above is as well true if the program hast been started without a command line argument.
	 * </ul>
	 */
	@Test
	public void inOrderTest() {
		commands = new String[] {
				"insert Hamburg:10",
				"info",
				"quit"
		};
		expectedResult = "Hamburg:10";
		oneLineTest(commands, expectedResult, "traverse=in-order");
		oneLineTest(commands, expectedResult);

		commands = new String[] {
				"insert Hamburg:10",
				"insert Muenchen:30",
				"info",
				"quit"
		};
		expectedResult = "Hamburg:10,Muenchen:30";
		oneLineTest(commands, expectedResult, "traverse=in-order");
		oneLineTest(commands, expectedResult);

		commands = new String[] {
				"insert Hamburg:10",
				"insert Muenchen:30",
				"insert Aachen:9",
				"info",
				"quit"
		};
		expectedResult = "Aachen:9,Hamburg:10,Muenchen:30";
		oneLineTest(commands, expectedResult, "traverse=in-order");
		oneLineTest(commands, expectedResult);

		commands = new String[] {
				"insert Hamburg:10",
				"insert Muenchen:30",
				"insert Aachen:9",
				"insert Zuerich:3",
				"info",
				"quit"
		};
		expectedResult = "Aachen:9,Hamburg:10,Muenchen:30,Zuerich:3";
		oneLineTest(commands, expectedResult, "traverse=in-order");
		oneLineTest(commands, expectedResult);
	}

	/**
	 * Runs the {@code info} command on different filled trees initialised with level-order traversal. Asserts that:
	 * <ul>
	 * <li>elements are printed in the correct syntax and order.
	 * </ul>
	 */
	@Test
	public void levelOrderTest() {
		commands = new String[] {
				"insert Hamburg:10",
				"info",
				"quit"
		};
		expectedResult = "Hamburg:10";
		oneLineTest(commands, expectedResult, "traverse=level-order");

		commands = new String[] {
				"insert Hamburg:10",
				"insert Muenchen:30",
				"info",
				"quit"
		};
		expectedResult = "Hamburg:10,Muenchen:30";
		oneLineTest(commands, expectedResult, "traverse=level-order");

		commands = new String[] {
				"insert Hamburg:10",
				"insert Muenchen:30",
				"insert Aachen:9",
				"info",
				"quit"
		};
		expectedResult = "Hamburg:10,Aachen:9,Muenchen:30";
		oneLineTest(commands, expectedResult, "traverse=level-order");

		commands = new String[] {
				"insert Hamburg:10",
				"insert Muenchen:30",
				"insert Aachen:9",
				"insert Zuerich:3",
				"info",
				"quit"
		};
		expectedResult = "Hamburg:10,Aachen:9,Muenchen:30,Zuerich:3";
		oneLineTest(commands, expectedResult, "traverse=level-order");

		commands = new String[] {
				"insert Hamburg:10",
				"insert Muenchen:30",
				"insert Aachen:9",
				"insert Zuerich:3",
				"insert Aaachen:3",
				"info",
				"quit"
		};
		expectedResult = "Hamburg:10,Aachen:9,Muenchen:30,Aaachen:3,Zuerich:3";
		oneLineTest(commands, expectedResult, "traverse=level-order");
	}
}