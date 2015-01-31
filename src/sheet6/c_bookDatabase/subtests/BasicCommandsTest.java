/**
 * 
 */
package sheet6.c_bookDatabase.subtests;

import org.junit.Test;

import test.TestObject;
import test.TestObject.SystemExitStatus;

/**
 * @author Joshua Gleitze
 * @version 1.0
 * @since 31.01.2015
 */
public class BasicCommandsTest extends BookDatabaseSubTest {

	@Test
	public void testIllegalCommand() {
		TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);

		// invalid command
		commands = new String[] {
				"xyz",
				"quit"
		};
		errorTest(commands, "", getFile(simpleValidFile));

		// argument for quit
		commands = new String[] {
				"quit now",
				"quit"
		};
		errorTest(commands, "", getFile(simpleValidFile));
	}

}
