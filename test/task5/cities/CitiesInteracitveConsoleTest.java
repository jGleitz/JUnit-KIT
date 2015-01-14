package test.task5.cities;

import org.junit.Test;

import test.InteractiveConsoleTest;
import test.TestObject;
import test.TestObject.SystemExitStatus;

/**
 * A test for the Interactive Console (Task B) <br>
 * <br>
 * People that checked this test for being correct and complete:
 * <ul>
 * <li>Roman Langrehr</li>
 * <li>Joshua Gleitze</li>
 * </ul>
 * <br>
 * <br>
 * Things that are currently not tested, but should be:
 * <ul>
 * <li>None</li>
 * </ul>
 * 
 * @author Roman Langrehr
 * @author Joshua Gleitze
 * @since 05.01.2015
 * @version 1.0
 *
 */
public class CitiesInteracitveConsoleTest extends InteractiveConsoleTest {

  /**
   * Tests the {@code quit} command of the interactive console. Asserts that:
   * <ul>
   * <li>the program can be terminated by running the command {@code quit}</li>
   * <li>(Implicatory) the program is not terminated by System.exit(x) with x>0</li>
   * <li>the program does not output anything when being terminated through {@code quit}</li>
   * </ul>
   */
  @Test
  public void testConsoleQuit() {
    oneLineTest("quit\n", "", "traverse=pre-order");
  }

  @Test
  public void testNoArgs() {
    oneLineTest("quit\n", "");
  }

  @Test
  public void testWrongArgs() {
    TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
    crashTest("quit\n", "blablabla");
  }

  @Test
  public void testWrongArgs2() {
    TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
    crashTest("quit\n", "traverse=blablabla");
  }

  @Test
  public void testWrongArgs3() {
    TestObject.allowSystemExit(SystemExitStatus.WITH_GREATER_THAN_0);
    crashTest("quit\n", "traverse=pre-order=blabla");
  }

  /**
   * Warning: Does not really check if the insert was correctly. Only detects exceptions while
   * inserting. To be sure insert works correctly, the insertSearch test should be successful.
   */
  @Test
  public void simpleInsertTest() {
    oneLineTest("insert Hintertupfingen:3\nquit\n", "", "traverse=pre-order");
  }

  /**
   * Warning: Does not really check if the insert was correctly. Only detects exceptions while
   * inserting. To be sure insert works correctly, the insertSearch test should be successful.
   */
  @Test
  public void simpleInsertTest2() {
    oneLineTest("insert Hintertupfingen:3\ninsert Hamburg:5\nquit\n", "", "traverse=pre-order");
  }

  /**
   * Warning: Does not really check if the insert was correctly. Only detects exceptions while
   * inserting. To be sure insert works correctly, the insertSearch test should be successful.
   */
  @Test
  public void simpleInsertTest3() {
    oneLineTest("insert Hintertupfingen:3\ninsert Hamburg:5\ninsert M�nchen:6\nquit\n", "",
        "traverse=pre-order");
  }

  @Test
  public void insertSearchTest() {
    oneLineTest("insert Hintertupfingen:3\nsearch Hintertupfingen\nquit\n", "Hintertupfingen:3"
        + System.lineSeparator(), "traverse=pre-order");
  }

  @Test
  public void insertSearchTest2() {
    oneLineTest("insert Pfaffenhofen:1\ninsert Hintertupfingen:3\nsearch Hintertupfingen\nquit\n",
        "Hintertupfingen:3" + System.lineSeparator(), "traverse=pre-order");
  }

  @Test
  public void insertSearchTest3() {
    oneLineTest("insert Pfaffenhofen:1\ninsert Hintertupfingen:3\nsearch Pfaffenhofen\nquit\n",
        "Pfaffenhofen:1" + System.lineSeparator(), "traverse=pre-order");
  }

  @Test
  public void insertSearchTest4() {
    oneLineTest("insert Pfaffenhofen:1\ninsert Waldstadt:3\nsearch Waldstadt\nquit\n", "Waldstadt:3"
        + System.lineSeparator(), "traverse=pre-order");
  }

  @Test
  public void searchNull() {
    oneLineTest("insert Pfaffenhofen:1\ninsert Waldstadt:3\nsearch Freudenstadt\nquit\n",
        "Freudenstadt:null" + System.lineSeparator(), "traverse=pre-order");
  }

  @Test
  public void searchOnEmptyTree() {
    oneLineTest("search Freudenstadt\nquit\n", "Freudenstadt:null" + System.lineSeparator(),
        "traverse=pre-order");
  }

  @Test
  public void emptyPreOrderTest() {
    oneLineTest("info\nquit\n", System.lineSeparator(), "traverse=pre-order");
  }

