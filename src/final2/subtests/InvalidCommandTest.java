package final2.subtests;

import org.junit.Test;

import test.Input;
import test.runs.ErrorRun;
import test.runs.NoOutputRun;
import test.runs.Run;

/**
 * Tests the program for error messages with not well-formed or invalid commands.
 * 
 * @author Roman Langrehr
 * @since 26.03.2015
 * 
 */
public class InvalidCommandTest extends LangtonSubtest {

	private static final String[] ARGUMENT_COMMANDS = new String[] {
			"create",
			"field",
			"position",
			"direction",
			"escape",
			"move"
	};

	private static final String[] INVALID_ANT_NAMES = new String[] {
			"ab",
			"\u00e4", // ae
			"$",
			"*"
	};

	private static final String[] INVALID_NUMBERS = new String[] {
			"a",
			"1t",
			"two",
			"1.5",
			"$",
			"9999999999999999999999999999999999999999999999999999999999999999"
	};

	private static final String[] NO_ARGUMENT_COMMANDS = new String[] {
			"ant",
			"print",
			"quit"
	};

	private static final String[] OUT_OF_BOUNDS_COORDINATES_FOR_4X4 = new String[] {
			"-1",
			"4"
	};

	private static final String[] VALID_ARGUMENTS = new String[] {
			"h,1,0",
			"1,0",
			"a",
			"a",
			"a",
			"1"
	};

