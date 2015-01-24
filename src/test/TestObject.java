package test;

/*
 * If Checkstyle complains about this file, just set up Checkstyle to ignore the whole JUnit Test package. You can do
 * that in your project settings.
 */

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Permission;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.junit.Assert;

/**
 * Gives you the ability to test a class you don't know the implementation of. We will from now on be referencing this
 * class as the "tested class". <br>
 * This class encapsulates the tested class. Use the constructors of {@code TestObject} to get instances of the tested
 * class. You can use the {@link #run run} and {@link #runStatic runStatic} methods to run methods on the instance.<br>
 * To point a test to the implementation it should test, set a JVM variable {@code className} to the name of the tested
 * class via the {@code -D} command. Example: {@code "-DclassName=de.joshuagleitze.tuple.NaturalNumberTuple"}<br>
 * <br>
 * <h4>Calling the constructors and {@code run}...-methods</h4> In the following, we will talk about "run methods". By
 * this term, the {@link #TestObject(Object...) constructors}, {@link #runStatic(Class, String, Object...) runStatic},
 * {@link #runStaticVoid(String, Object...) runStatic}, {@link #run(Class, String, Object...) run},
 * {@link #runVoid(String, Object...) runVoid} are meant. <h5>formal arguments</h5> Which method of the implemented
 * class to run will be decided based on the {@code formalArguments} parameter. But most of the time, you don't have
 * explicitly specify the type of the arguments. If you call a run method without the {@code formalArguments} parameter,
 * the formal parameters of the method will be guessed based on the classes of the {@code arguments} parameter. If there
 * is no method whose formal arguments match {@code formalArguments} or the guessed argument types,
 * {@link Assert#fail()} will be called to output an error message.
 * <p>
 * 90% of the time, you don't have to specify argument types. There are some special cases though:
 * <p>
 * <span class="strong">Primitive wrapper classes</span> <br>
 * Instances of primitive wrapper classes will be guessed as primitive types! For example, to call <br>
 * <code>public static void foo(Integer x)</code>,<br>
 * you'll have to specify the argument types, e.g. like this:<br>
 * <code>TestObject.runStaticVoid("foo", new Class<?>[]{Integer.getClass()}, new Integer(3))</code>
 * <p>
 * <span class="strong">Instances of {@code TestObject}</span><br>
 * will always be converted into the instance of the tested class. This applies even if they are nested in an array.
 * <p>
 * <span class="strong">Inheritance</span><br>
 * If you want to call a method in the tested class but specify an argument that is a subclass of the method's formal
 * argument, the subclass will be guessed an most likely an error message will occur, as the there is no such method in
 * the implemented class. For example, to call <br>
 * {@code public static void foo(Number x)} with {@code 3},<br>
 * you'll have to specify the argument types, e.g. like this:<br>
 * <code>TestObject.runStaticVoid("foo", new Class<?>[]{ Number.getClass()}, 3)</code>.
 * <p>
 * <span class="strong">Passing an array</span><br>
 * Because of the run method's signatures, you have to convert an array to {@code Object} if you pass it as as the only
 * argument. For example, calling a main method may look like this:<br>
 * <code>TestObject.runStaticVoid("main", (Object) new String[]{"arg1", "arg2"})</code>
 * <h5>return type</h5>
 * Run methods ending with "Void" can only be used to call void methods. If there is a method in the test class that
 * matches the arguments you provided, but it is not a void method, {@link Assert#fail()} will be called to output an
 * error message.
 * <p>
 * For the other run methods, you specify the expected return type through {@code expectedReturnType}. If a method of
 * the tested class matches the arguments provided but does not return an instance of {@code expectedReturnType},
 * {@link Assert#fail()} will be called to output an error message.
 * <p>
 * <span class="strong">Conversion</span><br>
 * A returned object that is an instance of the tested class will be converted to an instance of {@code TestObject} that
 * represent it. This applies even if it is nested in an array.
 * <h5>error messages</h5>
 * The run methods will output error messages via {@link Assert#fail()} in the following cases:
 * <ul>
 * <li>There is no method matching the formal parameters defined in {@code formalArguments} or implied by
 * {@code arguments}, respectively.
 * <li>Such a method exits, but it's not accessible for {@code TestObject} (<=> not {@code public}).
 * <li>An exception that was not set to be rethrown with {@link #rethrowExceptions(Class...)} is thrown while running
 * the method.
 * <li>The method is not a {@code void} method or does not return an instance of the class specified in
 * {@code expectedReturnType}, respectively.
 * <li>Some other strange cases that should not occur.
 * </ul>
 * <h5>exception handling</h5>
 * As mentioned above, normally {@link Assert#fail()} is called to output an error message if an exception is thrown
 * while running a method. You can change that behaviour by calling {@link #rethrowExceptions(Class...)}. If the
 * exception thrown in the tested class is an instance of the specified classes, it is wrapped in a
 * {@link TestMethodException} which is then thrown. For example, if we would like to test that a method throws a
 * {@code FooException}, we would do the following:
 * 
 * <pre>
 * <code>
 * TestObject.rethrowException(FooException);
 * try {
 *      TestObject.runStaticVoid("theMethod");
 *      fail("You are expected to throw a FooException!");
 * } catch (TestMethodException e) {
 *      if (e.getCause() instanceof FooException) {
 *          // everything went well
 *      }
 * }
 * </code>
 * </pre>
 * 
 * <h5>static fields</h5>
 * If you test methods, it might happen that the implementation you test uses static fields. Especially when you test a
 * {@code main} method, you usually want to the test the class in the state it is when the program is started.
 * Otherwise, consecutive calls of {@code main} may result in different results! So whenever you test a {@code main}
 * method or something that shall only be called once per program run, make sure to use {@link #resetClass()}:
 * 
 * <pre>
 * <code>
 * TestObject.resetClass(); // Always reset the class before running main!
 * TestObject.runStaticVoid("main");
 * </pre>
 * 
 * </code>
 * 
 * @author Joshua Gleitze
 * @version 2.1
 */
