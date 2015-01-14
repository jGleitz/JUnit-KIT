package test.task4.linkedTuple;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import test.TestObject;
import test.TestObject.SystemExitStatus;

/**
 * A test for the LinkedNaturalNumberTuple (Task A) <br>
 * <br>
 * People that checked this test for being correct and complete:
 * <ul>
 * <li>Joshua Gleitze</li>
 * </ul>
 * <br>
 * <br>
 * Things that are currently not tested, but should be:
 * <ul>
 * <li>The interactive console</li>
 * </ul>
 * 
 * @author Joshua Gleitzeua Gleitze
 */

public class LinkedNaturalNumberTupleTest {

    private static final int OUTPUT_LINE_WIDTH = 50;

    @Before
    public void allowSystemExit0() {
        TestObject.allowSystemExit(SystemExitStatus.WITH_0);
    }

    private static String NumbersToString(int[] numbers) {
        if (numbers == null) {
            return "null";
        }
        String result = "{";
        for (int i = 0; i < numbers.length; i++) {
            result += numbers[i];
            result += (i < numbers.length - 1) ? ", " : "}";
        }
        return result;
    }

    private static String standardMessage(String functionName, int[] testnumbers) {
        String result = "";
        result += functionName;
        result += " of a LinkedNaturalNumberTuple instantiated with ";
        result += NumbersToString(testnumbers);
        return (result + "\n").replaceAll("(.{1," + OUTPUT_LINE_WIDTH + "})\\s+", "$1\n");
    }

    private final int[] testnumbers = {0, 2, 2, 5, 1, -1, 2};
    private final int[] testnumbersShuffle = {0, 2, 5, 2, 1, -1, 2};
    private final int[] testnumbers2 = {0, 490, -50, -50, 5, 12, -1, 22, 4, 490, 490, 480, 11, -12, 0, 0, 0, 3};

    /**
     * Tests the {@code countNumbers(x)} method. Asserts that:
     * <ul>
     * <li>{@code countNumbers(x)} counts the occurrence of numbers in the tuple</li>
     * <li>{@code countNumbers(x)} returns {@code 0} for a {@code x} that is not in the tuple</li>
     * <li>{@code countNumbers(x)} returns {@code 0} if {@code x} is lower than 1</li>
     * <li>{@code countNumbers(x)} is consistent over two calls</li>
     * </ul>
     */
    @Test
    public void testCountNumbers() {
        Object result;
        Object result2;
        TestObject testObject;

        // test count with testnumbers
        testObject = new TestObject(testnumbers);
        result = testObject.run("countNumbers", 1);
        assertEquals(standardMessage("countNumbers(1)", testnumbers), 1, result);
        // assert that count is consistent
        testObject = new TestObject(testnumbers);
        result = testObject.run("countNumbers", 2);
        result2 = testObject.run("countNumbers", 2);
        assertEquals(standardMessage("countNumbers(2).equals(countNumbers(2))", testnumbers), true,
                result.equals(result2));
        // test for a nonexistent number
        testObject = new TestObject(testnumbers);
        result = testObject.run("countNumbers", 123);
        assertEquals(standardMessage("countNumbers(123)", testnumbers), 0, result);
        testObject = new TestObject(testnumbers);
        result = testObject.run("countNumbers", 0);
        assertEquals(standardMessage("countNumbers(0)", testnumbers), 0, result);

        // same for testnumbers2
        testObject = new TestObject(testnumbers2);
        result = testObject.run("countNumbers", 490);
        assertEquals(standardMessage("countNumbers(490)", testnumbers2), 3, result);
        result = testObject.run("countNumbers", 5);
        result2 = testObject.run("countNumbers", 5);
        assertEquals(standardMessage("countNumbers(5.equals(countNumbers(5))", testnumbers), true,
                result.equals(result2));
        testObject = new TestObject(testnumbers2);
        result = testObject.run("countNumbers", 123);
        assertEquals(standardMessage("countNumbers(123)", testnumbers2), 0, result);
        testObject = new TestObject(testnumbers2);
        result = testObject.run("countNumbers", -20);
        assertEquals(standardMessage("countNumbers(-20)", testnumbers2), 0, result);
    }

