package final2.subtests;

import org.junit.Test;

import test.Input;
import test.runs.Run;

/**
 * Checks the {@code move} command.
 * 
 * @author Joshua Gleitze
 */
public class MoveTest extends LangtonSubtest {

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
}
