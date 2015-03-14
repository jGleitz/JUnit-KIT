package final1.subtests;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;

/**
 * Starts the program with several valid input files without performing any actions. Checks if the tested class is able
 * to read in the files without output, exceptions or a call to {@code System.exit(x)} with {@code x>0}.
 * 
 * @author Joshua Gleitze
 * @author Martin Loeper
 * @version 1.1
 */
public class ValidInputFileTest extends RecommendationSubtest {

	private String[] input;

	public ValidInputFileTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	@Test
	public void complexTest() {
		noOutputTest("quit", Input.getFile(COMPLEX_INPUT_FILE));
	}

	/**
	 * Asserts that the tested class is able to read in input files that contain semantically dublicates.
	 */
	@Test
	public void duplicatesTest() {
		noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_DUPLICATES));
	}

	/**
	 * Asserts that the tested class is able to read in input files that have keywords as shop element names.
	 */
	@Test
	public void keywordTest() {
		input = new String[] {
				"contains contains containers",
				"containers contains dump(id=1)"
		};
		noOutputTest("quit", Input.getFile(input));

		input = new String[] {
				"contains (id=1) contained-in containers",
				"contains (id=1) part-of dump(id=2)"
		};
		noOutputTest("quit", Input.getFile(input));
	}

	@Test
	public void leadingZerosTest() {
		String[] file = new String[] {
				"CentOS5   ( id= 00105) contained-in operatingSystem",
				"centOS6  (  id  = 0000106) contained-in   OperatingSystem",
				"operatingSystem   contains centos7 ( id = 107 )",
				"operatingsystem contained-in Software  ",
				"CentOS7 (id=00107) successor-of centos6(id=106)", // id=00107 is equivalent to id=107, so it should
																	// work
				"CentOS5 (id=105) predecessor-of centos6(id=106)",
				"  writer (id=201) contained-in officesuite"
		};
		noOutputTest("quit", Input.getFile(file));
	}

	/**
	 * Asserts that the tested class is able to read in the one-lined test input files.
	 */
	@Test
	public void oneLineTest() {
		noOutputTest("quit", Input.getFile(ONE_LINE_INPUT_FILE1));
		noOutputTest("quit", Input.getFile(ONE_LINE_INPUT_FILE2));
	}

	/**
	 * Asserts that the tested class is able to read input files that define relations that may easily be confused with
	 * circles.
	 */
	@Test
	public void pseudoCirclesTest() {
		for (int r = 0; r < 6; r++) {
			input = new String[] {
					relation(r, 1, 2),
					reverse(r, 2, 3),
					reverse(r, 3, 1)
			};
			noOutputTest("quit", Input.getFile(input));

			input = new String[] {
					relation(r, 1, 2),
					reverse(r, 3, 2),
					relation(r, 3, 4),
					reverse(r, 4, 1)
			};
			noOutputTest("quit", Input.getFile(input));

			// diamond
			input = new String[] {
					relation(r, 2, 3),
					relation(r, 2, 4),
					relation(r, 3, 5),
					relation(r, 4, 5),
					relation(r, 1, 2)
			};
			noOutputTest("quit", Input.getFile(input));

			input = new String[] {
					relation(r, 2, 3),
					reverse(r, 4, 2),
					relation(r, 3, 5),
					reverse(r, 5, 4),
					relation(r, 1, 2)
			};
			noOutputTest("quit", Input.getFile(input));
		}

		for (int r = 0; r < 4; r++) {
			// circle by different relations
			input = new String[] {
					relation(r + 2, 1, 2),
					relation(r + 2, 2, 3),
					relation(r + 2, 3, 4),
					relation(((r + 2) % 4) + 2, 4, 5), // different relation
					relation(r + 2, 5, 6),
					relation(r + 2, 6, 7),
					relation(r + 2, 7, 8),
					relation(r + 2, 8, 1)
			};
			noOutputTest("quit", Input.getFile(input));

			// different order
			input = new String[] {
					relation(r + 2, 1, 2),
					relation(((r + 2) % 4) + 2, 2, 3), // different relation
					relation(r + 2, 3, 4),
					relation(r + 2, 8, 1),
					relation(r + 2, 4, 5),
					relation(r + 2, 5, 6),
					relation(r + 2, 6, 7),
					relation(r + 2, 7, 8),
			};
			noOutputTest("quit", Input.getFile(input));

			input = new String[] {
					relation(((r + 2) % 4) + 2, 1, 2), // different relation
					relation(r + 2, 2, 3),
					relation(((r + 1) % 4) + 2, 3, 4), // reverse
					relation(((r + 3) % 4) + 2, 4, 5), // different's reverse
					relation(((r + 2) % 4) + 2, 5, 6), // different relation
					relation(r + 2, 6, 7),
					relation(r + 2, 7, 8),
					reverse(r + 2, 1, 8)
			};
			noOutputTest("quit", Input.getFile(input));
			System.out.println(Arrays.toString(input));

			// different order
			input = new String[] {
					relation(r + 2, 4, 5),
					relation(r + 2, 1, 2),
					relation(r + 2, 2, 3),
					relation(r + 2, 7, 8),
					relation(r + 2, 3, 4),
					reverse(r + 2, 1, 8),
					relation(r + 2, 5, 6),
					relation(((r + 2) % 4) + 2, 6, 7), // different relation
			};
			noOutputTest("quit", Input.getFile(input));
		}
	}

	/**
	 * Asserts that the tested class is able to read in the input file from the task sheet, decorated with some legal
	 * spaces.
	 */
	@Test
	public void spacesTest() {
		noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_SPACES));
	}

	/**
	 * Asserts that the tested class is able to read in the input file given as an example on the task.
	 */
	@Test
	public void taskSheetInputFileTest() {
		noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE));
	}

	/**
	 * Asserts that 0 is a valid product ID.
	 */
	@Test
	public void zeroIdTest() {
		noOutputTest("quit", Input.getFile(ZERO_ID_INPUT_FILE));
	}
}