    /**
     * Tests the {@code equals(x)} method. Asserts that:
     * <ul>
     * <li>{@code equals(x)} returns {@code true} if {@code x} is a {@code LinkedNaturalNumberTuple} that was
     * instantiated with the same array as {@code this}.</li>
     * <li>{@code equals(x)} returns {@code false} if {@code x} is a {@code LinkedNaturalNumberTuple} that has different
     * numbers in it than {@code this}.</li>
     * <li>{@code equals(x)} returns {@code false} if {@code x} is a {@code LinkedNaturalNumberTuple} that has the same
     * numbers in a different order as {@code this} in it.</li>
     * <li>{@code equals(new Object())} returns {@code false}</li>
     * <li>{@code equals(this)} returns {@code true}</li>
     * <li>{@code equals(x)} is consistent over two calls</li>
     * </ul>
     */
    @Test
    public void testEquals() {
        Object result;
        Object result2;
        TestObject testObject;

        // check testnumbers with testnumbers 2
        testObject = new TestObject(testnumbers);
        result = testObject.run("equals", new TestObject(testnumbers), Object.class);
        assertEquals(
                standardMessage("equals(new NaturalNumberTuple(new int[]" + NumbersToString(testnumbers) + ")",
                        testnumbers), true, result);
        // check testnumbers with testnumbers
        testObject = new TestObject(testnumbers);
        result = testObject.run("equals", new TestObject(testnumbers2), Object.class);
        assertEquals(
                standardMessage("equals(new NaturalNumberTuple(new int[]" + NumbersToString(testnumbers2) + ")",
                        testnumbers), false, result);

        // check with another order
        testObject = new TestObject(testnumbers);
        result = testObject.run("equals", new TestObject(testnumbersShuffle), Object.class);
        assertEquals(
                standardMessage("equals(new NaturalNumberTuple(new int[]" + NumbersToString(testnumbersShuffle) + ")",
                        testnumbers), false, result);

        // check testnumbers2 with testnumbers
        testObject = new TestObject(testnumbers2);
        result = testObject.run("equals", new TestObject(testnumbers), Object.class);
        assertEquals(
                standardMessage("equals(new NaturalNumberTuple(new int[]" + NumbersToString(testnumbers) + ")",
                        testnumbers2), false, result);
        // check testnumbers2 with testnumbers2
        testObject = new TestObject(testnumbers2);
        result = testObject.run("equals", new TestObject(testnumbers2), Object.class);
        assertEquals(
                standardMessage("equals(new NaturalNumberTuple(new int[]" + NumbersToString(testnumbers2) + ")",
                        testnumbers2), true, result);

        // check testnumbers2 with new Object()
        testObject = new TestObject(testnumbers2);
        result = testObject.run("equals", new Object());
        assertEquals(standardMessage("equals(new Object())", testnumbers2), false, result);

        // check with this
        testObject = new TestObject(testnumbers);
        result = testObject.run("equals", testObject, Object.class);
        assertEquals(standardMessage("equals(this)", testnumbers), true, result);

        // check constancy
        TestObject testObject2 = new TestObject(testnumbers2);
        testObject = new TestObject(testnumbers2);
        result = testObject.run("equals", testObject2, Object.class);
        result2 = testObject.run("equals", testObject2, Object.class);
        assertEquals(standardMessage("equals(tuple).equals(equals(tuple))", testnumbers), true, result.equals(result2));
    }

