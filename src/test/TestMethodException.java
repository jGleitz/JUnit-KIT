/**
 * This exception is thrown if an exception that was set to be rethrown occurs while running a test method of
 * {@link TestObject}. Use {@link #getCause} to get the exception that occurred in the tested class.
 * 
 * @see TestObject
 */
package test;

/**
 * @author Joshua Gleitze
 * @version 1.0
 * @since 15.01.2015
 *
 */
public class TestMethodException extends RuntimeException {
    private static final long serialVersionUID = -4003741199866036203L;
    private final Throwable cause;

    TestMethodException(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
