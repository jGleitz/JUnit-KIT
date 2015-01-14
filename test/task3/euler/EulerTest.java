package test.task3.euler;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import test.TestObject;

public class EulerTest {

    /*
     * TAKE NOTE: If there is already a JUnitEulerTestFile.txt in the root of your Eclipse project, it will be deleted
     * after running this test!!!
     */
    
    @Test
    public void testMain() {
        String output;
        // define our tests
        // @formatter:off
        String[] testArray = new String[] {
            "1,2,3,4,5,6,7,8,9,1",
            "1,5,2,3,5,4,3,1",
            "1,2",
            "1,2,3,4,5,6,7,8,9",
            "1,2,3,4,5,6,5,4,3,2,1",
            "1,2,1,3,1,4,1,5,1,6,1,7,1,8,1,9",
            "1,2,1,3,1,4,2,3,2,4,3,4"
        };
        String[] expectedResultArray = new String[] {
            "true",
            "true",
            "false",
            "false",
            "false",
            "false",
            "false"
        };
        // @formatter:on
        String[] resultArray;
        String testFileName = "JUnitEulerTestFile.txt";

        // Write the test file for the class
        BufferedWriter outputWriter = null;
        try {
            outputWriter = new BufferedWriter(new FileWriter(testFileName));
            for (int i = 0; i < testArray.length; i++) {
                outputWriter.write(testArray[i]);
                outputWriter.newLine();
            }
            outputWriter.flush();
            outputWriter.close();
        } catch (IOException e) {
            fail("The test was unable to create a test file. That's a shame!");
        }

        // run the test file
        TestObject.runStatic("main", (Object) new String[]{testFileName});
        output = TestObject.getLastMethodOutput();

        // split lines into array
        resultArray = output.split("\n");

        // Test the output
        if (resultArray.length > testArray.length) {
            fail("Your program wrote more output lines than there were lines in the input file.");
        } else if (resultArray.length < testArray.length) {
            fail("Your program wrote less output lines than there were lines in the input file.");
        }

        for (int i = 0; i < resultArray.length; i++) {
            assertEquals("Your program's output for the line '" + testArray[i] + "'", expectedResultArray[i],
                    resultArray[i]);
        }
        
        // Remove the test file
        new File(testFileName).delete();
    }
}
