package test.test;

public class TestClass {
	private static int x = 0;

	public static int staticAddition() {
		return ++x;
	}

	public static int delegatedStaticAddition() {
		return AnotherTestClass.staticAddition();
	}

	public void throwNestedException() {
		try {
			throw new NonPublicException();
		} catch (NonPublicException e) {
		}
	}

	private class NonPublicException extends Exception {
		private static final long serialVersionUID = 1L;
	}

}
