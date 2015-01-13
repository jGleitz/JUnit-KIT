package test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Assert;

/**
 * Represents an expected result of a test. This class is mainly useful to be able to check multiple lines of output
 * with different criteria. <br>
 * <br>
 * Example:<br>
 * Say we expect some lines to be three times {@code "Success!"}, an empty line afterwards, and then a line starting
 * with {@code "Error,"}. We would construct this Array: <br>
 * 
 * <pre>
 * {@code
 * ExpectedResult[] expectedResults = new ExpectedResult[] {
 *      new ExpectedResult("Success!", TestType.SAME),
 *      new ExpectedResult("Success!", TestType.SAME),
 *      new ExpectedResult("Success!", TestType.SAME),
 *      new ExpectedResult(System.lineSeparator(), TestType.SAME),
 *      new ExpectedResult("Error,", TestType.STARTS_WITH)
 * }
 * </pre>
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @see InteractiveConsoleTest#multiLineTest(String, ExpectedResult[], String...)
 */
public final class ExpectedResult {
    private String testString;
    private ExpectedResult.TestType testType;

    /**
     * The test type represents which criterion should be used to to match the test String with the actual output
     * String.
     * 
     * @author Joshua Gleitze
     * @version 1.0
     *
     */
    public static enum TestType {
        /**
         * The output String has to be the same as the test string.
         */
        SAME,
        /**
         * The output String has to start with the test string.
         */
        STARTS_WITH;
    }

    /**
     * Construct an expected result where {@code testString} should match the actual output String as defined by the
     * {@code testType} criterion.
     * 
     * @param testString
     * @param testType
     */
    public ExpectedResult(String testString, ExpectedResult.TestType testType) {
        this.testString = testString;
        this.testType = testType;
    }

    /**
     * Creates an Array of expected results. Each result String has to match the corresponding String in
     * {@code testStrings}, by the criterion {@code testType}.
     * 
     * @param testStrings
     *            The Strings you want to test against.
     * @param testType
     *            The criterion every String should be tested with.
     * @return the constructed expected results
     */
    public static ExpectedResult[] getArray(String[] testStrings, TestType testType) {
        ExpectedResult[] result = new ExpectedResult[testStrings.length];
        for (int i = 0; i < testStrings.length; i++) {
            result[i] = new ExpectedResult(testStrings[i], testType);
        }
        return result;
    }

    /**
     * Creates an Array of expected results. Each output String has to match the corresponding String in
     * {@code testStrings}, by the criterion corresponding criterion in {@code testTypes}.
     * 
     * @param testStrings
     *            The Strings you want to test against.
     * @param testTypes
     *            The criteria each String should be tested with.
     * @return the constructed expected results
     */
    public static ExpectedResult[] getArray(String[] testStrings, TestType[] testTypes) {
        if (testStrings.length != testTypes.length) {
            throw new IllegalArgumentException("testTypes has to contain as much elements as strings does.");
        }
        ExpectedResult[] result = new ExpectedResult[testStrings.length];
        for (int i = 0; i < testStrings.length; i++) {
            result[i] = new ExpectedResult(testStrings[i], testTypes[i]);
        }
        return result;
    }

    /**
     * Asserts that the {@code actualResult} was the expected result. This method is calling {@link Assert#assertThat}
     * with the given {@code message}.
     * 
     * @param message
     *            The message that will be printed if the {@code actualResult} was not the expected result.
     * @param actualResult
     *            The actualResult you want to test.
     */
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

    /**
     * Returns the String this expected result tests against.
     * 
     * @return the test string
     */
    public String getTestString() {
        return this.testString;
    }

    /**
     * Returns the test criterion used to verify the expected result.
     * 
     * @return the test type
     */
    public TestType getTestType() {
        return this.testType;
    }
}