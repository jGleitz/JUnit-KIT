package final1.subtests;

import org.junit.Before;
import org.junit.Test;

import test.Input;
import test.SystemExitStatus;
import test.TestObject;

/**
 * Starts the program with several valid input files without performing any actions and checks if the program is able to
 * read in the files.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class InvalidInputFileTest extends RecommendationSubtest {

    @Before
    public void defaultSystemExitStatus() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
    }
   
    @Test
    public void synthaxErrorsTest() {
        errorTest("quit", Input.getFile(SYNTHAX_ERROR_1));
        errorTest("quit", Input.getFile(SYNTHAX_ERROR_2));
    }
    
    @Test
    public void semanticErrorsTest() {
        errorTest("quit", Input.getFile(SEMANTIC_ERROR_1));
        errorTest("quit", Input.getFile(SEMANTIC_ERROR_2));
        errorTest("quit", Input.getFile(SEMANTIC_ERROR_3));
    }
    
    @Test
    public void nameIDMissmatchTest() {
        errorTest("quit", Input.getFile(NAME_ID_MISSMATCH_1));
        errorTest("quit", Input.getFile(NAME_ID_MISSMATCH_2));
    }
    
    @Test
    public void nameTypeMissmatchTest() {
        errorTest("quit", Input.getFile(NAME_TYPE_MISSMATCH_1));
        errorTest("quit", Input.getFile(NAME_TYPE_MISSMATCH_2));
    }
    
    @Test
    public void circleTest() {
        errorTest("quit", Input.getFile(CIRCLE_1));
        errorTest("quit", Input.getFile(CIRCLE_2));
        errorTest("quit", Input.getFile(CIRCLE_3));
        errorTest("quit", Input.getFile(CIRCLE_4));
    }
    

}
