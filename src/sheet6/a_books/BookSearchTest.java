package sheet6.a_books;

import static org.junit.Assert.fail;
import static test.KitMatchers.containsExactlyDividedBy;

import org.junit.Test;

import test.Input;
import test.InteractiveConsoleTest;
import test.TestObject;
import test.TestObject.SystemExitStatus;

/**
 * A test for the Interactive Console (Task A) <br>
 * This test is based on the test for Sheet 5 Task C. The test is still under development.
 * 
 * !!! The info command is not tested at all at the moment !!!
 * 
 * @author Roman Langrehr
 * @since 30.01.2015
 * @version 0.1
 *
 */
public class BookSearchTest extends InteractiveConsoleTest {
	/*
	 * PREDEFINED BOOK INPUT FILES
	 */
	private String[] file;

	protected static final String[] book1 = new String[] {
			"Seite1",
			"lorem ipsum dolor sit amet consetetur sadipscing",
			"lorem ipsum dolor sit amet consetetur sadipscing",
			"test1",
			"Seite2",
			"lorem test2 amet"
	};
	protected static final String[] book2 = new String[] {
		"Seite1"
	};
	protected static final String[] book4 = new String[] {
			"Seite1",
			"Seite2",
			"lorem test2 amet"
	};
	protected static final String[] book5 = new String[] {
			"",
			"Seite1",
			""
	};
	protected static final String[] book61 = new String[] {
			"Seite1",
			"word1"
	};
	protected static final String[] book62 = new String[] {
			"Seite1",
			"word1",
			"Seite2",
			"word1"
	};
	protected static final String[] book7 = new String[] {
			"Seite1",
			"b c a",
			"Seite2",
			"b d",
			"Seite3",
			"aa ca a e"
	};


	/**
	 * Tests for an error message, when the user types an illegal command.
	 */
	@Test
	public void wrongCommand() {
		TestObject.allowSystemExit(SystemExitStatus.ALL);

		command = "bla";

		errorTest(addQuit(command), Input.getFile(book1));
	}

	/**
	 * Tests for an error message, when the user executes search without a keyword.
	 */
	@Test
	public void searchNoArg() {
		TestObject.allowSystemExit(SystemExitStatus.ALL);

		command = "search";

		errorTest(addQuit(command), Input.getFile(book1));
	}

	/**
	 * Tests for an error message, when the user executes search without two keyword.
	 */
	@Test
	public void searchTwoArgs() {
		TestObject.allowSystemExit(SystemExitStatus.ALL);

		command = "search word1 word2";
		errorTest(addQuit(command), Input.getFile(book1));
	}

	/**
	 * Launches the program with an not-existing file path as argument
	 */
	@Test
	public void testFileDoesNotExist() {
		TestObject.allowSystemExit(SystemExitStatus.ALL);

		command = "quit";
		errorTest(command, "C:/DieseDateiHatHoffentlichNiemand.txt");
	}

	/**
	 * Launches the program without the required file-path attribute.
	 */
	@Test
	public void testNoArgs() {
		TestObject.allowSystemExit(SystemExitStatus.ALL);

		command = "quit";
		errorTest(command);
	}

	/**
	 * Launches the program with two arguments (but exactly one is required).
	 */
	@Test
	public void testTooManyArgs() {
		TestObject.allowSystemExit(SystemExitStatus.ALL);

		command = "quit";
		errorTest(command, Input.getFile(book1), Input.getFile(book1));
	}

	/**
	 * Searches for a keyword which appearers on one page.
	 */
	@Test
	public void search1() {
		commands = new String[] {
				"search ipsum",
				"quit"
		};
		expectedResults = new String[] {
			"ipsum:1"
		};
		multiLineTest(commands, expectedResults, Input.getFile(book1));
	}

	/**
	 * Searches for a keyword which appearers on two pages.
	 */
	@Test
	public void search2() {
		commands = new String[] {
				"search lorem",
				"quit"
		};
		expectedResults = new String[] {
			"lorem:1,2"
		};
		multiLineTest(commands, expectedResults, Input.getFile(book1));
	}