public class TestObject {
    private static boolean allowSystemExit0 = false;
    private static boolean allowSystemExitGreater0 = false;
    private static final String className = System.getProperty("className");
    private static Class<?> clazz = new TestClassLoader().loadClass();
    private static String nextCallInput;
    private static Stack<String> programOutput = new Stack<String>();
    private static List<Class<? extends Exception>> rethrowExceptions = new LinkedList<Class<? extends Exception>>();
    private final Object instance;

    /**
     * Constructs an {@code TestObject} that represents an instance of the tested class. The constructor of the tested
     * class whose formal arguments match {@code argumentTypes} will be selected to construct this instance.
     * 
     * @param formalArguments
     *            The classes of the test class' constructor's formal arguments. The constructor will be selected based
     *            on the classes you provide.
     * @param arguments
     *            The arguments to be passed to the constructor.
     */
    public TestObject(Class<?>[] formalArguments, Object... arguments) {
        this.instance = run(clazz, "", formalArguments, arguments, null, true);
    }

    /**
     * Constructs an {@code TestObject} that represents an instance of the tested class. The constructor of the tested
     * class whose formal arguments match the classes of {@code arguments} will be selected to construct this instance.
     * 
     * @param arguments
     *            The arguments to be passed to the constructor of the tested class in the correct order. The
     *            constructor will be selected based on their types.
     */
    public TestObject(Object... arguments) {
        this(getTypeArray(arguments), arguments);
    }

    /**
     * Constructs a {@code TestObject} to represent {@code injectedInstance}.
     * 
     * @param injectInstance
     *            set this to whatever you want. Only used to distinguish from other constructors.
     * @param injectedInstance
     *            The instance the constructed {@code TestObject} will represent.
     */
    private TestObject(boolean injectInstance, Object injectedInstance) {
        this.instance = injectedInstance;
    }

    /**
     * Calling this method sets the policy for the tested class to call {@link System#exit(int)}. Setting this will
     * apply throughout the test, unless you call this method again. The default is that the tested class is not allowed
     * to call {@link System#exit} in any way (this status can be achieved by calling
     * {@code allowSystemExit(SystemExitStatus.NONE)} . {@link org.junit.Assert#fail} will be called if the tested class
     * tries to call {@code System.exit} with a status it is not allowed to.
     * 
     * @param status
     *            The status you want to allow calling {@link System#exit} with.
     */
    public static void allowSystemExit(SystemExitStatus status) {
        switch (status) {
        case ALL:
            allowSystemExit0 = true;
            allowSystemExitGreater0 = true;
            break;
        case WITH_0:
            allowSystemExit0 = true;
            allowSystemExitGreater0 = false;
            break;
        case WITH_GREATER_THAN_0:
            allowSystemExit0 = false;
            allowSystemExitGreater0 = true;
            break;
        case NONE:
            allowSystemExit0 = false;
            allowSystemExitGreater0 = false;
            break;
        default:
            break;
        }
    }

    /**
     * Gets the output to System.out of the last run method.
     * 
     * @return What the method that was last run using {@code TestObject} printed to System.out
     */
    public static String getLastMethodOutput() {
        return programOutput.peek();
    }

    /**
     * Gets the package the tested class is in.
     * 
     * @return The name of the package the tested class is in. If the tested class has no package (= is in the default
     *         package {@code null} is returned.
     */
    public static String getPackageName() {
        if (clazz.getPackage() == null) {
            return null;
        }
        return clazz.getPackage().getName();
    }

    /**
     * Returns the simple name of the tested class.
     * 
     * @return What {@code object.class.getSimpleName()} returns if {@code object} is an instance of the tested class.
     */
    public static String getSimpleName() {
        return clazz.getSimpleName();
    }

