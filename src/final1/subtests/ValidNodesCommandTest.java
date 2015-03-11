package final1.subtests;

import static org.junit.Assert.fail;

import org.junit.Test;

import test.Input;

/**
 * Performs valid calls to the {@code nodes} command and checks the results.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class ValidNodesCommandTest extends RecommendationSubtest {

    private static final String TASK_SHEET_NODES
            = "calc:202,centos5:105,centos6:106,centos7:107,impress:203,libreoffice:200,officesuite,operatingsystem,software,writer:201";
    
    /**
     * Asserts that the {@code nodes} command performs as expected for the example given on the task sheet.
     */
    @Test
    public void taskSheetExampleTest() {
        command = "nodes";
        expectedResult = TASK_SHEET_NODES;
        oneLineTest(addQuit(command), expectedResult, Input.getFile(TASK_SHEET_INPUT_FILE));
    }
    
    @Test
    public void spacesTest() {
        command = "nodes";
        expectedResult = TASK_SHEET_NODES;
        oneLineTest(addQuit(command), expectedResult, Input.getFile(TASK_SHEET_INPUT_FILE));
    }
    
    @Test
    public void incomplete() {
        fail("This test is still in the development state and therefore incomplete!");
    }
}
