package final2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import final2.subtests.InvalidCommandLineArgumentsTest;

/**
 * Test suite for the programming lecture's first second test.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
@RunWith(Suite.class)
@SuiteClasses({
		InvalidCommandLineArgumentsTest.class
})
public class LangtonTest {
}
