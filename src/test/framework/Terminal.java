package test.framework;

import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;

import test.framework.mocking.MockCompiler;
import test.framework.mocking.MockerJavaClassFile;
import test.framework.mocking.MockerJavaSourceFile;

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
    private static final String TERMINAL_MOCKER_BYTE_CODE = 
            "package edu.kit.informatik;" +
    
            "import java.util.LinkedList;" +
            "import java.util.List;" +
            "import java.util.Arrays;" + 
            "import java.util.concurrent.BlockingQueue;" +
            "import java.util.concurrent.LinkedBlockingQueue;" +

            "public final class Terminal {" +
                "private static List<String> printList = new LinkedList<>();" +
                "private static BlockingQueue<String> readList = new LinkedBlockingQueue<>();" +

                "public static void printLine(String out) {" +
                    "printList.add(out);" +
                "}" +

                "public static String readLine() {" + 
                "try {" +
                        "return readList.take();" +
                    "} catch (InterruptedException e) {" +
                        "throw new RuntimeException(\"the tested class got stuck because it tried to get " +
                            "more input from Terminal.readLine while there was no more available!\");" +
                    "}" +
                "}" +
                    
                "public static void resetOutput() {" +
                    "printList = new LinkedList<String>();" +
                "}" +
                    
                "public static String[] getAllOutput() {" +
                    "return printList.toArray(new String[printList.size()]);" +
                "}" +
                    
                "public static void appendInput(String[] input) {" +
                    "for (String i : input) {" +
                        "readList.offer(i);" +
                    "}" +
                "}" +
                    
                "public static void setInput(String[] input) {" +
                    "readList = new LinkedBlockingQueue<String>();" +
                    "appendInput(input);" +
                "}" +

            "}";
    // @formatter:on
    private final Class<?> terminalMocker;

    static {
        MockerJavaSourceFile terminalSourceFile = new MockerJavaSourceFile(DEFINED_CLASS_NAME,
                TERMINAL_MOCKER_BYTE_CODE);
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
        System.out.println(classLoader.getResource("test/framework/mocks/Terminal.txt"));
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
        String methodName = "";
        switch (method) {
        case APPEND_INPUT:
            methodName = "appendInput";
            break;
        case GET_OUTPUT:
            methodName = "getAllOutput";
            break;
        case RESET_OUTPUT:
            methodName = "resetOutput";
            break;
        case SET_INPUT:
            methodName = "setInput";
            break;
        default:
            throw new IllegalArgumentException("you have not provided a valid mocker method!");
        }
        Object returned;
        T result = null;
        try {
            returned = terminalMocker.getMethod(methodName, parameterTypes).invoke(null, parameters);
            if (returnType != Void.class) {
                result = returnType.cast(returned);
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
            throw new FrameworkException(methodName + " cannot be invoked on the terminal mocker class!\n"
                    + e.getClass().getSimpleName() + ": " + e.getMessage());
        } catch (InvocationTargetException e) {
            fail(e.getCause().getMessage());
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
        APPEND_INPUT, GET_OUTPUT, RESET_OUTPUT, SET_INPUT
    }
}