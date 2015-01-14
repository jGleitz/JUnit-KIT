/**
 * 
 */
package sheet4.c_waiting;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import test.ExpectedResult;
import test.ExpectedResult.TestType;
import test.TestObject;
import test.TestObject.SystemExitStatus;

/**
 * @author Joshua Gleitze
 *
 */
public class WaitingTest {

    // 2 seconds should be enough for everything. If not, there is a bug or something.
    @Rule
    public Timeout globalTimeout = new Timeout(2000000);

    private final String FIFO = "waitingarea=fifo";
    private final String LIFO = "waitingarea=lifo";
    private final String SJF = "waitingarea=sjf";
    private final String nl = System.getProperty("line.separator");

    // @formatter:off 
    private final String taskSheetExampleJobList = 
            "Task1,simple,0,5" + nl + 
            "Task2,simple,3,4" + nl + 
            "Task3,simple,5,3" + nl + 
            "Task4,simple,6,5";
    
    private final String sortingTestJobList = 
            "Task1,complex,0,2" + nl +
            "Task2,simple,1,1" + nl +
            "Task3,simple,2,1" + nl +
            "Task4,simple,3,1" + nl +
            "Task5,simple,4,1" + nl +
            "Task6,simple,5,1" + nl +
            "Task7,simple,6,1" + nl +
            "Task8,simple,7,1" + nl;
    // @formatter:on

    private String wholeFileMessage(String testFile, String actualResult, String expectedResult, String mode) {
        String result = "";
        result += "We passed the following file to your program, ";
        result += "invoking it with" + ((mode == null) ? "out a second parameter" : " " + mode) + ":" + nl + nl;
        result += testFile;
        result += nl + " and the output was: " + nl + nl;
        result += actualResult;
        result += nl + "Meahwhile, what we actually expected was:" + nl + nl;
        result += expectedResult;
        return result;
    }

    private String writeFile(String content) {
        String[] contentArray = content.split("" + nl);
        BufferedWriter outputWriter = null;
        SecureRandom random = new SecureRandom();
        String testFileName = new BigInteger(130, random).toString(32) + ".txt";
        try {
            outputWriter = new BufferedWriter(new FileWriter(testFileName));
            for (int i = 0; i < contentArray.length; i++) {
                outputWriter.write(contentArray[i]);
                outputWriter.newLine();
            }
            outputWriter.flush();
            outputWriter.close();
        } catch (IOException e) {
            fail("The test was unable to create a test file. That's a shame!");
        }
        return testFileName;
    }

    /**
     * Checks the error handling of the program for a bad second parameter. Asserts that:
     * <ul>
     * <li>The program prints an error message if the second parameter is random</li>
     * <li>The program prints an error message if the second parameter starts with 'waitingarea=' but does not continue
     * with 'fifo', 'lifo' or 'sjf'.</li>
     * <li>The program prints an error message if the second parameter starts with 'waitingarea=', continues with a
     * valid waitingarea but contains more than one "=" before that.</li>
     * </ul>
     */
    @Test
    public void testSecondParameterErrorMessages() {
        String result;
        String message;
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        String testFileName;
        testFileName = writeFile(taskSheetExampleJobList);
        TestObject.runStatic("main", (Object) new String[] {testFileName, "asdfasdfasf"});
        new File(testFileName).delete();
        result = TestObject.getLastMethodOutput();
        message = "We called your program with a valid file as first parameter, but bullshit"
                + " as second parameter. Therefore, your program should print an error message!";
        assertThat(message, result, startsWith("Error,"));
        testFileName = writeFile(taskSheetExampleJobList);
        TestObject.runStatic("main", (Object) new String[] {testFileName, "waitingarea=asdfasdfasf"});
        new File(testFileName).delete();
        result = TestObject.getLastMethodOutput();
        message = "We called your program with a valid file as first parameter and a second parameter "
                + "starting with 'waitingarea=' but ending with bullshit. Therefore, your program should"
                + " print an error message!";
        assertThat(message, result, startsWith("Error,"));
        testFileName = writeFile(taskSheetExampleJobList);
        TestObject.runStatic("main", (Object) new String[] {testFileName, "waitingarea==lifo"});
        new File(testFileName).delete();
        result = TestObject.getLastMethodOutput();
        message = "We called your program with a valid file as first parameter and the second parameter 'waitingarea==lifo'."
                + " Therefore, your program should print an error message!";
        assertThat(message, result, startsWith("Error,"));
    }

