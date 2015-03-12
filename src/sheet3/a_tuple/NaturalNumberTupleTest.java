package sheet3.a_tuple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import test.TestObject;

@SuppressWarnings("deprecation")
public class NaturalNumberTupleTest {

    private static String NumbersToString(int[] numbers) {
        String result = "{";
        for (int i = 0; i < numbers.length; i++) {
            result += numbers[i];
            result += (i < (numbers.length - 1)) ? ", " : "}";
        }
        return result;
    }

    private static String standardMessage(String functionName, int[] testnumbers) {
        return functionName + " of a NaturalNumberTuple instantiated with " + NumbersToString(testnumbers);
    }

    private final int[] testnumbers = {
            0,
            2,
            2,
            5,
            1,
            -1,
            2
    };
    private final int[] testnumbers2 = {
            0,
            490,
            -50,
            -50,
            5,
            12,
            -1,
            22,
            4,
            490,
            490,
            480,
            11,
            -12,
            0,
            0,
            0,
            3
    };
    private final int[] testnumbers3 = {
            1,
            2,
            3,
            4
    };

    @Test
    public void testCountNumbers() {
        int result;
        TestObject testObject;

        testObject = new TestObject(testnumbers);
        result = testObject.run(int.class, "countNumbers", 1);
        assertEquals(standardMessage("countNumbers(1)", testnumbers), 1, result);

        testObject = new TestObject(testnumbers2);
        result = testObject.run(int.class, "countNumbers", 490);
        assertEquals(standardMessage("countNumbers(490)", testnumbers2), 3, result);
    }

    @Test
    public void testEqualsNaturalNumberTuple() {
        boolean result;
        TestObject testObject;

        testObject = new TestObject(testnumbers);
        result = testObject.run(boolean.class, "equals", new TestObject(testnumbers));
        assertEquals(
            standardMessage("equals(new NaturalNumberTuple(new int[]" + NumbersToString(testnumbers) + ")", testnumbers),
            true, result);
        result = testObject.run(boolean.class, "equals", new TestObject(testnumbers2));
        assertEquals(
            standardMessage("equals(new NaturalNumberTuple(new int[]" + NumbersToString(testnumbers2) + ")",
                testnumbers), false, result);

        testObject = new TestObject(testnumbers2);
        result = testObject.run(boolean.class, "equals", new TestObject(testnumbers));
        assertEquals(
            standardMessage("equals(new NaturalNumberTuple(new int[]" + NumbersToString(testnumbers) + ")",
                testnumbers2), false, result);
        result = testObject.run(boolean.class, "equals", new TestObject(testnumbers2));
        assertEquals(
            standardMessage("equals(new NaturalNumberTuple(new int[]" + NumbersToString(testnumbers2) + ")",
                testnumbers2), true, result);
    }

    @Test
    public void testIndexOf() {
        int result;
        TestObject testObject;

        testObject = new TestObject(testnumbers);
        result = testObject.run(int.class, "indexOf", 1);
        assertEquals(standardMessage("indexOf(1)", testnumbers), 3, result);
        result = testObject.run(int.class, "indexOf", 1);
        assertEquals(standardMessage("indexOf(1) run a second time", testnumbers), 3, result);
        result = testObject.run(int.class, "indexOf", 2);
        assertEquals(standardMessage("indexOf(2)", testnumbers), 0, result);
        result = testObject.run(int.class, "indexOf", 5);
        assertEquals(standardMessage("indexOf(5)", testnumbers), 2, result);
        result = testObject.run(int.class, "indexOf", -2);
        assertEquals(standardMessage("indexOf(-2)", testnumbers), -1, result);
        result = testObject.run(int.class, "indexOf", 0);
        assertEquals(standardMessage("indexOf(0)", testnumbers), -1, result);

        testObject = new TestObject(testnumbers2);
        result = testObject.run(int.class, "indexOf", 490);
        assertEquals(standardMessage("indexOf(490)", testnumbers2), 0, result);
        result = testObject.run(int.class, "indexOf", 0);
        assertEquals(standardMessage("indexOf(0)", testnumbers2), -1, result);
        result = testObject.run(int.class, "indexOf", 3);
        assertEquals(standardMessage("indexOf(3)", testnumbers2), 9, result);
    }