    /**
     * Tests the {@code indexOf(x)} method. Asserts that:
     * <ul>
     * <li>{@code indexOf(x)} returns the first occurrence of {@code x}, where 0 is the first element in the tuple.</li>
     * <li>{@code indexOf(x)} returns {@code -1},if {@code x} is not in the tuple.</li>
     * <li>{@code indexOf(x)} returns {@code -1} if {@code x} is smaller than 1.</li>
     * <li>{@code indexOf(x)} is consitent over two calls</li>
     * </ul>
     */
    @Test
    public void testIndexOf() {
        Object result;
        Object result2;
        TestObject testObject;

        // check a few numbers with testnumbers
        testObject = new TestObject(testnumbers);
        result = testObject.run("indexOf", 1);
        assertEquals(standardMessage("indexOf(1)", testnumbers), 3, result);
        testObject = new TestObject(testnumbers);
        result = testObject.run("indexOf", 1);
        assertEquals(standardMessage("indexOf(1) run a second time", testnumbers), 3, result);
        testObject = new TestObject(testnumbers);
        result = testObject.run("indexOf", 2);
        assertEquals(standardMessage("indexOf(2)", testnumbers), 0, result);
        testObject = new TestObject(testnumbers);
        result = testObject.run("indexOf", 5);
        assertEquals(standardMessage("indexOf(5)", testnumbers), 2, result);

        // check if -1 is returned correctly
        testObject = new TestObject(testnumbers);
        result = testObject.run("indexOf", 12);
        assertEquals(standardMessage("indexOf(12)", testnumbers), -1, result);

        // these numbers should not be in the tuple in the first place!
        testObject = new TestObject(testnumbers);
        result = testObject.run("indexOf", -2);
        assertEquals(standardMessage("indexOf(-2)", testnumbers), -1, result);
        testObject = new TestObject(testnumbers);
        result = testObject.run("indexOf", 0);
        assertEquals(standardMessage("indexOf(0)", testnumbers), -1, result);

        // check a few numbers with testnumbers2
        testObject = new TestObject(testnumbers2);
        result = testObject.run("indexOf", 490);
        assertEquals(standardMessage("indexOf(490)", testnumbers2), 0, result);

        // check if -1 is returned correctly
        testObject = new TestObject(testnumbers2);
        result = testObject.run("indexOf", 2);
        assertEquals(standardMessage("indexOf(2)", testnumbers2), -1, result);

        // these numbers should not be in the tuple in the first place!
        testObject = new TestObject(testnumbers2);
        result = testObject.run("indexOf", 0);
        assertEquals(standardMessage("indexOf(0)", testnumbers2), -1, result);
        testObject = new TestObject(testnumbers2);
        result = testObject.run("indexOf", 3);
        assertEquals(standardMessage("indexOf(3)", testnumbers2), 9, result);

        // check constancy
        testObject = new TestObject(testnumbers);
        result = testObject.run("indexOf", 1);
        result2 = testObject.run("indexOf", 1);
        assertEquals(standardMessage("indexOf(1).equlas(indexOf(1))", testnumbers), true, result.equals(result2));
    }

    /**
     * Tests the {@code insert(x)} method. Asserts that:
     * <ul>
     * <li>{@code insert(x)} does not insert {@code x} if x is smaller than 1.</li>
     * <li>{@code insert(x)} does not insert {@code x} at the end of the tuple otherwise.</li>
     * <li>{@code insert(x)} does the above on an empty tuple, too.</li>
     * </ul>
     */
    @Test
    public void testInsert() {
        TestObject testObject;
        Object result;

        // -2 should not be inserted!
        testObject = new TestObject(testnumbers);
        testObject.run("insert", -2);
        result = testObject.run("indexOf", -2);
        assertEquals(standardMessage("insert(-2).indexOf(-2)", testnumbers), -1, result);
        testObject = new TestObject(testnumbers2);
        testObject.run("insert", -2);
        result = testObject.run("indexOf", -2);
        assertEquals(standardMessage("insert(-2).indexOf(-2)", testnumbers2), -1, result);

        // 0 should not be inserted!
        testObject = new TestObject(testnumbers);
        testObject.run("insert", 0);
        result = testObject.run("indexOf", 0);
        assertEquals(standardMessage("insert(0).indexOf(0)", testnumbers), -1, result);
        testObject = new TestObject(testnumbers2);
        testObject.run("insert", 0);
        result = testObject.run("indexOf", 0);
        assertEquals(standardMessage("insert(0).indexOf(0)", testnumbers2), -1, result);

        // check if insertions works as expected
        testObject = new TestObject(testnumbers);
        testObject.run("insert", 8);
        result = testObject.run("indexOf", 8);
        assertEquals(standardMessage("insert(8).indexOf(8)", testnumbers), 5, result);
        testObject = new TestObject(testnumbers2);
        testObject.run("insert", 8);
        result = testObject.run("indexOf", 8);
        assertEquals(standardMessage("insert(8).indexOf(8)", testnumbers2), 10, result);

        // check if insertions works on a empty tuple, too
        testObject = new TestObject(null, int[].class);
        testObject.run("insert", 8);
        result = testObject.run("indexOf", 8);
        assertEquals(standardMessage("insert(8).indexOf(8)", null), 0, result);
    }

