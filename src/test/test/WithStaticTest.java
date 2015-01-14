package test.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import test.TestObject;

public class WithStaticTest {

    @Test
    public void testTryIt() {
        Object result;
        result = TestObject.runStatic("tryIt");
        assertThat((int) result, is(1));
        result = TestObject.runStatic("tryIt");
        assertThat((int) result, is(1));
        result = TestObject.runStatic("tryIt");
        assertThat((int) result, is(1));
        result = TestObject.runStatic("tryIt");
        assertThat((int) result, is(1));
        result = TestObject.runStatic("tryIt");
        assertThat((int) result, is(1));
    }

}
