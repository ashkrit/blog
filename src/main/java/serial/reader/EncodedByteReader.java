package serial.reader;


import java.io.ByteArrayInputStream;

public class EncodedByteReader implements DataReader {

    private ByteArrayInputStream bis;

    public EncodedByteReader(byte[] buf) {
        bis = new ByteArrayInputStream(buf);
    }

    public int readInt() {

        int b = bis.read() & 0xff;
        int n = b & 0x7f;
        if (b > 0x7f) {
            b = bis.read() & 0xff;
            n ^= (b & 0x7f) << 7;
            if (b > 0x7f) {
                b = bis.read() & 0xff;
                n ^= (b & 0x7f) << 14;
                if (b > 0x7f) {
                    b = bis.read() & 0xff;
                    n ^= (b & 0x7f) << 21;
                    if (b > 0x7f) {
                        b = bis.read() & 0xff;
                        n ^= (b & 0x7f) << 28;
                        if (b > 0x7f) {
                            throw new RuntimeException("Invalid int encoding");
                        }
                    }
                }
            }
        }
        return (n >>> 1) ^ -(n & 1); // back to two's-complement
    }


}
