package test.mocking;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;

import javax.tools.SimpleJavaFileObject;

/**
 * Compiled java byte code suitable to mock the class names {@link #getName()}.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 04.02.2015
 */
public class MockerJavaClassFile extends SimpleJavaFileObject {
    private String name;
    private ByteArrayOutputStream outputStream;

    /**
     * Constructs a new mocker class file for the given class name. The compiler can write its results in it.
     * 
     * @param mockedName
     *            the class name of the mocked class
     */
    MockerJavaClassFile(String mockedName) {
        super(URI.create("string:///" + mockedName.replace('.', '/') + Kind.CLASS.extension), Kind.CLASS);
        this.name = mockedName;
    }

    /**
     * The compiled java byte code of the mocker class.
     */
    public byte[] getByteCode() {
        return outputStream.toByteArray();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        outputStream = new ByteArrayOutputStream();
        return outputStream;
    }

    @Override
    public boolean equals(Object object) {
        return (object instanceof MockerJavaClassFile && ((MockerJavaClassFile) object).getByteCode().equals(
            this.getByteCode()));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.getByteCode());
    }
}
