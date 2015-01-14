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
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Permission;
import java.util.Arrays;
import java.util.Stack;

import org.junit.Assert;

/**
 * Gives you the ability to test a class you don't know the implementation of. We will from now on be referencing this
 * class as the "tested class". <br>
 * This class encapsulates the tested class. Use the constructors of {@code TestObject} to get instances of the tested
 * class. You can use the {@link #run} and {@link #runStatic} methods to run methods on the instance or the class. You
 * can access the "real" instance and the "real" tested class via {@link #getImplementedClass()} and
 * {@link #getImplementedClass()} , respectively. But it is strongly discouraged to work with the "real" class!<br>
 * To set the name of the tested class, set a JVM variable {@code className} to the name of the tested class via the
 * {@code -D} command. Example: {@code "-DclassName=de.joshuagleitze.tuple.NaturalNumberTuple"}
 * 
 * @author Joshua Gleitze
 * @version 1.3
 */
public class TestObject {
    private static boolean allowSystemExit0 = false;
    private static boolean allowSystemExitGreater0 = false;
    private static final Class<?> clazz;
    private static String nextCallInput;
    private static Stack<String> programOutput;
    private final Object instance;

    /**
     * Gets the tested class by the System property {@code className}. To set the name of the tested class, set a JVM variable
     * via the {@code -D} command. Example: {@code "-DclassName=de.joshuagleitze.tuple.NaturalNumberTuple"}.
     */
    static {
        String className = System.getProperty("className");
        Class<?> c = null;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            fail("There obviously is no class '" + className + "'! So what should we test on?\n"
                    + "Please specify a class in the JVM-parameter via -DclassName=.\n"
                    + "Do not forget to state the correct package!\n"
                    + "Example: '-DclassName=joshuagleitze.tuple.NaturalNumberTuple'");
        }
        clazz = c;
        programOutput = new Stack<String>();
    }

    /**
     * This uses the default constructor of the tested class.
     */
    public TestObject() {
        this(new Object[] {});
    }

    /**
     * This uses the constructor in the tested class that takes only one argument. If no such constructor can be found
     * or another exception occurs during instantiation, {@link org.junit.Assert#fail(String)} will be called and output
     * a detailed error message.
     * 
     * @param argument
     *            The argument to be passed to the constructor of the tested class.
     */
    public TestObject(Object argument) {
        this(new Object[] { argument });
    }

    /**
     * This uses the constructor in the tested class that has only one formal argument {@code argument} of the type
     * {@code argumentType}. If no such constructor can be found or another exception occurs during instantiation,
     * {@link org.junit.Assert#fail(String)} will be called and output a detailed error message.
     * 
     * @param argument
     * @param argumentType
     */
    public TestObject(Object argument, Class<?> argumentType) {
        this(new Object[] { argument }, new Class<?>[] { argumentType });
    }

    /**
     * This is for a constructor in the tested class that takes more than one argument. Each Object in {@code argument}
     * is used as one parameter for the constructor of the tested class. This constructor will try to automatically
     * select the right constructor of the tested class by the classes of the passed {@code arguments}. If no such
     * constructor can be found or another exception occurs during instantiation, {@link org.junit.Assert#fail(String)}
     * will be called and output a detailed error message.<br >
     * This constructor will assume for any Object that is an instance of a elementary data type class, that an
     * elementary data type was meant. <br>
     * If this constructor fails to find (or select the right) constructor of the tested class, use
     * {@link #TestObject(Object[], Class[])}!
     * 
     * @param arguments
     *            The arguments to be passed to the constructor of the tested class in the correct order.
     */
    public TestObject(Object[] arguments) {
        this(arguments, getTypeArray(arguments));
    }

    /**
     * This is the most generic constructor. If any other constructor fails to instantiate the tested class, use this
     * one. It will find the constructor of the tested class with the formal arguments you provide in
     * {@code argumentTypes} and call it with the arguments you provide in {@code arguments}. If no such constructor can
     * be found or another exception occurs during instantiation, {@link org.junit.Assert#fail(String)} will be called
     * and output a detailed error message. <br>
     * In most cases, it is not necessary to use this one. {@link #TestObject(Object[])} will do the job most of the
     * time.
     * 
     * @param arguments
     *            The arguments to be passed to the constructor.
     * @param argumentTypes
     *            The classes of the test class' constructor's formal arguments. The constructor will be selected based
     *            on the classes you provide.
     */
    public TestObject(Object[] arguments, Class<?>[] argumentTypes) {
        this.instance = run("", arguments, argumentTypes, null, true);
    }

    /**
     * Calling this method sets the policy for the implemented class to call {@link System#exit(int)}. Setting this will
     * apply throughout the test, unless you call this method again. The default is that the implemented class is not
     * allowed to call {@link System#exit} in any way (this status can be achieved by calling
     * {@code allowSystemExit(SystemExitStatus.NONE)}. {@link org.junit.Assert#fail} will be called if the implemented
     * class tries to call {@code System.exit} with a status it is not allowed to.
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
     * Get the name of the implemented class.
     * 
     * @return What object.class.getSimpleName() returns if object is an instance of the implemented class.
     */
    public static String getClassSimpleName() {
        return clazz.getSimpleName();
    }

    /**
     * You can use this to get a reference of the "real" implemented class. Most of the time, you won't want to this!
     * It's only meant for things like class comparison.
     * 
     * @return the tested class
     */
    public static final Class<?> getImplementedClass() {
        return clazz;
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
     * Gets the package the class is in.
     * 
     * @return The name of the package the class is in. If the class has no package (= is in the default packagem
     *         {@code null} is returned.
     */
    public static String getPackageName() {
        if (clazz.getPackage() == null) {
            return null;
        }
        return clazz.getPackage().getName();
    }

    /**
     * Returns an Array containing the classes of the given Objects. For instances of elementary data type classes, the
     * class of the elementary data type will be returned, not the class of its class representative. (E.g. for 3,
     * int.class will be returned instead of Integer.class).
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
                    parameterTypes[i] = clazz;
                }
            }
        }
        return parameterTypes;
    }

    /**
     * Returns whether the tested class implements a method called {@code methodName} that takes no arguments.
     * 
     * @param methodName
     *            The name of the method you want to check for.
     * @return True if such a method is present in the tested class.
     */
    public static boolean hasMethod(String methodName) {
        return hasMethod(methodName, new Class<?>[] {});
    }

    /**
     * Returns whether the tested class implements a method called {@code methodName} that takes only one argument of
     * the type {@code argumentType}.
     * 
     * @param methodName
     *            The name of the method you want to check for.
     * @param argumentType
     *            The type of the (only) formal argument.
     * @return True if such a method is present in the tested class.
     */
    public static boolean hasMethod(String methodName, Class<?> argumentType) {
        return hasMethod(methodName, new Class<?>[] { argumentType });
    }

    /**
     * Returns whether the tested class implements a method called {@code methodName} that takes multiple arguments of
     * the types specified in {@code argumentTypes}.
     * 
     * @param methodName
     *            The name of the method you want to check for.
     * @param argumentTypes
     *            The types of the formal arguments.
     * @return True if such a method is present in the tested class.
     */
    public static boolean hasMethod(String methodName, Class<?>[] argumentTypes) {
        Method m;
        try {
            m = clazz.getDeclaredMethod(methodName, argumentTypes);
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
     * @return A String of the form "method(object1, object2, ...)"
     */
    public static String renderMethodCall(String methodName, Object[] parameters, boolean constructorCalled) {
        String result = "";
        if (constructorCalled) {
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
     * @param formalParameters
     *            the classes of the method's formal parameters.
     * @return A String of the form "method(class1, class2, ...)"
     */
    public static String renderMethodFormal(String methodName, Class<?>[] formalParameters) {
        String result = methodName + "(";
        if (formalParameters == null) {
            result += "null";
        } else {
            for (int i = 0; i < formalParameters.length; i++) {
                result += formalParameters[i].getName();
                result += (i < formalParameters.length - 1) ? ", " : "";
            }
        }
        result += ")";
        return result;
    }

    /**
     * This is the actual run method that is used for {@link #run} and {@link #runStatic} and {@link #TestObject}. It is
     * not meant to be used from the outside. For documentation see {@link TestObject#run(String, Object[], Class[])} or
     * {@link #runStatic(String, Object[], Class[])}. This method only differs by {@code inst}. If {@code inst} is null,
     * a static method is searched in the class, if an instance is given, a instance method is used.
     * 
     * @param methodName
     *            The name of the method you want to call.
     * @param arguments
     *            The arguments you want to pass to the method in the correct order.
     * @param argumentTypes
     *            The method's formal argument's classes. By them, the method will be selected.
     * @param inst
     *            The instance you want the method to run on. {@code null} if you want to call a static method.
     * @return What the method returns. {@code null} if it was a void method.
     */
    private static Object run(String methodName, Object[] arguments, Class<?>[] argumentTypes, Object inst,
            boolean callConstructor) {
        Object result = null;
        Object[] args = translateObjectsToImplemented(arguments);
        Class<?>[] types = translateClassesToImplemented(argumentTypes);
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
            }
        } catch (NoSuchMethodException e) {
            String message = "There obviously is no " + ((inst == null) ? "static" : "") + " method "
                    + renderMethodFormal(methodName, argumentTypes) + ", while there should be one.";
            fail(message);
        } catch (SecurityException e) {
            fail("SecurityException: '" + e.getMessage() + "'\n\n" + e.getStackTrace());
        } catch (IllegalAccessException e) {
            String message = "The " + ((inst == null) ? "static" : "") + " method "
                    + renderMethodFormal(methodName, argumentTypes) + " is not accessible! Correct its visibility!";
            fail(message);
        } catch (IllegalArgumentException e) {
            String message = "The passed arguments, '";
            Class<?>[] passedTypes = getTypeArray(arguments);
            for (int i = 0; i < arguments.length; i++) {
                message += "(" + passedTypes[i].getName() + ") ";
                message += arguments[i];
                message += (i < arguments.length - 1) ? ", " : "";
            }
            message += "' don't match the formal arguments of the " + ((inst == null) ? "static" : "") + " method "
                    + renderMethodFormal(methodName, argumentTypes)
                    + ". Most likely, this test contains an error which causes this. "
                    + "You wouldn't try to find and fix it, would you?";
            fail(message);
        } catch (InvocationTargetException e) {
            StringWriter stackTraceStringWriter = new StringWriter(); // will hold the printed stack trace of the actual
            // error.
            if (e.getCause() instanceof ExitException) {
                ExitException exitException = (ExitException) e.getCause();
                exitException.printStackTrace(new PrintWriter(stackTraceStringWriter));
                if (allowSystemExit0 && exitException.status == 0) {
                    return result;
                }
                if (allowSystemExitGreater0 && exitException.status > 0) {
                    return result;
                }
                String message = "While calling " + renderMethodCall(methodName, arguments, callConstructor)
                        + ", your code called System.exit(" + exitException.status
                        + "). This was not expected and is an error: \n\n" + stackTraceStringWriter.toString();
                fail(message);
            }
            e.getCause().printStackTrace(new PrintWriter(stackTraceStringWriter));
            String message = "An Exception occurred while running "
                    + renderMethodCall(methodName, arguments, callConstructor) + ": \n\n"
                    + stackTraceStringWriter.toString();
            fail(message);
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
        return result;
    }

    /**
     * Runs a static method of the test class that takes no arguments. If there is no such static method named
     * {@code methodName}, {@link org.junit.Assert#fail(String)} will be called to output a detailed error message.
     * 
     * @param methodName
     *            The name of the static method you want to call.
     * @return What the method returns. {@code null} if it was a void method.
     */
    public static Object runStatic(String methodName) {
        return runStatic(methodName, new Object[] {});
    }

    /**
     * Runs a static method of the tested class that takes only one argument. If there is no static method named
     * {@code methodName} that has {@code object.class} as only formal argument or any exception occurs while trying to
     * run it, {@link org.junit.Assert#fail(String)} will be called to output a detailed error message.<br>
     * Any passed Object that is an instance of a elementary data type class will be interpreted as an elementary data
     * type. So you can't for example call a method that is declared as {@code foo(Integer i)} with this version of
     * {@code runStatic}. Because if you call {@code runStatic("foo", new Integer(3))}, it will search for a static
     * method foo(int) in your tested class. <br>
     * If this version of {@code runStatic} fails to find (or select the right} method, use
     * {@link #runStatic(String, Object, Class)}.
     * 
     * @param methodName
     *            The name of the static method you want to call.
     * @param argument
     *            The argument ({@code Object}) you want to pass to the method. The method will be selected based on the
     *            class of this.
     * @return What the method returns. {@code null} if it was a void method.
     */
    public static Object runStatic(String methodName, Object argument) {
        return runStatic(methodName, new Object[] { argument });
    }

    /**
     *
     * Runs a static method of the tested class that takes only one argument. If there is no static method named
     * {@code methodName} that has {@code argumentType} as only formal argument or any exception occurs while trying to
     * run it, {@link org.junit.Assert#fail(String)} will be called to output a detailed error message.<br>
     * Most of the time, you don't need to use this version of {@code runStatic}, as {@link #runStatic(String, Object)}
     * does the job in 98% of all cases.
     * 
     * @param methodName
     *            The name of the static method you want to call.
     * @param argument
     *            The argument you want to pass to the method.
     * @param argumentType
     *            The type of {@code argument}. The method will be selected based on this.
     * @return What the method returns. {@code null} if it was a void method.
     */
    public static Object runStatic(String methodName, Object argument, Class<?> argumentType) {
        return runStatic(methodName, new Object[] { argument }, new Class<?>[] { argumentType });
    }

    /**
     * Runs a static method of the tested class that takes multiple arguments. It will select the method based the
     * classes of the {@code arguments}. If there is no static method that is named {@code methodName} and takes the
     * classes of {@code arguments} as formal arguments, {@link org.junit.Assert#fail(String)} will be called to output
     * a detailed error message. <br>
     * 
     * Any passed Object that is an instance of a elementary data type class will be interpreted as an elementary data
     * type. So you can't for example call a method that is declared as {@code foo(Integer i, Long l)} with this version
     * of {@code runStatic}. Because if you call <code>runStatic("foo", new Object[]{new Integer(3), new
     * Long(2L)})</code>, it will search for a static method {@code foo(int, long)} in your tested class. <br>
     * If this method fails to find the right method in the tested class, use
     * {@link #runStatic(String, Object[], Class[])}!
     * 
     * @param methodName
     *            The name of the static method you want to call.
     * @param arguments
     *            The arguments you want to pass to the method in the correct order.
     * @return What the method returns. {@code null} if it was a void method.
     */
    public static Object runStatic(String methodName, Object[] arguments) {
        return runStatic(methodName, arguments, getTypeArray(arguments));
    }

    /**
     * The most generic way to call a static method of the tested class. Runs a static method that takes multiple
     * arguments. It will search a method with the formal parameters provided in {@code argumentTypes} named
     * {@code methodName}. If there is no such method, {@link org.junit.Assert#fail(String)} will be called to output a
     * detailed error message. <br>
     * Usually, it should not be necessary to use this method. {@link #runStatic(String, Object[])} will do the job most
     * of the time!
     * 
     * @param methodName
     *            The name of the static method you want to call.
     * @param arguments
     *            The arguments you want to pass to the method in the correct order.
     * @param argumentTypes
     *            The method's formal argument's classes. By them, the method will be selected.
     * @return What the method returns. {@code null} if it was a void method.
     */
    public static Object runStatic(String methodName, Object[] arguments, Class<?>[] argumentTypes) {
        return run(methodName, arguments, argumentTypes, null, false);
    }

    /**
     * Provides input for the next method call. The String you provide will be accessible for the tested program through
     * the Terminal class. <br>
     * <br>
     * NOTE: While this whole class is independent, the functionality provided by this very method relies on the
     * Terminal class introduced for the programming lecture at the KIT!<br>
     * {@link Assert#fail} will be called to output an error if this method is used by a test but the implementation
     * lacks the Terminal class.
     * 
     * @param input
     */
    public static void setNextMethodCallInput(String input) {
        nextCallInput = input;
    }

    /**
     * Calls {@link #translateClassToImplemented(Class)} on an Array of classes.
     * 
     * @param classes
     *            The classes you want to have translated.
     * @return A new array containing the result of {@link #translateClassToImplemented(Class)} for every element of
     *         {@code classes}
     */
    private static Class<?>[] translateClassesToImplemented(Class<?>[] classes) {
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
     * Used for getting the implemented class were needed.
     * 
     * @param c
     *            A class to check.
     * @return If {@code clazz} is {@code TestObject.class}, the tested class will be returned. Otherwise, {@code clazz}
     *         is returned without modification.
     */
    private static Class<?> translateClassToImplemented(Class<?> c) {
        if (c == TestObject.class) {
            return clazz;
        } else {
            return c;
        }
    }

    /**
     * Calls {@link #translateObjectToImplemented(Object)} on an Array of objects.
     * 
     * @param objects
     *            The objects you want to have translated.
     * @return A new array containing the result of {@link #translateObjectToImplemented(Object)} for every element of
     *         {@code objects}
     */
    private static Object[] translateObjectsToImplemented(Object[] objects) {
        if (objects == null) {
            return null;
        }
        Object[] args = objects.clone();
        for (int i = 0; i < args.length; i++) {
            args[i] = translateObjectToImplemented(args[i]);
        }
        return args;
    }

    /**
     * Used for getting the instance of the tested class were needed.
     * 
     * @param object
     *            An object to check.
     * @return If {@code object}'s class is {@code TestObject.class}, the instance of the tested class {@code object}
     *         contains will be returned. Otherwise, {@code object} is returned without modification.
     */
    private static Object translateObjectToImplemented(Object object) {
        if (object != null && object.getClass() == TestObject.class) {
            return ((TestObject) object).getImplementedInstance();
        } else {
            return object;
        }
    }

    /**
     * You can use this to get a reference to the actual instance of the tested class. Most of the times, you won't want
     * to do this! It is only meant for comparison of instances and such things.
     * 
     * @return the "real" instance of the tested class.
     */
    public final Object getImplementedInstance() {
        return this.instance;
    }

    /**
     * Runs a method that takes no arguments. If there is no such method that is named {@code methodName},
     * {@link org.junit.Assert#fail(String)} will be called to output a detailed error message.
     * 
     * @param methodName
     *            The name of the method you want to call.
     * @return What the method returns. {@code null} if it was a void method.
     */
    public Object run(String methodName) {
        return this.run(methodName, new Object[] {});
    }

    /**
     * Runs a method that takes only one argument. If there is no method named {@code methodName} that has
     * {@code object.class} as only formal argument or any exception occurs while trying to run it,
     * {@link org.junit.Assert#fail(String)} will be called to output a detailed error message.<br>
     * Any passed Object that is an instance of a elementary data type class will be interpreted as an elementary data
     * type. So you can't for example call a method that is declared as {@code foo(Integer i)} with this version of
     * {@code run}. Because if you call {@code run("foo", new Integer(3))}, it will search for a Method foo(int) in your
     * tested class. <br>
     * If this version of {@code run} fails to find (or select the right} method, use
     * {@link #run(String, Object, Class)}.
     * 
     * @param methodName
     *            The name of the method you want to call.
     * @param argument
     *            The argument ({@code Object}) you want to pass to the method. The method will be selected based on the
     *            class of this.
     * @return What the method returns. {@code null} if it was a void method.
     */
    public Object run(String methodName, Object argument) {
        return this.run(methodName, new Object[] { argument });
    }

    /**
     *
     * Runs a method that takes only one argument. If there is no method named {@code methodName} that has
     * {@code argumentType} as only formal argument or any exception occurs while trying to run it,
     * {@link org.junit.Assert#fail(String)} will be called to output a detailed error message.<br>
     * Most of the time, you don't need to use this version of {@code run}, as {@link #run(String, Object)} does the job
     * in 98% of all cases.
     * 
     * @param methodName
     *            The name of the method you want to call.
     * @param argument
     *            The argument you want to pass to the method.
     * @param argumentType
     *            The type of {@code argument}. The method will be selected based on this.
     * @return What the method returns. {@code null} if it was a void method.
     */
    public Object run(String methodName, Object argument, Class<?> argumentType) {
        return this.run(methodName, new Object[] { argument }, new Class<?>[] { argumentType });
    }

    /**
     * Runs a method that takes multiple arguments. It will select the method based the classes of the {@code arguments}
     * . If there is no method that is named {@code methodName} and takes the classes of {@code arguments} as formal
     * arguments, {@link org.junit.Assert#fail(String)} will be called to output a detailed error message. <br>
     * 
     * Any passed Object that is an instance of a elementary data type class will be interpreted as an elementary data
     * type. So you can't for example call a method that is declared as {@code foo(Integer i, Long l)} with this version
     * of {@code run}. Because if you call <code>run("foo", new Object[]{new Integer(3), new
     * Long(2L)})</code>, it will search for a Method {@code foo(int, long)} in your tested class. <br>
     * If this method fails to find the right method in the tested class, use {@link #run(String, Object[], Class[])}!
     * 
     * @param methodName
     *            The name of the method you want to call.
     * @param arguments
     *            The arguments you want to pass to the method in the correct order.
     * @return What the method returns. {@code null} if it was a void method.
     */
    public Object run(String methodName, Object[] arguments) {
        return this.run(methodName, arguments, getTypeArray(arguments));
    }

    /**
     * The most generic way to call a method. Runs a method that takes multiple arguments. It will search a method with
     * the formal parameters provided in {@code argumentTypes} named {@code methodName}. If there is no such method,
     * {@link org.junit.Assert#fail(String)} will be called to output a detailed error message. <br>
     * Usually, it should not be necessary to use this method. {@link #run(String, Object[])} will do the job most of
     * the time!
     * 
     * @param methodName
     *            The name of the method you want to call.
     * @param arguments
     *            The arguments you want to pass to the method in the correct order.
     * @param argumentTypes
     *            The method's formal argument's classes. By them, the method will be selected.
     * @return What the method returns. {@code null} if it was a void method.
     */
    public Object run(String methodName, Object[] arguments, Class<?>[] argumentTypes) {
        return run(methodName, arguments, argumentTypes, this.instance, false);
    }

    /**
     * Provides methods to handle System.in and System.out. I decided to put this class into {@code TestObject} to have
     * everything at one spot. I know that there are some arguments that wouold strongly speak in favor of putting this
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
         * Gets everything that what was written to the Sysout since observing was started in one String.
         * 
         * @return A String containing everything that was printed to Sysout since Observing via
         *         {@link #observeSystemOut()} was called.
         */
        private static String getAll() {
            return outContent.toString();
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

        }

        @Override
        public void checkPermission(Permission perm, Object context) {
            // allow anything.
        }
    }

    /**
     * Represents a status {@link System#exit} can be called with. ALL stands for anything, WITH_0 stands for
     * {@code System.exit(0)} and WITH_GREATER_THAN_0 stands for System.exit(x) where x>0.
     * 
     * @author Joshua Gleitze
     *
     */
    public enum SystemExitStatus {
        ALL, WITH_0, WITH_GREATER_THAN_0, NONE
    }

}
