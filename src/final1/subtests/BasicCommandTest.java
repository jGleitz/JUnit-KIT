package final1.subtests;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

import test.Input;
import test.SystemExitStatus;

/**
 * Starts the program with either a simple one-line input file or the task sheet input file. Asserts that the program's
 * {@code quit} command works, responds correctly to erroneous commands and still works correctly after errors have been
 * provoked. The program must always quit with the zero ({@code 0}) exit status.
 */
public class BasicCommandTest extends RecommendationSubtest {

	private final static String[] INVALID_COMMANDS = {
			"bad",
			"nodes ",
			"nodes test",
			" nodes",
			"edges ",
			"edges test",
			" edges",
			"export ",
			"export test",
			" export",
			" recommend S1 105",
			"quit ",
			"quit test",
			" quit",
			"",
			" "
	};

	public BasicCommandTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * Asserts that the quit command works, using a simple one line input.
	 */
	@Test
	public void quitTest() {
		noOutputTest("quit", Input.getFile(ONE_LINE_INPUT_FILE2));
	}

	/**
	 * Asserts that the program prints error messages for unknown commands, invalid spaces and invalid arguments.
	 */
	@Test
	public void badArgumentsTest() {
		for (String invalid : INVALID_COMMANDS) {
			errorTest(addQuit(invalid), Input.getFile(TASK_SHEET_INPUT_FILE));
		}
	}

	/**
	 * Asserts that after an error has been triggered, the program still successfully responds to the {@code nodes}
	 * command using the example input file from the task sheet.
	 */
	@Test
	public void errorRecoveryTest() {
		for (String invalid : INVALID_COMMANDS) {
			String[] commands = {
					invalid,
					"nodes",
					"quit"
			};
			List<Matcher<String>> matchers = getMatchers(
				startsWith("Error,"),
				is("calc:202,centos5:105,centos6:106,centos7:107,impress:203,libreoffice:200,officesuite,operatingsystem,software,writer:201"));
			multiLineTest(commands, matchers, Input.getFile(TASK_SHEET_INPUT_FILE));
		}
	}

}
