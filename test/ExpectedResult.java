package test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class ExpectedResult {
    private String testString;
    private ExpectedResult.TestType testType;

    public static enum TestType {
        SAME, STARTS_WITH;
    }
    
    public ExpectedResult(String testString, ExpectedResult.TestType testType) {
        
        this.testString = testString;
        this.testType = testType;
    }
    
    public static ExpectedResult[] getArray(String[] strings, TestType testType) {
        ExpectedResult[] result = new ExpectedResult[strings.length];
        for (int i = 0; i < strings.length; i++) {
            result[i] = new ExpectedResult(strings[i], testType);
        }
        return result;
    }
    
    public static ExpectedResult[] getArray(String[] strings, TestType[] testTypes) {
        if (strings.length != testTypes.length) {
            throw new IllegalArgumentException("testTypes has to contain as much elements as strings does.");
        }
        ExpectedResult[] result = new ExpectedResult[strings.length];
        for (int i = 0; i < strings.length; i++) {
            result[i] = new ExpectedResult(strings[i], testTypes[i]);
        }
        return result;
    }

    public void assertResult(String message, String actualResult) {
        switch (this.testType) {
        case SAME:
            assertThat(message, actualResult, is(this.testString));
            break;
        case STARTS_WITH:
            assertThat(message, actualResult, startsWith(this.testString));
            break;
        default:
            break;
        }
    }
}