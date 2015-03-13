package test.framework;

import java.security.Permission;

import test.SystemExitStatus;

/**
 * Used to catch when the code is trying to call {@link System#exit}. This would leave a test in a hanging state. A
 * {@link ExitException} is thrown if the code tries to call {@link System#exit}.
 *
 * @author Joshua Gleitze
 *
 */
public class NoExitSecurityManager extends SecurityManager {
	private Class<?> targetClass;
	private SystemExitStatus lastExit = SystemExitStatus.NONE;

	/**
	 * Constructs a {@code NoExitSecurityManager}
	 *
	 * @param targetClass
	 *            The class that should be watched for calling {@code System.exit}
	 */
	public NoExitSecurityManager(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	@Override
	public void checkExit(int status) {
		super.checkExit(status);
		if (status > 0) {
			lastExit = SystemExitStatus.WITH_GREATER_THAN_0;
		} else if (status == 0) {
			lastExit = SystemExitStatus.WITH_0;
		}
		Class<?>[] classContext = getClassContext();
		for (Class<?> c : classContext) {
			if (c == targetClass) {
				throw new ExitException(status);
			}
		}
	}

	@Override
	public void checkPermission(Permission perm) {
		// allow anything.
	}

	@Override
	public void checkPermission(Permission perm, Object context) {
		// allow anything.
	}

	/**
	 * Sets the last exit status to {@link SystemExitStatus#NONE}.
	 */
	public void resetLastExitStatus() {
		lastExit = SystemExitStatus.NONE;
	}

	/**
	 * @return {@link SystemExitStatus#NONE} if no class using this security manager called {@code System.exit(x)} since
	 *         its creation or the last call to {@link NoExitSecurityManager#resetLastExitStatus()}.
	 *         {@link SystemExitStatus#WITH_0} or {@link SystemExitStatus#WITH_GREATER_THAN_0} if the last call to
	 *         {@code System.exit(x)} of a class using this security manager was with {@code x=0} or {@code x>0},
	 *         respectively.
	 */
	public SystemExitStatus lastExitStatus() {
		return lastExit;
	}
}