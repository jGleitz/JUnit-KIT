package test.framework;

/**
 * Thrown if an exception occurs that is unfixable and forces the framework to exit. The message points the user to
 * GitHub to report about the error.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 06.02.2015
 */
public class FrameworkException extends RuntimeException {
    private static final long serialVersionUID = 2221405766881026986L;
    private static final String GIT_HUB_POINTER = "This is a serious issue. But we'd like to help solve this!\n"
            + "Please report what happened on GitHub. Create a new issue at:\n"
            + "https://github.com/jGleitz/JUnit-KIT/issues";

    /**
     * New Framework Exception.
     * 
     * @param message
     *            Detailed error description
     */
    public FrameworkException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        String details = super.getMessage();
        return details + "\n\n" + GIT_HUB_POINTER;
    }
}
