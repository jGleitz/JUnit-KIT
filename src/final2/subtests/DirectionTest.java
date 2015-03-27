package final2.subtests;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.ExactRun;
import test.runs.NoOutputRun;
import test.runs.Run;

/**
 * Tests for the {@code direction} command.
 * 
 * @author Annika Berger
 * @author Joshua Gleitze
 */
public class DirectionTest extends LangtonSubtest {

	public DirectionTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}
	
	
	/**
	 * Asserts that command {@code direction} works with simple examples.
	 */
	@Test
	public void simpleDirectionTest() {
		runs = new Run[] {
				new ExactRun("direction f", is("N")),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new NoOutputRun("create b,1,0"),
				new ExactRun("direction b", is("S")),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		runs = new Run[] {
				new ExactRun("direction e", is("S")),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_2));

		runs = new Run[] {
				new NoOutputRun("create B,1,0"),
				new ExactRun("direction b", is("N")),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_2));
	}

	/**
	 * Asserts that command {@code direction} works after move command.
	 */
	@Test
	public void moveAntDirectionTest() {
		runs = new Run[] {
				new ExactRun("direction f", is("N")),
				move(1),
				new ExactRun("direction f", is("W")),
				move(2),
				new ExactRun("direction f", is("O")),
				move(1),
				new ExactRun("direction f", is("N")),
				move(1),
				new ExactRun("direction f", is("NO")),
				quit()
		};
		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));

		inputFile = new String[] {
				"000",
				"020",
				"0A0"
		};
		runs = new Run[] {
				new ExactRun("direction a", is("N")),
				move(1),
				new ExactRun("direction a", is("NW")),
				move(1),
				new ExactRun("direction a", is("SW")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		runs = new Run[] {
				new ExactRun("direction a", is("N")),
				move(1),
				new ExactRun("direction a", is("W")),
				quit()
		};
		sessionTest(runs, Input.getFile(PUBLIC_PRAKTOMAT_TEST_FILE_1));
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