    @Test
    public void testInsert() {
        TestObject testObject;
        Object result;

        testObject = new TestObject(testnumbers);
        testObject.run(TestObject.class, "insert", -2);
        result = testObject.run(int.class, "indexOf", -2);
        assertEquals(standardMessage("insert(-2).indexOf(-2)", testnumbers), -1, result);

        testObject = new TestObject(testnumbers2);
        testObject.run(TestObject.class, "insert", -2);
        result = testObject.run(int.class, "indexOf", -2);
        assertEquals(standardMessage("insert(-2).indexOf(-2)", testnumbers2), -1, result);

        testObject = new TestObject(testnumbers);
        testObject.run(TestObject.class, "insert", 8);
        result = testObject.run(int.class, "indexOf", 8);
        assertEquals(standardMessage("insert(8).indexOf(8)", testnumbers), 5, result);

        testObject = new TestObject(testnumbers2);
        testObject.run(TestObject.class, "insert", 8);
        result = testObject.run(int.class, "indexOf", 8);
        assertEquals(standardMessage("insert(8).indexOf(8)", testnumbers2), 10, result);

    }

    @Test
    public void testMax() {
        int result;
        TestObject testObject;
        int[] empty = new int[] {};

        testObject = new TestObject(testnumbers);
        result = testObject.run(int.class, "max");
        assertEquals(standardMessage("max()", testnumbers), 5, result);

        testObject = new TestObject(testnumbers2);
        result = testObject.run(int.class, "max");
        assertEquals(standardMessage("max()", testnumbers2), 490, result);

        // added in Version 2 of the task: If the tuple is empty -1 is to be
        // returned!
        testObject = new TestObject(empty);
        result = testObject.run(int.class, "max");
        assertEquals(standardMessage("max()", empty), -1, result);
    }

    @Test
    public void testMin() {
        int result;
        TestObject testObject;
        int[] empty = new int[] {};

        testObject = new TestObject(testnumbers);
        result = testObject.run(int.class, "min");
        assertEquals(standardMessage("min()", testnumbers), 1, result);

        testObject = new TestObject(testnumbers2);
        result = testObject.run(int.class, "min");
        assertEquals(standardMessage("min()", testnumbers2), 3, result);

        // added in Version 2 of the task: If the tuple is empty -1 is to be
        // returned!
        testObject = new TestObject(empty);
        result = testObject.run(int.class, "min");
        assertEquals(standardMessage("min()", empty), -1, result);
    }

    @Test
    public void testNaturalNumberTupleIntArray() {
        // check if there is a constructor that works;
        TestObject testObject = new TestObject(testnumbers);
        assertNotNull("The instantiation of your class does not work!", testObject);
    }

    @Test
    public void testRemove() {
        TestObject testObject;
        int result;

        testObject = new TestObject(testnumbers);
        testObject.run(TestObject.class, "remove", 2);
        result = testObject.run(int.class, "countNumbers", 2);
        assertEquals(standardMessage("remove(2).countNumbers(2)", testnumbers), 0, result);

        testObject = new TestObject(testnumbers2);
        testObject.run(TestObject.class, "remove", 490);
        result = testObject.run(int.class, "countNumbers", 490);
        assertEquals(standardMessage("remove(490).countNumbers(490)", testnumbers2), 0, result);
    }

    @Test
    public void testSort() {
        // sort() is not obligatory. Therefore, we only test it if it was
        // implemented. (Otherwise, an error would be
        // shown for it not being implemented)
        if (TestObject.hasMethod("sort")) {
            TestObject testObject;
            int result;

            testObject = new TestObject(testnumbers);
            testObject.run(TestObject.class, "sort");
            result = testObject.run(int.class, "indexOf", 1);
            assertEquals(standardMessage("sort().indexOf(1)", testnumbers), 0, result);
            result = testObject.run(int.class, "indexOf", 2);
            assertEquals(standardMessage("sort().indexOf(2)", testnumbers), 1, result);
            result = testObject.run(int.class, "indexOf", 5);
            assertEquals(standardMessage("sort().indexOf(5)", testnumbers), 4, result);
            testObject.run(TestObject.class, "sort");
            testObject.run(TestObject.class, "sort");
            result = testObject.run(int.class, "indexOf", 1);
            assertEquals(standardMessage("sort().sort().sort().indexOf(1)", testnumbers), 0, result);
            result = testObject.run(int.class, "indexOf", 2);
            assertEquals(standardMessage("sort().sort().sort().indexOf(2)", testnumbers), 1, result);
            result = testObject.run(int.class, "indexOf", 5);
            assertEquals(standardMessage("sort().sort().sort().indexOf(5)", testnumbers), 4, result);

            testObject = new TestObject(testnumbers2);
            testObject.run(TestObject.class, "sort");
            result = testObject.run(int.class, "indexOf", 3);
            assertEquals(standardMessage("sort().indexOf(3)", testnumbers2), 0, result);
            result = testObject.run(int.class, "indexOf", 4);
            assertEquals(standardMessage("sort().indexOf(4)", testnumbers2), 1, result);
            result = testObject.run(int.class, "indexOf", 5);
            assertEquals(standardMessage("sort().indexOf(5)", testnumbers2), 2, result);
            result = testObject.run(int.class, "indexOf", 490);
            assertEquals(standardMessage("sort().indexOf(490)", testnumbers2), 7, result);
        }
    }

