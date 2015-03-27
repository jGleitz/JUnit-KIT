package final2.subtests;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.Run;

/**
 * Checks the {@code move} command.
 * 
 * @author Joshua Gleitze
 */
public class MoveTest extends LangtonSubtest {

	public MoveTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * Asserts that {@code move} works on a very simple, easy to understand example: The rule is set to
	 * {@code 90-90-90-90-90} and we have only one ant on this 2x2 board:
	 * 
	 * <pre>
	 * 0a
	 * 00
	 * </pre>
	 * 
	 * The ant should run through the board clockwise, changing all colours to {@code 3} in the first four steps. In the
	 * next four steps, the ant should run clockwise once again, changing all colours back to {@code 1} and leaving the
	 * board in its initial state.
	 */
	@Test
	public void simpleMoveTest() {
		String[] inputFile = {
				"0a",
				"00"
		};
		runs = new Run[] {
				move(0),
				checkPitch(inputFile),
				move(1),
				checkPitch(new String[] {
						"00",
						"0a"
				}),
				move(1),
				checkPitch(new String[] {
						"00",
						"a3"
				}),
				move(1),
				checkPitch(new String[] {
						"a0",
						"33"
				}),
				move(1),
				checkPitch(new String[] {
						"3a",
						"33"
				}),
				move(4),
				checkPitch(inputFile),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), "rule=90-90-90-90-90");
	}

	/**
	 * Takes a board that repeats in its movement and asserts that the results stay the same for a high move count.
	 */
	@Test
	public void testConsistency() {
		String[] inputFile = {
				"0a0000",
				"000000",
				"000l00",
				"000000",
				"00000z",
				"000000"
		};
		String[][] standardStates = {
				{
						"0a0000",
						"000000"
				},
				{
						"000000",
						"0a0000"
				},
				{
						"000000",
						"a30000"
				},
				{
						"a00000",
						"330000"
				},
				{
						"3a0000",
						"330000"
				},
				{
						"330000",
						"3a0000"
				},
				{
						"330000",
						"a00000"
				},
				{
						"a30000",
						"000000"
				},

		};
		String[][] athleticStates = {
				{
						"000l00",
						"000000"
				},
				{
						"003l00",
						"003300"
				},
		};
		String[][] lazyStates = {
				{
						"00000z",
						"000000"
				},
				{
						"000000",
						"00000z"
				},
				{
						"000000",
						"0000z3"
				},
				{
						"0000z0",
						"000033"
				},
				{
						"00003z",
						"000033"
				},
				{
						"000033",
						"00003z"
				},
				{
						"000033",
						"0000z0"
				},
				{
						"0000z3",
						"000000"
				},
		};
		List<Run> runList = new LinkedList<>();
		for (int i = 1; i < 80; i++) {
			runList.add(move(1));
			String[] pitch = merge(standardStates[i % 8], athleticStates[i % 2], lazyStates[(((i - 1) / 4) + 1) % 8]);
			runList.add(checkPitch(pitch));
		}
		runList.add(quit());
		sessionTest(runArray(runList), Input.getFile(inputFile), "speedup=4", "rule=90-90-90-90-90");

		runList = new LinkedList<>();
		for (int i = 47; i < 6000; i += 47) {
			runList.add(move(47));
			String[] pitch = merge(standardStates[i % 8], athleticStates[i % 2], lazyStates[(((i - 1) / 4) + 1) % 8]);
			runList.add(checkPitch(pitch));
		}
		runList.add(quit());
		sessionTest(runArray(runList), Input.getFile(inputFile), "speedup=4", "rule=90-90-90-90-90");
	}

	/**
	 * Asserts that {@code move} works on a test file with all ant and cell types. Please refer to
	 * {@link LangtonSubtest#ALL_TYPES_BOARD} for a detailed description of what is supposed to happen.
	 */
	@Test
	public void allTypesMoveTest() {

		runs = new Run[] {
				move(1),
				checkPitch(ALL_TYPES_PITCHES[0]),
				move(1),
				checkPitch(ALL_TYPES_PITCHES[1]),
				move(1),
				checkPitch(ALL_TYPES_PITCHES[2]),
				move(1),
				checkPitch(ALL_TYPES_PITCHES[3]),
				move(1),
				checkPitch(ALL_TYPES_PITCHES[4]),
				quit()
		};
		sessionTest(runs, Input.getFile(ALL_TYPES_BOARD), ALL_TYPES_RULE, ALL_TYPES_SPEEDUP);
	};

	/**
	 * Asserts that the tested class terminates without any output after all ants have left the field.
	 */
	@Test
	public void terminateAfterAllHaveLeftTest() {
		inputFile = new String[] {
			"a"
		};
		runs = new Run[] {
			move(1)
		};
		sessionTest(runs, Input.getFile(inputFile), "rule=90-90-90-270-90");
		inputFile = new String[] {
				"0a0l0c",
				"303030",
				"030303",
				"303030",
				"030303"
		};
		runs = new Run[] {
				move(1),
				move(1),
				move(1),
				move(1),
				move(1),
				move(1),
				move(1),
				move(1),
				move(1),
		};
		sessionTest(runs, Input.getFile(inputFile), "rule=90-90-90-270-90", "speedup=1");
		runs = new Run[] {
			move(9)
		};
		sessionTest(runs, Input.getFile(inputFile), "rule=90-90-90-270-90", "speedup=1");
		inputFile = new String[] {
				"0i0l0k",
				"303030",
				"030303",
				"303030",
				"030303"
		};
		runs = new Run[] {
			move(1)
		};
		sessionTest(runs, Input.getFile(inputFile), "rule=90-90-90-270-90", "speedup=9");
	}

	/**
	 * Asserts that ants that have left the board are no longer recognized as obstacles.
	 */
	@Test
	public void noZombieAntsTest() {
		inputFile = new String[] {
				"00B400",
				"000C0d",
				"000000"
		};
		runs = new Run[] {
				move(1),
				checkPitch(new String[] {
						"000c00",
						"000000",
						"00000d"
				}),
				move(1),
				checkPitch(new String[] {
						"000400",
						"000000",
						"0000d3"
				}),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), "rule=90-90-90-270-315");
	}

	private static Run[] runArray(List<Run> runs) {
		return runs.toArray(new Run[runs.size()]);
	}

	private static String[] merge(String[]... stringArrays) {
		int size = 0;
		for (String[] a : stringArrays) {
			size += a.length;
		}
		String[] result = new String[size];
		int i = 0;
		for (String[] a : stringArrays) {
			for (int j = 0; j < a.length; j++) {
				result[i] = a[j];
				i++;
			}
		}
		return result;
	}
}