  @Test
  public void emptyLevelOrderTest() {
    oneLineTest("info\nquit\n", System.lineSeparator(), "traverse=level-order");
  }

  @Test
  public void emptyInOrderTest() {
    oneLineTest("info\nquit\n", System.lineSeparator(), "traverse=in-order");
  }

  @Test
  public void preOrderTest() {
    oneLineTest("insert Hamburg:10\ninfo\nquit\n", "Hamburg:10" + System.lineSeparator(),
        "traverse=pre-order");
  }

  @Test
  public void preOrderTest2() {
    oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninfo\nquit\n", "Hamburg:10,Muenchen:30"
        + System.lineSeparator(),
        "traverse=pre-order");
  }

  @Test
  public void preOrderTest3() {
    oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninfo\nquit\n",
        "Hamburg:10,Aachen:9,Muenchen:30" + System.lineSeparator(), "traverse=pre-order");
  }

  @Test
  public void preOrderTest4() {
    oneLineTest(
        "insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninsert Zuerich:3\ninfo\nquit\n",
        "Hamburg:10,Aachen:9,Muenchen:30,Zuerich:3" + System.lineSeparator(), "traverse=pre-order");
  }

  @Test
  public void inOrderTest() {
    oneLineTest("insert Hamburg:10\ninfo\nquit\n", "Hamburg:10" + System.lineSeparator(),
        "traverse=in-order");
  }

  @Test
  public void inOrderTest2() {
    oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninfo\nquit\n", "Hamburg:10,Muenchen:30"
        + System.lineSeparator(), "traverse=in-order");
  }

  @Test
  public void inOrderTest3() {
    oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninfo\nquit\n",
        "Aachen:9,Hamburg:10,Muenchen:30" + System.lineSeparator(), "traverse=in-order");
  }

  @Test
  public void inOrderTest4() {
    oneLineTest(
        "insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninsert Zuerich:3\ninfo\nquit\n",
        "Aachen:9,Hamburg:10,Muenchen:30,Zuerich:3" + System.lineSeparator(), "traverse=in-order");
  }
  
  @Test
  public void inOrderTestWithoutParameter() {
    oneLineTest("insert Hamburg:10\ninfo\nquit\n", "Hamburg:10" + System.lineSeparator());
  }

  @Test
  public void inOrderTestWithoutParameter2() {
    oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninfo\nquit\n", "Hamburg:10,Muenchen:30"
        + System.lineSeparator());
  }

  @Test
  public void inOrderTestWithoutParameter3() {
    oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninfo\nquit\n",
        "Aachen:9,Hamburg:10,Muenchen:30" + System.lineSeparator());
  }

  @Test
  public void inOrderTestWithoutParameter4() {
    oneLineTest(
        "insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninsert Zuerich:3\ninfo\nquit\n",
        "Aachen:9,Hamburg:10,Muenchen:30,Zuerich:3" + System.lineSeparator());
  }

  @Test
  public void levelOrderTest() {
    oneLineTest("insert Hamburg:10\ninfo\nquit\n", "Hamburg:10" + System.lineSeparator(),
        "traverse=level-order");
  }

  @Test
  public void levelOrderTest2() {
    oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninfo\nquit\n", "Hamburg:10,Muenchen:30"
        + System.lineSeparator(), "traverse=level-order");
  }

  @Test
  public void levelOrderTest3() {
    oneLineTest("insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninfo\nquit\n",
        "Hamburg:10,Aachen:9,Muenchen:30" + System.lineSeparator(), "traverse=level-order");
  }

  @Test
  public void levelOrderTest4() {
    oneLineTest(
        "insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninsert Zuerich:3\ninfo\nquit\n",
        "Hamburg:10,Aachen:9,Muenchen:30,Zuerich:3" + System.lineSeparator(),
        "traverse=level-order");
  }

  @Test
  public void levelOrderTest5() {
    oneLineTest(
        "insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninsert Zuerich:3\ninsert Aaachen:3\ninfo\nquit\n",
        "Hamburg:10,Aachen:9,Muenchen:30,Aaachen:3,Zuerich:3" + System.lineSeparator(),
        "traverse=level-order");
  }

  @Test
  public void preOrderTest5() {
    oneLineTest(
        "insert Hamburg:10\ninsert Muenchen:30\ninsert Aachen:9\ninsert Zuerich:3\ninsert Aaachen:3\ninfo\nquit\n",
        "Hamburg:10,Aachen:9,Aaachen:3,Muenchen:30,Zuerich:3" + System.lineSeparator(),
        "traverse=pre-order");
  }
}