    @Test
    public void testSwap() {
        int result;
        TestObject testObject;

        // swapping non existant numbers
        testObject = new TestObject(testnumbers);
        testObject.run(TestObject.class, "swap", new Object[] {
                2000,
                -20
        });
        result = testObject.run(int.class, "indexOf", 1);
        assertEquals(standardMessage("swap(2000, -20).indexOf(1)", testnumbers), 3, result);
        result = testObject.run(int.class, "indexOf", 2);
        assertEquals(standardMessage("swap(2000, -20).indexOf(2)", testnumbers), 0, result);
        result = testObject.run(int.class, "indexOf", 5);
        assertEquals(standardMessage("swap(2000, -20).indexOf(5)", testnumbers), 2, result);

        testObject = new TestObject(testnumbers2);
        testObject.run(TestObject.class, "swap", new Object[] {
                2000,
                -20
        });
        result = testObject.run(int.class, "indexOf", 490);
        assertEquals(standardMessage("swap(2000, -20).indexOf(490)", testnumbers2), 0, result);
        result = testObject.run(int.class, "indexOf", 2);
        assertEquals(standardMessage("swap(2000, -20).indexOf(2)", testnumbers2), -1, result);
        result = testObject.run(int.class, "indexOf", 5);
        assertEquals(standardMessage("swap(2000, -20).indexOf(5)", testnumbers2), 1, result);

        // swapping existant numbers
        testObject = new TestObject(testnumbers);
        testObject.run(TestObject.class, "swap", new Object[] {
                2,
                3
        });
        result = testObject.run(int.class, "indexOf", 1);
        assertEquals(standardMessage("swap(2, 3).indexOf(1)", testnumbers), 2, result);
        result = testObject.run(int.class, "indexOf", 2);
        assertEquals(standardMessage("swap(2, 3).indexOf(2)", testnumbers), 0, result);
        result = testObject.run(int.class, "indexOf", 5);
        assertEquals(standardMessage("swap(2, 3).indexOf(5)", testnumbers), 3, result);

        testObject = new TestObject(testnumbers2);
        testObject.run(TestObject.class, "swap", new Object[] {
                2,
                3
        });
        result = testObject.run(int.class, "indexOf", 12);
        assertEquals(standardMessage("swap(2, 3).indexOf(12)", testnumbers2), 3, result);
        result = testObject.run(int.class, "indexOf", 22);
        assertEquals(standardMessage("swap(2, 3).indexOf(22)", testnumbers2), 2, result);
        result = testObject.run(int.class, "indexOf", 490);
        assertEquals(standardMessage("swap(2, 3).indexOf(490)", testnumbers2), 0, result);
    }

    @Test
    public void testToSet() {
        TestObject result1;
        boolean result2;
        TestObject testObject;

        testObject = new TestObject(testnumbers3);
        result1 = testObject.run(TestObject.class, "toSet");
        result2 = testObject.run(boolean.class, "equals", result1);
        assertEquals(standardMessage("tuple.toSet().equals(tuple)", testnumbers3), true, result2);

        testObject = new TestObject(testnumbers2);
        result1 = testObject.run(TestObject.class, "toSet");
        result2 = testObject.run(boolean.class, "equals", result1);
        assertEquals(standardMessage("tuple.toSet().equals(tuple)", testnumbers2), false, result2);
    }

    @Test
    public void testPrint() {
        Object result;
        TestObject testObject;

        testObject = new TestObject(testnumbers);
        testObject.runVoid("print");
        result = TestObject.getLastMethodOutput();
        assertEquals(standardMessage("print()", testnumbers), "2,2,5,1,2\n", result);

        testObject = new TestObject(testnumbers2);
        testObject.runVoid("print");
        result = TestObject.getLastMethodOutput();
        assertEquals(standardMessage("print()", testnumbers2), "490,5,12,22,4,490,490,480,11,3\n", result);
    }

}
