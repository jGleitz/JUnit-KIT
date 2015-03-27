package final2.subtests;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.ExactRun;
import test.runs.NoOutputRun;
import test.runs.Run;

/**
 * Tests command 'create'.
 * 
 * @author Annika Berger
 * @author Roman Langrehr
 *
 */
public class CreateTest extends LangtonSubtest {

	public CreateTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * Asserts that nortwards looking ants are inserted correctly
	 */
	@Test
	public void createNorthAnt() {
		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ExactRun("ant", is("a")),
				new NoOutputRun("create B,1,1"),
				new ExactRun("ant", is("a,b")),
				new ExactRun("position b", is("1,1")),
				new ExactRun("direction b", is("N")),
				new ExactRun("field 1,1", is("b")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"b000",
				"0000",
				"0a00",
				"Z000"
		};
		runs = new Run[] {
				new ExactRun("ant", is("a,b,z")),
				new NoOutputRun("create H,1,3"),
				new ExactRun("ant", is("a,b,h,z")),
				new ExactRun("position h", is("1,3")),
				new ExactRun("direction h", is("N")),
				new ExactRun("field 1,3", is("b")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}

	/**
	 * Asserts that empty places, which had been used before, can be reused to create an ant.
	 */
	@Test
	public void recyclePlaceTest() {
		inputFile = new String[] {
				"0000",
				"0b00",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new NoOutputRun("escape b"),
				new NoOutputRun("create c,1,1"),
				new ExactRun("ant", is("a,c")),
				new ExactRun("field 1,1", is("c")),
				new ExactRun("direction c", is("S")),
				new ExactRun("position c", is("1,1")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new NoOutputRun("move 1"),
				new NoOutputRun("create b,2,1"),
				new ExactRun("ant", is("a,b")),
				new ExactRun("field 2,1", is("b")),
				new ExactRun("direction b", is("S")),
				new ExactRun("position b", is("2,1")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}

	/**
	 * Asserts that the command create can reuse the name of ants, that have left the playing field.
	 */
	@Test
	public void recylceAntNameTest() {
		inputFile = new String[] {
				"0000",
				"0b00",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new NoOutputRun("escape b"),
				new NoOutputRun("create b,0,0"),
				new ExactRun("ant", is("a,b")),
				new ExactRun("field 0,0", is("b")),
				new ExactRun("direction b", is("S")),
				new ExactRun("position b", is("0,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"0B00",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new NoOutputRun("move 1"),
				new NoOutputRun("create b,0,0"),
				new ExactRun("ant", is("a,b")),
				new ExactRun("field 0,0", is("b")),
				new ExactRun("direction b", is("S")),
				new ExactRun("position b", is("0,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}

	/**
	 * Asserts that command 'create ant,x,y' works on simple examples.
	 */
	@Test
	public void simpleCreateTest() {
		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ExactRun("ant", is("a")),
				new NoOutputRun("create b,1,1"),
				new ExactRun("ant", is("a,b")),
				new ExactRun("position b", is("1,1")),
				new ExactRun("direction b", is("S")),
				new ExactRun("field 1,1", is("b")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"b000",
				"0000",
				"0a00",
				"Z000"
		};
		runs = new Run[] {
				new ExactRun("ant", is("a,b,z")),
				new NoOutputRun("create h,1,3"),
				new ExactRun("ant", is("a,b,h,z")),
				new ExactRun("position h", is("1,3")),
				new ExactRun("direction h", is("S")),
				new ExactRun("field 1,3", is("h")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}
}
