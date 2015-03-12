package final1;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import final1.subtests.InvalidInputFileTest;
import final1.subtests.ValidInputFileTest;
import final1.subtests.ValidNodesCommandTest;

/**
 * Test suite for the programming lecture's first final test.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
@RunWith(Suite.class)
@SuiteClasses({
        ValidInputFileTest.class,
        InvalidInputFileTest.class,
        ValidNodesCommandTest.class
})
public class RecommendationTest {
}
