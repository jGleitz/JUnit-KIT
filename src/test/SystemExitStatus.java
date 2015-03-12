package test;

/**
 * Represents a status {@link System#exit} can be called with.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public enum SystemExitStatus {
    /**
     * {@code System.exit(x)} with any argument {@code x}.
     */
    ALL,
    /**
     * No call to {@code System.exit(x)} at all.
     */
    NONE,
    /**
     * {@code System.exit(0)}.
     */
    WITH_0,
    /**
     * {@code System.exit(x)} with x>0.
     */
    WITH_GREATER_THAN_0;

    @Override
    public String toString() {
        switch (this) {
        case ALL:
            return "System.exit(x) with any x >= 0";
        case NONE:
            return "no call to System.exit(x) at all";
        case WITH_0:
            return "System.exit(0)";
        case WITH_GREATER_THAN_0:
            return "System.exit(x) with any x > 0";
        default:
            throw new IllegalArgumentException();
        }
    }
}