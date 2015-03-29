package final2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import test.KITSuite;
import final2.subtests.*;

/**
 * Test suite for the programming lecture's second final task.
 * 
 * @author Joshua Gleitze
 * @author Roman Langrehr
 */
@RunWith(KITSuite.class)
@SuiteClasses({
		QuitTest.class,
		ValidCommandLineArgumentsTest.class,
		ValidInputFileTest.class,
		PrintTest.class,
		MoveTest.class,
		PositionTest.class,
		AntTest.class,
		CreateTest.class,
		FieldTest.class,
		DirectionTest.class,
		EscapeTest.class,
		InvalidCommandLineArgumentsTest.class,
		InvalidInputFileTest.class,
		InvalidCommandTest.class,
		CommandInteractionTest.class,
		PraktomatPublicTest.class,
})
public class LangtonTest {
}
