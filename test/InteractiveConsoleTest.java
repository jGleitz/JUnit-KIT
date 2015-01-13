package test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Before;

import test.TestObject.SystemExitStatus;

/**
 * This class contains some useful methods for testing with the interactive console.
 * 
 * @author Roman Langrehr
 * @since 05.01.2015
 * @version 1.0
 *
 */
public class InteractiveConsoleTest {

  /**
   * Calls the main method on the test object and runs all commands on it. Than it checks, weather
   * the output was the expected result.
   * 
   * @param commands
   *          The commands to run on the test object. Every command must be in a single line
   * @param expectedOutput
   *          What the test object should print on the console. Use {@link System#lineSeparator()}
   *          when you expect the TestObject to start a new line.
   * @param args0
   *          The arguments for the {@code main}-method
   */
  protected void easyTest(String commands, String expectedOutput, String... args0) {
    TestObject.setNextMethodCallInput(commands);
    String[][] args = new String[][]{args0};
    TestObject.runStatic("main", args);
    String result = TestObject.getLastMethodOutput();
    assertThat(consoleMessage(commands), result, is(expectedOutput));
  }

  /**
   * Does the same as {@link #easyTest(String, String, String...)}, but it it only checks weather
   * the output begins with Error or not. <br>
   * This method does NOT allow to call System.exit(1). If you expect the TestObject to do so, call
   * {@code TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);} before.
   */
  protected void crashTest(String commands, String... args0) {
    String expectedOutputStart = "Error,";
    TestObject.setNextMethodCallInput(commands);
    String[][] args = new String[][]{args0};
    TestObject.runStatic("main", args);
    String result = TestObject.getLastMethodOutput();
    assertThat(consoleMessage(commands), result, startsWith(expectedOutputStart));
  }

  @Before
  public void allowSystemExit0() {
    TestObject.allowSystemExit(SystemExitStatus.WITH_0);
  }

  private static String consoleMessage(String commands) {
    String result = "";
    result += "We ran a session on your interactive console, running the commands \n\n" + commands
        + "\n\n but got unexpected output.";
    return result;
  }
}