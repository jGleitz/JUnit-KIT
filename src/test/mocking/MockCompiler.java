package test.mocking;

import java.io.File;
import java.io.FileFilter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

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
    private static final JavaCompiler compiler = getJavaCompiler();
    private static final MockCompilerFileManager fileManager = new MockCompilerFileManager(
            compiler.getStandardFileManager(null, null, null));

    /**
     * Compiles the source code provided in {@code sourceFile}. Any compilation error will by printed to
     * {@code System.err}.
     * 
     * @param sourceFile
     *            The {@link MockerJavaSourceFile} to compile
     * @return the {@link MockerJavaClassFile} containing the compiled java byte code
     */
    public static MockerJavaClassFile compile(MockerJavaSourceFile sourceFile) {
        Writer errorWriter = new StringWriter();
        boolean compilationSuccessful = compiler.getTask(errorWriter, fileManager, null, null, null,
            Arrays.asList(sourceFile)).call();
        if (!compilationSuccessful) {
            throw new FrameworkException("Unable to compile " + sourceFile.getName() + ":\n\n" + errorWriter.toString());
        }
        return fileManager.pollCompiledMocker(sourceFile.getName());
    }

    private static JavaCompiler getJavaCompiler() {
        JavaCompiler comp = ToolProvider.getSystemJavaCompiler();
        if (comp == null) {
            boolean isWindows = System.getProperty("os.name").startsWith("Windows");
            if (isWindows) {
                File javaProgramFolder = new File("C:\\Program Files\\Java");
                if (!javaProgramFolder.exists()) {
                    throw new FrameworkException("Your system does not provide a Java Compiler. It's Windows but we "
                            + "can't find your Java installation!");
                }
                FileFilter jdkFilter = new FileFilter() {
                    final int javaVersion;
                    {
                        String javaVersionString = System.getProperty("java.version");
                        javaVersion = Integer.parseInt(javaVersionString.substring(2, 3));
                    }
                    
                    @Override
                    public boolean accept(File file) {
                        if (file.isDirectory() && file.getName().startsWith("jdk1.")) {
                            int sdkVersion = Integer.parseInt(file.getName().substring(5, 6));
                            return (sdkVersion <= javaVersion);
                        }
                        return false;
                    }
                };
                File[] jdkFolders = javaProgramFolder.listFiles(jdkFilter);
                for (File f : jdkFolders) {
                    System.setProperty("java.home", f.getAbsolutePath());
                    comp = ToolProvider.getSystemJavaCompiler();
                    if (comp != null) {
                        break;
                    }
                }
                if (comp == null && jdkFolders.length == 0) {
                    throw new FrameworkException("Your system does not provide a Java Compiler. It's Windows, but "
                            + "we can't find a JDK!");
                } else if (comp == null) {
                    throw new FrameworkException("Your system does not provide a Java Compiler. It's Windows and "
                            + "we did find " + jdkFolders.length + " JDKs. Still, no compiler!");
                }
            } else {
                throw new FrameworkException("Your System does not provide a Java Compiler and is not Windows – "
                        + "we're all out of ideas!");
            }
        }
        return comp;
    }
}