    /**
     * Checks the error handling of the program for a bad first parameter. Asserts that:
     * <ul>
     * <li>The program prints an error message if the first parameter does not lead to an actual file.</li>
     * </ul>
     */
    @Test
    public void testFirstParameterErrorMessages() {
        String result;
        String message;
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        TestObject.runStatic("main", (Object) new String[] {"I_do_no_exists.adsf"});
        result = TestObject.getLastMethodOutput();
        message = "We called your program with a path leading nowhere as a first parameter. Therefore, your program should"
                + " print an error message!";
        assertThat(message, result, startsWith("Error,"));
        TestObject.runStatic("main", (Object) new String[] {"I_do_no_exists.adsf", FIFO});
        result = TestObject.getLastMethodOutput();
        message = "We called your program with a path leading nowhere as a first parameter. Therefore, your program should"
                + " print an error message!";
        assertThat(message, result, startsWith("Error,"));
    }

    /**
     * Runs the example given on the task sheet on the tested class. It does not provide a second command line
     * parameter. Asserts that the program behaves as explained in the task sheet.
     */
    @Test
    public void testTaskSheetExample() {
        // @formatter:off
        String expectedResult =
                "0:Task1(5),Waiting:empty" + nl +
                "1:Task1(4),Waiting:empty" + nl +
                "2:Task1(3),Waiting:empty" + nl +
                "3:Task1(2),Waiting:Task2(4)" + nl +
                "4:Task1(1),Waiting:Task2(4)" + nl +
                "5:Task2(4),Waiting:Task3(3)" + nl +
                "6:Task2(3),Waiting:Task3(3),Task4(5)" + nl +
                "7:Task2(2),Waiting:Task3(3),Task4(5)" + nl +
                "8:Task2(1),Waiting:Task3(3),Task4(5)" + nl +
                "9:Task3(3),Waiting:Task4(5)" + nl +
                "10:Task3(2),Waiting:Task4(5)" + nl +
                "11:Task3(1),Waiting:Task4(5)" + nl +
                "12:Task4(5),Waiting:empty" + nl +
                "13:Task4(4),Waiting:empty" + nl +
                "14:Task4(3),Waiting:empty" + nl +
                "15:Task4(2),Waiting:empty" + nl +
                "16:Task4(1),Waiting:empty";
        // @formatter:on
        runTest(taskSheetExampleJobList, expectedResult, null);
    }

    /**
     * Runs the example given on the task sheet on the tested class. It does provide waitingarea=fifo as the second
     * parameter. Asserts that the program behaves as explained in the task sheet.
     */
    @Test
    public void testTaskSheetExampleWithFifo() {
        // @formatter:off
        String expectedResult =
                "0:Task1(5),Waiting:empty" + nl +
                "1:Task1(4),Waiting:empty" + nl +
                "2:Task1(3),Waiting:empty" + nl +
                "3:Task1(2),Waiting:Task2(4)" + nl +
                "4:Task1(1),Waiting:Task2(4)" + nl +
                "5:Task2(4),Waiting:Task3(3)" + nl +
                "6:Task2(3),Waiting:Task3(3),Task4(5)" + nl +
                "7:Task2(2),Waiting:Task3(3),Task4(5)" + nl +
                "8:Task2(1),Waiting:Task3(3),Task4(5)" + nl +
                "9:Task3(3),Waiting:Task4(5)" + nl +
                "10:Task3(2),Waiting:Task4(5)" + nl +
                "11:Task3(1),Waiting:Task4(5)" + nl +
                "12:Task4(5),Waiting:empty" + nl +
                "13:Task4(4),Waiting:empty" + nl +
                "14:Task4(3),Waiting:empty" + nl +
                "15:Task4(2),Waiting:empty" + nl +
                "16:Task4(1),Waiting:empty";
        // @formatter:on
        runTest(taskSheetExampleJobList, expectedResult, FIFO);
    }

