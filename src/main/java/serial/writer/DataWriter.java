package serial.writer;


public interface DataWriter {

    byte[] toByteArray();

    void writeInt(int value);

    void close();
}
