package sheet6.a_books;

import static org.junit.Assert.fail;

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

	protected static final String[] book1 = new String[] {
			"Seite1",
			"Lorem ipsum dolor sit amet consetetur sadipscing",
			"Lorem ipsum dolor sit amet consetetur sadipscing",
			"test1",
			"Seite2",
			"Lorem test2 amet"
	};
	protected static final String[] book2 = new String[] {
		"Seite1"
	};
	protected static final String[] book4 = new String[] {
			"Seite1",
			"Seite2",
			"Lorem test2 amet"
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
	protected static final String[] empytBook = new String[] {};

	/*
	 * ERROR TESTS
	 */
	/**
	 * Fails the test as this test is incomplete an therefore does not grant anything. Remove this method as soon as the
	 * test reaches a certain degree of relevance.
	 */
	@Test
	public void incomplete() {
		fail("\n\nThis test is under development and incomplete!\nEspecially the info command is not tested at all the moment!\n\n");
	}

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
	 * Searches for a keyword which appearers on two page.
	 */
	@Test
	public void search2() {
		commands = new String[] {
				"search Lorem",
				"quit"
		};
		expectedResults = new String[] {
			"Lorem:1,2"
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
	 * Launches the program with a valid empty file (no pages)
	 */
	@Test
	public void emptyFile() {
		oneLineTest("quit", "", Input.getFile(empytBook));
	}

	/**
	 * Launches the program with a valid book file containing empty pages.
	 */
	@Test
	public void emptyLine() {
		oneLineTest("quit", "", Input.getFile(book5));
	}

	/**
	 * Executes a search command on an empty book.
	 */
	@Test
	public void searchOnEmptyBook() {
		commands = new String[] {
				"search hiergiebtsnichtszufinden",
				"quit"
		};
		expectedResults = new String[] {
			"hiergiebtsnichtszufinden:null"
		};
		multiLineTest(commands, expectedResults, Input.getFile(empytBook));
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
}