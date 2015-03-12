package test.mocking;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import test.framework.FrameworkException;

/**
 * Helper class to compile a {@link MockerJavaSourceFile}.
 *
 * @author Joshua Gleitze
 * @version 1.0
 * @since 04.02.2015
 */
public abstract class MockCompiler {
    private static final File cacheFile = new File(".serialization/mockCompilerCache.ser");
    private static Map<String, MockerJavaClassFile> cache = getCache();
    private static final JavaCompiler compiler = getJavaCompiler();
    private static final MockCompilerFileManager fileManager = new MockCompilerFileManager(
        compiler.getStandardFileManager(null, null, null));
    private static String OS = null;

    /**
     * Compiles the source code provided in {@code sourceFile}. The compiler will run silent. If an error occurs while
     * compiling, the compiler's error text will be provided with the exception.
     *
     * @param sourceFile
     *            The {@link MockerJavaSourceFile} to compile
     * @return the {@link MockerJavaClassFile} containing the compiled java byte code
     * @throws FrameworkException
     *             if an error occurs while compiling. The exception will contain the compiler's error text.
     */
    public static MockerJavaClassFile compile(MockerJavaSourceFile sourceFile) {
        // check if this version of the class is already cached
        if (!cache.containsKey(sourceFile.getName()) || !cache.get(sourceFile.getName()).isFromSourceFile(sourceFile)) {
            // will contain the compiler's error output
            final Writer errorWriter = new StringWriter();
            // creates the compilation task, runs it and returns the result
            final boolean compilationSuccessful = compiler.getTask(errorWriter, fileManager, null, null, null,
                Arrays.asList(sourceFile)).call();
            if (!compilationSuccessful) {
                throw new FrameworkException("Unable to compile " + sourceFile.getName() + ":\n\n"
                        + errorWriter.toString());
            }
            final MockerJavaClassFile compiled = fileManager.pollCompiledMocker(sourceFile.getName());
            if (compiled == null) {
                throw new FrameworkException("Unable to retrieved compiled '" + sourceFile.getName() + "'."
                        + "\nMake sure that the full qualified name of the MockerJavaSourceFile matches exactly the \n"
                        + "package and class name declaration in the code!");
            }
            compiled.setSourceFile(sourceFile);
            // write compilation result to cache
            writeCache(sourceFile, compiled);
        }
        // return from cache
        return cache.get(sourceFile.getName());
    }

    /**
     * Reads in the serialised cache and returns it, if present. Returns an empty map otherwise.
     *
     * @return The cache map for compilation results.
     */
    @SuppressWarnings("unchecked")
    private static Map<String, MockerJavaClassFile> getCache() {
        if (cacheFile.exists()) {
            FileInputStream fs = null;
            ObjectInputStream is = null;
            Map<String, MockerJavaClassFile> result;
            try {
                fs = new FileInputStream(cacheFile);
                is = new ObjectInputStream(fs);
                result = (HashMap<String, MockerJavaClassFile>) is.readObject();
                return result;
            } catch (final IOException e) {
                // stay silent - return new cache
            } catch (final ClassNotFoundException e) {
                // stay silent - return new cache
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (final IOException e) {
                        // stay silent
                    }
                }
            }
        }
        return new HashMap<String, MockerJavaClassFile>();
    }

    /**
     * Tries to get the system's Java Compiler.
     *
     * @return A java Compiler, if possible
     * @throws FrameworkException
     *             if there is no known way for us to get the system's compiler.
     */
    private static JavaCompiler getJavaCompiler() {
        JavaCompiler comp = ToolProvider.getSystemJavaCompiler();
        if (comp == null) {
            if (isWindows()) {
                final File javaProgramFolder = new File("C:\\Program Files\\Java");
                final File javaProgramFolder32bit = new File("C:\\Program Files (x86)\\Java");
                if (!javaProgramFolder.exists() && !javaProgramFolder32bit.exists()) {
                    throw new FrameworkException("Your system does not provide a Java Compiler. It's Windows but we "
                            + "can't find your Java installation!");
                }
                final FileFilter jdkFilter = new FileFilter() {
                    final int javaVersion;
                    {
                        final String javaVersionString = System.getProperty("java.version");
                        javaVersion = Integer.parseInt(javaVersionString.substring(2, 3));
                    }

                    @Override
                    public boolean accept(File file) {
                        if (file.isDirectory() && file.getName().startsWith("jdk1.")) {
                            final int sdkVersion = Integer.parseInt(file.getName().substring(5, 6));
                            return (sdkVersion <= javaVersion);
                        }
                        return false;
                    }
                };
                final List<File> jdkFolders = new LinkedList<>();
                if (javaProgramFolder.exists()) {
                    jdkFolders.addAll(Arrays.asList(javaProgramFolder.listFiles(jdkFilter)));
                }
                if (javaProgramFolder32bit.exists()) {
                    jdkFolders.addAll(Arrays.asList(javaProgramFolder32bit.listFiles(jdkFilter)));
                }
                jdkFolders.addAll(Arrays.asList(javaProgramFolder32bit.listFiles(jdkFilter)));
                for (final File f : jdkFolders) {
                    System.setProperty("java.home", f.getAbsolutePath());
                    comp = ToolProvider.getSystemJavaCompiler();
                    if (comp != null) {
                        break;
                    }
                }
                if ((comp == null) && (jdkFolders.size() == 0)) {
                    throw new FrameworkException("Your system does not provide a Java Compiler. It's Windows, but "
                            + "we can't find a JDK!");
                } else if (comp == null) {
                    throw new FrameworkException("Your system does not provide a Java Compiler. It's Windows and "
                            + "we did find " + jdkFolders.size() + " JDKs. Still, no compiler!");
                }
            } else {
                throw new FrameworkException("Your System does not provide a Java Compiler and is not Windows – "
                        + "we're all out of ideas!");
            }
        }
        return comp;
    }

    private static boolean isWindows() {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS.startsWith("Windows");
    }

    /**
     * Serialises the cache so it can be read in again on next run.
     *
     * @param sourceFile
     *            The source File that was compiled
     * @param classFile
     *            The compilation result
     * @throws FrameworkException
     *             if we are unable to write the cache to disk
     */
    private static void writeCache(MockerJavaSourceFile sourceFile, MockerJavaClassFile classFile) {
        cache.put(sourceFile.getName(), classFile);
        try {
            final File parentFile = cacheFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (isWindows()) {
                Runtime.getRuntime().exec("attrib -s -h -r " + parentFile.getAbsolutePath());
            }
            cacheFile.createNewFile();
        } catch (final IOException e1) {
            throw new FrameworkException("unable to create file " + cacheFile.getAbsolutePath()
                + " to write compiler cache in.");
        }
        FileOutputStream fs = null;
        ObjectOutputStream os = null;
        try {
            fs = new FileOutputStream(cacheFile);
            os = new ObjectOutputStream(fs);
            os.writeObject(cache);
        } catch (final IOException e) {
            throw new FrameworkException("error while writing compiler cache into " + cacheFile.getAbsolutePath() + ".");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (final IOException e) {
                    // stay silent
                }
            }
        }
    }
}
