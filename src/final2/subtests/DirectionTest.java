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
 * @author Roman Langrehr
 */
public class DirectionTest extends LangtonSubtest {

	public DirectionTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * Tests if the addition of {@code 45}, {@code 90}, {@code 270} and {@code 315} works on all Directions ({@code N},
	 * {@code NE}, {@code E}, {@code SE}, {@code S}, {@code SW}, {@code W} and {@code NW}).<br />
	 * <br />
	 * The colors {@code 0}, {@code 1}, {@code 2} and {@code 3} are used to make the rotations about {@code 45},
	 * {@code 90}, {@code 270} and {@code 315} degree.<br />
	 * <br />
	 * The ants looking {@code N} and {@code S} are created immediately on the playing field, the other ants are created
	 * like that:<br />
	 * (x is the cell, the ant will enter in the next move) <br />
	 * <br />
	 * {@code NE}:<br />
	 * {@code 00x}<br />
	 * {@code 000}<br />
	 * {@code 0A0}<br />
	 * moves to the bring the ant in the starting position: {@code 1}<br />
	 * <br />
	 * {@code E}:<br />
	 * {@code 000}<br />
	 * {@code 01x}<br />
	 * {@code 0A0}<br />
	 * moves to the bring the ant in the starting position: {@code 1}<br />
	 * <br />
	 * {@code SE}:<br />
	 * {@code 0a0}<br />
	 * {@code 030}<br />
	 * {@code 00x}<br />
	 * moves to the bring the ant in the starting position: {@code 1}<br />
	 * <br />
	 * {@code SW}:<br />
	 * {@code 0a0}<br />
	 * {@code 000}<br />
	 * {@code x00}<br />
	 * moves to the bring the ant in the starting position: {@code 1}<br />
	 * <br />
	 * {@code W}:<br />
	 * {@code 000}<br />
	 * {@code x20}<br />
	 * {@code 0A0}<br />
	 * moves to the bring the ant in the starting position: {@code 1}<br />
	 * <br />
	 * {@code NW}:<br />
	 * {@code x00}<br />
	 * {@code 030}<br />
	 * {@code 0A0}<br />
	 * moves to the bring the ant in the starting position: {@code 1}<br />
	 * <br />
	 */
	@Test
	public void allDirectionAdditionsTest() {
		String rule = "rule=45-90-270-315-45";

		// Addition to N

		inputFile = new String[] {
				"000",
				"000",
				"0A0"
		};
		runs = new Run[] {
				move(1),
				new ExactRun("direction a", is("NO")),
				new ExactRun("position a", is("1,1")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"000",
				"010",
				"0A0"
		};
		runs = new Run[] {
				move(1),
				new ExactRun("direction a", is("O")),
				new ExactRun("position a", is("1,1")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"000",
				"020",
				"0A0"
		};
		runs = new Run[] {
				move(1),
				new ExactRun("direction a", is("W")),
				new ExactRun("position a", is("1,1")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"000",
				"030",
				"0A0"
		};
		runs = new Run[] {
				move(1),
				// while the task sheet specifies "SW" for northwest, this was an error, see this official statement:
				// https://ilias.studium.kit.edu/ilias.php?ref_id=413546&cmdClass=ilobjforumgui&thr_pk=53116&pos_pk=197760&offset=0&cmd=viewThread&cmdNode=ed:5v&baseClass=ilRepositoryGUI#197760
				new ExactRun("direction a", is("NW")),
				new ExactRun("position a", is("1,1")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		// Addition to NE

		inputFile = new String[] {
				"000",
				"000",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("O")),
				new ExactRun("position a", is("0,2")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"001",
				"000",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("SO")),
				new ExactRun("position a", is("0,2")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"002",
				"000",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				// while the task sheet specifies "SW" for northwest, this was an error, see this official statement:
				// https://ilias.studium.kit.edu/ilias.php?ref_id=413546&cmdClass=ilobjforumgui&thr_pk=53116&pos_pk=197760&offset=0&cmd=viewThread&cmdNode=ed:5v&baseClass=ilRepositoryGUI#197760
				new ExactRun("direction a", is("NW")),
				new ExactRun("position a", is("0,2")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"003",
				"000",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("N")),
				new ExactRun("position a", is("0,2")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		// Addition to E

		inputFile = new String[] {
				"000",
				"010",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("SO")),
				new ExactRun("position a", is("1,2")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"000",
				"011",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("S")),
				new ExactRun("position a", is("1,2")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"000",
				"012",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("N")),
				new ExactRun("position a", is("1,2")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"000",
				"013",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("NO")),
				new ExactRun("position a", is("1,2")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		// Addition to SE

		inputFile = new String[] {
				"0a0",
				"030",
				"000"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("S")),
				new ExactRun("position a", is("2,2")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"0a0",
				"030",
				"001"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("SW")),
				new ExactRun("position a", is("2,2")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"0a0",
				"030",
				"002"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("NO")),
				new ExactRun("position a", is("2,2")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"0a0",
				"030",
				"003"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("O")),
				new ExactRun("position a", is("2,2")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		// Addition to S

		inputFile = new String[] {
				"0a0",
				"000",
				"000"
		};
		runs = new Run[] {
				move(1),
				new ExactRun("direction a", is("SW")),
				new ExactRun("position a", is("1,1")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"0a0",
				"010",
				"000"
		};
		runs = new Run[] {
				move(1),
				new ExactRun("direction a", is("W")),
				new ExactRun("position a", is("1,1")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"0a0",
				"020",
				"000"
		};
		runs = new Run[] {
				move(1),
				new ExactRun("direction a", is("O")),
				new ExactRun("position a", is("1,1")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"0a0",
				"030",
				"000"
		};
		runs = new Run[] {
				move(1),
				new ExactRun("direction a", is("SO")),
				new ExactRun("position a", is("1,1")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		// Addition to SW

		inputFile = new String[] {
				"0a0",
				"000",
				"000"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("W")),
				new ExactRun("position a", is("2,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"0a0",
				"000",
				"100"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				// while the task sheet specifies "SW" for northwest, this was an error, see this official statement:
				// https://ilias.studium.kit.edu/ilias.php?ref_id=413546&cmdClass=ilobjforumgui&thr_pk=53116&pos_pk=197760&offset=0&cmd=viewThread&cmdNode=ed:5v&baseClass=ilRepositoryGUI#197760
				new ExactRun("direction a", is("NW")),
				new ExactRun("position a", is("2,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"0a0",
				"000",
				"200"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("SO")),
				new ExactRun("position a", is("2,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"0a0",
				"000",
				"300"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("S")),
				new ExactRun("position a", is("2,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		// Addition to W

		inputFile = new String[] {
				"000",
				"020",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				// while the task sheet specifies "SW" for northwest, this was an error, see this official statement:
				// https://ilias.studium.kit.edu/ilias.php?ref_id=413546&cmdClass=ilobjforumgui&thr_pk=53116&pos_pk=197760&offset=0&cmd=viewThread&cmdNode=ed:5v&baseClass=ilRepositoryGUI#197760
				new ExactRun("direction a", is("NW")),
				new ExactRun("position a", is("1,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"000",
				"120",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("N")),
				new ExactRun("position a", is("1,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"000",
				"220",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("S")),
				new ExactRun("position a", is("1,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"000",
				"320",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("SW")),
				new ExactRun("position a", is("1,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		// Addition to NW

		inputFile = new String[] {
				"000",
				"030",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("N")),
				new ExactRun("position a", is("0,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"100",
				"030",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("NO")),
				new ExactRun("position a", is("0,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"200",
				"030",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("SW")),
				new ExactRun("position a", is("0,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

		inputFile = new String[] {
				"300",
				"030",
				"0A0"
		};
		runs = new Run[] {
				move(1), // to bring the ant in the starting position
				move(1),
				new ExactRun("direction a", is("W")),
				new ExactRun("position a", is("0,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), rule);

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

		runs = new Run[] {
				move(4),
				new ExactRun("direction b", is("S")),
				new ExactRun("direction r", is("SO")),
				new ExactRun("direction s", is("O")),
				quit()
		};
		sessionTest(runs, Input.getFile(ALL_TYPES_BOARD), ALL_TYPES_RULE, ALL_TYPES_SPEEDUP);
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
				// while the task sheet specifies "SW" for northwest, this was an error, see this official statement:
				// https://ilias.studium.kit.edu/ilias.php?ref_id=413546&cmdClass=ilobjforumgui&thr_pk=53116&pos_pk=197760&offset=0&cmd=viewThread&cmdNode=ed:5v&baseClass=ilRepositoryGUI#197760
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
}
