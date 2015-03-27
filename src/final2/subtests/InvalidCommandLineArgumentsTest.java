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
 * @author Martin Löper
 * @author Roman Langrehr
 */
public class InvalidCommandLineArgumentsTest extends LangtonSubtest {

	public InvalidCommandLineArgumentsTest() {
		setExpectedSystemExitStatus(SystemExitStatus.EXACTLY.status(1));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see final2.subtests.LangtonSubtest#consoleMessage(java.lang.String[], java.lang.String[])
	 */
	@Override
	protected String consoleMessage(String[] commands, String[] commandLineArguments) {
		String result = "";
		result += "We ran a session on your interactive console, \n" + getArguments(commandLineArguments)
				+ "\n\nbut got unexpected output:\n";
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.InteractiveConsoleTest#getArguments(java.lang.String[])
	 */
	@Override
	protected String getArguments(String[] commandLineArguments) {
		if (commandLineArguments.length > 0) {
			commandLineArguments[0] = "inputFile";
		}
		return super.getArguments(commandLineArguments);
	}

	/**
	 * Asserts that the tested class prints an error message if no command line arguments are provided.
	 */
	@Test
	public void noArgumentTest() {
		errorTest("quit");
	}

	/**
	 * Asserts that the tested class prints an error message if arguments are passed in invalid argument format.
	 */
	@Test
	public void invalidArgumentFormatTest() {
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule:45-45-45-45-45");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=45-45-45-45-45", "speedup:5");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup:5", "rule=45-45-45-45-45");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup5");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule45-45-45-45-45");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule-45-45-45-45-45");
	}

	/**
	 * Asserts that the tested class prints an error message if a command line argument is specified more than once
	 */
	@Test
	public void duplicateArgumentsTest() {
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), Input.getFile(TASK_SHEET_INPUT_FILE_1));
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=45-45-45-45-45", "rule=45-45-45-45-45");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=45-45-45-45-45", "rule=45-90-270-315-90");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=1", "speedup=1");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=1", "speedup=5");
	}

	/**
	 * Asserts that the tested class prints an error message if an invalid rule command is passed.
	 */
	@Test
	public void invalidRuleArgumentsTest() {
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=270-90-315-45-90-270");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=270-90-315-45");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=270-90-320-45-90");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=135-90-315-45-90");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=270-90-315-45.5-90");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=270-90--315-45-90");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=270-90-315-45-90-");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=270-90-315-45-90--");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=270-90-315-45-90---");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=-270-90-315-45-90");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=--270-90-315-45-90");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=-270-90--315-45-90");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule={270,90,315,45,90}");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=270-0-315-45-90");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=270-180-315-45-90");

		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1),
			"rule=270-90-9999999999999999999999999999999999999-45-90");

		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "Rule=90-315-45-90-270");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "ruLe=90-315-45-90-270");

		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=+90-315-45-90-270");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=90-+315-45-90-270");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=90-315-+45-90-270");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=90-315-45-+90-270");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=90-315-45-90-+270");
	}

	/**
	 * Asserts that the tested class prints an error message if an invalid speedup command is passed.
	 */
	@Test
	public void invalidSpeedupArgumentsTest() {
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=0");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=1.2");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=-1");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=-5");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=99999999999999999999999999999999999999999");

		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedUp=12");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "Speedup=12");

		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=+12");
	}

	/**
	 * Asserts that the tested class prints an error message if a non-existent file is provided as command line
	 * argument.
	 */
	@Test
	public void noSuchFileTest() {
		errorTest("quit", "IHopefullyDontExistsOnAnyMachine.unusualFileExtension");
	}

	@Test
	public void tooManyArgumentsTest() {
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), Input.getFile(TASK_SHEET_INPUT_FILE_1),
			"rule=45-45-45-45-45");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=1");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), Input.getFile(TASK_SHEET_INPUT_FILE_1),
			"rule=45-45-45-45-45", "speedup=1");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=1", "rule=45-45-45-45-45",
			"rule=45-45-45-45-45");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "rule=45-45-45-45-45", "speedup=1",
			"rule=45-90-270-315-90");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=1", "rule=45-45-45-45-45", "speedup=1");
		errorTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_1), "speedup=1", "speedup=5", "rule=45-45-45-45-45");
	}
}
