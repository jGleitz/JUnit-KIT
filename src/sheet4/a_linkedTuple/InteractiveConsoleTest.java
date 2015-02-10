package sheet4.a_linkedTuple;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import test.SystemExitStatus;
import test.TestObject;

/**
 * A test for the Interactive Console (Task A) <br>
 * <br>
 * Unfortunately, this test only tested a very small amount of commands.
 * 
 * @author Joshua Gleitze
 */
@SuppressWarnings("deprecation")
public class InteractiveConsoleTest {

    @Before
    public void allowSystemExit0() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_0);
    }

    private static String consoleMessage(String commands) {
        String result = "";
        result += "We ran a session on your interactive console, running the commands \n\n" + commands
                + "\n\n but got unexpected output.";
        return result;
    }

    /**
     * Tests the {@code quit} command of the interactive console. Asserts that:
     * <ul>
     * <li>the program can be terminated by running the command {@code quit}</li>
     * <li>(Implicatory) the program is not terminated by System.exit(x) with x>0</li>
     * <li>the program does not output anything when being terminated through {@code quit}</li>
     * </ul>
     */
    @Test
    public void testConsoleQuit() {
        String commands, expectedOutput, result;
        commands = "quit\n";
        expectedOutput = "";
        result = TestObject.getLastMethodOutput();
        assertThat(consoleMessage(commands), result, is(expectedOutput));
    }

    /**
     * Tests the {@code insert} command of the interactive console. Asserts that:
     * <ul>
     * <li>the command {@code insert} can be run without an error</li>
     * <li>the command {@code insert} does not output anything</li>
     * </ul>
     */
    @Test
    public void testConsoleInsert() {
        String commands, expectedOutput, result;
        commands = "insert 1\n";
        commands += "quit\n";
        expectedOutput = "";
        TestObject.setNextMethodCallInput(commands);
        TestObject.runStaticVoid("main", (Object) new String[]{});
        result = TestObject.getLastMethodOutput();
        assertThat(consoleMessage(commands), result, is(expectedOutput));
    }

    @Test
    public void testConsoleMax() {
        String commands, expectedStart, result;
        commands = "max\n";
        commands += "quit\n";
        expectedStart = "Error,";
        TestObject.setNextMethodCallInput(commands);
        TestObject.runStaticVoid("main", (Object) new String[]{});
        result = TestObject.getLastMethodOutput();
        assertThat(consoleMessage(commands), result, startsWith(expectedStart));
    }
}
