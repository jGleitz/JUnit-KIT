package sheet6.a_books;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import test.InteractiveConsoleTest;
import test.TestObject;
import test.TestObject.SystemExitStatus;

/**
 * A test for the Interactive Console (Task A) <br>
 * This test is based on the test for Sheet 5 Task C. The test is still under development.
 * 
 * !!! The info command is not tested at all at the moment !!!
 * 
 * @author Roman Langrehr
 * @since 30.01.2015
 * @version 0.1
 *
 */
public class BookSearchTest extends InteractiveConsoleTest {
  /*
   * PREDEFINED BOOK INPUT FILES
   */

  protected static final String BASE_FILE_PATH = "";
  protected static final File BOOK_1 = new File(BASE_FILE_PATH + "book1.txt");
  protected static final File BOOK_2 = new File(BASE_FILE_PATH + "book2.txt");
  protected static final File EMPTY_BOOK = new File(BASE_FILE_PATH + "empty_book.txt");
  protected static final File BOOK_4 = new File(BASE_FILE_PATH + "book4.txt");
  protected static final File BOOK_5 = new File(BASE_FILE_PATH + "book5.txt");
  protected static final File BOOK_6_1 = new File(BASE_FILE_PATH + "book6_1.txt");
  protected static final File BOOK_6_2 = new File(BASE_FILE_PATH + "book6_2.txt");
  protected static final File BOOK_7 = new File(BASE_FILE_PATH + "book7.txt");

  @BeforeClass
  public static void createFiles() {
    try {
      PrintStream book1stream = new PrintStream(BOOK_1);
      book1stream.println("Seite1");
      book1stream.println("Lorem ipsum dolor sit amet consetetur sadipscing");
      book1stream.println("Lorem ipsum dolor sit amet consetetur sadipscing");
      book1stream.println("test1");
      book1stream.println("Seite2");
      book1stream.println("Lorem test2 amet");
      book1stream.close();
      PrintStream book2stream = new PrintStream(BOOK_2);
      book2stream.println("Seite1");
      book2stream.close();
      PrintStream book4stream = new PrintStream(BOOK_4);
      book4stream.println("Seite1");
      book4stream.println("Seite2");
      book4stream.println("Lorem test2 amet");
      book4stream.close();
      PrintStream book5stream = new PrintStream(BOOK_5);
      book5stream.println();
      book5stream.println("Seite1");
      book5stream.println();
      book5stream.close();
      book5stream.close();
      PrintStream book61stream = new PrintStream(BOOK_6_1);
      book61stream.println("Seite1");
      book61stream.println("word1");
      book61stream.close();
      PrintStream book62stream = new PrintStream(BOOK_6_2);
      book62stream.println("Seite1");
      book62stream.println("word1");
      book62stream.println("Seite2");
      book62stream.println("word1");
      book62stream.close();
      PrintStream book7stream = new PrintStream(BOOK_7);
      book7stream.println("Seite1");
      book7stream.println("b c a");
      book7stream.println("Seite2");
      book7stream.println("b d");
      book7stream.println("Seite3");
      book7stream.println("aa ca a e");
      book7stream.close();
      EMPTY_BOOK.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @AfterClass
  public static void deleteFiles() {
    BOOK_1.delete();
    BOOK_2.delete();
    EMPTY_BOOK.delete();
    BOOK_4.delete();
    BOOK_5.delete();
    BOOK_6_1.delete();
    BOOK_6_2.delete();
    BOOK_7.delete();
  }

  /*
   * ERROR TESTS
   */
  /**
   * Fails the test as this test is incomplete an therefore does not grant anything. Remove this
   * method as soon as the test reaches a certain degree of relevance.
   */
  @Test
  public void incomplete() {
    fail("\n\nThis test is under development and incomplete!\nEspecially the info command is not tested at all the moment!\n\n");
  }

  /**
   * Tests for an error message, when the user types an illegal command.
   */
  @Test
  public void wrongCommand() {
    errorTest("bla\nquit\n", BOOK_1.getAbsolutePath());
  }

  /**
   * Tests for an error message, when the user executes search without a keyword.
   */
  @Test
  public void searchNoArg() {
    errorTest("search\nquit\n", BOOK_1.getAbsolutePath());
  }

  /**
   * Tests for an error message, when the user executes search without two keyword.
   */
  @Test
  public void searchTwoArgs() {
    errorTest("search word1 word2\nquit\n", BOOK_1.getAbsolutePath());
  }

  /**
   * Launches the program with an not-existing file path as argument
   */
  @Test
  public void testFileDoesNotExist() {
    TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
    errorTest("quit\n", "C:/DieseDateiHatHoffentlichNiemand.txt");
  }

  /**
   * Launches the program without the required file-path attribute.
   */
  @Test
  public void testNoArgs() {
    TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
    errorTest("quit\n");
  }

  /**
   * Launches the program with two arguments (but exactly one is required).
   */
  @Test
  public void testTooManyArgs() {
    TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
    errorTest("quit\n", BOOK_1.getAbsolutePath(), BOOK_1.getAbsolutePath());
  }

  /**
   * Searches for a keyword which appearers on one page.
   */
  @Test
  public void search1() {
    oneLineTest("search ipsum\nquit\n", "ipsum:1", BOOK_1.getAbsolutePath());
  }

  /**
   * Searches for a keyword which appearers on two page.
   */
  @Test
  public void search2() {
    oneLineTest("search Lorem\nquit\n", "Lorem:1,2", BOOK_1.getAbsolutePath());
  }

  /**
   * Searches for a keyword which doesn't appear on any page.
   */
  @Test
  public void searchNull() {
    oneLineTest("search muellll\nquit\n", "muellll:null", BOOK_1.getAbsolutePath());
  }

  /**
   * Launches the program with a valid book with an empty page.
   */
  @Test
  public void emptyPage() {
    oneLineTest("quit\n", "", BOOK_4.getAbsolutePath());
  }

  /**
   * Launches the program with a valid empty file (no pages)
   */
  @Test
  public void emptyFile() {
    oneLineTest("quit\n", "", EMPTY_BOOK.getAbsolutePath());
  }

  /**
   * Launches the program with a valid book file containing empty pages.
   */
  @Test
  public void emptyLine() {
    oneLineTest("quit\n", "", BOOK_5.getAbsolutePath());
  }

  /**
   * Executes a search command on an empty book.
   */
  @Test
  public void searchOnEmptyBook() {
    oneLineTest("search hiergiebtsnichtszufinden\nquit\n", "hiergiebtsnichtszufinden:null",
        EMPTY_BOOK.getAbsolutePath());
  }

  /**
   * Executes a search command on an book containing empty files.
   */
  @Test
  public void searchOnEmptyLine() {
    oneLineTest("search amet\nquit\n", "amet:2", BOOK_4.getAbsolutePath());
  }

  /**
   * Calls insert with an argument.
   */
  @Test
  public void testInfoWithArg() {
    errorTest("info ipsum\nquit\n", BOOK_1.getAbsolutePath());
  }
}