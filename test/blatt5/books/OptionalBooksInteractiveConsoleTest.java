package test.blatt5.books;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import test.InteractiveConsoleTest;

/**
 * 
 * A test for wrong inputs on the Interactive Console (Task B) <br>
 * <br>
 * People that checked this test for being correct and complete:
 * <ul>
 * <li>Roman Langrehr</li>
 * </ul>
 * <br>
 * <br>
 * Things that are currently not tested, but should be:
 * <ul>
 * <li>None</li>
 * </ul>
 * 
 * @author Roman Langrehr
 * @since 13.01.2015
 * @version 1.0
 *
 */
public class OptionalBooksInteractiveConsoleTest extends InteractiveConsoleTest {

  @BeforeClass
  public static void createFiles() {
    BooksInteractiveConsoleTest.createFiles();
  }

  @AfterClass
  public static void deleteFiles() {
    BooksInteractiveConsoleTest.deleteFiles();
  }

  @Test
  public void wrongCommand() {
    crashTest("bla\nquit\n", BooksInteractiveConsoleTest.BOOK_1.getAbsolutePath(),
        "traverse=pre-order");
  }

  @Test
  public void wrongCommand2() {
    crashTest("bla bla\nquit\n", BooksInteractiveConsoleTest.BOOK_1.getAbsolutePath(),
        "traverse=pre-order");
  }
}