    /**
     * Tests the {@code max()} method. Asserts that:
     * <ul>
     * <li>{@code max()} returns the greatest number in the tuple.</li>
     * <li>{@code max()} returns {@code -1} if the tuple is empty.</li>
     * </ul>
     */
    @Test
    public void testMax() {
        Object result;
        TestObject testObject;
        int[] empty = new int[] {};

        testObject = new TestObject(testnumbers);
        result = testObject.run("max");
        assertEquals(standardMessage("max()", testnumbers), 5, result);

        testObject = new TestObject(testnumbers2);
        result = testObject.run("max");
        assertEquals(standardMessage("max()", testnumbers2), 490, result);

        // check if -1 is returned correctly
        testObject = new TestObject(empty);
        result = testObject.run("max");
        assertEquals(standardMessage("max()", empty), -1, result);

        testObject = new TestObject(null, int[].class);
        result = testObject.run("max");
        assertEquals(standardMessage("max()", null), -1, result);
    }

    /**
     * Tests the {@code min()} method. Asserts that:
     * <ul>
     * <li>{@code min()} returns the smallest number in the tuple.</li>
     * <li>{@code min()} returns {@code -1} if the tuple is empty.</li>
     * </ul>
     */
    @Test
    public void testMin() {
        Object result;
        TestObject testObject;
        int[] empty = new int[] {};

        testObject = new TestObject(testnumbers);
        result = testObject.run("min");
        assertEquals(standardMessage("min()", testnumbers), 1, result);

        testObject = new TestObject(testnumbers2);
        result = testObject.run("min");
        assertEquals(standardMessage("min()", testnumbers2), 3, result);

        // check if -1 is returned correctly
        testObject = new TestObject(empty);
        result = testObject.run("min");
        assertEquals(standardMessage("min()", empty), -1, result);
        testObject = new TestObject(null, int[].class);
        result = testObject.run("min");
        assertEquals(standardMessage("min()", null), -1, result);
    }

    /**
     * Tests the constructor. Asserts that:
     * <ul>
     * <li>The constructor runs without an Exception if called with an Array of Integers</li>
     * <li>The constructor runs without an Exception if called with {@code null}</li>
     * </ul>
     */
    @Test
    public void testConstructor() {
        TestObject testObject;

        // check if there is a constructor that works for an array;
        testObject = new TestObject(testnumbers);
        assertNotNull("The instantiation of your class does not work with new int[]" + NumbersToString(testnumbers)
                + "!", testObject);

        // check if there is a constructor that works for null.
        testObject = new TestObject(testnumbers);
        assertNotNull("The instantiation of your class does not work with null!", testObject);
    }

