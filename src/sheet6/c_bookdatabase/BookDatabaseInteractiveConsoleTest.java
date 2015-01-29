package sheet6.c_bookdatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import test.InteractiveConsoleTest;
import test.TestObject;
import test.TestObject.SystemExitStatus;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

import org.hamcrest.Matcher;

/**
 * A test for the Interactive Console (Task C) <br>
 * <br>
 * People that checked this test for being correct and complete:
 * <ul>
 * <li>Martin Löper</li>
 * </ul>
 * <br>
 * <br>
 * Things that are currently not tested, but should be:
 * <ul>
 * <li>Several search queries: whitespace-ignore, 
 *                             case-sensitivity ignore (especially for AND/OR), 
 *                             nested searchqueries (depth: >5),
 *                             malformed searchqueries (e.g. AD(title=a,creator=b))</li>
 * </ul>
 * 
 * @author Martin Löper
 * @since 29.01.2015
 * @version 1.0
 *
 */
public class BookDatabaseInteractiveConsoleTest extends InteractiveConsoleTest {
    protected static final String BASE_FILE_PATH = "";
    protected static final File DATASET_PUBLIC_TEST = new File(BASE_FILE_PATH + "dataset1.txt");
    protected static final File DATASET_EMPTY = new File(BASE_FILE_PATH + "dataset_empty.txt");
    protected static final File DATASET_INVALID_INPUT = new File(BASE_FILE_PATH + "invalid_input.txt");  
    protected static final File DATASET_INVALID_INPUT2 = new File(BASE_FILE_PATH + "invalid_input2.txt");  
    protected static final File DATASET_INVALID_INPUT3 = new File(BASE_FILE_PATH + "invalid_input3.txt");
    protected static final File DATASET_INVALID_INPUT4 = new File(BASE_FILE_PATH + "invalid_input4.txt");
    protected static final File DATASET_INVALID_INPUT5 = new File(BASE_FILE_PATH + "invalid_input5.txt");
    protected static final File DATASET_INVALID_INPUT6 = new File(BASE_FILE_PATH + "invalid_input6.txt");
    protected static final File DATASET_INVALID_INPUT7 = new File(BASE_FILE_PATH + "invalid_input7.txt");
    protected static final File DATASET_INVALID_INPUT8 = new File(BASE_FILE_PATH + "invalid_input8.txt");
    
