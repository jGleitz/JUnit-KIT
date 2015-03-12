package final1.subtests;

import static org.junit.Assert.fail;

import org.junit.Test;

import test.Input;
import test.SystemExitStatus;

/**
 * Starts the program with several valid input files without performing any actions. Checks if the tested class is able
 * to read in the files without output, exceptions or a call to {@code System.exit(x)} with {@code x>0}.
 *
 * @author Joshua Gleitze
 * @author Martin Loeper
 * @version 1.1
 */
public class ValidInputFileTest extends RecommendationSubtest {

    private String[] input;

    public ValidInputFileTest() {
        setAllowedSystemExitStatus(SystemExitStatus.WITH_0);
    }

    /**
     * Asserts that the tested class is able to read in the input file given as an example on the task.
     */
    @Test
    public void taskSheetInputFileTest() {
        noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE));
    }

    /**
     * Asserts that the tested class is able to read in the input file from the task sheet, decorated with some legal
     * spaces.
     */
    @Test
    public void spacesTest() {
        noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_SPACES));
    }

    /**
     * Asserts that the tested class is able to read in input files that define relations that may easily be confused
     * with circles.
     */
    @Test
    public void pseudoCirclesTest() {
        noOutputTest("quit", Input.getFile(PSEUDO_CIRCLE_INPUT_FILE1));
        noOutputTest("quit", Input.getFile(PSEUDO_CIRCLE_INPUT_FILE2));
    }

    /**
     * Asserts that the tested class is able to read in input files that contain semantically dublicates.
     */
    @Test
    public void duplicatesTest() {
        noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_DUPLICATES));
    }

    /**
     * Asserts that the tested class is able to read in the one-lined test input files.
     */
    @Test
    public void oneLineTest() {
        noOutputTest("quit", Input.getFile(ONE_LINE_INPUT_FILE1));
        noOutputTest("quit", Input.getFile(ONE_LINE_INPUT_FILE2));
    }

    /**
     * Asserts that the tested class is able to read in input files that have keywords as shop element names.
     */
    @Test
    public void keywordTest() {
        input = new String[] {
                "contains contains containers",
                "containers contains dump(id=1)"
        };
        noOutputTest("quit", Input.getFile(input));

        input = new String[] {
                "contains (id=1) contained-in containers",
                "contains (id=1) part-of dump(id=2)"
        };
        noOutputTest("quit", Input.getFile(input));
    }

    @Test
    public void incomplete() {
        fail("This test is still in the development state and therefore incomplete!");
    }

}