    /**
     * Returns whether the tested class implements a method called {@code methodName} that takes multiple arguments of
     * the types specified in {@code argumentTypes}.
     * 
     * @param methodName
     *            The name of the method you want to check for.
     * @param formalArguments
     *            The types of the formal arguments.
     * @return True if such a method is present in the tested class.
     */
    public static boolean hasMethod(String methodName, Class<?>... formalArguments) {
        Method m;
        try {
            m = clazz.getDeclaredMethod(methodName, formalArguments);
        } catch (NoSuchMethodException | SecurityException e) {
            return false;
        }
        return (m != null);
    }

    /**
     * Returns a String that represents a call of a method.
     * 
     * @param methodName
     *            The method's name
     * @param parameters
     *            parameters that were used to call the method.
     * @param isConstructor
     *            Whether the method call was a call to the constructor. If set to {@code true}, {@code methodName} will
     *            be ignored and the tested class' name will be used to render the method call.
     * @return A String of the form "method(object1, object2, ...)"
     */
    public static String renderMethodCall(String methodName, Object[] parameters, boolean isConstructor) {
        String result = "";
        if (isConstructor) {
            result += clazz.getSimpleName();
        } else {
            result += methodName;
        }
        result += "(";
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getClass().isArray()) {
                result += Arrays.deepToString((Object[]) parameters[i]);
            } else {
                result += parameters[i].toString();
            }
            result += (i < parameters.length - 1) ? ", " : "";
        }
        result += ")";
        return result;
    }

    /**
     * Returns a String that represents the formal declaration of a method.
     * 
     * @param methodName
     *            The method's name
     * @param formalArguments
     *            the classes of the method's formal parameters.
     * @param isStatic
     *            Whether the method called was static.
     * @param isConstructor
     *            Whether the method call was a call to the constructor. If set to {@code true}, {@code methodName} and
     *            {@code isStatic} will be ignored and the class name will be used to render the method call.
     * @return A String of the form "constructor/method/static method method(class1, class2, ...)"
     */
    public static String renderMethodFormal(String methodName, Class<?>[] formalArguments, boolean isStatic,
            boolean isConstructor) {
        String result = "";
        if (isConstructor) {
            result += "constructor " + clazz.getSimpleName();
        } else {
            result += (isStatic) ? "static method " : "method ";
            result += methodName;
        }
        result += "(";
        if (formalArguments == null) {
            result += "null";
        } else {
            for (int i = 0; i < formalArguments.length; i++) {
                result += formalArguments[i].getSimpleName();
                result += (i < formalArguments.length - 1) ? ", " : "";
            }
        }
        result += ")";
        return result;
    }

    /**
     * Resets the tested class. Resetting the tested class means that it will be in the state it was when the program
     * was started. Especially all static fields will be reset to their initial values.
     */
    public static void resetClass() {
        clazz = new TestClassLoader().loadClass();
    }

    /**
     * Sets which Exceptions will be rethrown (wrapped in a {@link TestMethodException}) instead of outputting an error
     * message. If an exception occurs while running a run method, {@code Assert#fail()} is normally called to output an
     * error message. But if you expect the test class to throw an exception, it can instead be rethrown. If an
     * exception that is an instance of a class in {@code exceptionClasses} occurs while running a tested method, it
     * will be wrapped in a {@link TestMethodException} which will then be thrown. The setting applies until
     * {@code rethrowExceptions} is called again. To rethrow all exceptions, call
     * {@code rethrowExceptions(Exception.class)}. To always output an error message (which is the default), run
     * {@code rethrowExceptions()} or {@code rethrowExceptions(null)}.
     * 
     * @param exceptionClasses
     *            If an exception occurs that is an instance of an exception class in {@code exceptionClasses}, it will
     *            be rethrown.
     */
    @SafeVarargs
    // We don't rely on exceptionClasses really just consisting of classes that extend Exception. We just want to make
    // clear that anything different than a class extending Exception would not make sense (but will not lead to an
    // ClassCastException)
    public static void rethrowExceptions(Class<? extends Exception>... exceptionClasses) {
        rethrowExceptions = new LinkedList<Class<? extends Exception>>();
        for (Class<? extends Exception> exceptionClass : exceptionClasses) {
            if (exceptionClass != null) {
                rethrowExceptions.add(exceptionClass);
            }
        }
    }

    /**
     * Runs a static method on the tested class. The method named {@code methodName} taking the formal arguments
     * {@code formalArguments} will be selected.
     * 
     * @param <T>
     *            The return type. (Set trough {@code expectedReturnType}).
     * @param expectedReturnType
     *            {@code Class<T>} what you expect the method to return.
     * @param methodName
     *            The name of the static method you want to call.
     * @param formalArguments
     *            The method's formal argument's classes. By them, the method will be selected.
     * @param arguments
     *            The arguments you want to pass to the method.
     * @return What the method returns.
     */
    public static <T> T runStatic(Class<T> expectedReturnType, String methodName, Class<?>[] formalArguments,
            Object... arguments) {
        return run(expectedReturnType, methodName, formalArguments, arguments, null, false);
    }

    /**
     * Runs a static method on the tested class. The method named {@code methodName} taking the classes of
     * {@code arguments} as formal arguments will be selected.
     * 
     * @param <T>
     *            The return type. (Set trough {@code expectedReturnType}).
     * @param expectedReturnType
     *            {@code Class<T>} what you expect the method to return.
     * @param methodName
     *            The name of the static method you want to call.
     * @param arguments
     *            The arguments you want to pass to the method.
     * @return What the method returns.
     */
    public static <T> T runStatic(Class<T> expectedReturnType, String methodName, Object... arguments) {
        return runStatic(expectedReturnType, methodName, getTypeArray(arguments), arguments);
    }

    /**
     * Runs a static void method on the tested class. The method named {@code methodName} taking the formal arguments
     * {@code formalArguments} will be selected.
     * 
     * @param methodName
     *            The name of the static void method you want to call.
     * @param formalArguments
     *            The method's formal argument's classes. By them, the method will be selected.
     * @param arguments
     *            The arguments you want to pass to the method.
     */
    public static void runStaticVoid(String methodName, Class<?>[] formalArguments, Object... arguments) {
        run(null, methodName, formalArguments, arguments, null, false);
    }

    /**
     * Runs a static void method on the tested class. The method named {@code methodName} taking the classes of
     * {@code arguments} as formal arguments will be selected.
     * 
     * @param methodName
     *            The name of the static void method you want to call.
     * @param arguments
     *            The arguments you want to pass to the method.
     */
    public static void runStaticVoid(String methodName, Object... arguments) {
        runStaticVoid(methodName, getTypeArray(arguments), arguments);
    }

    /**
     * Provides input for the next method call. The String you provide will be accessible for the tested program through
     * the {@code Terminal} class. <br>
     * <br>
     * NOTE: While {@code TestObject} is independent, the functionality provided by this very method relies on the
     * Terminal class introduced for the programming lecture at the KIT!<br>
     * {@link Assert#fail} will be called to output an error if this method is used by a test but the implementation of
     * the tested class lacks the {@code Terminal} class.
     * 
     * @param input
     *            the input that should be provided through the {@code Terminal} class on the next method call.
     */
    public static void setNextMethodCallInput(String input) {
        nextCallInput = input;
    }

    /**
     * Calls {@link #translateToTestObject(Object)} on an Array of objects.
     * 
     * @param objects
     *            the objects to convert
     * @return A copy of {@code objects} where any occurrence of an instance of the tested class has been converted to
     *         an instance of {@code TestObject} that represents it.
     */
    public static Object[] translateAllToTestObject(Object[] objects) {
        if (objects == null) {
            return objects;
        }
        Object[] args = objects.clone();
        for (int i = 0; i < args.length; i++) {
            args[i] = translateToTestObject(args[i]);
        }
        return args;
    }

    /**
     * Converts an instance of the tested class into an instance of {@code TestObject} that represents it.
     * 
     * @param object
     *            an object to convert
     * @return If {@code object}'s class is the tested class, an instance of {@code TestObject} that represents is.
     *         {@code object} is returned without modification otherwise.
     */
    public static Object translateToTestObject(Object object) {
        if (object == null) {
            return object;
        }
        if (object.getClass().isArray()) {
            return translateAllToTestObject((Object[]) object);
        }
        if (object.getClass() == clazz) {
            return new TestObject(true, object);
        }
        return object;
    }

    /**
     * Returns an Array containing the classes of the given Objects. For instances of primitive wrapper classes, the
     * class of the primitive type will be used, not the primitive wrapper class. (E.g. for both {@code 3} and
     * {@code new Integer(3)}, {@code int.class} will be used instead of {@code Integer.class}). For instances of
     * {@code TestObject}, the tested class will be used.
     * 
     * @param arguments
     *            The Objects you want to get the classes of.
     * @return The classes of {@code arguments}
     */
    private static Class<?>[] getTypeArray(Object[] arguments) {
        if (arguments == null) {
            return null;
        }
        Class<?>[] parameterTypes = new Class[arguments.length];
        Class<?> clazz;
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i] == null) {
                parameterTypes[i] = Object.class;
            } else {
                clazz = arguments[i].getClass();
                if (clazz == Integer.class) {
                    parameterTypes[i] = int.class;
                } else if (clazz == Long.class) {
                    parameterTypes[i] = long.class;
                } else if (clazz == Float.class) {
                    parameterTypes[i] = float.class;
                } else if (clazz == Double.class) {
                    parameterTypes[i] = double.class;
                } else {
                    parameterTypes[i] = translateClassToImplemented(clazz);
                }
            }
        }
        return parameterTypes;
    }

    /**
     * Check if a class is a primitve type wrapper of a primitive type.
     * 
     * @param wrapper
     *            the wrapper class to check
     * @param primitiveClass
     *            the primitive type class to check against
     * @return {@code true} if {@code wrapper} is an instance of the wrapper class of {@code primitiveClass}
     */
    private static boolean isWrapperOf(Object wrapper, Class<?> primitiveClass) {
        if (primitiveClass == byte.class) {
            return (wrapper instanceof Byte);
        } else if (primitiveClass == short.class) {
            return (wrapper instanceof Short);
        } else if (primitiveClass == int.class) {
            return (wrapper instanceof Integer);
        } else if (primitiveClass == long.class) {
            return (wrapper instanceof Long);
        } else if (primitiveClass == float.class) {
            return (wrapper instanceof Float);
        } else if (primitiveClass == double.class) {
            return (wrapper instanceof Double);
        } else if (primitiveClass == boolean.class) {
            return (wrapper instanceof Boolean);
        } else if (primitiveClass == char.class) {
            return (wrapper instanceof Character);
        }
        return false;
    }

    /**
     * Runs a method on the instance.
     * 
     * @param expectedReturnType
     *            The class of the return type you expect. {@link Assert#fail()} will be called if the method returns
     *            something that is not an instance of {@code expectedReturnType}
     * @param methodName
     *            The name of the method you want to call.
     * @param formalArguments
     *            The method's formal argument's classes. By them, the method will be selected.
     * @param arguments
     *            The arguments you want to pass to the method in the correct order.
     * @param inst
     *            The instance you want the method to run on. {@code null} if you want to call a static method.
     * @return What the method returns. {@code null} if it was a void method.
     */
    @SuppressWarnings(value = "unchecked")
    private static <T> T run(Class<T> expectedReturnType, String methodName, Class<?>[] formalArguments,
            Object[] arguments, Object inst, boolean callConstructor) {
        Object result = null;
        Object[] args = translateAllToImplemented(arguments);
        Class<?>[] types = translateAllClassesToImplemented(formalArguments);
        NoExitSecurityManager.setup(); // prevent System.exit()
        if (nextCallInput != null) {
            Console.setTerminalInput(nextCallInput); // provide input in the Terminal class
        }
        Console.observeSystemOut(); // observe the Sysout
        try {
            if (callConstructor) {
                Constructor<?> cunstructor = clazz.getDeclaredConstructor(types);
                result = cunstructor.newInstance(args);
            } else {
                Method method = clazz.getDeclaredMethod(methodName, types);
                result = method.invoke(inst, args);
                result = translateToTestObject(result);
            }
        } catch (NoSuchMethodException e) {
            String message = "There obviously is no "
                    + renderMethodFormal(methodName, formalArguments, (inst == null), callConstructor)
                    + ", in your class while there should be one.\n";
            fail(message);
        } catch (NullPointerException e) {
            String message = "The " + renderMethodFormal(methodName, formalArguments, false, callConstructor)
                    + " is expected to be static!";
            fail(message);
        } catch (SecurityException e) {
            fail("SecurityException: '" + e.getMessage() + "'\n\n" + e.getStackTrace());
        } catch (IllegalAccessException e) {
            String message = "The " + renderMethodFormal(methodName, formalArguments, (inst == null), callConstructor)
                    + " is not accessible! Correct its visibility!";
            fail(message);
        } catch (IllegalArgumentException e) {
            String message = "The passed arguments, '";
            Class<?>[] passedTypes = getTypeArray(arguments);
            for (int i = 0; i < arguments.length; i++) {
                message += "(" + passedTypes[i].getName() + ") ";
                message += arguments[i];
                message += (i < arguments.length - 1) ? ", " : "";
            }
            message += "' don't match the formal arguments of the "
                    + renderMethodFormal(methodName, formalArguments, (inst == null), callConstructor)
                    + ". Most likely, this test contains an error which causes this. "
                    + "You wouldn't try to find and fix it, would you?";
            fail(message);
        } catch (InvocationTargetException e) {
            StringWriter stackTraceStringWriter = new StringWriter(); // will hold the printed stack trace of the actual
                                                                      // error.
            if (e.getCause() instanceof ExitException) {
                ExitException exitException = (ExitException) e.getCause();
                exitException.printStackTrace(new PrintWriter(stackTraceStringWriter));
                if (!((allowSystemExit0 && exitException.status == 0) || (allowSystemExitGreater0 && exitException.status > 0))) {
                    String message = "While calling " + renderMethodCall(methodName, arguments, callConstructor)
                            + ", your code called System.exit(" + exitException.status
                            + "). This was not expected and is an error: \n\n" + stackTraceStringWriter.toString();
                    fail(message);
                }
            } else {
                for (Class<?> exceptionClass : rethrowExceptions) {
                    if (exceptionClass.isInstance(e.getCause())) {
                        throw new TestMethodException(e.getCause());
                    }
                }
                e.getCause().printStackTrace(new PrintWriter(stackTraceStringWriter));
                String message = "An Exception occurred while running "
                        + renderMethodCall(methodName, arguments, callConstructor) + ": \n\n"
                        + stackTraceStringWriter.toString();
                fail(message);
            }
        } catch (InstantiationException e) {
            String message = clazz.getName() + " could not be instantiated. This are the exception details: \n\n"
                    + e.getMessage() + "\n\n" + e.getStackTrace();
            fail(message);
        } finally {
            NoExitSecurityManager.reset();
            programOutput.add(Console.getAll());
            Console.resetSystemOut();
            if (nextCallInput != null) {
                Console.resetTerminalInput();
                nextCallInput = null;
            }
        }

        if (expectedReturnType == null) {
            if (result != null) {
                fail("The " + renderMethodFormal(methodName, formalArguments, (inst == null), callConstructor)
                        + " is expected to be a void method!");
            }
            return null;
        }
        if (result != null && !expectedReturnType.isInstance(result) && !isWrapperOf(result, expectedReturnType)) {
            fail("The " + renderMethodFormal(methodName, formalArguments, (inst == null), callConstructor)
                    + " is expected to return a " + expectedReturnType.getSimpleName() + " but instead returned a "
                    + result.getClass().getSimpleName() + ".\n");
        }
        return (T) result;
    }

    /**
     * Calls {@link #translateClassToImplemented(Class)} on an Array of classes.
     * 
     * @param classes
     *            the classes to convert
     * @return A copy of {@code classes} where all occurrences of {@code TestObject.class} have been converted into the
     *         tested class.
     */
    private static Class<?>[] translateAllClassesToImplemented(Class<?>[] classes) {
        if (classes == null) {
            return null;
        }
        Class<?>[] cs = classes.clone();
        for (int i = 0; i < cs.length; i++) {
            cs[i] = translateClassToImplemented(cs[i]);
        }
        return cs;
    }

    /**
     * Calls {@link #translateToImplemented(Object)} on an Array of objects.
     * 
     * @param objects
     *            the objects to convert
     * @return A copy of {@code objects} where any occurrence of an instance of {@code TestObject} has been converted
     *         into the instance of the tested class that was represented by it.
     */
    private static Object[] translateAllToImplemented(Object[] objects) {
        if (objects == null) {
            return null;
        }
        Object[] args = objects.clone();
        for (int i = 0; i < args.length; i++) {
            args[i] = translateToImplemented(args[i]);
        }
        return args;
    }

    /**
     * Converts {@code TestObject.class} into the tested class.
     * 
     * @param c
     *            a class to convert
     * @return If {@code c} is {@code TestObject.class}, the tested class will be returned. Otherwise, {@code clazz} is
     *         returned without modification.
     */
    private static Class<?> translateClassToImplemented(Class<?> c) {
        if (c == TestObject.class) {
            return clazz;
        } else {
            return c;
        }
    }

    /**
     * Converts an instance of {@code TestObject} to the instance of the tested class represented by it.
     * 
     * @param object
     *            An object to convert.
     * @return If {@code object}'s class is {@code TestObject.class}, the instance of the tested class {@code object}
     *         represents will be returned. Otherwise, {@code object} is returned without modification.
     */
    private static Object translateToImplemented(Object object) {
        if (object == null) {
            return object;
        }
        if (object instanceof Object[]) {
            return translateAllToImplemented((Object[]) object);
        } else if (object.getClass() == TestObject.class) {
            return ((TestObject) object).instance;
        } else {
            return object;
        }
    }

    /**
     * Compares if the instances of the tested class represented by {@code this} and {@code otherTestObject} are equal.
     * 
     * @param otherTestObject
     *            an instance of {@code TestObject} you want to check.
     * @return if the instance of the tested class represented by {@code this} and the instance of the tested class
     *         represented by {@code otherTestObject} are equal in terms of their {@code equals} method.
     */
    @Override
    public boolean equals(Object otherTestObject) {
        if (!(otherTestObject instanceof TestObject)) {
            return false;
        }
        return (this.instance.equals(((TestObject) otherTestObject).instance));
    }

    /**
     * Returns the hashCode of the instance of the tested class represented by {@code this}.
     * 
     * @return What {@code hashCode} of the instance of the tested class represented by {@code this} returns.
     */
    @Override
    public int hashCode() {
        return this.instance.hashCode();
    }

    /**
     * Runs a method on the instance of tested class represented by {@code this}. The method named {@code methodName}
     * taking the formal arguments {@code formalArguments} will be selected.
     * 
     * @param <T>
     *            The return type. (Set trough {@code expectedReturnType}).
     * @param expectedReturnType
     *            {@code Class<T>} what you expect the method to return.
     * @param methodName
     *            The name of the method you want to call.
     * @param formalArguments
     *            The method's formal argument's classes. By them, the method will be selected.
     * @param arguments
     *            The arguments you want to pass to the method.
     * @return What the method returns.
     */
    public <T> T run(Class<T> expectedReturnType, String methodName, Class<?>[] formalArguments, Object... arguments) {
        return run(expectedReturnType, methodName, formalArguments, arguments, this.instance, false);
    }

    /**
     * Runs a method on the instance of tested class represented by {@code this}. The method named {@code methodName}
     * taking the classes of {@code arguments} as formal arguments will be selected.
     * 
     * @param <T>
     *            The return type. (Set trough {@code expectedReturnType}).
     * @param expectedReturnType
     *            {@code Class<T>} what you expect the method to return.
     * @param methodName
     *            The name of the static method you want to call.
     * @param arguments
     *            The arguments you want to pass to the method.
     * @return What the method returns.
     */
    public <T> T run(Class<T> expectedReturnType, String methodName, Object... arguments) {
        return this.run(expectedReturnType, methodName, getTypeArray(arguments), arguments);
    }

    /**
     * Runs a void method on the instance of tested class represented by {@code this}. The method named
     * {@code methodName} taking the formal arguments {@code formalArguments} will be selected.
     * 
     * @param methodName
     *            The name of the static void method you want to call.
     * @param formalArguments
     *            The method's formal argument's classes. By them, the method will be selected.
     * @param arguments
     *            The arguments you want to pass to the method.
     */
    public void runVoid(String methodName, Class<?>[] formalArguments, Object... arguments) {
        run(null, methodName, formalArguments, arguments, this.instance, false);
    }

    /**
     * Runs a void method on the instance of tested class represented by {@code this}. The method named
     * {@code methodName} taking the classes of {@code arguments} as formal arguments will be selected.
     * 
     * @param methodName
     *            The name of the static void method you want to call.
     * @param arguments
     *            The arguments you want to pass to the method.
     */
    public void runVoid(String methodName, Object... arguments) {
        this.runVoid(methodName, getTypeArray(arguments), arguments);
    }

    /**
     * Represents a status {@link System#exit} can be called with.
     * 
     * @author Joshua Gleitze
     *
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

    /**
     * Provides methods to handle System.in and System.out. I decided to put this class into {@code TestObject} to have
     * everything at one spot. I know that there are some arguments that would strongly speak in favor of putting this
     * in ań own class. Nevertheless, {@code TestObject} is made to make testing as easy as possible, and I'm convinced
     * that having {@code Console} nested in {@code TestObject} is easier to handle.
     * 
     * @author Joshua Gleitze
     *
     */
    private static class Console {
        private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        private static Class<?> terminalClass = null;
        private static Object terminalIn;
        private static boolean triedInformatikPackage = false;

        /**
         * Gets everything that what was written to the Sysout since observing was started in one String.
         * 
         * @return A String containing everything that was printed to Sysout since Observing via
         *         {@link #observeSystemOut()} was called.
         */
        private static String getAll() {
            return outContent.toString();
        }

        private static Class<?> loadTerminalClass() {
            return loadTerminalClass(getPackageName());
        }

        private static Class<?> loadTerminalClass(String packagePath) {
            Class<?> c = null;
            String clearPackagePath = packagePath;
            if (clearPackagePath.length() > 0 && clearPackagePath.lastIndexOf(".") == clearPackagePath.length() - 1) {
                clearPackagePath = clearPackagePath.substring(0, clearPackagePath.length() - 2);
            }
            String classPath = packagePath + ((packagePath != "") ? "." : "") + "Terminal";
            try {
                c = Class.forName(classPath);
            } catch (ClassNotFoundException e) {
                if (packagePath != "") {
                    String newPackagePath;
                    newPackagePath = (clearPackagePath.lastIndexOf(".") > 0) ? clearPackagePath.substring(0,
                            clearPackagePath.lastIndexOf(".")) : "";
                    c = loadTerminalClass(newPackagePath);
                } else if (!triedInformatikPackage) {
                    c = loadTerminalClass("edu.kit.informatik");
                } else {
                    String message = "";
                    message += "This test expects you to use the Terminal class. Nevertheless, we could not find it in the package path of the class you provided.";
                    fail(message);
                }
            }
            return c;
        }

        /**
         * Starts to observe the Sysout. Don't forget to use {@link #resetSystemOut()} to stop observing after you're
         * done!.
         */
        private static void observeSystemOut() {
            System.setOut(new PrintStream(outContent));
        }

        /**
         * Stops observing the Sysout after it was started with {@link #observeSystemOut()}.
         */
        private static void resetSystemOut() {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
            outContent.reset();
        }

        private static void resetTerminalInput() {
            setTerminalInField(null);
        }

        private static void setTerminalInField(Object value) {
            if (terminalClass == null) {
                terminalClass = loadTerminalClass();
            }
            try {
                Field in = terminalClass.getDeclaredField("in");
                boolean wasAccessible = in.isAccessible();
                in.setAccessible(true);
                if (value == null) {
                    in.set(null, terminalIn);
                } else {
                    terminalIn = in.get(null);
                    in.set(null, value);
                }
                in.setAccessible(wasAccessible);
            } catch (NoSuchFieldException e) {
                String message = "";
                message += "The implementation you have of the Terminal class does not have a field 'in'."
                        + " Please use the Terminal class that was provided for the programming lecture at the KIT!.";
                fail(message);
            } catch (SecurityException e) {
                fail("SecurityException while trying to access Terminal.in");
            } catch (IllegalArgumentException e) {
                fail("IllegalArgumentException while setting Terminal.in. Obviously, I can't code.");
            } catch (IllegalAccessException e) {
                fail("IllegalAccessException while setting Terminal.in. Obviously, I can't code.");
            }
        }

        private static void setTerminalInput(String input) {
            if (input != null) {
                BufferedReader newIn = new BufferedReader(new StringReader(input));
                setTerminalInField(newIn);
            }
        }
    }

    /**
     * This exception is thrown if the tested method called {@link System#exit}. It is then handled to output a error
     * message for that.
     * 
     * @author Joshua Gleitze
     *
     */
    private static class ExitException extends SecurityException {
        private static final long serialVersionUID = -5649621212556394572L;
        public final int status;

        public ExitException(int status) {
            super("The method called System.exit(" + status + ")");
            this.status = status;
        }
    }

    /**
     * This {@link SecurityManager} is used to catch when the code is trying to call {@link System#exit}. This would
     * leave the test in a hanging state. Therefore, we handle it.
     * 
     * @author Joshua Gleitze
     *
     */
    private static class NoExitSecurityManager extends SecurityManager {
        private static SecurityManager systemSecurityManager;

        private static void reset() {
            System.setSecurityManager(systemSecurityManager);
        }

        private static void setup() {
            systemSecurityManager = System.getSecurityManager();
            System.setSecurityManager(new NoExitSecurityManager());
        }

        @Override
        public void checkExit(int status) {
            super.checkExit(status);
            Class<?>[] classContext = this.getClassContext();
            for (Class<?> c : classContext) {
                if (c == clazz) {
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
    }

    /**
     * Loads the tested class. A new {@code TestClassLoader} will always return a fresh class. Therefore, a class can be
     * reset by constructing a new {@code TestClassLoader} and running {@link TestClassLoader#loadClass()}.
     * 
     * @author Joshua Gleitze
     * @version 1.0
     * @since 24.01.2015
     *
     */
    private static class TestClassLoader extends ClassLoader {
        private final static byte[] classData = getClassData();

        private TestClassLoader() {
            super();
        }

        /**
         * Reads in the class file.
         * 
         * @return The tested class' file contents.
         */
        private static byte[] getClassData() {
            byte[] result = null;

            if (className == null || className.length() == 0) {
                fail("\nYou have not provided a class name! So what should we test on?\n"
                        + "Please specify a class in the JVM-parameter via -DclassName=.\n"
                        + "Do not forget to state the correct package!\n"
                        + "Example: '-DclassName=joshuagleitze.tuple.NaturalNumberTuple'\n\n"
                        + "For help to set up the tests, see "
                        + "https://github.com/jGleitz/JUnit-KIT/wiki/Using-the-tests-in-Eclipse\n");
            }
            // get this class loader to get the resource path from it.
            ClassLoader loader = TestObject.class.getClassLoader();
            String classFileName = className.replace(".", System.getProperty("file.separator")) + ".class";
            // get the path of the tested class
            URL url = loader.getResource(classFileName);

            if (url == null) {
                fail("\nThere obviously is no class '" + className + "'! So what should we test on?\n"
                        + "Please specify a class in the JVM-parameter via -DclassName=.\n"
                        + "Do not forget to state the correct package!\n"
                        + "Example: '-DclassName=joshuagleitze.tuple.NaturalNumberTuple'\n\n"
                        + "For help to set up the tests, see "
                        + "https://github.com/jGleitz/JUnit-KIT/wiki/Using-the-tests-in-Eclipse\n");
            }

            // read in the tested class
            InputStream input = null;
            try {
                URLConnection connection = url.openConnection();
                input = connection.getInputStream();
            } catch (MalformedURLException e) {
                fail("We are unable to correctly locate your class file. This is a fatal error. Please contact the team!");
            } catch (IOException e) {
                fail("An input/output error occurred while trying to read in your class file. This is a fatal error! "
                        + "Please contact the team!");
            }
            try {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int data = input.read();
                while (data != -1) {
                    buffer.write(data);
                    data = input.read();
                }
                result = buffer.toByteArray();
            } catch (IOException e) {
                fail("An input/output error occurred while trying to read in your class file. This is a fatal error! "
                        + "Please contact the team!");
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    fail("An input/error exception occurred while trying to close the reader that read in your class file. Honestly, "
                            + "I have no idea how this might have happened. Please contact the team!");
                }
            }
            return result;
        }

        /**
         * Returns the tested class. A new {@code TestClassLoader} will always return a "fresh" class!
         * 
         * @return The tested class
         */
        private Class<?> loadClass() {
            return defineClass(className, classData, 0, classData.length);
        }

    }

}
