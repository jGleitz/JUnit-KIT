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
 * @version 1.0
 *
 */
public class InvalidCommandTest extends LangtonSubtest {

	private static final String[] NO_ARGUMENT_COMMANDS = new String[] {
			"ant",
			"print",
			"quit"
	};

	private static final String[] ARGUMENT_COMMANDS = new String[] {
			"create",
			"position",
			"direction",
			"escape",
			"move"
	};

	private static final String[] VALID_ARGUMENTS = new String[] {
			"h,1,3",
			"1,2",
			"a",
			"a",
			"1"
	};

	private static final String[] INVALID_NUMBERS = new String[] {
			"a",
			"12t",
			"three",
			"12.5",
			"$",
			"9999999999999999999999999999999999999999999999999999999999999999"
	};

	private static final String[] INVALID_ANT_NAMES = new String[] {
			"ab",
			"§",
			"*"
	};

	/**
	 * Asserts that the program recognizes arguments on commands, that don't need arguments.
	 */
	@Test
	public void notRequiredArgumentTest() {
		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ErrorRun("print arg"),
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
				new ErrorRun("ant arg"),
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
				new ErrorRun("quit arg"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}

	/**
	 * Asserts that the program reacts correctly on commands with missing arguments
	 */
	@Test
	public void missingArgumentTest() {
		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ErrorRun("move"),
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
				new NoOutputRun("create h,1"),
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
				new NoOutputRun("create 2,1"),
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
				new NoOutputRun("create"),
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
				new NoOutputRun("escape"),
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
				new ErrorRun("position"),
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
				new ErrorRun("field 1"),
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
				new ErrorRun("field"),
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
				new ErrorRun("direction"),
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
				new ErrorRun("escape"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}

	/**
	 * Asserts that the program detects not existing commands.
	 */
	@Test
	public void invalidCommandNameTest() {
		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ErrorRun("blablabla"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}

	/**
	 * Asserts that the program detects too many arguments for commands that need at least one argument
	 */
	@Test
	public void tooManyArgumentsTest() {
		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ErrorRun("move 1,2"),
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
				new NoOutputRun("create h,1,2,3"),
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
				new NoOutputRun("create c,d,2,1"),
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
				new NoOutputRun("escape c,d"),
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
				new ErrorRun("position c,d"),
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
				new ErrorRun("field 1,2,3"),
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
				new ErrorRun("direction c,d"),
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
				new ErrorRun("escape c,d"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}

	/**
	 * Tests the program to handle wrong whitespace characters.
	 */
	public void wrongWhitespacesTest() {
		for (String commandName : NO_ARGUMENT_COMMANDS) {
			inputFile = new String[] {
					"0000",
					"0000",
					"0a00",
					"0000"
			};
			runs = new Run[] {
					new ErrorRun(commandName + " "),
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
					new ErrorRun(" " + commandName),
					quit()
			};
			sessionTest(runs, Input.getFile(inputFile));
		}

		for (int i = 0; i < ARGUMENT_COMMANDS.length; i++) {
			inputFile = new String[] {
					"b000",
					"0000",
					"0a00",
					"Z000"
			};
			runs = new Run[] {
					new ErrorRun(ARGUMENT_COMMANDS[i] + "  " + VALID_ARGUMENTS[i]),
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
					new ErrorRun(" " + ARGUMENT_COMMANDS[i] + " " + VALID_ARGUMENTS[i]),
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
					new ErrorRun(ARGUMENT_COMMANDS[i] + " " + VALID_ARGUMENTS[i] + " "),
					quit()
			};
			sessionTest(runs, Input.getFile(inputFile));
		}
	}

	@Test
	public void wrongCommaTest() {
		for (String commandName : NO_ARGUMENT_COMMANDS) {
			inputFile = new String[] {
					"0000",
					"0000",
					"0a00",
					"0000"
			};
			runs = new Run[] {
					new ErrorRun(commandName + " ,"),
					quit()
			};
			sessionTest(runs, Input.getFile(inputFile));
		}

		for (int i = 0; i < ARGUMENT_COMMANDS.length; i++) {
			inputFile = new String[] {
					"b000",
					"0000",
					"0a00",
					"Z000"
			};
			runs = new Run[] {
					new ErrorRun(ARGUMENT_COMMANDS[i] + " ," + VALID_ARGUMENTS[i]),
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
					new ErrorRun(ARGUMENT_COMMANDS[i] + " " + VALID_ARGUMENTS[i] + ","),
					quit()
			};
			sessionTest(runs, Input.getFile(inputFile));
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
				new ErrorRun("position 1,,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
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

		inputFile = new String[] {
				"b000",
				"0000",
				"0a00",
				"Z000"
		};
		runs = new Run[] {
				new ErrorRun("position 1 1"),
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
				new ErrorRun("position,1,1"),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		for (int i = 0; i < ARGUMENT_COMMANDS.length; i++) {
			inputFile = new String[] {
					"b000",
					"0000",
					"0a00",
					"Z000"
			};
			runs = new Run[] {
					new ErrorRun(ARGUMENT_COMMANDS[i] + "," + VALID_ARGUMENTS[i]),
					quit()
			};
			sessionTest(runs, Input.getFile(inputFile));
		}
	}

	/**
	 * Asserts that the program handles invalid numbers.
	 */
	@Test
	public void invalidNumberTests() {
		for (String invalidNumber : INVALID_NUMBERS) {
			inputFile = new String[] {
					"0000",
					"0000",
					"0a00",
					"0000"
			};
			runs = new Run[] {
					new ErrorRun("move " + invalidNumber),
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
					new ErrorRun("create h,1," + invalidNumber),
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
					new ErrorRun("create h," + invalidNumber + ",1"),
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
					new ErrorRun("position 1," + invalidNumber),
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
					new ErrorRun("position " + invalidNumber + ",1"),
					quit()
			};
			sessionTest(runs, Input.getFile(inputFile));
		}
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

			inputFile = new String[] {
					"0000",
					"0000",
					"0a00",
					"0000"
			};
			runs = new Run[] {
					new ErrorRun("direction " + invalidAntName),
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
					new ErrorRun("escape " + invalidAntName),
					quit()
			};
			sessionTest(runs, Input.getFile(inputFile));
		}
	}
}