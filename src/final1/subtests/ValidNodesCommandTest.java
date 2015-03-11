package final1.subtests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

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
    
    private void testAgainstTaskSheet(String[] input) {
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
