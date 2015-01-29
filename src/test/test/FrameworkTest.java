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
 * @version 1.1
 * @since 24.01.2015
 *
 */
public class FrameworkTest {

	/**
	 * tests the {@link TestObject#resetClass()} functionality. It is expected to reset static fields. Asserts that:
	 * <ul>
	 * <li>static fields are reset after calling {@code resetClass()} and the class continues to work.
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

}
