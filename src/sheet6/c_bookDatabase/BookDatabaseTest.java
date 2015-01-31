package sheet6.c_bookDatabase;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sheet6.c_bookDatabase.subtests.*;

/**
 * A test for the Interactive Console of the Book Database (Task C) <br>
 * This test consists of various subtests, all placed in the {@link sheet6.c_bookDatabase.subtests} package. See them
 * for documentation<br>
 * NOTE: This test is far from being complete and under heavy development!
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 29.01.2015
 */
@RunWith(Suite.class)
@SuiteClasses({
		CommandLineArgumentsTest.class,
		InputFileParsingTest.class,
		BasicCommandsTest.class,
		CompleteSearchScenarioTest.class
})
public class BookDatabaseTest {
}
