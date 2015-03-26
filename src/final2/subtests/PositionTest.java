package final2.subtests;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.ExactRun;
import test.runs.NoOutputRun;
import test.runs.Run;

/**
 * 
 * @author Annika Berger
 *
 */
public class PositionTest extends LangtonSubtest {

	public PositionTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * Asserts that {@code position name} works on a very simple example.
	 */
	@Test
	public void simplePositionTest() {
		inputFile = new String[] {
				"000",
				"0a0",
				"000"
		};

		runs = new Run[] {
				new ExactRun("position a", is("1,1")),
				new ExactRun("position A", is("1,1")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"Z00",
				"0a0",
				"000"
		};

		runs = new Run[] {
				new ExactRun("position a", is("1,1")),
				new ExactRun("position A", is("1,1")),
				new ExactRun("position z", is("0,0")),
				new ExactRun("position Z", is("0,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
		
		inputFile = new String[] {
				"Z000",
				"00a0",
				"000h"
		};

		runs = new Run[] {
				new ExactRun("position a", is("1,2")),
				new ExactRun("position A", is("1,2")),
				new ExactRun("position z", is("0,0")),
				new ExactRun("position Z", is("0,0")),
				new ExactRun("position h", is("2,3")),
				new ExactRun("position H", is("2,3")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}
	
	/**
	 * Asserts that {@code position name} works on a example with simple moves.
	 */
	@Test
	public void moveAntPositionTest() {
		inputFile = new String[] {
				"000",
				"0a0",
				"000"
		};

		runs = new Run[] {
				new ExactRun("position a", is("1,1")),
				new ExactRun("position A", is("1,1")),
				move(1),
				new ExactRun("position a", is("2,1")),
				new ExactRun("position A", is("2,1")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"z00",
				"0A0",
				"000"
		};

		runs = new Run[] {
				new ExactRun("position a", is("1,1")),
				new ExactRun("position A", is("1,1")),
				new ExactRun("position z", is("0,0")),
				new ExactRun("position Z", is("0,0")),
				move(1),
				new ExactRun("position a", is("0,1")),
				new ExactRun("position A", is("0,1")),
				new ExactRun("position z", is("1,0")),
				new ExactRun("position Z", is("1,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
		
		inputFile = new String[] {
				"z000",
				"00A0",
				"H000"
		};

		runs = new Run[] {
				new ExactRun("position a", is("1,2")),
				new ExactRun("position A", is("1,2")),
				new ExactRun("position z", is("0,0")),
				new ExactRun("position Z", is("0,0")),
				new ExactRun("position h", is("2,0")),
				new ExactRun("position H", is("2,0")),
				move(1),
				new ExactRun("position a", is("0,2")),
				new ExactRun("position A", is("0,2")),
				new ExactRun("position z", is("0,0")),
				new ExactRun("position Z", is("0,0")),
				new ExactRun("position h", is("1,0")),
				new ExactRun("position H", is("1,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
		
		
		inputFile = new String[] {
				"0a0",
				"000",
				"000"
		};

		runs = new Run[] {
				new ExactRun("position a", is("0,1")),
				new ExactRun("position A", is("0,1")),
				move(1),
				new ExactRun("position a", is("1,1")),
				new ExactRun("position A", is("1,1")),
				move(1),
				new ExactRun("position a", is("1,2")),
				new ExactRun("position A", is("1,2")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));

		inputFile = new String[] {
				"0z0",
				"000",
				"0A0"
		};

		runs = new Run[] {
				new ExactRun("position a", is("2,1")),
				new ExactRun("position A", is("2,1")),
				new ExactRun("position z", is("0,1")),
				new ExactRun("position Z", is("0,1")),
				move(1),
				new ExactRun("position a", is("1,1")),
				new ExactRun("position A", is("1,1")),
				new ExactRun("position z", is("0,1")),
				new ExactRun("position Z", is("0,1")),
				move(1),
				new ExactRun("position a", is("1,0")),
				new ExactRun("position A", is("1,0")),
				new ExactRun("position z", is("0,1")),
				new ExactRun("position Z", is("0,1")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}
	
	/**
	 * Asserts that {@code position name} works on simple examples with newly created ants.
	 */
	@Test
	public void createAntPositionTest() {
		inputFile = new String[] {
				"000",
				"0a0",
				"000"
		};

		runs = new Run[] {
				new ExactRun("position a", is("1,1")),
				new ExactRun("position A", is("1,1")),
				new NoOutputRun("create z,0,0"),
				new ExactRun("position a", is("1,1")),
				new ExactRun("position A", is("1,1")),
				new ExactRun("position z", is("0,0")),
				new ExactRun("position Z", is("0,0")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
		
		inputFile = new String[] {
				"z00",
				"0a0",
				"000"
		};

		runs = new Run[] {
				new ExactRun("position a", is("1,1")),
				new ExactRun("position A", is("1,1")),
				new ExactRun("position z", is("0,0")),
				new ExactRun("position Z", is("0,0")),
				new NoOutputRun("create H,2,2"),
				new ExactRun("position a", is("1,1")),
				new ExactRun("position A", is("1,1")),
				new ExactRun("position z", is("0,0")),
				new ExactRun("position Z", is("0,0")),
				new ExactRun("position h", is("2,2")),
				new ExactRun("position H", is("2,2")),
				quit()
		};
		sessionTest(runs, Input.getFile(inputFile));
	}
}
