package sheet6.c_bookDatabase.subtests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

import org.junit.Test;

/**
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 31.01.2015
 */
public class CompleteSearchScenarioTest extends BookDatabaseSubTest {


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
		commands = new String[] {
				"search creator=ralf_reussner",
				"search year=2006",
				"search AND(creator=ralf_reussner,year=2006)",
				"search OR(creator=ralf_reussner,year=2006)",
				"search isbn=12345",
				"quit"
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

		multiLineTest(commands, expectedResultMatchers, "0.5", getFile(file));
	}
}
