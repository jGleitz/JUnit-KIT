package final2.subtests;

import java.util.List;

import org.hamcrest.Matcher;

import test.Input;
import test.InteractiveConsoleTest;
import test.runs.LineRun;
import test.runs.NoOutputRun;
import test.runs.Run;

/**
 * Base class for implementing subtest for the programming lecture's second final task. Should contain some convenience
 * methods and fields.
 * 
 * @author Joshua Gleitze
 * @author Martin Loeper
 * @author Christian Hilden
 */
public abstract class LangtonSubtest extends InteractiveConsoleTest {

	/**
	 * The first input file provided on the task sheet (page 6)
	 */
	protected static final String[] TASK_SHEET_INPUT_FILE_1 = new String[] {
			"000",
			"000",
			"0F0"
	};

	/**
	 * The second input file provided on the task sheet (page 7).
	 */
	protected static final String[] TASK_SHEET_INPUT_FILE_2 = new String[] {
			"1*0",
			"3e4",
			"12*"
	};

	/**
	 * The first board.txt Praktomat test file. ("Fundamental tests with ordinary ant")
	 */
	protected static final String[] PUBLIC_PRAKTOMAT_TEST_FILE_1 = new String[] {
			"0000",
			"0*00",
			"0A00",
			"0000"
	};

	/**
	 * The second board.txt Praktomat test file. ("Sporty ant movement")
	 */
	protected static final String[] PUBLIC_PRAKTOMAT_TEST_FILE_2 = new String[] {
			"0000",
			"0*00",
			"0I00",
			"0000"
	};

	/**
	 * The third board.txt Praktomat test file. ("Lazy ant movement")
	 */
	protected static final String[] PUBLIC_PRAKTOMAT_TEST_FILE_3 = new String[] {
			"0000",
			"0*00",
			"0R00",
			"0000"
	};

	/**
	 * The fourth board.txt Praktomat test file. ("Ordinary ant movement ")
	 */
	protected static final String[] PUBLIC_PRAKTOMAT_TEST_FILE_4 = new String[] {
			"0000",
			"0000",
			"A000",
			"0000"
	};

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
				+ joinAsNumberdLines(commands) + "\n\nbut got unexpected output:\n";
		return result;
	}

	/**
	 * Checks if the given expected pitch matches the program output.
	 * 
	 * @param pExpectedPitch
	 *            the lines of the expected pitch
	 * @return the run which checks the pitch
	 */
	protected Run checkPitch(String[] pExpectedPitch) {
		List<Matcher<String>> matchers = joinAsIsMatchers(pExpectedPitch);
		return new LineRun("print", matchers);
	}

	/**
	 * Returns the given pitch description in lowercase as expected by the print command.
	 * 
	 * @param pPitchLines
	 *            the pitch description
	 * @return lowercase pitch description
	 */
	protected String[] pitchToLowercase(String[] pPitchLines) {
		String[] out = new String[pPitchLines.length];
		for (int i = 0; i < pPitchLines.length; i++) {
			out[i] = pPitchLines[i].toLowerCase();
		}
		return out;
	}

	/**
	 * @param count
	 *            How many moves the tested class shall perform.
	 * @return A run for {@code move count}.
	 */
	protected Run move(int count) {
		return new NoOutputRun("move " + count);
	}
}
