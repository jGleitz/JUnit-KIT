package final2.subtests;

import org.junit.Test;

import test.Input;
import test.runs.Run;

/**
 * Checks the {@code print} command without running any other commands. Asserts that {@code print} works, which is
 * crucial for other tests to work.
 * 
 * @author Joshua Gleitze
 */
public class PrintTest extends LangtonSubtest {

	@Test
	public void correctInputFilePrintTest() {
		String[][] inputFiles = {
				PUBLIC_PRAKTOMAT_TEST_FILE_1,
				PUBLIC_PRAKTOMAT_TEST_FILE_2,
				PUBLIC_PRAKTOMAT_TEST_FILE_3,
				PUBLIC_PRAKTOMAT_TEST_FILE_4,
				TASK_SHEET_INPUT_FILE_1,
				TASK_SHEET_INPUT_FILE_2
		};

		runs = new Run[] {
				null,
				quit()
		};
		for (String[] inputFile : inputFiles) {
			runs[0] = checkPitch(pitchToLowercase(inputFile));
			sessionTest(runs, Input.getFile(inputFile));
		}
	}
}
