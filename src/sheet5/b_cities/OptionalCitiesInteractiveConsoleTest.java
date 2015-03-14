package sheet5.b_cities;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

import org.junit.Test;

/**
 * A test for wrong inputs on the Interactive Console (Task B) <br>
 * <br>
 * People that checked this test for being correct and complete:
 * <ul>
 * <li>Roman Langrehr</li>
 * <li>Joshua Gleitze
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
 * @since 13.01.2015
 * @version 1.0
 *
 */
public class OptionalCitiesInteractiveConsoleTest extends CitiesInteracitveConsoleTest {

	/**
	 * Calls the {@code search} command with no arguments. Asserts that:
	 * <ul>
	 * <li>An error message is printed.
	 * </ul>
	 */
	@Test
	public void searchNoArg() {
		commands = new String[] {
				"insert Pfaffenhofen:1",
				"insert Hintertupfingen:3",
				"search",
				"quit"
		};
		errorTest(commands, "traverse=pre-order");
	}

	/**
	 * Calls the {@code search} command with no arguments. Asserts that:
	 * <ul>
	 * <li>An error message is printed.
	 * <li>The program continues to work correctly afterwards.
	 * </ul>
	 */
	@Test
	public void searchNoArgRecover() {
		commands = new String[] {
				"insert Pfaffenhofen:1",
				"insert Hintertupfingen:3",
				"search Hintertupfingen",
				"search",
				"search Hintertupfingen",
				"quit"
		};
		// @formatter:off
        expectedResultMatchers = getMatchers(
            is("Hintertupfingen:3"),
            startsWith("Error,"),
            is("Hintertupfingen:3")
                );
        // @formatter:on
		multiLineTest(commands, expectedResultMatchers, "traverse=pre-order");
	}

	/**
	 * Calls the {@code insert} command with no arguments. Asserts that:
	 * <ul>
	 * <li>An error message is printed.
	 * </ul>
	 */
	@Test
	public void insertNoArg() {
		commands = new String[] {
				"insert",
				"quit"
		};
		errorTest(commands, "traverse=pre-order");
	}

	/**
	 * Calls the {@code insert} command with no arguments. Asserts that:
	 * <ul>
	 * <li>An error message is printed.
	 * <li>The program continues to work correctly afterwards.
	 * </ul>
	 */
	@Test
	public void insertNoArgRecover() {
		commands = new String[] {
				"insert Pfaffenhofen:1",
				"insert Hintertupfingen:3",
				"search Hintertupfingen",
				"insert",
				"search Hintertupfingen",
				"quit"
		};
		// @formatter:off
        expectedResultMatchers = getMatchers(
            is("Hintertupfingen:3"),
            startsWith("Error,"),
            is("Hintertupfingen:3")
                );
        // @formatter:on
		multiLineTest(commands, expectedResultMatchers, "traverse=pre-order");
	}

	/**
	 * Calls the {@code search} command with a bad argument. Asserts that:
	 * <ul>
	 * <li>An error message is printed.
	 * </ul>
	 */
	@Test
	public void insertWrongArg() {
		commands = new String[] {
				"insert blablabla",
				"quit"
		};
		errorTest(commands, "traverse=pre-order");
	}

	@Test
	public void insertWrongArgRecover() {
		commands = new String[] {
				"insert Pfaffenhofen:1",
				"insert Hintertupfingen:3",
				"search Hintertupfingen",
				"insert blablabla",
				"search Hintertupfingen",
				"quit"
		};
		// @formatter:off
        expectedResultMatchers = getMatchers(
            is("Hintertupfingen:3"),
            startsWith("Error,"),
            is("Hintertupfingen:3")
                );
        // @formatter:on
		multiLineTest(commands, expectedResultMatchers, "traverse=pre-order");
	}

	@Test
	public void insertWrongArg2() {
		commands = new String[] {
				"insert bla:bla:bla",
				"quit"
		};
		errorTest(commands, "traverse=pre-order");
	}

	@Test
	public void insertWrongArg3() {
		commands = new String[] {
				"insert :",
				"quit"
		};
		errorTest(commands, "traverse=pre-order");
	}

	@Test
	public void insertWrongArg4() {
		commands = new String[] {
				"insert bla:",
				"quit"
		};
		errorTest(commands, "traverse=pre-order");
	}

	@Test
	public void wrongCommand() {
		commands = new String[] {
				"bla",
				"quit"
		};
		errorTest(commands, "traverse=pre-order");
	}

	@Test
	public void wrongCommandRecover() {
		commands = new String[] {
				"insert Pfaffenhofen:1",
				"insert Hintertupfingen:3",
				"search Hintertupfingen",
				"bla",
				"search Hintertupfingen",
				"quit"
		};
		// @formatter:off
        expectedResultMatchers = getMatchers(
            is("Hintertupfingen:3"),
            startsWith("Error,"),
            is("Hintertupfingen:3")
                );
        // @formatter:on
		multiLineTest(commands, expectedResultMatchers, "traverse=pre-order");
	}

	@Test
	public void wrongCommand2() {
		commands = new String[] {
				"bla bla",
				"quit"
		};
		errorTest(commands, "traverse=pre-order");
	}

	@Test
	public void multiWrongRecover() {
		commands = new String[] {
				"insert Pfaffenhofen:1",
				"insert Hintertupfingen:3",
				"search Hintertupfingen",
				"insert blablabla",
				"doof x:c",
				"doof",
				"search",
				"insert a:b:c:d:e",
				"insert :",
				"search Hintertupfingen",
				"quit"
		};
		// @formatter:off
        expectedResultMatchers = getMatchers(
            is("Hintertupfingen:3"),
            startsWith("Error,"),
            startsWith("Error,"),
            startsWith("Error,"),
            startsWith("Error,"),
            startsWith("Error,"),
            startsWith("Error,"),
            is("Hintertupfingen:3")
                );
        // @formatter:on
		multiLineTest(commands, expectedResultMatchers, "traverse=pre-order");
	}
}