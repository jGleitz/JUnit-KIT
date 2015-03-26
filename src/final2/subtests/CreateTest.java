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
 * @author Annika Berger
 *
 */
public class CreateTest extends LangtonSubtest {

	public CreateTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}
	
	/**
	 * Asserts that command 'create ant,x,y' works on simple examples.
	 */
	@Test
	public void SimpleCreateTest() {
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
				quit()
		};
		
		sessionTest(runs, Input.getFile(inputFile));
	}
}
