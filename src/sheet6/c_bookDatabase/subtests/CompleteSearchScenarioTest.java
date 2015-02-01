package sheet6.c_bookDatabase.subtests;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.CoreMatchers.is;

import java.util.UUID;

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

		int randomTestRuns = 5;
		for (int i = 0; i < randomTestRuns; i++) {
			String[] shuffledCommands = searchForAll(shuffle(queries));
			String[] shuffledFile = shuffleCase(file);
			multiLineTest(shuffledCommands, expectedResultMatchers, "0.5", Input.getFile(shuffledFile));
		}
	}

	/**
	 * Runs a test scenario with {@code AND} and {@code OR} as attribute values. Asserts that the tested class is not
	 * confused by that.
	 */
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

	/**
	 * Runs a test with a tolerance of {@code 1}. Asserts that the program behaves correctly in that case.
	 */
	@Test
	public void testHighTolerance() {

		queries = new String[] {
				"title=" + UUID.randomUUID().toString(),
				"AND(" + "creator=" + UUID.randomUUID().toString() + "," + "creator=" + UUID.randomUUID().toString()
						+ ")",
				and(and(and("creator=" + UUID.randomUUID().toString(), "creator=" + UUID.randomUUID().toString()),
						and("creator=" + UUID.randomUUID().toString(), "creator=" + UUID.randomUUID().toString())),
						and(and("title=" + UUID.randomUUID().toString(), "title=" + UUID.randomUUID().toString()),
								and("title=" + UUID.randomUUID().toString(), "title=" + UUID.randomUUID().toString()))),
				"creator=a",
				"year=0"
		};
		// @formatter:off
        expectedResultMatchers = getMatchers(
        	is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,true"),
        	is("creator=unknown,title=grundkurs_programmieren_in_java,year=2007,true"),
          	is("creator=ralf_reussner,title=unknown,year=2006,false"),
                        
          	is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,true"),
         	is("creator=unknown,title=grundkurs_programmieren_in_java,year=2007,false"),
         	is("creator=ralf_reussner,title=unknown,year=2006,true"),
	                        
         	is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,true"),
            is("creator=unknown,title=grundkurs_programmieren_in_java,year=2007,false"),
            is("creator=ralf_reussner,title=unknown,year=2006,false"),
                        
            is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,true"),
            is("creator=unknown,title=grundkurs_programmieren_in_java,year=2007,false"),
            is("creator=ralf_reussner,title=unknown,year=2006,true"),
                        
            is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"),
            is("creator=unknown,title=grundkurs_programmieren_in_java,year=2007,true"),
            is("creator=ralf_reussner,title=unknown,year=2006,true")
		);
        // @formatter:on

		multiLineTest(searchForAll(queries), expectedResultMatchers, "1", Input.getFile(taskSheetInputFile));

		int randomTestRuns = 3;
		for (int i = 0; i < randomTestRuns; i++) {
			String[] shuffledCommands = searchForAll(shuffle(queries));
			String[] shuffledFile = shuffleCase(taskSheetInputFile);
			multiLineTest(shuffledCommands, expectedResultMatchers, "1", Input.getFile(shuffledFile));
		}
	}
	
	/**
	 * Runs a test with a tolerance of {@code 0.0000000000001}. Asserts that the program behaves correctly in that case.
	 */
	@Test
	public void testLowTolerance() {
		queries = new String[] {
				"creator=galileocomputin",
				or("title=java_istauch_eine_insel", "creator=ralf_reussmer"),
				or(or(or("title=java_ist_auch_ein_berg", "creator=ralf_reusner"),
						or("title=grundkurs_programmieren_ni_java", "creator=galileocomputin")), "year=2005"),
				or(or("creator=galileocomputing", "year=2007"), "creator=ralf_reussner")
		};
		// @formatter:off
        expectedResultMatchers = getMatchers(
        	is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"),
        	is("creator=unknown,title=grundkurs_programmieren_in_java,year=2007,false"),
          	is("creator=ralf_reussner,title=unknown,year=2006,false"),
                        
          	is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"),
        	is("creator=unknown,title=grundkurs_programmieren_in_java,year=2007,false"),
          	is("creator=ralf_reussner,title=unknown,year=2006,false"),
	                        
          	is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"),
        	is("creator=unknown,title=grundkurs_programmieren_in_java,year=2007,false"),
          	is("creator=ralf_reussner,title=unknown,year=2006,false"),
                        
            is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,true"),
            is("creator=unknown,title=grundkurs_programmieren_in_java,year=2007,true"),
            is("creator=ralf_reussner,title=unknown,year=2006,true")
		);
        // @formatter:on

		multiLineTest(searchForAll(queries), expectedResultMatchers, "0.0000000000001",
				Input.getFile(taskSheetInputFile));

		int randomTestRuns = 3;
		for (int i = 0; i < randomTestRuns; i++) {
			String[] shuffledCommands = searchForAll(shuffle(queries));
			String[] shuffledFile = shuffleCase(taskSheetInputFile);
			multiLineTest(shuffledCommands, expectedResultMatchers, "0.0000000000001", Input.getFile(shuffledFile));
		}
	}

	/**
	 * Runs a test with a tolerance of {@code 0}. Asserts that the program behaves correctly in that case.
	 */
	@Test
	public void testNoTolerance() {
		queries = new String[] {
				"creator=galileocomputing",
				or(or("creator=galileocomputing", "year=2007"), "creator=ralf_reussner")
		};
		// @formatter:off
        expectedResultMatchers = getMatchers(
          	is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"),
        	is("creator=unknown,title=grundkurs_programmieren_in_java,year=2007,false"),
          	is("creator=ralf_reussner,title=unknown,year=2006,false"),
                        
            is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"),
            is("creator=unknown,title=grundkurs_programmieren_in_java,year=2007,false"),
            is("creator=ralf_reussner,title=unknown,year=2006,false")
		);
        // @formatter:on

		multiLineTest(searchForAll(queries), expectedResultMatchers, "0", Input.getFile(taskSheetInputFile));

		int randomTestRuns = 3;
		for (int i = 0; i < randomTestRuns; i++) {
			String[] shuffledCommands = searchForAll(shuffle(queries));
			String[] shuffledFile = shuffleCase(taskSheetInputFile);
			multiLineTest(shuffledCommands, expectedResultMatchers, "0", Input.getFile(shuffledFile));
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

	private String and(String p1, String p2) {
		return "AND(" + p1 + "," + p2 + ")";
	}

	private String or(String p1, String p2) {
		return "OR(" + p1 + "," + p2 + ")";
	}
}
