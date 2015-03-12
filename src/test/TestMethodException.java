package test;

/**
 * Thrown if an exception that was set to be rethrown occurs while running a test method of {@link TestObject}. Use
 * {@link #getCause} to get the exception that occurred in the tested class.
 *
 * @see TestObject
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

    /**
     * Returns the actual exception that occurred while running the tested method.
     * 
     * @return The exception
     */
    @Override
    public Throwable getCause() {
        return cause;
    }
}
