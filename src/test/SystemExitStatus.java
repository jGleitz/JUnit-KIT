package test;

/**
 * Represents a status {@link System#exit} can be called with.
 *
 * @author Joshua Gleitze
 * @version 1.2
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
	WITH_GREATER_THAN_0,
	/**
	 * {@code System.exit(x)} with a x that has to be specified through {@link #setExactStatus()}.
	 */
	EXACTLY;

	private int exactSystemExitStatus = -1;

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
		case EXACTLY:
			return "System.exit(" + exactSystemExitStatus + ")";
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * @param status
	 *            A status {@code System.exit} was called with.
	 * @param mandatory
	 *            Whether it was obligatory to call {@code System.exit}.
	 * @return Whether {@code status} matches the system exit status described by this enum instance. If {@code status}
	 *         is null, and {@code obligatory} is {@code true}, this method always returns {@code false}. If
	 *         {@code status} is null, and {@code obligatory} is {@code false}, this method always returns {@code true}.
	 */
	public boolean matches(Integer status, boolean mandatory) {
		if (status == null) {
			return !mandatory;
		}
		switch (this) {
		case ALL:
			return true;
		case NONE:
			return false;
		case WITH_0:
			return status == 0;
		case WITH_GREATER_THAN_0:
			return status > 0;
		case EXACTLY:
			return status == exactSystemExitStatus;
		default:
			throw new IllegalStateException("this was not recognised!");
		}
	}

	/**
	 * @return If this is {@link #EXACTLY}, the exact status implied by the enum.
	 * @throws IllegalStateException
	 *             If {@code this} is not {@link #EXACTLY} or {@link #setExactStatus(int)} has not been called yet.
	 */
	public int getExactStatus() {
		if (this != EXACTLY) {
			throw new IllegalStateException("This method may only be called for the EXACTLY enum");
		}
		if (exactSystemExitStatus == -1) {
			throw new IllegalStateException("The exact status has not been set yet!");
		}
		return exactSystemExitStatus;
	}

	/**
	 * @param status
	 *            The status that is implied by {@link #EXACTLY}.
	 * @return {@code this}
	 * @throws IllegalStateException
	 *             If {@code this} is not {@link #EXACTLY}.
	 * @throws IllegalArgumentException
	 *             If {@code status < 0}.
	 */
	public SystemExitStatus status(int status) {
		if (this != EXACTLY) {
			throw new IllegalStateException("This method may only be called for the EXACTLY enum");
		}
		if (status < 0) {
			throw new IllegalArgumentException("There is no system exit status lower than 0!");
		}
		this.exactSystemExitStatus = status;
		return this;
	}
}
