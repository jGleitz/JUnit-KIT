package sheet5.b_cities;

import org.junit.Test;

import test.InteractiveConsoleTest;

/**
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
public class OptionalCitiesInteractiveConsoleTest extends InteractiveConsoleTest {

  @Test
  public void searchNoArg() {
    errorTest("insert Pfaffenhofen:1\ninsert Hintertupfingen:3\nsearch\nquit\n",
        "traverse=pre-order");
  }

  @Test
  public void insertNoArg() {
    errorTest("insert\nquit\n", "traverse=pre-order");
  }

  @Test
  public void searchNoArg2() {
    errorTest("insert Pfaffenhofen:1\ninsert Hintertupfingen:3\nsearch \nquit\n",
        "traverse=pre-order");
  }

  @Test
  public void insertNoArg2() {
    errorTest("insert \nquit\n", "traverse=pre-order");
  }

  @Test
  public void insertWrongArg() {
    errorTest("insert blablabla\nquit\n", "traverse=pre-order");
  }

  @Test
  public void insertWrongArg2() {
    errorTest("insert bla:bla:bla\nquit\n", "traverse=pre-order");
  }

  @Test
  public void insertWrongArg3() {
    errorTest("insert :\nquit\n", "traverse=pre-order");
  }

  @Test
  public void insertWrongArg4() {
    errorTest("insert bla:\nquit\n", "traverse=pre-order");
  }

  @Test
  public void insertWrongArg6() {
    errorTest("insert bla bla:5\nquit\n", "traverse=pre-order");
  }

  @Test
  public void wrongCommand() {
    errorTest("bla\nquit\n", "traverse=pre-order");
  }

  @Test
  public void wrongCommand2() {
    errorTest("bla bla\nquit\n", "traverse=pre-order");
  }
}