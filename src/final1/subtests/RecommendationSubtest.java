package final1.subtests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import test.Input;
import test.InteractiveConsoleTest;
import test.SystemExitStatus;
import test.TestObject;

/**
 * Base class for implementing subtest for the programming lecture's first final task. Contains some convenience methods
 * and fields.
 *
 * @author Joshua Gleitze
 * @author Martin Loeper
 * @version 1.1
 */
public abstract class RecommendationSubtest extends InteractiveConsoleTest {
	/**
	 * The input file as given on the task sheet.
	 */
	protected String[] TASK_SHEET_INPUT_FILE = new String[] {
			"CentOS5 ( id= 105) contained-in operatingSystem",
			"centOS6 ( id = 106) contained-in OperatingSystem",
			"operatingSystem contains centos7 ( id = 107 )",
			"operatingsystem contained-in Software",
			"CentOS7 (id=107) successor-of centos6(id=106)",
			"CentOS5 (id=105) predecessor-of centos6(id=106)",
			"writer (id=201) contained-in officesuite",
			"calc (id=202) contained-in officesuite",
			"impress (id=203) contained-in officesuite",
			"officesuite contained-in software",
			"LibreOffice (id=200) contained-in officesuite",
			"writer (id=201) part-of LibreOffice (id=200)",
			"calc (id=202) part-of libreoffice (id=200)",
			"libreoffice (id=200) has-part impress (id=203)"
	};

	/**
	 * The input file from the task sheet decorated with legal spaces.
	 */
	protected String[] TASK_SHEET_INPUT_FILE_SPACES = new String[] {
			"CentOS5(id=105) contained-in operatingSystem",
			"    centOS6   (  id   =   106)   contained-in    OperatingSystem    ",
			"operatingSystem contains centos7 ( id = 107 )",
			"operatingsystem contained-in Software",
			"CentOS7 (id=107) successor-of centos6(id=106) ",
			" CentOS5 (id=105) predecessor-of centos6(id=106)",
			"writer (id=201) contained-in officesuite",
			"calc (id=202) contained-in officesuite ",
			"impress (id=203) contained-in officesuite",
			"officesuite contained-in software",
			"LibreOffice (id=200) contained-in officesuite",
			"writer (id=201) part-of LibreOffice   (  id  =  200   )  ",
			"calc(id=202) part-of libreoffice(id=200)",
			"libreoffice (id=200) has-part impress (id=203)"
	};

	/**
	 * Extends the task sheet input file with semantically duplicate lines and some backwards relations.
	 */
	protected String[] TASK_SHEET_INPUT_FILE_DUPLICATES = new String[] {
			"CentOS5(id=105) contained-in operatingSystem",
			"    centOS6   (  id   =   106)   contained-in    OperatingSystem    ",
			"operatingSystem contains centos7 ( id = 107 )",
			"operatingsystem contained-in Software",
			"software contains operatingsystem",
			"operatingsystem contained-in Software",
			"CentOS7 (id=107) successor-of centos6(id=106) ",
			" CentOS5 (id=105) predecessor-of centos6(id=106)",
			"writer (id=201) contained-in officesuite",
			"calc (id=202) contained-in officesuite ",
			"impress (id=203) contained-in officesuite",
			"officesuite contained-in software",
			"calc (id=202) contained-in officesuite ",
			"LibreOffice (id=200) contained-in officesuite",
			"officesuite contains LibreOffice(id=200)",
			"writer (id=201) part-of LibreOffice   (  id  =  200   )  ",
			"calc(id=202) part-of libreoffice(id=200)",
			"libreoffice (id=200) has-part impress (id=203)",
			" officesuite contained-in   software",
	};

	protected String[] ONE_LINE_INPUT_FILE1 = new String[] {
		"B(id=1) successor-of A(id=2)"
	};

	protected String[] ONE_LINE_INPUT_FILE2 = new String[] {
		"A contains B(id=2)"
	};

	protected String[] PSEUDO_CIRCLE_INPUT_FILE1 = new String[] {
			"A(id=1) successor-of B(id=2)",
			"B(id=2) predecessor-of C(id=3)",
			"C(id=3) predecessor-of A(id=1)"
	};

	protected String[] PSEUDO_CIRCLE_INPUT_FILE2 = new String[] {
			"A (id=1) successor-of B (id=2)",
			"C (id=3) predecessor-of B (id=2)",
			"C (id=3) successor-of D (id=5)",
			"D (id=5) predecessor-of A (id=1)"
	};

	/**
	 * Runs {@link #errorTest(String, String...)} with the provided arguments and asserts that {@code System.exit(x)}
	 * was called with {@code x > 0} afterwards.
	 * <p>
	 * This method is deprecated as its function is now provided by
	 * {@link InteractiveConsoleTest#setExpectedSystemStatus}
	 *
	 * @param command
	 *            The command to run on the console.
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	@Deprecated
	protected void exitTest(String command, String... args0) {
		exitTest(wrapInArray(command), args0);
	}

	/**
	 * Runs {@link #errorTest(String[], String...)} with the provided arguments and asserts that {@code System.exit(x)}
	 * was called with {@code x > 0} afterwards.
	 * <p>
	 * This method is deprecated as its function is now provided by
	 * {@link InteractiveConsoleTest#setExpectedSystemStatus}
	 *
	 * @param commands
	 *            The commands to run on the console
	 * @param args0
	 *            The arguments for the {@code main}-method
	 */
	@Deprecated
	protected void exitTest(String[] commands, String... args0) {
		TestObject.allowSystemExit(SystemExitStatus.ALL);
		errorTest(commands, args0);
		assertThat(consoleMessage(commands, args0) + "\nWrong system exit status!",
			TestObject.getLastMethodsSystemExitStatus(), is(SystemExitStatus.WITH_GREATER_THAN_0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.InteractiveConsoleTest#consoleMessage(java.lang.String[], java.lang.String[])
	 */
	@Override
	protected String consoleMessage(String[] commands, String[] commandLineArguments) {
		String result = "";
		result += "We ran a session on your interactive console" + Input.fileMessage(commandLineArguments[0])
				+ " running the commands \n\n" + joinOnePerLine(commands) + "\n\nbut got unexpected output:\n";
		return result;
	}
}
