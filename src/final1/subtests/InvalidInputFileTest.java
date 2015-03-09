package final1.subtests;

import static org.junit.Assert.fail;

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
    private String[] input;

    @Before
    public void defaultSystemExitStatus() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
    }
    
    @Test
    public void incomplete() {
        fail("This test is still in the development state and therefore incomplete!");
    }
   
    @Test
    public void syntaxErrorsTest() {
        input = new String[] {
                "CentOS5 id= 12) contains operatingsystems"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "CentOS5 (id= 12) likes operatingsystems"
        };
        exitTest("quit", Input.getFile(input));
    }
    
    @Test
    public void semanticErrorsTest() {
        input = new String[] {
                "CentOS5 (id= 12) contains operatingsystems"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "operatingsystems part-of CentOS5 (id = 10)"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "operatingsystems contained-in CentOS5 (id = 10)"
        };
        exitTest("quit", Input.getFile(input));
    }
    
    @Test
    public void nameIDMissmatchTest() {
        input = new String[] {
                "CentOS5 (id = 5) contained-in operatingsystems",
                "CentOS5 (id = 6) contained-in Cent"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "CentOS5 (id = 5) contained-in operatingsystems",
                "CentOS5Alt (id = 5) contained-in Cent"
        };
        exitTest("quit", Input.getFile(input));
    }
    
    @Test
    public void nameTypeMissmatchTest() {
        input = new String[] {
                "CentOS5 (id = 5) contained-in operatingsystems",
                "CentOS5 contained-in operatingsystems"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "CentOS5 contained-in operatingsystems",
                "CentOS5 (id = 5) contained-in operatingsystems"
        };
        exitTest("quit", Input.getFile(input));
    }
    
    @Test
    public void circleTest() {
        input = new String[] {
                "operatingsystems contained-in operatingsystems"
        };
        exitTest("quit", Input.getFile(input));

        input = new String[] {
                "os (id=1) part-of os (id=1)"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "writer (id=1) part-of office (id=2)",
                "office (id=2) part-of officeSuite (id=3)",
                "officeSuite (id=3) part-of WorkTools (id=4)",
                "worktools (id=4) part-of writer (id=1)"
        };
        exitTest("quit", Input.getFile(input));
        
        input = new String[] {
                "writer contained-in office",
                "office contained-in writer",
                "officeSuite contained-in worktools",
                "writer contains worktools"
        };
        exitTest("quit", Input.getFile(input));
    }
    

}
