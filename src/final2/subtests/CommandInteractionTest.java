package final2.subtests;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import test.Input;
import test.runs.ErrorRun;
import test.runs.ExactRun;
import test.runs.NoOutputRun;
import test.runs.Run;

/**
 * Tests the interaction of commands: Changes made by commands have to be reflected by all other commands.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class CommandInteractionTest extends LangtonSubtest {
	/**
	 * Asserts that all commands works on a test file with all ant and cell types. Please refer to
	 * {@link LangtonSubtest#ALL_TYPES_BOARD} for a detailed description of what is supposed to happen.
	 */
	@Test
	public void allTypesAllCommandsTest() {
		runs = new Run[] {
				checkPitch(pitchToLowercase(ALL_TYPES_BOARD)),
				new NoOutputRun("escape c"),
				new ErrorRun("position c"),
				new ErrorRun("direction c"),
				new ErrorRun("create z,2,1"),
				new ErrorRun("create a,1,0"),
				new ErrorRun("escape z"),
				new ExactRun("ant", is("a,b,d,i,j,r,s")),
				move(1),
				new ExactRun("field 2,0", is("0")),
				new ExactRun("field 3,0", is("0")),
				new ErrorRun("escape c"),
				new ErrorRun("position c"),
				new ErrorRun("direction c"),
				move(1),
				new NoOutputRun("create c,2,0"),
				checkPitch(ALL_TYPES_PITCHES[1]),
				new ExactRun("ant", is("b,c,d,i,r,s")),
				new ExactRun("field 2,0", is("c")),
				new ErrorRun("create c,2,1"),
				new ErrorRun("create a,1,0"),
				new ExactRun("ant", is("b,c,d,i,r,s")),
				new ExactRun("field 3,0", is("0")),
				new ExactRun("position c", is("2,0")),
				new ExactRun("direction c", is("S")),
				new ErrorRun("move -1"),
				new ErrorRun("quit now"),
				move(1),
				checkPitch(ALL_TYPES_PITCHES[2]),
				new NoOutputRun("escape i"),
				new ExactRun("ant", is("b,c,r,s")),
				new ExactRun("field 0,3", is("3")),
				new NoOutputRun("create i,0,3"),
				new ExactRun("ant", is("b,c,i,r,s")),
				new ExactRun("field 0,3", is("i")),
				new ErrorRun(" quit"),
				new NoOutputRun("escape i"),
				new ExactRun("ant", is("b,c,r,s")),
				new ExactRun("field 0,3", is("3")),
				new NoOutputRun("create i,0,3"),
				new ExactRun("field 0,3", is("i")),
				new ExactRun("ant", is("b,c,i,r,s")),
				checkPitch(ALL_TYPES_PITCHES[2]),
				move(2),
				new ExactRun("ant", is("b")),
				checkPitch(ALL_TYPES_PITCHES[4]),
				quit()
		};
		sessionTest(runs, Input.getFile(ALL_TYPES_BOARD), ALL_TYPES_RULE, ALL_TYPES_SPEEDUP);
	}
}
