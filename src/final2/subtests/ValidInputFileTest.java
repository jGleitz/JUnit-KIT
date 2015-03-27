package final2.subtests;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;

/**
 * Asserts that all kinds of valid input files are accepted.
 * 
 * @author Annika Berger
 *
 */
public class ValidInputFileTest extends LangtonSubtest {
	public ValidInputFileTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * Asserts that valid square Input Files are accepted by the tested class.
	 */
	@Test
	public void squareInputFileTest() {
		sessionTest(onlyQuit(), Input.getFile(TASK_SHEET_INPUT_FILE_1));
		sessionTest(onlyQuit(), Input.getFile(TASK_SHEET_INPUT_FILE_2));
		sessionTest(onlyQuit(), Input.getFile(PUBLIC_PRAKTOMAT_TEST_FILE_1));
		sessionTest(onlyQuit(), Input.getFile(PUBLIC_PRAKTOMAT_TEST_FILE_2));
		sessionTest(onlyQuit(), Input.getFile(PUBLIC_PRAKTOMAT_TEST_FILE_3));
		sessionTest(onlyQuit(), Input.getFile(PUBLIC_PRAKTOMAT_TEST_FILE_4));
		inputFile = new String[] {
			"a"

		};
		sessionTest(onlyQuit(), Input.getFile(inputFile));
		inputFile = new String[] {
				"***",
				"*a*",
				"***",

		};
		sessionTest(onlyQuit(), Input.getFile(inputFile));
	}

	/**
	 * Asserts that valid rectangular (but not square) Input Files are accepted by the tested class.
	 */
	@Test
	public void rectangularInputFileTest() {
		inputFile = new String[] {
				"0000",
				"00a0"

		};
		sessionTest(onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"00",
				"a0",
				"00",
				"00"

		};
		sessionTest(onlyQuit(), Input.getFile(inputFile));
		inputFile = new String[] {
				"0",
				"a"

		};
		sessionTest(onlyQuit(), Input.getFile(inputFile));
		inputFile = new String[] {
			"0a"

		};
		sessionTest(onlyQuit(), Input.getFile(inputFile));
	}

	/**
	 * 
	 */
	@Test
	public void allAntsTest() {
		inputFile = new String[] {
				"abcdefg",
				"hijklmn",
				"opqrstu",
				"vwxyz00"
		};
		sessionTest(onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"ABCDEFG",
				"HIJKLMN",
				"OPQRSTU",
				"VWXYZ00"
		};
		sessionTest(onlyQuit(), Input.getFile(inputFile));
	}

}