    /**
     * Runs the example given on the task sheet on the tested class. It does provide waitingarea=lifo as the second
     * parameter. Asserts that the program behaves as expected.
     */
    @Test
    public void testTaskSheetExampleWithLifo() {
        // @formatter:off
        String expectedResult =
                "0:Task1(5),Waiting:empty" + nl +
                "1:Task1(4),Waiting:empty" + nl +
                "2:Task1(3),Waiting:empty" + nl +
                "3:Task1(2),Waiting:Task2(4)" + nl +
                "4:Task1(1),Waiting:Task2(4)" + nl +
                "5:Task3(3),Waiting:Task2(4)" + nl +
                "6:Task3(2),Waiting:Task4(5),Task2(4)" + nl +
                "7:Task3(1),Waiting:Task4(5),Task2(4)" + nl +
                "8:Task4(5),Waiting:Task2(4)" + nl +
                "9:Task4(4),Waiting:Task2(4)" + nl +
                "10:Task4(3),Waiting:Task2(4)" + nl +
                "11:Task4(2),Waiting:Task2(4)" + nl +
                "12:Task4(1),Waiting:Task2(4)" + nl +
                "13:Task2(4),Waiting:empty" + nl +
                "14:Task2(3),Waiting:empty" + nl +
                "15:Task2(2),Waiting:empty" + nl +
                "16:Task2(1),Waiting:empty";
        // @formatter:on
        runTest(taskSheetExampleJobList, expectedResult, LIFO);
    }

    /**
     * Runs the example given on the task sheet on the tested class. It does provide waitingarea=sjf as the second
     * parameter. Asserts that the program behaves as expected.
     */
    @Test
    public void testTaskSheetExampleWithSjf() {
        // @formatter:off
        String expectedResult =
                "0:Task1(5),Waiting:empty" + nl +
                "1:Task1(4),Waiting:empty" + nl +
                "2:Task1(3),Waiting:empty" + nl +
                "3:Task1(2),Waiting:Task2(4)" + nl +
                "4:Task1(1),Waiting:Task2(4)" + nl +
                "5:Task3(3),Waiting:Task2(4)" + nl +
                "6:Task3(2),Waiting:Task2(4),Task4(5)" + nl +
                "7:Task3(1),Waiting:Task2(4),Task4(5)" + nl +
                "8:Task2(4),Waiting:Task4(5)" + nl +
                "9:Task2(3),Waiting:Task4(5)" + nl +
                "10:Task2(2),Waiting:Task4(5)" + nl +
                "11:Task2(1),Waiting:Task4(5)" + nl +
                "12:Task4(5),Waiting:empty" + nl +
                "13:Task4(4),Waiting:empty" + nl +
                "14:Task4(3),Waiting:empty" + nl +
                "15:Task4(2),Waiting:empty" + nl +
                "16:Task4(1),Waiting:empty";
        // @formatter:on
        runTest(taskSheetExampleJobList, expectedResult, SJF);
    }

