package final1.subtests;

import static test.KitMatchers.suits;
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
	 * Relations and their reverse counterparts.
	 */
	private String[][] relations = new String[][] {
			{
					"contains",
					"contained-in"
			},
			{
					"successor-of",
					"predecessor-of"
			},
			{
					"has-part",
					"part-of"
			}
	};

	/**
	 * @param number
	 *            Indicates which relation to use. {@code number % 6} is assigned as followed:
	 *            <p>
	 *            {@code 0  =>  contains}, <br>
	 *            {@code 1  =>  contained-in}, <br>
	 *            {@code 2  =>  successor-of}, <br>
	 *            {@code 3  =>  predecessor-of}, <br>
	 *            {@code 4  =>  has-part}, <br>
	 *            {@code 5  =>  part-of}
	 * @param from
	 *            An int between {@code 0} and {@code 26} indicating the element the relation comes from.
	 * @param to
	 *            An int between {@code 0} and {@code 26} indicating the element the relation comes goes to.
	 * @return A valid relation as described above.
	 */
	protected String relation(int number, int from, int to) {
		return relate(relations[(number / 2) % 6][number % 2], from, to);
	}

	/**
	 * @param number
	 *            Indicates which relation to use. {@code number % 6} is assigned as followed:
	 *            <p>
	 *            {@code 1  =>  contains}, <br>
	 *            {@code 0  =>  contained-in}, <br>
	 *            {@code 3  =>  successor-of}, <br>
	 *            {@code 2  =>  predecessor-of}, <br>
	 *            {@code 5  =>  has-part}, <br>
	 *            {@code 4  =>  part-of}
	 * @param from
	 *            An int between {@code 0} and {@code 26} indicating the element the relation comes from.
	 * @param to
	 *            An int between {@code 0} and {@code 26} indicating the element the relation comes goes to.
	 * @return A valid relation as described above.
	 */
	protected String reverse(int number, int from, int to) {
		return relate(relations[(number / 2) % 6][(number + 1) % 2], from, to);
	}

	private String relate(String relationName, int from, int to) {
		switch (relationName) {
		case "contains":
		case "contained-in":
			return category(from) + " " + relationName + " " + category(to);
		default:
			return product(from) + " " + relationName + " " + product(to);
		}
	}

	private String product(int number) {
		return String.valueOf((char) (number + 64)) + " (id=" + number + ")";
	}

	private String category(int number) {
		return String.valueOf((char) (number + 64));
	}

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

	protected String[] ZERO_ID_INPUT_FILE = new String[] {
			"A contains B(id=0)",
			"A contains C(id=1)"
	};

	protected String[] COMPLEX_INPUT_FILE = new String[] {
			"software contains vim(id=6)",
			"software contains officesuite",
			"software contains chromium(id=41)",
			"officesuite contains libreoffice(id=333)",
			"officesuite contains MSO(id=666666)",
			"officesuite contained-in suite",
			"suite contains distributions",
			"suite contains libreoffice(id=333)",
			"libreoffice(id=333) successor-of OOO(id=222)",
			"writer(id=201) part-of libreoffice(id=333)",
			"calc(id=202) part-of libreoffice(id=333)",
			"impress(id=203) part-of libreoffice(id=333)",
			"math(id=204) part-of libreoffice(id=333)",
			"math(id=204) part-of writer(id=201)",
			"OOO(id=222) has-part writer(id=201)",
			"OOO(id=222) has-part calc(id=202)",
			"OOO(id=222) has-part impress(id=203)",
			"OOO(id=222) has-part math(id=204)",
			"OOO(id=222) predecessor-of OracleOO(id=555)",
			"OOO(id=222) predecessor-of AOO(id=444)",
			"StarOffice(id=111) predecessor-of OOO(id=222)",
			"chromium(id=41) has-part webkit(id=1)",
			"webkit(id=1) part-of Blink(id=5)",
			"Blink(id=5) successor-of webkit(id=1)",
			"KHTML(id=4) predecessor-of webkit(id=1)",
			"KJS(id=3) predecessor-of webkit(id=1)",
			"webkit2(id=2) successor-of webkit(id=1)",
			"distributions contains LinuxDistros",
			"LinuxDistros contains Debian(id=78)",
			"LinuxDistros contains DebBased",
			"LinuxDistros contains Ub1204LTS(id=1204)",
			"LinuxDistros contains Ub1210(id=1210)",
			"LinuxDistros contains Ub1304(id=1304)",
			"LinuxDistros contains Ub1310(id=1310)",
			"LinuxDistros contains Ub1404LTS(id=1404)",
			"apt(id=7) contained-in software",
			"apt(id=7) part-of Debian(id=78)",
			"DebBased contains Ub1204LTS(id=1204)",
			"DebBased contains Ub1210(id=1210)",
			"DebBased contains Ub1304(id=1304)",
			"DebBased contains Ub1310(id=1310)",
			"DebBased contains Ub1404LTS(id=1404)",
			"DebBased contains Debian(id=78)",
			"DebBased contains apt(id=7)",
			"Ub1404LTS(id=1404) successor-of Ub1310(id=1310)",
			"Ub1310(id=1310) successor-of Ub1304(id=1304)",
			"Ub1304(id=1304) successor-of Ub1210(id=1210)",
			"Ub1210(id=1210) successor-of Ub1204LTS(id=1204)",
			"Ub1404LTS(id=1404) successor-of Ub1204LTS(id=1204)",
			"god(id=0) successor-of Microsoft(id=69)",
			"god(id=0) predecessor-of chucknorris(id=42)",
			"Microsoft(id=69) part-of chucknorris(id=42)",
			"chucknorris(id=42) predecessor-of evil(id=666)",
			"evil(id=666) predecessor-of Hurd(id=1337)"
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
			TestObject.getLastMethodsSystemExitStatus(), suits(SystemExitStatus.WITH_GREATER_THAN_0, true));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.InteractiveConsoleTest#consoleMessage(java.lang.String[], java.lang.String[])
	 */
	@Override
	protected String consoleMessage(String[] commands, String[] commandLineArguments) {
		String result = "";
		String fileMessage = (commandLineArguments.length > 0) ? Input.fileMessage(commandLineArguments[0]) : "";
		result += "We ran a session on your interactive console" + fileMessage + " running the commands \n\n"
				+ joinOnePerLine(commands) + "\n\nbut got unexpected output:\n";
		return result;
	}
}
