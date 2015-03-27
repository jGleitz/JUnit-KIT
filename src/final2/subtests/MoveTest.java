package final2.subtests;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.ExactRun;
import test.runs.Run;

/**
 * Checks the {@code move} command.
 * 
 * @author Joshua Gleitze
 * @author Annika Berger
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
	 * <code>
	 * 0a
	 * 00
	 * </code>
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
	 * Asserts that when doing more than one move at a time all ants do one move and then start again with the next
	 * move. When doing one step with ant a, then one with ant b they are going to move in a rectangle without
	 * 'crashing'. If a moves first and does all 92 moves at a time it first moves to field 1,0, and then wants to go to
	 * the field 2,0 where already b is. As it is blocked a turns on its field and then moves back to 2,0 next. After
	 * that its direction is W and therefore leaves the board.
	 */
	@Test
	public void multiMoveTest() {
		inputFile = new String[] {
				"000",
				"0B0",
				"A00"
		};
		runs = new Run[] {
				move(92),
				new ExactRun("position a", is("2,0")),
				new ExactRun("position b", is("1,1")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile), "rule=90-90-90-90-90");
	}
}