    /**
     * Tests the {@code remove(x)} method. Asserts that:
     * <ul>
     * <li>{@code remove(x)} removes all occurrences of {@code x} in the tuple and returns {@code true} if {@code x} was
     * in the tuple</li>
     * <li>{@code remove(x)} returns {@code false} if {@code x} was not in the tuple</li>
     * <li>{@code remove(x)} returns {@code false} if called a second time with the same parameter {@code x}</li>
     * </ul>
     */
    @Test
    public void testRemove() {
        TestObject testObject;
        Object result;

        testObject = new TestObject(testnumbers);
        result = testObject.run("remove", 2);
        assertEquals(standardMessage("remove(2)", testnumbers), true, result);
        result = testObject.run("remove", 2);
        assertEquals(standardMessage("2x remove(2)", testnumbers), false, result);
        result = testObject.run("countNumbers", 2);
        assertEquals(standardMessage("remove(2).countNumbers(2)", testnumbers), 0, result);

        testObject = new TestObject(testnumbers2);
        result = testObject.run("remove", 490);
        assertEquals(standardMessage("remove(490)", testnumbers), true, result);
        result = testObject.run("remove", 490);
        assertEquals(standardMessage("2x remove(490)", testnumbers), false, result);
        result = testObject.run("countNumbers", 490);
        assertEquals(standardMessage("remove(490).countNumbers(490)", testnumbers2), 0, result);

        testObject = new TestObject(testnumbers2);
        result = testObject.run("remove", 120);
        assertEquals(standardMessage("remove(120)", testnumbers), false, result);

        testObject = new TestObject(testnumbers);
        result = testObject.run("remove", 120);
        assertEquals(standardMessage("remove(120)", testnumbers), false, result);
    }

    /**
     * Tests the package name of the implemented class. Asserts that:
     * <ul>
     * <li>The package name starts with {@code edu.kit.informatik}</li>
     * </ul>
     */
    @Test
    public void testPackage() {
        String result, expectedStart;
        result = TestObject.getPackageName();
        expectedStart = "edu.kit.informatik";
        assertThat("Bad package path!", result, startsWith(expectedStart));
    }

    /**
     * Tests the class name of the implemented class. Asserts that:
     * <ul>
     * <li>The class name is {@code LinkedNaturalNumberTuple}</li>
     * </ul>
     */
    @Test
    public void testClassName() {
        assertEquals("Please name your class correctly!", "LinkedNaturalNumberTuple", TestObject.getClassSimpleName());
    }

    /**
     * Tests the {@code toString()} method. Asserts that:
     * <ul>
     * <li>{@code toString()} returns a String of numbers of the tuple, separated by commas, that does not contain any
     * whitespace.</li>
     * <li>{@code toString()} returns {@code ""} (empty String) if called on a empty tuple.</li>
     */
    @Test
    public void testToString() {
        Object result;
        TestObject testObject;

        testObject = new TestObject(testnumbers);
        result = testObject.run("toString");
        assertEquals(standardMessage("toString()", testnumbers), "2,2,5,1,2", result);

        testObject = new TestObject(testnumbers2);
        result = testObject.run("toString");
        assertEquals(standardMessage("toString()", testnumbers2), "490,5,12,22,4,490,490,480,11,3", result);

        testObject = new TestObject(null, int[].class);
        result = testObject.run("toString");
        assertEquals(standardMessage("toString()", null), "", result);
    }

