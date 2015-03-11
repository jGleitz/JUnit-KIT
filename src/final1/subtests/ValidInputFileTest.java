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

    private String[] input;
    
    /**
     * Asserts that the tested class is able to read in the input file given as an example on the task sheet without
     * output, exceptions or a call to {@code System.exit(x)} with {@code x>0}.
     */
    @Test
    public void taskSheetInputFileTest() {
        noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE));
    }
    
    @Test
    public void spacesTest() {
        noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_SPACES));
    }
    
    @Test
    public void pseudoCirclesTest() {
        noOutputTest("quit", Input.getFile(PSEUDO_CIRCLE_INPUT_FILE1));
        noOutputTest("quit", Input.getFile(PSEUDO_CIRCLE_INPUT_FILE2));
    }
    
    @Test
    public void duplicatesTest() {
        noOutputTest("quit", Input.getFile(TASK_SHEET_INPUT_FILE_DUPLICATES));
    }
    
    @Test
    public void oneLineTest() {
        noOutputTest("quit", Input.getFile(ONE_LINE_INPUT_FILE1));
        noOutputTest("quit", Input.getFile(ONE_LINE_INPUT_FILE2));
    }
    
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
