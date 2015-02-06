package test;

/**
 * Represents a status {@link System#exit} can be called with.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public enum SystemExitStatus {
    /**
     * The tested class may call {@code System.exit(x)} with any argument {@code x}.
     */
    ALL,
    /**
     * The tested class may not call {@code System.exit(x)} at all.
     */
    NONE,
    /**
     * The tested class may only call {@code System.exit(0)}.
     */
    WITH_0,
    /**
     * The tested class may only call {@code System.exit(x)} if x>0.
     */
    WITH_GREATER_THAN_0
}