package test.mocking;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * A very simple wrapper for an {@link ByteArrayOutputStream} that is serialisable. Once the stream is closed, its bytes
 * are saved. For serialisation, only those bytes will be saved and restored.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 * @since 10.02.2015
 */
public class SerializableByteArrayOutputStream extends OutputStream implements Serializable {
    private static final long serialVersionUID = 1L;
    private byte[] bytes;
    private transient ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void close() throws IOException {
        bytes = outputStream.toByteArray();
    }

    /**
     * Gets the bytes written to the stream.
     * 
     * @return the bytes that were written into this stream
     * @throws IllegalStateException
     *             if this method is called before the stream was closed.
     */
    public byte[] toByteArray() {
        if (bytes == null) {
            throw new IllegalStateException("Bytes cannot be retrieved before the stream is closed!");
        }
        return bytes;
    }

}
