package sheet6.c_bookDatabase.subtests;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.not;

import test.TestObject;
import test.TestObject.SystemExitStatus;

/**
 * Asserts the program's behaviour to handle the basic commands. The tested class should print error messages for
 * unknown commands. Meanwhile, it should be able to handle the {@code search} and {@code quit} command. Note that the
 * correct execution and parsing of a {@code search} is asserted in {@link SearchTermParsingTest} and
 * {@link CompleteSearchScenarioTest}.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 31.01.2015
 */
public class BasicCommandsTest extends BookDatabaseSubTest {

    /**
     * Tests the {@code quit} command. Asserts that:
     * <ul>
     * <li>The program can be terminated using the {@code quit} command
     * <li>The program does not print anything to the console
     * <li>If the program calls {@code System.exit(x)}, {@code x=0}
     * </ul>
     */
    @Test
    public void testQuit() {
        // simple quit
        command = "quit";
        oneLineTest(command, "", "0.5", getFile(simpleValidFile));
    }
    
    /**
     * Tests the {@code search} command. Asserts that:
     * <ul>
     * <li>The program accepts the {@code search} command
     * <li>The program does not print an error message
     * <li>If the program calls {@code System.exit(x)}, {@code x=0}
     * </ul>
     */
    @Test
    public void testSearch() {
        // simple search
        commands = new String[] {
                "search creator=Mustermann",
                "quit"
        };
        oneLineTest(commands, not(startsWith("Error,")), "0.5", getFile(simpleValidFile));
    }

    /**
     * Tests invalid commands. Asserts that the program prints an error message for:
     * <ul>
     * <li>unknown commands
     * <li>parameters for {@code quit}
     * </ul>
     */
    @Test
    public void testIllegalCommand() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);

        // invalid command
        commands = new String[] {
                "xyz",
                "quit"
        };
        errorTest(commands, "0.5", getFile(simpleValidFile));

        // argument for quit
        commands = new String[] {
                "quit now",
                "quit"
        };
        errorTest(commands, "0.5", getFile(simpleValidFile));
        
        // no argument for search
        commands = new String[] {
                "search",
                "quit"
        };
        errorTest(commands, "0.5", getFile(simpleValidFile));
        
        // starts right, ends wrong
        commands = new String[] {
                "searchi creator=Mustermann",
                "quit"
        };
        errorTest(commands, "0.5", getFile(simpleValidFile));
        
        // starts right, ends wrong
        commands = new String[] {
                "quiti",
                "quit"
        };
        errorTest(commands, "0.5", getFile(simpleValidFile));
    }

}
