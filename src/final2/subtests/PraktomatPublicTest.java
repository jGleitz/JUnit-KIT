package final2.subtests;

import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.LineRun;
import test.runs.NoOutputRun;
import test.runs.Run;

/**
 * Simulates the Praktomat's public test.
 * 
 * @author Martin L�per
 * @version 1.0
 */
public class PraktomatPublicTest extends LangtonSubtest {

	public PraktomatPublicTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * "Fundamental tests with ordinary ant" Asserts that tested program fulfills the first public Praktomat test.
	 */
	@Test
	public void fundamentalTestsWithOrdinaryAnt() {
		String[] expectedOutput = pitchToLowercase(PUBLIC_PRAKTOMAT_TEST_FILE_1);

		runs = new Run[] {
				checkPitch(expectedOutput),

				new LineRun("position A", is("2,1")),
				new LineRun("position a", is("2,1")),
				new LineRun("field 2,1", is("a")),
				new LineRun("direction a", is("N")),
				new LineRun("ant", is("a")),
				new NoOutputRun("create e,0,0"),
				new LineRun("ant", is("a,e")),
				new LineRun("direction e", is("S")),
				new NoOutputRun("move 1"),
				new LineRun("direction e", is("O")),
				new NoOutputRun("quit")
		};

		sessionTest(runs, Input.getFile(PUBLIC_PRAKTOMAT_TEST_FILE_1));
	}

	/**
	 * "Sporty ant movement" Asserts that tested program fulfils the second public Praktomat test.
	 */
	@Test
	public void sportyAntMovement() {
		String[] expectedOutput1 = pitchToLowercase(PUBLIC_PRAKTOMAT_TEST_FILE_2);
		String[] expectedOutput2 = new String[] {
				"0000",
				"0*00",
				"3300",
				"i000"
		};

		runs = new Run[] {
				checkPitch(expectedOutput1),
				new NoOutputRun("move 1"),
				checkPitch(expectedOutput2),
				new LineRun("direction i", is("O")),
				new NoOutputRun("quit")
		};

		sessionTest(runs, Input.getFile(PUBLIC_PRAKTOMAT_TEST_FILE_2), "speedup=3");
	}

	/**
	 * "Lazy ant movement" Asserts that tested program fulfills the thrid public Praktomat test.
	 */
	@Test
	public void lazyAntMovement() {
		String[] expectedOutput1 = pitchToLowercase(PUBLIC_PRAKTOMAT_TEST_FILE_3);
		String[] expectedOutput2 = new String[] {
				"0000",
				"0*00",
				"r300",
				"0000"
		};

		runs = new Run[] {
				checkPitch(expectedOutput1),
				new NoOutputRun("move 1"),
				checkPitch(expectedOutput1),
				new LineRun("direction r", is("W")),
				new NoOutputRun("move 1"),
				checkPitch(expectedOutput1),
				new NoOutputRun("move 1"),
				checkPitch(expectedOutput1),
				new NoOutputRun("move 1"),
				checkPitch(expectedOutput2),
				new NoOutputRun("quit")
		};

		sessionTest(runs, Input.getFile(PUBLIC_PRAKTOMAT_TEST_FILE_3), "speedup=3");
	}

	/**
	 * "Ordinary ant movement" Asserts that tested program fulfills the fourth public Praktomat test.
	 */
	@Test
	public void ordinaryAntMovement() {
		String[] expectedOutput = new String[] {
				"0330",
				"3003",
				"a003",
				"0330"
		};

		runs = new Run[] {
				new NoOutputRun("move 8"),
				checkPitch(expectedOutput),
				new NoOutputRun("quit")
		};

		sessionTest(runs, Input.getFile(PUBLIC_PRAKTOMAT_TEST_FILE_4), "rule=45-45-45-45-45");
	}
}
