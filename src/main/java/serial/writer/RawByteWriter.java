package serial.writer;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class RawByteWriter implements DataWriter {

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream os;

    public RawByteWriter() {
        try {
            os = new ObjectOutputStream(bos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] toByteArray() {
        return bos.toByteArray();
    }

    @Override
    public void writeInt(int value) {
        try {
            os.writeInt(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
