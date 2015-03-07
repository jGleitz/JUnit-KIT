package test.mocking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

/**
 * Compiled java byte code suitable to mock the class names {@link #getName()}.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 04.02.2015
 */
public class MockerJavaClassFile implements Serializable, JavaFileObject {
    private static final long serialVersionUID = 1L;
    private String name;
    private SerializableByteArrayOutputStream outputStream = new SerializableByteArrayOutputStream();
    int sourceHash;

    /**
     * Constructs a new mocker class file for the given class name. The compiler can write its results in it.
     * 
     * @param mockedName
     *            the class name of the mocked class
     */
    MockerJavaClassFile(String mockedName) {
        this.name = mockedName;
    }

    /**
     * Provides the source file this class file was compiled from. It is crucial to call this method after this class
     * file has been compiled!
     * 
     * @param sourceFile
     *            the source file this class file was compiled from.
     */
    void setSourceFile(MockerJavaSourceFile sourceFile) {
        this.sourceHash = sourceFile.hashCode();
    }

    /**
     * Whether this class file contains the compilation of {@code sourceFile}.
     * 
     * @param sourceFile
     *            the source file to check against
     * @return {@code true} if, and only if, this class file contains the compilation for exactly (same name, same code)
     *         the provided source File.
     */
    public boolean isFromSourceFile(MockerJavaSourceFile sourceFile) {
        return (sourceFile.hashCode() == sourceHash);
    }

    /**
     * The compiled java byte code of the mocker class.
     */
    public byte[] getByteCode() {
        return this.outputStream.toByteArray();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return this.outputStream;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, Arrays.hashCode(this.getByteCode()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            MockerJavaClassFile cf = (MockerJavaClassFile) obj;
            return (this.getName().equals(cf.getName()) && Arrays.equals(this.getByteCode(), cf.getByteCode()));
        }
        return false;
    }

    @Override
    public URI toUri() {
        return URI.create("string:///" + this.name.replace('.', '/') + Kind.CLASS.extension);
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return null;
    }

    @Override
    public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
        return null;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return null;
    }

    @Override
    public Writer openWriter() throws IOException {
        return null;
    }

    @Override
    public long getLastModified() {
        return 0;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public Kind getKind() {
        return Kind.CLASS;
    }

    @Override
    public boolean isNameCompatible(String simpleName, Kind kind) {
        return false;
    }

    @Override
    public NestingKind getNestingKind() {
        return null;
    }

    @Override
    public Modifier getAccessLevel() {
        return null;
    }
}
