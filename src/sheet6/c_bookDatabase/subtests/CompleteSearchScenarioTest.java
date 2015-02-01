package sheet6.c_bookDatabase.subtests;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

import test.Input;

/**
 * Asserts the program's correct behaviour for complete search scenarios. This means that a more or less complex input
 * file is read in on which {@code search} operations are run. The program's complete output is asserted to match
 * <i>exactly</i> the expected one.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 31.01.2015
 */
public class CompleteSearchScenarioTest extends BookDatabaseSubTest {
	private String[] queries;

	/**
	 * Fails the test as this test is incomplete an therefore does not grant anything. Remove this method as soon as the
	 * test reaches a certain degree of relevance.
	 */
	@Test
	public void incomplete() {
		fail("\n\nThis test is under development and incomplete!\n\n");
	}

	/**
	 * Simulates the Praktomat's public test. Asserts that:
	 * <ul>
	 * <li>the program outputs the expected search responses.
	 * <li>an error message is printed for the last search command (key isbn not allowed)
	 * </ul>
	 */
	@Test
	public void publicPraktomatTest() {
		file = new String[] {
				"creator=galileocomputing,title=java_ist_auch_eine_insel",
				"title=grundkursprogrammieren_in_java,year=2007",
				"creator=ralf_reussner,year=2006"
		};
		
		queries = new String[] {
				"creator=ralf_reussner",
				"year=2006",
				"AND(creator=ralf_reussner,year=2006)",
				"OR(creator=ralf_reussner,year=2006)",
				"isbn=12345"
		};
		// @formatter:off
		expectedResultMatchers = getMatchers(
				is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"),
				is("creator=unknown,title=grundkursprogrammieren_in_java,year=2007,false"),
				is("creator=ralf_reussner,title=unknown,year=2006,true"),

				is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"),
				is("creator=unknown,title=grundkursprogrammieren_in_java,year=2007,true"),
				is("creator=ralf_reussner,title=unknown,year=2006,true"),

				is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"),
				is("creator=unknown,title=grundkursprogrammieren_in_java,year=2007,false"),
				is("creator=ralf_reussner,title=unknown,year=2006,true"),

				is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"),
				is("creator=unknown,title=grundkursprogrammieren_in_java,year=2007,true"),
				is("creator=ralf_reussner,title=unknown,year=2006,true"),
				
				startsWith("Error,")
		);
		// @formatter:on
		
		multiLineTest(searchForAll(queries), expectedResultMatchers, "0.5", Input.getFile(file));

		int randomTestRuns = 2;
		for (int i = 0; i < randomTestRuns; i++) {
			String[] shuffledCommands = searchForAll(shuffle(queries));
			String[] shuffledFile = shuffleCase(file);
			multiLineTest(shuffledCommands, expectedResultMatchers, "0.5", Input.getFile(shuffledFile));
		}
	}

	@Test
	public void testAndOrAttributeValues() {
		file = new String[] {
				"creator=AND,title=OR",
				"title=and,creator=or"
		};
		
		queries = new String[] {
				"creator=and",
				"OR(creator=AND,title=and)",
				"AND(creator=AND,title=OR)",
				"OR(AND(creator=AND,title=OR),AND(title=and,creator=or))",
		};
		// @formatter:off
		expectedResultMatchers = getMatchers(
				is("creator=and,title=or,year=unknown,true"),
				is("creator=or,title=and,year=unknown,false"),

				is("creator=and,title=or,year=unknown,true"),
				is("creator=or,title=and,year=unknown,true"),

				is("creator=and,title=or,year=unknown,true"),
				is("creator=or,title=and,year=unknown,false"),

				is("creator=and,title=or,year=unknown,true"),
				is("creator=or,title=and,year=unknown,true")
		);
		// @formatter:on
		
		multiLineTest(searchForAll(queries), expectedResultMatchers, "0.00000000001", Input.getFile(file));

		int randomTestRuns = 3;
		for (int i = 0; i < randomTestRuns; i++) {
			String[] shuffledCommands = searchForAll(shuffle(queries));
			String[] shuffledFile = shuffleCase(file);
			multiLineTest(shuffledCommands, expectedResultMatchers, "0.00000000001", Input.getFile(shuffledFile));
		}
	}

	private String[] searchForAll(String[] queries) {
		String[] result = new String[queries.length + 1];
		for (int i = 0; i < queries.length; i++) {
			result[i] = "search " + queries[i];
		}
		result[queries.length] = "quit";
		return result;
	}
}