	/**
	 * Searches for a keyword which doesn't appear on any page.
	 */
	@Test
	public void searchNull() {
		commands = new String[] {
				"search muellll",
				"quit"
		};
		expectedResults = new String[] {
			"muellll:null"
		};
		multiLineTest(commands, expectedResults, Input.getFile(book1));
	}

	/**
	 * Launches the program with a valid book with an empty page.
	 */
	@Test
	public void emptyPage() {
		oneLineTest("quit", "", Input.getFile(book4));
	}

	/**
	 * Launches the program with a valid book file containing empty pages.
	 */
	@Test
	public void emptyLine() {
		oneLineTest("quit", "", Input.getFile(book5));
	}

	/**
	 * Executes a search command on an book containing empty files.
	 */
	@Test
	public void searchOnEmptyLine() {
		commands = new String[] {
				"search amet",
				"quit"
		};
		expectedResults = new String[] {
			"amet:2"
		};
		multiLineTest(commands, expectedResults, Input.getFile(book4));
	}

	/**
	 * Calls insert with an argument.
	 */
	@Test
	public void testInfoWithArg() {
		TestObject.allowSystemExit(SystemExitStatus.ALL);

		commands = new String[] {
				"info ipsum",
				"quit"
		};
		errorTest(commands, Input.getFile(book1));
	}

	/**
	 * Calls quit with an argument.
	 */
	@Test
	public void testQuitWithArg() {
		TestObject.allowSystemExit(SystemExitStatus.ALL);

		commands = new String[] {
				"quit ipsum",
				"quit"
		};
		errorTest(commands, Input.getFile(book1));
	}

	/**
	 * Just a regular quit
	 */
	@Test
	public void testQuit() {
		command = "quit";
		oneLineTest(command, "", Input.getFile(book1));
	}

	/**
	 * Tests the {@code info} command. Asserts that:
	 * <ul>
	 * <li>The output of {@code info} is exactly as defined on the task sheet.
	 */
	@Test
	public void testInfo() {
		command = "info";
		file = new String[] {
				"Seite1",
				"Hallo Welt",
				"Seite2",
				"Hallo Menschen"
		};
		expectedResults = new String[] {
				"Hallo:1,2",
				"Welt:1",
				"Menschen:2"
		};
		expectedResultMatcher = containsExactlyDividedBy(expectedResults, ",");
		oneLineTest(addQuit(command), expectedResultMatcher, Input.getFile(file));

		file = new String[] {
				"Seite1",
				"lorem ipsum dolor sit amet consetetur sadipscing",
				"sed diam nonumy eirmod tempor invidunt",
				"Seite2",
				"ut labore et dolore magna",
				"aliquyam erat sed diam voluptua",
				"Seite3",
				"at vero eos et accusam et justo duo",
				"olores et ea rebum stet clita kasd gubergren",
				"no sea takimata sanctus est lorem ipsum dolor sit amet"
		};
		expectedResults = new String[] {
				"accusam:3",
				"aliquyam:2",
				"amet:1,3",
				"at:3",
				"clita:3",
				"consetetur:1",
				"diam:1,2",
				"dolor:1,3",
				"dolore:2",
				"duo:3",
				"ea:3",
				"eirmod:1",
				"eos:3",
				"erat:2",
				"est:3",
				"et:2,3",
				"gubergren:3",
				"invidunt:1",
				"ipsum:1,3",
				"justo:3",
				"kasd:3",
				"labore:2",
				"lorem:1,3",
				"magna:2",
				"no:3",
				"nonumy:1",
				"olores:3",
				"rebum:3",
				"sadipscing:1",
				"sanctus:3",
				"sea:3",
				"sed:1,2",
				"sit:1,3",
				"stet:3",
				"takimata:3",
				"tempor:1",
				"ut:2",
				"vero:3",
				"voluptua:2"
		};
		expectedResultMatcher = containsExactlyDividedBy(expectedResults, ",");
		oneLineTest(addQuit(command), expectedResultMatcher, Input.getFile(file));
	}
}