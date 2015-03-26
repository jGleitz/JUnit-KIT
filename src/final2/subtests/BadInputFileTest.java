package final2.subtests;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.ErrorRun;
import test.runs.Run;

/**
 * Checks that the tested class prints error messages for malformed input files.
 * 
 * @author Annika Berger
 */
public class BadInputFileTest extends LangtonSubtest {
	private static final Run[] onlyQuit = {
		quit()
	};

	public BadInputFileTest() {
		setExpectedSystemStatus(SystemExitStatus.EXACTLY.status(1));
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
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));

		inputFile = new String[] {
				"00",
				"000",
				"0a0"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));

		inputFile = new String[] {
				"000000",
				"000000",
				"0a000"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));

		inputFile = new String[] {
				"00",
				"00a",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));
	}

	/**
	 * Asserts that the tested class detects input files that do not contain an ant.
	 */
	@Test
	public void noAntTest() {
		inputFile = new String[] {
				"000",
				"000",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));
		inputFile = new String[] {
				"000",
				"0*0",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));
		inputFile = new String[] {
				"0000",
				"0000",
				"0000"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));
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
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));
		inputFile = new String[] {
				"a00",
				"000",
				"00a"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));
		inputFile = new String[] {
				"0z0",
				"000",
				"0Z0"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));
		inputFile = new String[] {
				"000",
				"H00",
				"00H"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));
	}

	/**
	 * Asserts that the tested class detects input files that contain invalid characters.
	 */
	@Test
	public void invalidCharactersTest() {
		inputFile = new String[] {
				"0-0",
				"000",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));

		inputFile = new String[] {
				"000",
				"700",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));
		inputFile = new String[] {
				"000",
				"00+",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));
		inputFile = new String[] {
				"000",
				"&00",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));
		inputFile = new String[] {
				"0 0",
				"000",
				"000"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));
		inputFile = new String[] {
				"000",
				"000",
				">00"
		};
		sessionTest(new ErrorRun(), onlyQuit, Input.getFile(inputFile));
	}
}