	/**
	 * Asserts that the program avoids the creation of ants on blocked cells.
	 */
	@Test
	public void createAntOnBlockedCellTest() {
		inputFile = new String[] {
				"0000",
				"0*00",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ErrorRun("create h,1,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"0000",
				"0b00",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ErrorRun("create h,1,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ErrorRun("create a,2,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"0000",
				"0000",
				"0A00",
				"0000"
		};
		runs = new Run[] {
				new NoOutputRun("move 1"),
				new ErrorRun("create h,1,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}

	/**
	 * Asserts that the program avoids the situation with two ants with the same name
	 */
	@Test
	public void createExistingAntTest() {
		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ErrorRun("create a,1,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new NoOutputRun("create b,0,0"),
				new ErrorRun("create b,1,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}

	/**
	 * Asserts that the program handles invalid ant names
	 */
	@Test
	public void invalidAntNameTest() {
		for (String invalidAntName : INVALID_ANT_NAMES) {
			inputFile = new String[] {
					"0000",
					"0000",
					"0a00",
					"0000"
			};
			runs = new Run[] {
					new ErrorRun("create " + invalidAntName + ",1,1"),
					quit()
			};
			sessionTest(runs, Input.getFile(inputFile));

			runs = new Run[] {
					new ErrorRun("direction " + invalidAntName),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

			runs = new Run[] {
					new ErrorRun("escape " + invalidAntName),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

			runs = new Run[] {
					new ErrorRun("position " + invalidAntName),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));
		}
	}

	/**
	 * Asserts that the program detects not existing commands.
	 */
	@Test
	public void invalidCommandNameTest() {
		runs = new Run[] {
				new ErrorRun("blablabla"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));
	}

	/**
	 * Asserts that the program handles invalid numbers.
	 */
	@Test
	public void invalidNumberTests() {
		for (String invalidNumber : INVALID_NUMBERS) {
			runs = new Run[] {
					new ErrorRun("move " + invalidNumber),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

			runs = new Run[] {
					new ErrorRun("create h,1," + invalidNumber),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

			runs = new Run[] {
					new ErrorRun("create h," + invalidNumber + ",1"),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

			runs = new Run[] {
					new ErrorRun("field 1," + invalidNumber),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

			runs = new Run[] {
					new ErrorRun("field " + invalidNumber + ",1"),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));
		}
	}

	/**
	 * Asserts that the program reacts correctly on commands with missing arguments
	 */
	@Test
	public void missingArgumentTest() {
		runs = new Run[] {
				new ErrorRun("move"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("create h,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("create 2,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("create"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("escape"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("position"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("field 1"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("field"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("direction"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("escape"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));
	}

	/**
	 * Checks whether the program reacts correctly to "get"-calls with not existing ants
	 */
	@Test
	public void notExistingAntTest() {
		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ErrorRun("direction b"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ErrorRun("field b"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ErrorRun("escape b"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		// Same test, expect that we remove the ant here dynamically.
		inputFile = new String[] {
				"0000",
				"00b0",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new NoOutputRun("escape b"),
				new ErrorRun("direction b"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"0000",
				"00b0",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new NoOutputRun("escape b"),
				new ErrorRun("escape b"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}

	/**
	 * Asserts that the program recognizes arguments on commands that don't need arguments.
	 */
	@Test
	public void notRequiredArgumentTest() {
		runs = new Run[] {
				new ErrorRun("print arg"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("ant arg"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("quit arg"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));
	}

	/**
	 * Asserts that program manages out of bound coordinates
	 */
	@Test
	public void outOfBoundsCoordinatesTest() {
		for (String outOfBoundsCoordinats : OUT_OF_BOUNDS_COORDINATES_FOR_4X4) {
			inputFile = new String[] {
					"0000",
					"0000",
					"0a00",
					"0000"
			};
			runs = new Run[] {
					new ErrorRun("create h,1," + outOfBoundsCoordinats),
					quit()
			};
			sessionTest(runs, Input.getFile(inputFile));

			inputFile = new String[] {
					"0000",
					"0000",
					"0a00",
					"0000"
			};
			runs = new Run[] {
					new ErrorRun("create h," + outOfBoundsCoordinats + ",1"),
					quit()
			};
			sessionTest(runs, Input.getFile(inputFile));

			inputFile = new String[] {
					"0000",
					"0000",
					"0a00",
					"0000"
			};
			runs = new Run[] {
					new ErrorRun("field 1," + outOfBoundsCoordinats),
					quit()
			};
			sessionTest(runs, Input.getFile(inputFile));

			inputFile = new String[] {
					"0000",
					"0000",
					"0a00",
					"0000"
			};
			runs = new Run[] {
					new ErrorRun("field " + outOfBoundsCoordinats + ",1"),
					quit()
			};
			sessionTest(runs, Input.getFile(inputFile));
		}
	}

	/**
	 * Asserts that the program detects too many arguments for commands that need at least one argument
	 */
	@Test
	public void tooManyArgumentsTest() {
		runs = new Run[] {
				new ErrorRun("move 1,2"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("create h,1,2,3"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("create c,d,2,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("escape c,d"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("position c,d"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("field 1,2,3"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("direction c,d"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("escape c,d"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));
	}

	/**
	 * Asserts that the program handles " " and "," differently.
	 */
	@Test
	public void whitespaceCommaMixedUpTest() {
		inputFile = new String[] {
				"b000",
				"0000",
				"0a00",
				"Z000"
		};
		runs = new Run[] {
				new ErrorRun("create h 1,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"b000",
				"0000",
				"0a00",
				"Z000"
		};
		runs = new Run[] {
				new ErrorRun("create,h,1,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		runs = new Run[] {
				new ErrorRun("field 1 1"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ErrorRun("field,1,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		for (int i = 0; i < ARGUMENT_COMMANDS.length; i++) {
			runs = new Run[] {
					new ErrorRun(ARGUMENT_COMMANDS[i] + "," + VALID_ARGUMENTS[i]),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));
		}
	}

	@Test
	public void wrongCommaTest() {
		for (String commandName : NO_ARGUMENT_COMMANDS) {
			runs = new Run[] {
					new ErrorRun(commandName + " ,"),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));
		}

		for (int i = 0; i < ARGUMENT_COMMANDS.length; i++) {
			runs = new Run[] {
					new ErrorRun(ARGUMENT_COMMANDS[i] + " ," + VALID_ARGUMENTS[i]),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

			runs = new Run[] {
					new ErrorRun(ARGUMENT_COMMANDS[i] + " " + VALID_ARGUMENTS[i] + ","),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));
		}

		inputFile = new String[] {
				"b000",
				"0000",
				"0a00",
				"Z000"
		};
		runs = new Run[] {
				new ErrorRun("create h,,1,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"b000",
				"0000",
				"0a00",
				"Z000"
		};
		runs = new Run[] {
				new ErrorRun("create h,1,,,,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"b000",
				"0000",
				"0a00",
				"Z000"
		};
		runs = new Run[] {
				new ErrorRun("field 1,,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}

	/**
	 * Tests the program to handle wrong whitespace characters.
	 */
	@Test
	public void wrongWhitespacesTest() {
		for (String commandName : NO_ARGUMENT_COMMANDS) {
			runs = new Run[] {
					new ErrorRun(commandName + " "),
					quit()
			};

			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

			runs = new Run[] {
					new ErrorRun(commandName + "  "),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

			runs = new Run[] {
					new ErrorRun(" " + commandName),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));
		}

		for (int i = 0; i < ARGUMENT_COMMANDS.length; i++) {
			runs = new Run[] {
					new ErrorRun(ARGUMENT_COMMANDS[i] + "  "),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

			runs = new Run[] {
					new ErrorRun(ARGUMENT_COMMANDS[i] + "  " + VALID_ARGUMENTS[i]),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

			runs = new Run[] {
					new ErrorRun(" " + ARGUMENT_COMMANDS[i] + " " + VALID_ARGUMENTS[i]),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

			runs = new Run[] {
					new ErrorRun(ARGUMENT_COMMANDS[i] + " " + VALID_ARGUMENTS[i] + " "),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

			runs = new Run[] {
					new ErrorRun(ARGUMENT_COMMANDS[i] + " " + VALID_ARGUMENTS[i] + "  "),
					quit()
			};
			sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));
		}
	}
}