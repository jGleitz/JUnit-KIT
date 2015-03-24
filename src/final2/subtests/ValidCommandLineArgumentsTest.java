package final2.subtests;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;

/**
 * Calls the tested class' main method with legal arguments. Checks if the tested class does not print an error exits
 * with system exit status 1.
 * 
 * @author Joshua Gleitze
 * @author Christian Hilden
 * @version 1.0
 */
public class ValidCommandLineArgumentsTest extends LangtonSubtest {

	public ValidCommandLineArgumentsTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * Asserts that the tested class works when passing a well formed rule argument
	 */
	@Test
	public void ruleArgumentTest() {
		noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=270-90-315-45-90");
		noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=45-45-45-45-45");
		noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=0-0-0-0-0");
	}

	/**
	 * Asserts that the tested class works when passing a well formed speedup argument
	 */
	@Test
	public void speedupArgumentTest() {
		noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=1");
		noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=99");
		noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=2147483647");
	}

	/**
	 * Asserts that the tested class works when passing well formed arguments in different order
	 */
	@Test
	public void orderArgumentTest() {
		noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=1", "rule=270-90-315-45-90");
		noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=270-90-315-45-90", "speedup=5");
	}

	/**
	 * Asserts that the tested class works when passing no arguments except a valid input file.
	 */
	@Test
	public void noOptionalArgumentsTest() {
		noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1));
	}

}
