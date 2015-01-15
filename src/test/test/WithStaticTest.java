package test.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import test.TestObject;

public class WithStaticTest {

    @Test
    public void testTryIt() {
        int result;
        result = TestObject.runStatic(int.class, "tryIt");
        assertThat(result, is(1));
        result = TestObject.runStatic(int.class, "tryIt");
        assertThat((int) result, is(1));
        result = TestObject.runStatic(int.class, "tryIt");
        assertThat((int) result, is(1));
        result = TestObject.runStatic(int.class, "tryIt");
        assertThat((int) result, is(1));
        result = TestObject.runStatic(int.class, "tryIt");
        assertThat((int) result, is(1));
    }

}
