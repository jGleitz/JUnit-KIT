package test.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import test.TestObject;

/**
 * Please run this test to test the new class reset functionality! Set up this class to test
 * {@code test.test.WithStatic}!
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 24.01.2015
 *
 */
public class WithStaticTest {

	@Test
	public void testTryIt() {
		int result;
		result = TestObject.runStatic(int.class, "tryIt");
		assertThat(result, is(1));
		TestObject.resetClass();
		result = TestObject.runStatic(int.class, "tryIt");
		assertThat((int) result, is(1));
		TestObject.resetClass();
		result = TestObject.runStatic(int.class, "tryIt");
		assertThat((int) result, is(1));
		TestObject.resetClass();
		result = TestObject.runStatic(int.class, "tryIt");
		assertThat((int) result, is(1));
		TestObject.resetClass();
		result = TestObject.runStatic(int.class, "tryIt");
		assertThat((int) result, is(1));
	}

}
