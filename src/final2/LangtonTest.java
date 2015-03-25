package final2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import test.KITSuite;
import final2.subtests.InvalidCommandLineArgumentsTest;
import final2.subtests.MoveTest;
import final2.subtests.PraktomatPublicTest;
import final2.subtests.PrintTest;
import final2.subtests.ValidCommandLineArgumentsTest;

/**
 * Test suite for the programming lecture's first second test.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
@RunWith(KITSuite.class)
@SuiteClasses({
		ValidCommandLineArgumentsTest.class,
		InvalidCommandLineArgumentsTest.class,
		PrintTest.class,
		MoveTest.class,
		PraktomatPublicTest.class
})
public class LangtonTest {
}
