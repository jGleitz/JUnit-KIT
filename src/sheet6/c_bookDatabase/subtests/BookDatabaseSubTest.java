package sheet6.c_bookDatabase.subtests;

import test.Input;
import test.InteractiveConsoleTest;

/**
 * Contains all helper methods and fields to test Sheet 6 Task C.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 31.01.2015
 */
public class BookDatabaseSubTest extends InteractiveConsoleTest {
    /**
     * You can put a test input file in here
     */
    protected String[] file;
    /**
     * You can put a one lined test input file in here
     */
    protected String line;

    /*
     * PREDEFINED BOOK INPUT FILES
     */
    /**
     * The input file that was given as valid on the task sheet.
     */
    protected static final String[] taskSheetInputFile = {
            "title=Java_ist_auch_eine_Insel,creator=GalileoComputing",
            "tITle=Grundkurs_Programmieren_iN_JAVA,year=2007",
            "Creator=Ralf_Reussner,year=2006"
    };

    /**
     * A very simple input file, intended not to produce a parsing error (to focus on other things). Therefore it only
     * contains lower case characters between a and z and all attributes.
     */
    protected static final String[] simpleValidFile = {
        "title=musterbuch,creator=mustermann,year=2000"
    };

    /*
     * HELPER METHODS
     */

    @Override
    protected String consoleMessage(String commands, String[] commandLineArguments) {
        String result = "";
        result += "We ran a session on your interactive console" + getArguments(commandLineArguments)
                + Input.fileMessage(commandLineArguments) + "running the commands \n\n" + commands
                + "\n\n but got unexpected output:\n";
        return result;
    }

    @Override
    protected String getArguments(String[] commandLineArguments) {
        String result = "";
        if (commandLineArguments.length == 2) {
            result = "\n that has been called with the command line arguments [" + commandLineArguments[0] + ", ";
            result += (Input.isFile(commandLineArguments[1])) ? "textFile" : commandLineArguments[1];
            result += "]";
        }
        return result;
    }
}
