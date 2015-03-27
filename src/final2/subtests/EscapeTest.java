package final2.subtests;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.ExactRun;
import test.runs.NoOutputRun;
import test.runs.Run;

/**
 * Tests command 'escape'.
 * 
 * @author Annika Berger
 * @author Joshua Gleitze
 *
 */
public class EscapeTest extends LangtonSubtest {
	public EscapeTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * Asserts that command 'escape ant' works on simple examples.
	 */
	@Test
	public void simpleEscapeTest() {
		inputFile = new String[] {
				"b000",
				"0000",
				"0a00",
				"Z000"
		};
		runs = new Run[] {
				new ExactRun("ant", is("a,b,z")),
				new NoOutputRun("escape b"),
				new ExactRun("ant", is("a,z")),
				quit()
		};

		sessionTest(runs, Input.getFile(inputFile));
	}

	/**
	 * Asserts that command 'escape ant' works on simple examples with created ants.
	 */
	@Test
	public void escapeCreatedAntTest() {
		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"Z000"
		};
		runs = new Run[] {
				new ExactRun("ant", is("a,z")),
				new NoOutputRun("create b,0,0"),
				new ExactRun("ant", is("a,b,z")),
				new NoOutputRun("escape b"),
				new ExactRun("ant", is("a,z")),
				quit()
		};

		sessionTest(runs, Input.getFile(inputFile));
	}

	/**
	 * Asserts that the tested class terminates silently when the last ant is {@code escape}d.
	 */
	@Test
	public void escapeTerminatesTest() {
		runs = new Run[] {
				new NoOutputRun("escape a"),
				new NoOutputRun("escape c"),
				move(2),
				new NoOutputRun("escape i"),
				move(3),
				new NoOutputRun("escape b")
		};
		sessionTest(runs, Input.getFile(ALL_TYPES_BOARD), ALL_TYPES_RULE, ALL_TYPES_SPEEDUP);
	}

	/**
	 * Asserts that {@code escape} works on a test file with all ant and cell types. Please refer to
	 * {@link LangtonSubtest#ALL_TYPES_BOARD} for a detailed description of what is supposed to happen.
	 */
	@Test
	public void escapeAllTypesTest() {
		runs = new Run[] {
				move(2),
				new NoOutputRun("escape i"),
				new NoOutputRun("escape r"),
				new NoOutputRun("escape s"),
				new NoOutputRun("escape d"),
				new NoOutputRun("escape c"),
				checkPitch(new String[] {
						"0330",
						"b041",
						"0*30",
						"0330",
				}),
				quit()
		};
		sessionTest(runs, Input.getFile(ALL_TYPES_BOARD), ALL_TYPES_RULE, ALL_TYPES_SPEEDUP);
	}
}
