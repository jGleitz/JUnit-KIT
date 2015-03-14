package final1.subtests;

import static org.junit.Assert.fail;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;

/**
 * Launches the program with a valid input file and then tests some illegal recommend commands.
 *
 * @author Roman Langrehr
 * @since 12.03.2015
 * @version 1.0
 *
 */
public class InvalidRecommendTerms extends RecommendationSubtest {
	private String[] commands;

	public InvalidRecommendTerms() {
		setExpectedSystemStatus(SystemExitStatus.EXACTLY.status(1));
	}

	@Test
	public void incomplete() {
		fail("This test is still in the development state and therefore incomplete!");
	}

	/**
	 * Asserts that the program detects very simple invalid recommend terms (like an empty recommend term)
	 */
	@Test
	public void simpleInvalidTest() {
		commands = new String[] {
			"recommend"
		};
		errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

		commands = new String[] {
			"recommend "
		};
		errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

		commands = new String[] {
			"recommend S1"
		};
		errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

		commands = new String[] {
			"recommend S1, 105"
		};
		errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));
	}

	/**
	 * Asserts that the program detects terms with invalid brackets
	 */
	@Test
	public void illegalBracketsTest() {
		String[] TERMS = new String[] {
				"UNION",
				"INTERSECTION"
		};

		for (String term : TERMS) {
			commands = new String[] {
				"recommend (S1 105)"
			};
			errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

			commands = new String[] {
				"recommend " + term + "(S1 105, S3 105))"
			};
			errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

			commands = new String[] {
				"recommend " + term + "((S1 105, S3 105)"
			};
			errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

			commands = new String[] {
				"recommend " + term + "((S1 105, S3 105))"
			};
			errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

			commands = new String[] {
				"recommend " + term + "(S1 105, UNION(S2 105, S3 105)"
			};
			errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

			commands = new String[] {
				"recommend " + term + "(S1 105, UNION(S2 105, S3 105)))"
			};
			errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));
		}
	}

	/**
	 * Asserts that the program detects invalid arguments for UNION(...) and INTERSECTION(...)
	 */
	@Test
	public void illegalComplexTermArguments() {
		String[] TERMS = new String[] {
				"UNION",
				"INTERSECTION"
		};

		for (String term : TERMS) {
			commands = new String[] {
				"recommend " + term + "(S1 105)"
			};
			errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

			commands = new String[] {
				"recommend " + term + "(S1 105, S1 106, S1 107)"
			};
			errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

			commands = new String[] {
				"recommend " + term + "(S1 105, S1 106, S1 107)"
			};
			errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

			commands = new String[] {
				"recommend " + term + "(S1 105,, S1 106)"
			};
			errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));
		}
	}

	/**
	 * Asserts that the program detects semantic errors like not existing search strategies or product ids.
	 */
	@Test
	public void semanticErrors() {
		commands = new String[] {
			"recommend S4 105"
		};
		errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

		commands = new String[] {
			"recommend S1 25318" // product id doesn't exist
		};
		errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

		commands = new String[] {
			"recommend S1(S2 105, S3 106)"
		};
		errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));
	}

	/**
	 * Asserts that the program detects invalid product ids.
	 */
	@Test
	public void invalidProductIds() {
		commands = new String[] {
			"recommend S1 1a2"
		};
		errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

		commands = new String[] {
			"recommend S1 1 2"
		};
		errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

		commands = new String[] {
			"recommend S1 efj"
		};
		errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));

		commands = new String[] {
			"recommend S1 999999999999999999999999999999999999999999999999999999999999999999999999999999999"
		};
		errorTest(addQuit(commands), Input.getFile(TASK_SHEET_INPUT_FILE));
	}
}