    /**
     * Tests the {@code swap(x, y)} method. {@code swap(x, y)} is called on a tuple that has the number {@code a} at
     * position {@code x} and the number {@code b} at position {@code y}. This method asserts that:
     * <ul>
     * <li>{@code swap(x, y)} returns {@code true} and after the call the number at position {@code x} is {@code b} and
     * the number at position {@code y} is {@code a}, while other numbers have been left unchanged.</li>
     * <li>the above happens regardless if {@code x<y}, {@code x>y} or {@code x=y}, as a long as {@code x} and {@code y}
     * are valid indexes in the tuple.</li>
     * <li>{@code swap(x, y)} returns {@code true} if called a second time and after the call the number at position
     * {@code x} is {@code a} and the number at position {@code y} is {@code b}, while other numbers have been left
     * unchanged.</li>
     * <li>the above happens regardless if {@code x<y}, {@code x>y} or {@code x=y}, as a long as {@code x} and {@code y}
     * are valid indexes in the tuple.</li>
     * <li>{@code swap(x, y)} returns {@code false} and leaves the tuple unchanged if either {@code x} or {@code y}
     * isn't a valid index in the tuple.</li>
     * <li>the above happens regardless if {@code x<y}, {@code x>y} or {@code x=y}.</li>
     * </ul>
     */
    @Test
    public void testSwap() {
        Object result;
        TestObject testObject;

        // swapping non existent numbers
        testObject = new TestObject(testnumbers);
        result = testObject.run("swap", new Object[] {-20, 2000});
        assertEquals(standardMessage("swap(-20, 2000)", testnumbers), false, result);
        result = testObject.run("indexOf", 1);
        assertEquals(standardMessage("swap(-20, 2000).indexOf(1)", testnumbers), 3, result);
        result = testObject.run("indexOf", 2);
        assertEquals(standardMessage("swap(-20, 2000).indexOf(2)", testnumbers), 0, result);
        result = testObject.run("indexOf", 5);
        assertEquals(standardMessage("swap(-20, 2000).indexOf(5)", testnumbers), 2, result);

        testObject = new TestObject(testnumbers);
        result = testObject.run("swap", new Object[] {40, 40});
        assertEquals(standardMessage("swap(40, 40)", testnumbers), false, result);
        result = testObject.run("indexOf", 1);
        assertEquals(standardMessage("swap(40, 40).indexOf(1)", testnumbers), 3, result);
        result = testObject.run("indexOf", 2);
        assertEquals(standardMessage("swap(40, 40).indexOf(2)", testnumbers), 0, result);
        result = testObject.run("indexOf", 5);
        assertEquals(standardMessage("swap(40, 40).indexOf(5)", testnumbers), 2, result);

        testObject = new TestObject(testnumbers2);
        result = testObject.run("swap", new Object[] {2000, 2});
        assertEquals(standardMessage("swap(2000, 2)", testnumbers2), false, result);
        result = testObject.run("indexOf", 490);
        assertEquals(standardMessage("swap(2000, 2).indexOf(490)", testnumbers2), 0, result);
        result = testObject.run("indexOf", 2);
        assertEquals(standardMessage("swap(2000, 2).indexOf(2)", testnumbers2), -1, result);
        result = testObject.run("indexOf", 5);
        assertEquals(standardMessage("swap(2000, 2).indexOf(5)", testnumbers2), 1, result);

        // swapping existent numbers
        testObject = new TestObject(testnumbers);
        result = testObject.run("swap", new Object[] {2, 3});
        assertEquals(standardMessage("swap(2, 3)", testnumbers), true, result);
        result = testObject.run("indexOf", 1);
        assertEquals(standardMessage("swap(2, 3).indexOf(1)", testnumbers), 2, result);
        result = testObject.run("indexOf", 2);
        assertEquals(standardMessage("swap(2, 3).indexOf(2)", testnumbers), 0, result);
        result = testObject.run("indexOf", 5);
        assertEquals(standardMessage("swap(2, 3).indexOf(5)", testnumbers), 3, result);

        testObject = new TestObject(testnumbers2);
        result = testObject.run("swap", new Object[] {2, 3});
        assertEquals(standardMessage("swap(2, 3)", testnumbers), true, result);
        result = testObject.run("indexOf", 12);
        assertEquals(standardMessage("swap(2, 3).indexOf(12)", testnumbers2), 3, result);
        result = testObject.run("indexOf", 22);
        assertEquals(standardMessage("swap(2, 3).indexOf(22)", testnumbers2), 2, result);
        result = testObject.run("indexOf", 490);
        assertEquals(standardMessage("swap(2, 3).indexOf(490)", testnumbers2), 0, result);

        // swapping existent numbers
        testObject = new TestObject(testnumbers);
        result = testObject.run("swap", new Object[] {3, 2});
        assertEquals(standardMessage("swap(3, 2)", testnumbers), true, result);
        result = testObject.run("indexOf", 1);
        assertEquals(standardMessage("swap(3, 2).indexOf(1)", testnumbers), 2, result);
        result = testObject.run("indexOf", 2);
        assertEquals(standardMessage("swap(3, 2).indexOf(2)", testnumbers), 0, result);
        result = testObject.run("indexOf", 5);
        assertEquals(standardMessage("swap(3, 2).indexOf(5)", testnumbers), 3, result);

        testObject = new TestObject(testnumbers2);
        result = testObject.run("swap", new Object[] {3, 2});
        assertEquals(standardMessage("swap(3, 2)", testnumbers), true, result);
        result = testObject.run("indexOf", 12);
        assertEquals(standardMessage("swap(3, 2).indexOf(12)", testnumbers2), 3, result);
        result = testObject.run("indexOf", 22);
        assertEquals(standardMessage("swap(3, 2).indexOf(22)", testnumbers2), 2, result);
        result = testObject.run("indexOf", 490);
        assertEquals(standardMessage("swap(3, 2).indexOf(490)", testnumbers2), 0, result);

        // swapping numbers and the same point
        testObject = new TestObject(testnumbers);
        result = testObject.run("swap", new Object[] {1, 1});
        assertEquals(standardMessage("swap(1, 1)", testnumbers), true, result);
        result = testObject.run("indexOf", 1);
        assertEquals(standardMessage("swap(1, 1).indexOf(1)", testnumbers), 3, result);
        result = testObject.run("indexOf", 2);
        assertEquals(standardMessage("swap(1, 1).indexOf(2)", testnumbers), 0, result);
        result = testObject.run("indexOf", 5);
        assertEquals(standardMessage("swap(1, 1).indexOf(5)", testnumbers), 2, result);

        testObject = new TestObject(testnumbers2);
        result = testObject.run("swap", new Object[] {1, 1});
        assertThat(standardMessage("swap(1, 1)", testnumbers), (boolean) result, is(true));
        result = testObject.run("indexOf", 12);
        assertEquals(standardMessage("swap(1, 1).indexOf(12)", testnumbers2), 2, result);
        result = testObject.run("indexOf", 22);
        assertEquals(standardMessage("swap(1, 1).indexOf(22)", testnumbers2), 3, result);
        result = testObject.run("indexOf", 490);
        assertEquals(standardMessage("swap(1, 1).indexOf(490)", testnumbers2), 0, result);

        // swapping existent numbers twice
        testObject = new TestObject(testnumbers);
        testObject.run("swap", new Object[] {0, 3});
        result = testObject.run("swap", new Object[] {0, 3});
        assertEquals(standardMessage("2x swap(0, 3)", testnumbers), true, result);
        result = testObject.run("indexOf", 2);
        assertEquals(standardMessage("2x swap(0, 3).indexOf(2)", testnumbers), 0, result);
        result = testObject.run("indexOf", 5);
        assertEquals(standardMessage("2x swap(0, 3).indexOf(5)", testnumbers), 2, result);
        result = testObject.run("indexOf", 1);
        assertEquals(standardMessage("2x swap(0, 3).indexOf(1)", testnumbers), 3, result);

        testObject = new TestObject(testnumbers2);
        testObject.run("swap", new Object[] {4, 0});
        result = testObject.run("swap", new Object[] {4, 0});
        assertEquals(standardMessage("2x swap(4, 0)", testnumbers), true, result);
        result = testObject.run("indexOf", 4);
        assertEquals(standardMessage("2x swap(4, 0).indexOf(4)", testnumbers2), 4, result);
        result = testObject.run("indexOf", 22);
        assertEquals(standardMessage("2x swap(4, 0).indexOf(22)", testnumbers2), 3, result);
        result = testObject.run("indexOf", 490);
        assertEquals(standardMessage("2x swap(4, 0).indexOf(490)", testnumbers2), 0, result);

        testObject = new TestObject(testnumbers2);
        testObject.run("swap", new Object[] {1, 1});
        result = testObject.run("swap", new Object[] {1, 1});
        assertEquals(standardMessage("swap(1, 1)", testnumbers), true, result);
        result = testObject.run("indexOf", 12);
        assertEquals(standardMessage("swap(1, 1).indexOf(12)", testnumbers2), 2, result);
        result = testObject.run("indexOf", 22);
        assertEquals(standardMessage("swap(1, 1).indexOf(22)", testnumbers2), 3, result);
        result = testObject.run("indexOf", 490);
        assertEquals(standardMessage("swap(1, 1).indexOf(490)", testnumbers2), 0, result);
    }
}
