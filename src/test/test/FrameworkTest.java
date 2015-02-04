package test.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import test.TestObject;

/**
 * Run this test to check the funcionality of the test framework. Help is appreciated! Set up this class to test
 * {@code test.test.TestClass}.
 * 
 * @author Joshua Gleitze
 * @version 2
 * @since 24.01.2015
 *
 */
public class FrameworkTest {

    /**
     * tests the {@link TestObject#resetClass()} functionality. It is expected to reset static fields. Asserts that:
     * <ul>
     * <li>static fields are reset after calling {@code resetClass()} and the class continues to work.
     * <li>the above is true for both the tested class as well as for classes loaded by the tested class.
     * </ul>
     */
    @Test
    public void testClassReset() {
        int result;
        result = TestObject.runStatic(int.class, "staticAddition");
        assertThat(result, is(1));
        TestObject.resetClass();
        result = TestObject.runStatic(int.class, "staticAddition");
        assertThat((int) result, is(1));
        TestObject.resetClass();
        result = TestObject.runStatic(int.class, "staticAddition");
        assertThat((int) result, is(1));
        TestObject.resetClass();

        result = TestObject.runStatic(int.class, "delegatedStaticAddition");
        assertThat(result, is(1));
        TestObject.resetClass();
        result = TestObject.runStatic(int.class, "delegatedStaticAddition");
        assertThat((int) result, is(1));
        TestObject.resetClass();
        result = TestObject.runStatic(int.class, "delegatedStaticAddition");
        assertThat((int) result, is(1));
        TestObject.resetClass();
    }

    /**
     * tests the {@link TestObject#getPackageName()} functionality. Asserts that:
     * <ul>
     * <li> {@code getPackageName()} returns the correct class path.
     * </ul>
     */
    @Test
    public void testGetPackageName() {
        assertThat(TestObject.getPackageName(), is("test.test"));
    }

    /**
     * test for #31 (https://github.com/jGleitz/JUnit-KIT/issues/31).
     */
    @Test
    public void testThrowInnerException() {
        new TestObject().runVoid("throwNestedException");
    }

}
