package final2.subtests;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.ExactRun;
import test.runs.NoOutputRun;
import test.runs.Run;

/**
 * Test command 'ant'.
 * @author Annika Berger
 *
 */
public class AntTest extends LangtonSubtest {
	
	public AntTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}
	
	/**
	 * Asserts that command ant works on simple examples.
	 */
	@Test
	public void simpleAntTest() {
		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ExactRun("ant", is("a")),
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
				quit()
		};
		
		sessionTest(runs, Input.getFile(inputFile));
	}
	
	/**
	 * Asserts that command ant works on simple examples with creating new ants.
	 */
	@Test
	public void createAntAntTest() {
		inputFile = new String[] {
				"0000",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ExactRun("ant", is("a")),
				new NoOutputRun("create Z,1,1"),
				new ExactRun("ant", is("a,z")),
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
				new ExactRun("ant", is("a")),
				new NoOutputRun("create Z,1,1"),
				new ExactRun("ant", is("a,z")),
				new NoOutputRun("create b,3,3"),
				new ExactRun("ant", is("a,b,z")),
				quit()
		};
		
		sessionTest(runs, Input.getFile(inputFile));
	}
	
	/**
	 * Asserts that command ant works on simple examples with escaping ants.
	 */
	@Test
	public void escapeAntAntTest() {
		inputFile = new String[] {
				"0b00",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ExactRun("ant", is("a,b")),
				new NoOutputRun("escape b"),
				new ExactRun("ant", is("a")),
				quit()
		};
		
		sessionTest(runs, Input.getFile(inputFile));
		
		inputFile = new String[] {
				"Z000",
				"00h0",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ExactRun("ant", is("a,h,z")),
				new NoOutputRun("escape h"),
				new ExactRun("ant", is("a,z")),
				new NoOutputRun("escape z"),
				new ExactRun("ant", is("a")),
				quit()
		};
		
		sessionTest(runs, Input.getFile(inputFile));
	}
	
	/**
	 * Asserts that command works on examples with ant leaving board.
	 */
	@Test
	public void leaveBoardAntTest() {
		inputFile = new String[] {
				"0B00",
				"0000",
				"0a00",
				"0000"
		};
		runs = new Run[] {
				new ExactRun("ant", is("a,b")),
				move(1),
				new ExactRun("ant", is("a")),
				quit()
		};
	}

}
