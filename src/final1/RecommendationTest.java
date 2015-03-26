package final1;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import test.KITSuite;
import final1.subtests.BasicCommandTest;
import final1.subtests.EdgesCommandTest;
import final1.subtests.ExportCommandTest;
import final1.subtests.InvalidCommandLineArgumentsTest;
import final1.subtests.InvalidInputFileTest;
import final1.subtests.InvalidRecommendTerms;
import final1.subtests.ValidInputFileTest;
import final1.subtests.ValidNodesCommandTest;
import final1.subtests.ValidRecommendCommandTest;

/**
 * Test suite for the programming lecture's first final test.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
@RunWith(KITSuite.class)
@SuiteClasses({
		InvalidCommandLineArgumentsTest.class,
		BasicCommandTest.class,
		ValidInputFileTest.class,
		InvalidInputFileTest.class,
		ValidNodesCommandTest.class,
		ValidRecommendCommandTest.class,
		InvalidRecommendTerms.class,
		EdgesCommandTest.class,
		ExportCommandTest.class
})
public class RecommendationTest {
}
