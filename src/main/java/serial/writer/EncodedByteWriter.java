package serial.writer;


import java.io.ByteArrayOutputStream;

public class EncodedByteWriter implements DataWriter {

    private ByteArrayOutputStream bos = new ByteArrayOutputStream();

    @Override
    public byte[] toByteArray() {
        return bos.toByteArray();
    }

    @Override
    public void writeInt(int n) {
        // move sign to low-order bit, and flip others if negative
        n = (n << 1) ^ (n >> 31);
        if ((n & ~0x7F) != 0) {
            bos.write((byte) ((n | 0x80) & 0xFF));
            n >>>= 7;
            if (n > 0x7F) {
                bos.write((byte) ((n | 0x80) & 0xFF));
                n >>>= 7;
                if (n > 0x7F) {
                    bos.write((byte) ((n | 0x80) & 0xFF));
                    n >>>= 7;
                    if (n > 0x7F) {
                        bos.write((byte) ((n | 0x80) & 0xFF));
                        n >>>= 7;
                    }
                }
            }
        }
        bos.write((byte) n);
    }

    @Override
    public void close() {

    }
}
