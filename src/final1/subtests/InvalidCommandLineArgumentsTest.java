package final1.subtests;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;

/**
 * Calls the tested class' main method with illegal arguments. Checks if the tested class prints error messages and
 * exits with system exit status 1.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class InvalidCommandLineArgumentsTest extends RecommendationSubtest {

	public InvalidCommandLineArgumentsTest() {
		setExpectedSystemStatus(SystemExitStatus.EXACTLY.status(1));
	}

	/**
	 * Asserts that the tested class prints an error message if no command line arguments are provided.
	 */
	@Test
	public void noArgumentTest() {
		errorTest("");
	}

	/**
	 * Asserts that the tested class prints an error message if more than one command line argument is provided.
	 */
	@Test
	public void tooManyArgumentsTest() {
		errorTest("", Input.getFile(TASK_SHEET_INPUT_FILE), Input.getFile(TASK_SHEET_INPUT_FILE_DUPLICATES));
		errorTest("", Input.getFile(TASK_SHEET_INPUT_FILE), "lorem", "ipsum", "dolor", "sit", "amet");
	}

	/**
	 * Asserts that the tested class prints an error message if a non-existent file is provided as command line
	 * argument.
	 */
	@Test
	public void noSuchFileTest() {
		errorTest("", "IHopefullyDontExistsOnAnyMachine.unusualFileExtension");
	}
}
