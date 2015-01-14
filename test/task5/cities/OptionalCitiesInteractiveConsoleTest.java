package test.task5.cities;

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
    crashTest("insert Pfaffenhofen:1\ninsert Hintertupfingen:3\nsearch\nquit\n",
        "traverse=pre-order");
  }

  @Test
  public void insertNoArg() {
    crashTest("insert\nquit\n", "traverse=pre-order");
  }

  @Test
  public void searchNoArg2() {
    crashTest("insert Pfaffenhofen:1\ninsert Hintertupfingen:3\nsearch \nquit\n",
        "traverse=pre-order");
  }

  @Test
  public void insertNoArg2() {
    crashTest("insert \nquit\n", "traverse=pre-order");
  }

  @Test
  public void insertWrongArg() {
    crashTest("insert blablabla\nquit\n", "traverse=pre-order");
  }

  @Test
  public void insertWrongArg2() {
    crashTest("insert bla:bla:bla\nquit\n", "traverse=pre-order");
  }

  @Test
  public void insertWrongArg3() {
    crashTest("insert :\nquit\n", "traverse=pre-order");
  }

  @Test
  public void insertWrongArg4() {
    crashTest("insert bla:\nquit\n", "traverse=pre-order");
  }

  @Test
  public void insertWrongArg6() {
    crashTest("insert bla bla:5\nquit\n", "traverse=pre-order");
  }

  @Test
  public void wrongCommand() {
    crashTest("bla\nquit\n", "traverse=pre-order");
  }

  @Test
  public void wrongCommand2() {
    crashTest("bla bla\nquit\n", "traverse=pre-order");
  }
}