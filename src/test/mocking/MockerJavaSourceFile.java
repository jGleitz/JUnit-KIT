package test.mocking;

import java.net.URI;
import java.util.Objects;

import javax.tools.SimpleJavaFileObject;

/**
 * Java source code meant to mock another class.
 *
 * @author Joshua Gleitze
 * @version 1.0
 * @since 04.02.2015
 */
public class MockerJavaSourceFile extends SimpleJavaFileObject {
    private String code;
    private String name;

    /**
     * Constructs a mocker source code file to mock the class named {@code mockedName} with the class defined in
     * {@code code}. Make sure that the package and class name stated in the source code match the ones in
     * {@code mockedName}!
     *
     * @param mockedName
     *            The name of the class to be mocked.
     * @param code
     *            The source code to mock the class with.
     */
    public MockerJavaSourceFile(String mockedName, String code) {
        super(URI.create("string:///" + mockedName.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
        name = mockedName;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if ((obj != null) && (getClass() == obj.getClass())) {
            MockerJavaSourceFile sf = (MockerJavaSourceFile) obj;
            return (getName().equals(sf.getName()) && getCharContent(true).equals(sf.getCharContent(true)));
        }
        return false;
    }
}
