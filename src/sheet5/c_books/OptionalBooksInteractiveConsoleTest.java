package sheet5.c_books;

import org.junit.Test;

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
public class OptionalBooksInteractiveConsoleTest extends BooksInteractiveConsoleTest {

	@Test
	public void wrongCommand() {
		errorTest("bla\nquit\n", BOOK_1.getAbsolutePath(), "traverse=pre-order");
	}

	@Test
	public void wrongCommand2() {
		errorTest("bla bla\nquit\n", BOOK_1.getAbsolutePath(), "traverse=pre-order");
	}
}