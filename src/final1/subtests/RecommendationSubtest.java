package final1.subtests;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import test.InteractiveConsoleTest;
import test.SystemExitStatus;
import test.TestObject;

/**
 * Base class for implementing subtest for the programming lecture's first final task. Contains some convenience methods
 * and fields.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public abstract class RecommendationSubtest extends InteractiveConsoleTest {
    protected String[] TASK_SHEET_INPUT_FILE = new String[] {
            "CentOS5(id=105) contained-in operatingSystem",
            "    centOS6   (  id   =   106)   contained-in    OperatingSystem    ",
            "operatingSystem contains centos7 ( id = 107 )",
            "operatingsystem contained-in Software",
            "CentOS7 (id=107) successor-of centos6(id=106)",
            "CentOS5 (id=105) predecessor-of centos6(id=106)",
            "writer (id=201) contained-in officesuite",
            "calc (id=202) contained-in officesuite",
            "impress (id=203) contained-in officesuite",
            "officesuite contained-in software",
            "LibreOffice (id=200) contained-in officesuite",
            "writer (id=201) part-of LibreOffice   (  id  =  200   )  ",
            "calc (id=202) part-of libreoffice(id=200)",
            "libreoffice (id=200) has-part impress (id=203)"
    };

    protected String[] PSEUDO_CIRCLE_INPUT_FILE1 = new String[] {
            "A(id=1) successor-of B(id=2)",
            "B(id=2) predecessor-of C(id=3)",
            "C(id=3) predecessor-of A(id=1)"
    };
    
    protected String[] PSEUDO_CIRCLE_INPUT_FILE2 = new String[] {
            "A (id=1) successor-of B (id=2)",
            "C (id=3) predecessor-of B (id=2)",
            "C (id=3) successor-of D (id=5)",
            "D (id=5) predecessor-of A (id=1)"
    };
    
    /**
     * Runs {@link #errorTest(String, String...)} with the provided arguments and asserts that {@code System.exit(x)}
     * was called with {@code x > 0} afterwards.
     * 
     * @param command
     *            The command to run on the console.
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void exitTest(String command, String... args0) {
        exitTest(wrapInArray(command), args0);
    }

    /**
     * Runs {@link #errorTest(String[], String...)} with the provided arguments and asserts that {@code System.exit(x)}
     * was called with {@code x > 0} afterwards.
     * 
     * @param commands
     *            The commands to run on the console
     * @param args0
     *            The arguments for the {@code main}-method
     */
    protected void exitTest(String[] commands, String... args0) {
        TestObject.allowSystemExit(SystemExitStatus.ALL);
        errorTest(commands, args0);
        assertThat(consoleMessage(commands, args0) + "\nWrong system exit status!",
            TestObject.getLastMethodsSystemExitStatus(), is(SystemExitStatus.WITH_GREATER_THAN_0));
    }
}
