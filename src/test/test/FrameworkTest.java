package test.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Test;

import test.TestObject;
import test.framework.TestClassLoader;
import test.mocking.MockCompiler;
import test.mocking.MockerJavaClassFile;
import test.mocking.MockerJavaSourceFile;

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
		assertThat(result, is(1));
		TestObject.resetClass();
		result = TestObject.runStatic(int.class, "staticAddition");
		assertThat(result, is(1));
		TestObject.resetClass();

		result = TestObject.runStatic(int.class, "delegatedStaticAddition");
		assertThat(result, is(1));
		TestObject.resetClass();
		result = TestObject.runStatic(int.class, "delegatedStaticAddition");
		assertThat(result, is(1));
		TestObject.resetClass();
		result = TestObject.runStatic(int.class, "delegatedStaticAddition");
		assertThat(result, is(1));
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

	/**
	 * Asserts that the mocking procedure works as expected: If classes are mocked multiple times, the newest version
	 * will be used for a new class loader. Furthermore, after calling {@link TestClassLoader#forget(String)}, the
	 * "normal" version of the class is loaded again for a new class loader.
	 */
	@Test
	public void testMultipleMocking() {
		int result;
		MockerJavaSourceFile sf;
		MockerJavaClassFile cf;
		//@formatter:off
        String mockClass =
                "package test.test;" +

            "public class AnotherTestClass {" +
            "private static int x = 0;" +

                "public static int staticAddition() {" +
                "return ++x;" +
                "}" +
                "}";
        //@formatter:on

		result = TestObject.runStatic(int.class, "delegatedStaticAddition");
		assertThat(result, is(1));

		sf = new MockerJavaSourceFile("test.test.AnotherTestClass", mockClass);
		cf = MockCompiler.compile(sf);
		TestClassLoader.mock(cf);
		TestObject.resetClass();

		result = TestObject.runStatic(int.class, "delegatedStaticAddition");
		assertThat(result, is(1));

		sf = new MockerJavaSourceFile("test.test.AnotherTestClass", mockClass.replace("x = 0", "x = 1"));
		cf = MockCompiler.compile(sf);
		TestClassLoader.mock(cf);
		TestObject.resetClass();

		result = TestObject.runStatic(int.class, "delegatedStaticAddition");
		assertThat(result, is(2));

		sf = new MockerJavaSourceFile("test.test.AnotherTestClass", mockClass.replace("x = 0", "x = 6"));
		cf = MockCompiler.compile(sf);
		TestClassLoader.mock(cf);
		TestObject.resetClass();

		result = TestObject.runStatic(int.class, "delegatedStaticAddition");
		assertThat(result, is(7));

		TestClassLoader.forget("test.test.AnotherTestClass");
		TestObject.resetClass();

		result = TestObject.runStatic(int.class, "delegatedStaticAddition");
		assertThat(result, is(1));
	}

	@After
	public void removeMocking() {
		TestClassLoader.forget("test.test.AnotherTestClass");
		TestObject.resetClass();
	}

}
