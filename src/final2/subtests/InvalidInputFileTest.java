package final2.subtests;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.ErrorOrNoOutputRun;
import test.runs.ErrorRun;
import test.runs.Run;

/**
 * Checks that the tested class prints error messages for malformed input files.
 * 
 * @author Annika Berger
 * @author Roman Langrehr
 */
public class InvalidInputFileTest extends LangtonSubtest {

	public InvalidInputFileTest() {
		setExpectedSystemExitStatus(SystemExitStatus.EXACTLY.status(1));
	}

	/**
	 * Asserts that the tested class detects input files that do not form a rectangle board.
	 */
	@Test
	public void notARectangleTest() {
		inputFile = new String[] {
				"0000",
				"0a00",
				"0000",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"00",
				"000",
				"0a0"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"000000",
				"000000",
				"0a000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"00",
				"00a",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"0000",
				"00a",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

	}

	/**
	 * Asserts that the tested class detects input files with empty lines.
	 * <p>
	 * If these tests fail for you and you're using the <code>FileInputHelper</code>, please see this post on ILIAS:
	 * http://goo.gl/MXXkpP
	 */
	@Test
	public void emptyLinesTest() {
		inputFile = new String[] {
				"",
				"0000",
				"0a00",
				"0000",
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"0000",
				"0a00",
				"",
				"0000",
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"0000",
				"0a00",
				"",
				"",
				"0000",
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"0000",
				"0a00",
				"0000",
				"",
		};
		/*
		 * If these tests fail for you and you're using the FileInputHelper, please see this post on ILIAS:
		 * http://goo.gl/MXXkpP
		 */
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"0000",
				"0a00",
				"0000",
				"",
				"",
		};
		/*
		 * If these tests fail for you and you're using the FileInputHelper, please see this post on ILIAS:
		 * http://goo.gl/MXXkpP
		 */
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"",
				"",
				"",
		};
		/*
		 * If these tests fail for you and you're using the FileInputHelper, please see this post on ILIAS:
		 * http://goo.gl/MXXkpP
		 */
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
			"",
		};
		/*
		 * If these tests fail for you and you're using the FileInputHelper, please see this post on ILIAS:
		 * http://goo.gl/MXXkpP
		 */
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));
	}

	/**
	 * Asserts that the tested class detects input files that do not contain an ant.
	 */
	@Test
	public void noAntTest() {
		// Allow every system exit status, as ErrorOrNoOutputRun will handle the system exit status checking.
		setExpectedSystemExitStatus(null);
		setAllowedSystemExitStatus(SystemExitStatus.ALL);
		inputFile = new String[] {
				"000",
				"000",
				"000"
		};
		sessionTest(new ErrorOrNoOutputRun(true), new Run[0], Input.getFile(inputFile));
		inputFile = new String[] {
				"000",
				"0*0",
				"000"
		};
		sessionTest(new ErrorOrNoOutputRun(true), new Run[0], Input.getFile(inputFile));
		inputFile = new String[] {
				"0000",
				"0000",
				"0000"
		};
		sessionTest(new ErrorOrNoOutputRun(true), new Run[0], Input.getFile(inputFile));
		inputFile = new String[] {
				"****",
				"****",
				"****"
		};
		sessionTest(new ErrorOrNoOutputRun(true), new Run[0], Input.getFile(inputFile));
		setExpectedSystemExitStatus(SystemExitStatus.EXACTLY.status(1));
	}

	/**
	 * Asserts that the tested class detects input files that do contain several ants with the same name.
	 */
	@Test
	public void duplicateAntTest() {
		inputFile = new String[] {
				"0a0",
				"000",
				"0A0"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));
		inputFile = new String[] {
				"a00",
				"000",
				"00a"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));
		inputFile = new String[] {
				"0z0",
				"000",
				"0Z0"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));
		inputFile = new String[] {
				"000",
				"H00",
				"00H"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));
	}

	/**
	 * Asserts that the tested class detects input files that contain invalid characters.
	 */
	@Test
	public void invalidCharactersTest() {
		inputFile = new String[] {
				"a-0",
				"000",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"a00",
				"500",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));
		inputFile = new String[] {
				"a00",
				"00+",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));
		inputFile = new String[] {
				"a00",
				"&00",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));
		inputFile = new String[] {
				"a00",
				"000",
				">00"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"000",
				"0+00",
				"a00"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"000",
				"0+10",
				"a00"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"000",
				"\u00e400", // \u00e4 = ae
				"b00"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));
	}

	/**
	 * Asserts that the tested class detects input files that contain whitespaces.
	 */
	@Test
	public void whitespaceTest() {
		inputFile = new String[] {
				"00 ",
				"00a",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				" 00",
				"00a",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"000",
				"0 a",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				" 000",
				"00a",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"000",
				"00a",
				"000 "
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));
		inputFile = new String[] {
				"000",
				"00a",
				"000 ",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));
		inputFile = new String[] {
				"00 ",
				"00a",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				" 00",
				"00a",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));

		inputFile = new String[] {
				"000",
				"0 a",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit(), Input.getFile(inputFile));
	}
}
