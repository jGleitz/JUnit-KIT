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
	 * Asserts that escaping an ant has the desired effect on all other commands.
	 */
	@Test
	public void reallyEscapedTest() {

	}

	/**
	 * Asserts that the tested class terminates silently when the last ant is {@code escape}d.
	 */
	@Test
	public void escapeTerminatesTest() {

	}

}
