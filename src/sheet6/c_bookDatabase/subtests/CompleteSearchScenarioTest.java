package sheet6.c_bookDatabase.subtests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.fail;

import org.junit.Test;

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
