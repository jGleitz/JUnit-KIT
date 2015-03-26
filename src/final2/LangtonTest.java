package final2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import final2.subtests.AntTest;
import final2.subtests.BadInputFileTest;
import final2.subtests.CreateTest;
import final2.subtests.EscapeTest;
import final2.subtests.InvalidCommandLineArgumentsTest;
import final2.subtests.MoveTest;
import final2.subtests.PositionTest;
import final2.subtests.PraktomatPublicTest;
import final2.subtests.PrintTest;
import final2.subtests.QuitTest;
import final2.subtests.ValidCommandLineArgumentsTest;

/**
 * Test suite for the programming lecture's second final task.
 * 
 * @author Joshua Gleitze
 * @author Roman Langrehr
 */
@RunWith(Suite.class)
@SuiteClasses({
		ValidCommandLineArgumentsTest.class,
		InvalidCommandLineArgumentsTest.class,
		PraktomatPublicTest.class,
		MoveTest.class,
		PrintTest.class,
		BadInputFileTest.class,
		PositionTest.class,
		AntTest.class,
		CreateTest.class,
		EscapeTest.class,
		QuitTest.class
})
public class LangtonTest {
}
