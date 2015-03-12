package final1.subtests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.fail;
import static test.KitMatchers.containsExactlyDividedBy;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

import test.Input;

/**
 * Performs valid calls to the {@code nodes} command and checks the results.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class ValidNodesCommandTest extends RecommendationSubtest {

    /**
     * Asserts that the {@code nodes} command performs as expected for the example given on the task sheet.
     */
    @Test
    public void taskSheetExampleTest() {
        testAgainstTaskSheet(TASK_SHEET_INPUT_FILE);
    }
    
    @Test
    public void spacesTest() {
        testAgainstTaskSheet(TASK_SHEET_INPUT_FILE_SPACES);
    }
    
    @Test
    public void duplicatesTest() {
        testAgainstTaskSheet(TASK_SHEET_INPUT_FILE_DUPLICATES);
    }
    
    @Test
    public void oneLineTest() {
        String[] queries = new String[] {
                "nodes",
                "recommend S1 1",
                "recommend S1 3",
                "recommend S4 1",
                "recommend S2 1",
                "recommend S2 2",
                "redbutton",
                "recommend S3 1",
                "recommend S3 2"
        };
        List<Matcher<String>> matchers = getMatchers(
                is("a:2,b:1"),
                is(""),
                startsWith("Error,"),
                startsWith("Error,"),
                is(""),
                is("b:1"),
                startsWith("Error,"),
                is("a:2"),
                is("")
        );
        // edges: new String[] { "b:1-[successor-of]->a", "a-[predecessor-of]->b" }
        multiLineTest(addQuit(queries), matchers, Input.getFile(ONE_LINE_INPUT_FILE1));
        
        queries = new String[] {
                "nodes",
                "recommend S1 1",
                "recommend S1 2",
                "recommend S2 2",
                "recommend S3 2",
        };
        matchers = getMatchers(
                is("a,b:2"),
                startsWith("Error,"),
                is(""),
                is(""),
                is("")
        );
        multiLineTest(addQuit(queries), matchers, Input.getFile(ONE_LINE_INPUT_FILE2));
    }
    
    private void testAgainstTaskSheet(String[] input) {
        // the following queries/matchers are taken directly from the task sheet
        String[] queries = new String[] {
                "nodes",
                "recommend S1 105",
                "recommend S3 107",
                "recommend UNION(S1 105,S3 107)",
                "recommend S1 201",
                "recommend UNION(S1 201,INTERSECTION(S1 105,S3 107))",
        };
        List<Matcher<String>> matchers = getMatchers(
                is("calc:202,centos5:105,centos6:106,centos7:107,impress:203,libreoffice:200,officesuite,operatingsystem,software,writer:201"),
                is("centos6:106,centos7:107"),
                is("centos5:105,centos6:106"),
                is("centos5:105,centos6:106,centos7:107"),
                is("calc:202,impress:203,libreoffice:200"),
                is("calc:202,centos6:106,impress:203,libreoffice:200")
        );
        multiLineTest(addQuit(queries), matchers, Input.getFile(input));
    }
    
    @Test
    public void incomplete() {
        fail("This test is still in the development state and therefore incomplete!");
    }
}
