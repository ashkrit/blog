package serial.reader;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class RawByteDataReader implements DataReader {

    private ObjectInputStream is;

    public RawByteDataReader(byte[] buffer) throws IOException {
        ByteArrayInputStream bos = new ByteArrayInputStream(buffer);
        is = new ObjectInputStream(bos);
    }

    @Override
    public int readInt() throws IOException {
        return is.readInt();
    }
}
