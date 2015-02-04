package test.framework.mocking;

import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * Helper class to compile a {@link MockerJavaSourceFile}.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 04.02.2015
 */
public abstract class MockCompiler {
    private static final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
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
        compiler.getTask(null, fileManager, null, null, null, Arrays.asList(sourceFile)).call();
        return fileManager.pollCompiledMocker(sourceFile.getName());
    }

}
