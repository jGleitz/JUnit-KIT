package final2.subtests;

import test.Input;
import test.InteractiveConsoleTest;

/**
 * Base class for implementing subtest for the programming lecture's second final task. Should contain some convenience methods
 * and fields.
 * 
 * @author Joshua Gleitze
 * @author Martin Loeper
 * @author Christian Hilden
 * @version 1.0
 */
public abstract class LangtonSubtest extends InteractiveConsoleTest {
    
    protected String[] TASK_SHEET_INPUT_FILE_1 = new String[] {
            "000",
            "000",
            "0F0"
    };
    
    protected String[] TASK_SHEET_INPUT_FILE_2 = new String[] {
            "1*0",
            "3e4",
            "12*"
    };

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.InteractiveConsoleTest#consoleMessage(java.lang.String[], java.lang.String[])
	 */
	@Override
	protected String consoleMessage(String[] commands, String[] commandLineArguments) {
		String result = "";
		String fileMessage = (commandLineArguments.length > 0) ? Input.fileMessage(commandLineArguments[0]) : "";
		result += "We ran a session on your interactive console" + fileMessage + " running the commands \n\n"
				+ joinOnePerLine(commands) + "\n\nbut got unexpected output:\n";
		return result;
	}
}
