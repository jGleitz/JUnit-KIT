package test.framework.mocking;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

/**
 * File manager to handle the compilation results from {@link MockCompiler}.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 04.02.2015
 */
public class MockCompilerFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private Map<String, MockerJavaClassFile> mockJavaClassFiles = new HashMap<>();

    protected MockCompilerFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling)
            throws IOException {
        JavaFileObject result;
        if (kind == Kind.CLASS) {
            MockerJavaClassFile mockFile = new MockerJavaClassFile(className);
            result = mockFile;
            mockJavaClassFiles.put(className, mockFile);
        } else {
            result = super.getJavaFileForOutput(location, className, kind, sibling);
        }
        return result;
    }

    /**
     * Retrieves and removes the compilation of {@code mockName} from this file manager.
     * 
     * @param mockName
     *            the full qualified name of the compiled class
     * @return the {@link MockerJavaClassFile}
     */
    public MockerJavaClassFile pollCompiledMocker(String mockName) {
        return mockJavaClassFiles.remove(mockName);
    }

}
