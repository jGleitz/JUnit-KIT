package final2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import test.KITSuite;
import final2.subtests.DirectionTest;
import final2.subtests.FieldTest;
import final2.subtests.ValidInputFileTest;
import final2.subtests.AntTest;
import final2.subtests.InvalidInputFileTest;
import final2.subtests.CreateTest;
import final2.subtests.EscapeTest;
import final2.subtests.InvalidCommandLineArgumentsTest;
import final2.subtests.InvalidCommandTest;
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
		PraktomatPublicTest.class,
})
public class LangtonTest {
}