    @Test
    public void testSjfSorting() {
        String expectedResult = "0:Task1(4),Waiting:empty" + nl + "1:Task1(3),Waiting:Task2(1)" + nl
                + "2:Task1(2),Waiting:Task2(1),Task3(1)" + nl + "3:Task1(1),Waiting:Task2(1),Task3(1),Task4(1)" + nl
                + "4:Task2(1),Waiting:Task3(1),Task4(1),Task5(1)" + nl
                + "5:Task3(1),Waiting:Task4(1),Task5(1),Task6(1)" + nl
                + "6:Task4(1),Waiting:Task5(1),Task6(1),Task7(1)" + nl
                + "7:Task5(1),Waiting:Task6(1),Task7(1),Task8(1)" + nl + "8:Task6(1),Waiting:Task7(1),Task8(1)" + nl
                + "9:Task7(1),Waiting:Task8(1)" + nl + "10:Task8(1),Waiting:empty";
        runTest(sortingTestJobList, expectedResult, SJF);
    }

    @Test
    public void testFifoSorting() {
        String expectedResult = "0:Task1(4),Waiting:empty" + nl + "1:Task1(3),Waiting:Task2(1)" + nl
                + "2:Task1(2),Waiting:Task2(1),Task3(1)" + nl + "3:Task1(1),Waiting:Task2(1),Task3(1),Task4(1)" + nl
                + "4:Task2(1),Waiting:Task3(1),Task4(1),Task5(1)" + nl
                + "5:Task3(1),Waiting:Task4(1),Task5(1),Task6(1)" + nl
                + "6:Task4(1),Waiting:Task5(1),Task6(1),Task7(1)" + nl
                + "7:Task5(1),Waiting:Task6(1),Task7(1),Task8(1)" + nl + "8:Task6(1),Waiting:Task7(1),Task8(1)" + nl
                + "9:Task7(1),Waiting:Task8(1)" + nl + "10:Task8(1),Waiting:empty";
        runTest(sortingTestJobList, expectedResult, FIFO);
    }

    @Test
    public void testLifoSorting() {
        String expectedResult = "0:Task1(4),Waiting:empty" + nl + "1:Task1(3),Waiting:Task2(1)" + nl
                + "2:Task1(2),Waiting:Task3(1),Task2(1)" + nl + "3:Task1(1),Waiting:Task4(1),Task3(1),Task2(1)" + nl
                + "4:Task5(1),Waiting:Task4(1),Task3(1),Task2(1)" + nl
                + "5:Task6(1),Waiting:Task4(1),Task3(1),Task2(1)" + nl
                + "6:Task7(1),Waiting:Task4(1),Task3(1),Task2(1)" + nl
                + "7:Task8(1),Waiting:Task4(1),Task3(1),Task2(1)" + nl + "8:Task4(1),Waiting:Task3(1),Task2(1)" + nl
                + "9:Task3(1),Waiting:Task2(1)" + nl + "10:Task2(1),Waiting:empty";
        runTest(sortingTestJobList, expectedResult, LIFO);
    }

    private void runTest(String inputFile, String expectedResult, String mode) {
        String actualResult;
        String[] resultArray;
        Object arguments;
        ExpectedResult[] expectedResultArray = ExpectedResult.getArray(expectedResult.split("" + nl), TestType.SAME);
        String testFileName = writeFile(inputFile);
        TestObject.allowSystemExit(SystemExitStatus.WITH_0);
        if (mode == null) {
            arguments = new String[] {testFileName};
        } else {
            arguments = new String[] {testFileName, mode};
        }
        TestObject.runStatic("main", arguments);
        new File(testFileName).delete();
        actualResult = TestObject.getLastMethodOutput();
        resultArray = actualResult.split(nl);
        String wholeFileMessage = wholeFileMessage(inputFile, actualResult, expectedResult, mode);
        assertThat(wholeFileMessage + nl
                + " The number of lines of your program's output mismatched the expected ones.", resultArray.length,
                is(expectedResultArray.length));
        for (int i = 0; i < resultArray.length; i++) {
            expectedResultArray[i].assertResult(wholeFileMessage + nl + "Line " + i + " was bad:", resultArray[i]);
        }
    }

}
