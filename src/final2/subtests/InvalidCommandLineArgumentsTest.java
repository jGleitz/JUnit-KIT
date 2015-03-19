package final2.subtests;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;

/**
 * Calls the tested class' main method with illegal arguments. Checks if the tested class prints error messages and
 * exits with system exit status 1.
 * 
 * @author Joshua Gleitze
 * @author Christian Hilden
 * @version 1.0
 */
public class InvalidCommandLineArgumentsTest extends LangtonSubtest {

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
     * Asserts that the tested class prints an error message if arguments are passed in invalid argument format.
     */
    @Test
    public void invalidArgumentFormatTest() {
        errorTest("", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule:{45,45,45,45,45}");
        errorTest("", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule={45,45,45,45,45}", "speedup:5");
        errorTest("", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup:5", "rule={45,45,45,45,45}");
    }
	
	/**
	 * Asserts that the tested class prints an error message if a command line argument is specified more than once
	 */
	@Test
	public void duplicateArgumentsTest() {
		errorTest("", Input.getFile(TASK_SHEET_INPUT_FILE_1), Input.getFile(TASK_SHEET_INPUT_FILE_1));
		errorTest("", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule={45,45,45,45,45}", "rule={45,45,45,45,45}");
		errorTest("", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule={45,45,45,45,45}", "rule={45,90,270,315,90}");
		errorTest("", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=1", "speedup=1");
		errorTest("", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=1", "speedup=5");
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
