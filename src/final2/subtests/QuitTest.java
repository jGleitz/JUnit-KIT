package final2.subtests;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.runs.Run;

/**
 * Tests command 'quit'.
 * 
 * @author Annika Berger
 *
 */
public class QuitTest extends LangtonSubtest {

	public QuitTest() {
		setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
	}

	/**
	 * 
	 */
	@Test
	public void simpleQuitTest() {
		
		runs = new Run[] {
			quit()
		};

		sessionTest(runs, Input.getFile(TASK_SHEET_INPUT_FILE_1));
	}

}