    @BeforeClass
    public static void createFiles() {
        try {
            PrintStream dataset1 = new PrintStream(DATASET_PUBLIC_TEST);
            dataset1.println("creator=galileocomputing,title=java_ist_auch_eine_insel");
            dataset1.println("title=grundkursprogrammieren_in_java,year=2007");
            dataset1.println("creator=ralf_reussner,year=2006");
            dataset1.close();
            
            PrintStream dataset2 = new PrintStream(DATASET_INVALID_INPUT);
            dataset2.println("invalidkeyword=java");
            dataset2.close();
            
            PrintStream dataset3 = new PrintStream(DATASET_INVALID_INPUT2);
            dataset3.println("title=java,creator=reussner,title=java,year=2005");
            dataset3.close();
            
            PrintStream dataset4 = new PrintStream(DATASET_INVALID_INPUT3);
            dataset4.println("title=java,creator=reussner,year=2016");
            dataset4.close();
            
            PrintStream dataset5 = new PrintStream(DATASET_INVALID_INPUT4);
            dataset5.println("title=java,creator=reussner,year=-1");
            dataset5.close();
            
            PrintStream dataset6 = new PrintStream(DATASET_INVALID_INPUT5);
            dataset6.println("title=java,creator=reussner,year=2014.2");
            dataset6.close();
            
            PrintStream dataset7 = new PrintStream(DATASET_INVALID_INPUT6);
            dataset7.println("title=java,creator=reussner,year=2014");  // valid
            dataset7.println(); // invalid, because every line consists of a book entry and for a book at least one attribute must be set
            dataset7.println("title=java,creator=galileocomputing,year=2004");
            dataset7.close();
            
            PrintStream dataset8 = new PrintStream(DATASET_INVALID_INPUT7);
            dataset8.println("title=java,creator=reussner,year=2014,titel=test");   // titel instead of title (typo)
            dataset8.println("creator=reussner,year=2014,titel=test");              // without title attribute set
            dataset8.println("creator=reussner,year=2014,tl=test");                 // maybe a regex will fail here
            dataset8.close();
            
            PrintStream dataset9 = new PrintStream(DATASET_INVALID_INPUT8);
            dataset9.println("title=java$_ist_auch_eine_insel,creator=galileocomputing,year=2014");
            dataset9.close();
            
            DATASET_EMPTY.createNewFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * 
     * 
     * Tests concerning file input parsing. 
     * 
     * 
     */
    
    /**
     * Provides an empty input file.
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void parserEmptyInput() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit", "0.5", DATASET_EMPTY.getAbsolutePath());
    }
    
    /**
     * Provides an invalid input file. Cause: invalid keyword
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void parserTestInvalidInput1() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit", "0.5", DATASET_INVALID_INPUT.getAbsolutePath());
    }
    
    /**
     * Provides an invalid input file. Cause: keyword occurrence multiple times
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void parserTestInvalidInput2() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit", "0.5", DATASET_INVALID_INPUT2.getAbsolutePath());
    }
    
    /**
     * Provides an invalid input file. Cause: year out of boundary [0,2015] --> 2016
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void parserTestInvalidInput3() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit", "0.5", DATASET_INVALID_INPUT3.getAbsolutePath());
    }
    
    /**
     * Provides an invalid input file. Cause: year out of boundary [0,2015] --> -1
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void parserTestInvalidInput4() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit", "0.5", DATASET_INVALID_INPUT4.getAbsolutePath());
    }
    
    /**
     * Provides an invalid input file. Cause: year not an integer --> float
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void parserTestInvalidInput5() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit", "0.5", DATASET_INVALID_INPUT5.getAbsolutePath());
    }
    
    /**
     * Provides an invalid input file. Cause: blank line (without attributes/finals)
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void parserTestInvalidInput6() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit", "0.5", DATASET_INVALID_INPUT6.getAbsolutePath());
    }
    
    /**
     * Provides an invalid input file. Cause: invalid keywords which are similar to valid ones
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void parserTestInvalidInput7() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit", "0.5", DATASET_INVALID_INPUT7.getAbsolutePath());
    }
    
    /**
     * Provides an invalid input file. Cause: invalid value character in title attribute
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void parserTestInvalidInput8() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit", "0.5", DATASET_INVALID_INPUT8.getAbsolutePath());
    }
    
    /*
     * 
     * 
     * Tests concerning command-line input parsing.
     * 
     * 
     */
    
    /**
     * Starts program without cli parameter.
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void cliNoArgs() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit");
    }
    
    /**
     * Starts program with only one (valid) cli parameter: the path
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void cliOneValidArg2() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit", "", DATASET_PUBLIC_TEST.getAbsolutePath());
    }
    
    /**
     * Starts program with only one (valid) cli parameter: the tolerance limit
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void cliOneValidArg() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit", "0.5");
    }
    
    /**
     * Starts program with one (valid) cli parameter: the tolerance limit and an invalid (not existing) path.
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void cliInvalidPathArg() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit", "", "0.5", "C:\filethatdoesnotexist.txt");
    }
    
    /**
     * Starts program with two cli parameter. The second one is valid, but the first one is not a float.
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void cliInvalidFirstParam() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit", "", "0.5fd", DATASET_PUBLIC_TEST.getAbsolutePath());
    }
    
    /**
     * Starts program with two cli parameter. The second one is valid, but the first one does not match the boundaries [0,1].
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}>0
     * <li>an error message is printed
     * </ul>
     */
    @Test
    public void cliInvalidFirstParam2() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        errorTest("quit", "", "1.1", DATASET_PUBLIC_TEST.getAbsolutePath());
        errorTest("quit", "", "-0.1", DATASET_PUBLIC_TEST.getAbsolutePath());
    }
    
    /**
     * Starts program with two cli parameter. Both are valid, but it might not be as obvious.
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}=0
     * <li>no error message is printed
     * </ul>
     */
    @Test
    public void cliValidFirstParam() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_0);
        oneLineTest("quit", "", "0.5f", DATASET_PUBLIC_TEST.getAbsolutePath());
        oneLineTest("quit", "", "0.5d", DATASET_PUBLIC_TEST.getAbsolutePath()); // i don't know if double is mandatory
    }
    
    /**
     * Starts program with two cli parameter. Both are valid. this does test the boundaries [0,1].
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}=0
     * <li>no error message is printed
     * </ul>
     */
    @Test
    public void cliValidFirstParam2() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_0);
        oneLineTest("quit", "", "0", DATASET_PUBLIC_TEST.getAbsolutePath());
        oneLineTest("quit", "", "1", DATASET_PUBLIC_TEST.getAbsolutePath());
    }
    
    /**
     * Starts program with valid cli parameters. Tests the quit command.
     * Asserts that:
     * <ul>
     * <li>the program terminates
     * <li>if {@code System.exit(x)} is called, {@code x}=0
     * <li>no error message is printed
     * </ul>
     */
    @Test
    public void cliValidArgAndQuit() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_0);
        oneLineTest("quit", "", "0.5", DATASET_PUBLIC_TEST.getAbsolutePath());
    }
    
    /*
     * 
     * 
     * Full Test Scenarios
     * 
     * 
     */
    
    
    /**
     * Simulates the Praktomat's public test.
     * Asserts that:
     * <ul>
     * <li>the program outputs the expected search responses.
     * <li>an error message is printed (key isbn not allowed)
     * </ul>
     */
    @Test
    public void publicTest() {  
        // the following is not mandatory, I think, according to: 
        // https://ilias.studium.kit.edu/ilias.php?ref_id=366572&cmdClass=ilobjforumgui&thr_pk=51304&cmd=viewThread&cmdNode=ed:5v&baseClass=ilRepositoryGUI 
        //TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
        Vector<Matcher<String>> expectedResults = new Vector<Matcher<String>>();
        expectedResults.add(is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"));
        expectedResults.add(is("creator=unknown,title=grundkursprogrammieren_in_java,year=2007,false"));
        expectedResults.add(is("creator=ralf_reussner,title=unknown,year=2006,true"));
        
        expectedResults.add(is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"));
        expectedResults.add(is("creator=unknown,title=grundkursprogrammieren_in_java,year=2007,true"));
        expectedResults.add(is("creator=ralf_reussner,title=unknown,year=2006,true"));
        
        expectedResults.add(is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"));
        expectedResults.add(is("creator=unknown,title=grundkursprogrammieren_in_java,year=2007,false"));
        expectedResults.add(is("creator=ralf_reussner,title=unknown,year=2006,true"));
        
        expectedResults.add(is("creator=galileocomputing,title=java_ist_auch_eine_insel,year=unknown,false"));
        expectedResults.add(is("creator=unknown,title=grundkursprogrammieren_in_java,year=2007,true"));
        expectedResults.add(is("creator=ralf_reussner,title=unknown,year=2006,true"));
        
        expectedResults.add(startsWith("Error,"));
        
        multiLineTest(new String[] { "search creator=ralf_reussner", "search year=2006", "search AND(creator=ralf_reussner,year=2006)", "search OR(creator=ralf_reussner,year=2006)", "search isbn=12345", "quit" }, 
                expectedResults, "0.5", DATASET_PUBLIC_TEST.getAbsolutePath());
    }
    
    @AfterClass
    public static void deleteFiles() {
        DATASET_PUBLIC_TEST.deleteOnExit();
        DATASET_EMPTY.deleteOnExit();
        DATASET_INVALID_INPUT.deleteOnExit();
        DATASET_INVALID_INPUT2.deleteOnExit();
        DATASET_INVALID_INPUT3.deleteOnExit();
        DATASET_INVALID_INPUT4.deleteOnExit();
        DATASET_INVALID_INPUT5.deleteOnExit();
        DATASET_INVALID_INPUT6.deleteOnExit();
        DATASET_INVALID_INPUT7.deleteOnExit();
        DATASET_INVALID_INPUT8.deleteOnExit();
    }
}