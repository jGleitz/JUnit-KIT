package test.framework;

import java.lang.reflect.InvocationTargetException;

import test.mocking.MockCompiler;
import test.mocking.MockerJavaClassFile;
import test.mocking.MockerJavaSourceFile;

/**
 * Connector to the Terminal class provided by the programming lecture. It collects everything the tested class prints
 * through {@code Terminal.printLine()}. Plus, you can provide input that the tested class can read in through
 * {@code Terminal.readLine()}.
 *
 * @author Joshua Gleitze
 * @version 1.2
 */
public class Terminal {
	private static final String DEFINED_CLASS_NAME = "edu.kit.informatik.Terminal";
	private static final Class<?>[] STRING_ARRAY_TYPE = new Class<?>[] {
		String[].class
	};

	// @formatter:off
	private static final String[] TERMINAL_MOCKER_SOURCE = new String[] {
			"package edu.kit.informatik;",

			"import java.util.ArrayList;",
			"import java.util.Iterator;",
			"import java.util.List;",

			"public final class Terminal {",
				"private static List<List<String>> printLists;",
				"private static List<String> globalPrintList;",
				"private static List<String> readList = new ArrayList<>();",
				"private static int commandCounter = 0;",
				
				"static {",
					"resetOutput();",
				"}",

				"public static void printLine(String out) {",
					"printLists.get(commandCounter).add(out);",
					"globalPrintList.add(out);",
				"}",

				"public static String readLine() {",
					"String result = \"\";",
					"if (commandCounter >= readList.size()) {",
						"throw new RuntimeException(\"The tested class tried to get more input than there actually was!\\n\" + ",
						"\"Most likely, it was expected to terminate!\");",
					"}",
					"result = readList.get(commandCounter);",
					"commandCounter++;",
					"return result;",
				"}",

				"public static void resetOutput() {",
					"printLists = new ArrayList<List<String>>();",
					"printLists.add(new ArrayList<String>());",
					"globalPrintList = new ArrayList<String>();",
				"}",

				"public static String[] getAllOutput() {",
					"return globalPrintList.toArray(new String[globalPrintList.size()]);",
				"}",

				"public static void appendInput(String[] input) {",
					"for (String i : input) {",
						"readList.add(i);",
						"printLists.add(new ArrayList<String>());",
					"}",
				"}",
					
				"public static String[][] getOutputPerCommand() {",
					"String[][] result = new String[printLists.size()][];",
					"for (int i = 0; i < printLists.size(); i++) {",
						"result[i] = printLists.get(i).toArray(new String[printLists.get(i).size()]);",
					"}",
					"return result;",
				"}",

				"public static void setInput(String[] input) {",
					"readList = new ArrayList<String>();",
					"appendInput(input);",
				"}",
				
				"public static String[] getInput() {",
					"return readList.toArray(new String[readList.size()]);",
				"}",

			"}"
	};
	// @formatter:on
	private final Class<?> terminalMocker;

	static {
		MockerJavaSourceFile terminalSourceFile = new MockerJavaSourceFile(DEFINED_CLASS_NAME,
				joinSource(TERMINAL_MOCKER_SOURCE));
		MockerJavaClassFile teminalClassFile = MockCompiler.compile(terminalSourceFile);
		TestClassLoader.mock(teminalClassFile);
	}

	/**
	 * Creates a new Terminal handler. It will observe everything that will be printed by the tested class trough
	 * {@code Terminal.printLine()}. Plus, you can provide input to the tested class trough it.
	 *
	 * @param classLoader
	 *            The class loader to retrieve Terminal class from. It is crucial that this is the same class loader as
	 *            the one
	 */
	public Terminal(ClassLoader classLoader) {
		terminalMocker = loadMockerUnchecked(classLoader);
	}

	private static String joinSource(String[] sourceCode) {
		StringBuilder resultBuilder = new StringBuilder();
		for (String line : sourceCode) {
			resultBuilder.append(line);
			resultBuilder.append("\n");
		}
		return resultBuilder.toString();
	}

	private static Object[] wrap(Object[] o) {
		return new Object[] {
			o
		};
	}

	/**
	 * Returns everything that has been written by the tested class through {@code Terminal.printLine} since the
	 * creation of this Terminal or since the last call to {@link #reset()}.
	 *
	 * @return the tested class' output
	 */
	public String[] getOutput() {
		return invokeUnchecked(String[].class, Method.GET_OUTPUT, null);
	}

	/**
	 * Returns all input provided for the tested class through {@link #provideInput(String[])}.
	 * 
	 * @return the tested class' input.
	 */
	public String[] getInput() {
		return invokeUnchecked(String[].class, Method.GET_INPUT, null);
	}

	/**
	 * Returns everything that has been written by the tested class through {@code Terminal.printLine} since the
	 * creation of this terminal, grouped per <i>input</i> command. This means: If {@code n} items have been added to
	 * the Terminal's input through {@link #provideInput(String[])} since the last call to {@link #reset()}, this
	 * method's returned array will consist of {@code n+1} String arrays. The first of these arrays will consist of the
	 * output printed <i>before</i> the tested class read the first command. From there, the {@code i}th String array
	 * will consist of the output printed after the tested class read the {@code i}th command. Each String in the String
	 * arrays corresponds to one call to {@code Terminal.printLine} of the tested class.
	 * 
	 * @return The grouped output, as described above.
	 */
	public String[][] getOutputPerCommand() {
		return invokeUnchecked(String[][].class, Method.GET_OUTPUT_PER_COMMAND, null);
	}

	/**
	 * Provides the given input through the Terminal class. One element of the Array will be served per call to
	 * {@code Terminal.readLine()}. Execution will be blocked once no more elements.
	 *
	 * @param input
	 *            what {@code Terminal.printLine} should return per call.
	 */
	public void provideInput(String[] input) {
		invokeUnchecked(Void.class, Method.SET_INPUT, STRING_ARRAY_TYPE, wrap(input));
	}

	/**
	 * Resets the Terminal class. All captured output will be discarded and the input Queue will be cleared.
	 */
	public void reset() {
		invokeUnchecked(Void.class, Method.RESET_OUTPUT, null);
		invokeUnchecked(Void.class, Method.SET_INPUT, STRING_ARRAY_TYPE, wrap(new String[0]));
	}

	private <T> T invokeUnchecked(Class<T> returnType, Method method, Class<?>[] parameterTypes, Object... parameters) {
		Object returned;
		T result = null;
		try {
			returned = terminalMocker.getMethod(method.toString(), parameterTypes).invoke(null, parameters);
			if (returnType != Void.class) {
				result = returnType.cast(returned);
			}
		} catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
			throw new FrameworkException(method + " cannot be invoked on the terminal mocker class!\n"
					+ e.getClass().getSimpleName() + ": " + e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}

	private Class<?> loadMockerUnchecked(ClassLoader classLoader) {
		try {
			return classLoader.loadClass(DEFINED_CLASS_NAME);
		} catch (ClassNotFoundException e) {
			throw new FrameworkException("The terminal mocker class cannot be loaded!");
		}
	}

	private static enum Method {
		APPEND_INPUT("appendInput"), GET_OUTPUT("getAllOutput"), GET_OUTPUT_PER_COMMAND("getOutputPerCommand"), RESET_OUTPUT(
				"resetOutput"), SET_INPUT("setInput"), GET_INPUT("getInput");

		private String name;

		private Method(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}
	}
}