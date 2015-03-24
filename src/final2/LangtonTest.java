package final2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import final2.subtests.InvalidCommandLineArgumentsTest;
import final2.subtests.PraktomatPublicTest;
import final2.subtests.ValidCommandLineArgumentsTest;

/**
 * Test suite for the programming lecture's first second test.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
@RunWith(Suite.class)
@SuiteClasses({
		ValidCommandLineArgumentsTest.class,
		InvalidCommandLineArgumentsTest.class,
		PraktomatPublicTest.class
})
public class LangtonTest {
}
