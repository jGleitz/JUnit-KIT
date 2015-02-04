package test.framework;

/**
 * Thrown if a tested method calls {@link System#exit}.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class ExitException extends SecurityException {
    private static final long serialVersionUID = -5649621212556394572L;
    public final int status;

    /**
     * Constructs a {@code ExitException}
     * 
     * @param status
     *            The status parameter the tested method tried to call {@code System.exit} with.
     */
    public ExitException(int status) {
        super("The method called System.exit(" + status + ")");
        this.status = status;
    }
}