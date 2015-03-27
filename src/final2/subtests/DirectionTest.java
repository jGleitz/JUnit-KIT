package final2.subtests;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.ExactRun;
import test.runs.Run;

/**
 * Tests for the {@code direction} command.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class DirectionTest extends LangtonSubtest {

	public DirectionTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * Asserts that {@code direction} works on a test file with all ant and cell types. Please refer to
	 * {@link LangtonSubtest#ALL_TYPES_BOARD} for a detailed description of what is supposed to happen.
	 */
	@Test
	public void allTypesDirectionTest() {
		runs = new Run[] {
				new ExactRun("direction a", is("S")),
				new ExactRun("direction b", is("S")),
				new ExactRun("direction I", is("S")),
				new ExactRun("direction r", is("S")),
				new ExactRun("direction c", is("N")),
				new ExactRun("direction D", is("N")),
				new ExactRun("direction j", is("N")),
				new ExactRun("direction s", is("N")),
				move(1),
				new ExactRun("direction a", is("W")),
				new ExactRun("direction b", is("W")),
				new ExactRun("direction r", is("SO")),
				new ExactRun("direction i", is("N")),
				move(1),
				new ExactRun("direction d", is("S")),
				new ExactRun("direction b", is("N")),
				new ExactRun("direction r", is("SO")),
				move(1),
				new ExactRun("direction c", is("W")),
				new ExactRun("direction r", is("SO")),
				new ExactRun("direction b", is("O")),
				move(1),
				new ExactRun("direction b", is("S")),
				new ExactRun("direction r", is("SO")),
				new ExactRun("direction s", is("O")),
				quit()
		};
		sessionTest(runs, Input.getFile(ALL_TYPES_BOARD), ALL_TYPES_RULE, ALL_TYPES_SPEEDUP);
	}
}
