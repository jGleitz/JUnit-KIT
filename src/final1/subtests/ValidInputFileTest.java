package final1.subtests;

import static org.junit.Assert.fail;

import org.junit.Test;

import test.Input;

/**
 * Starts the program with several valid input files without performing any actions and checks if the program is able to
 * read in the files.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class ValidInputFileTest extends RecommendationSubtest {

    /**
     * Asserts that the tested class is able to read in the input file given as an example on the task sheet without
     * output, exceptions or a call to {@code System.exit(x)} with {@code x>0}.
     */
    @Test
    public void taskSheetInputFileTest() {
        noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE));
    }
    
    @Test
    public void incomplete() {
        fail("This test is still in the development state and therefore incomplete!");
    }

}
