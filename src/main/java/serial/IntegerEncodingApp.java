package serial;


import serial.reader.DataReader;
import serial.reader.EncodedByteReader;
import serial.reader.RawByteDataReader;
import serial.writer.DataWriter;
import serial.writer.EncodedByteWriter;
import serial.writer.RawByteWriter;

import java.io.IOException;
import java.util.function.BiFunction;

public class IntegerEncodingApp {

    public static final String RAW = "raw";
    public static final String ENCODED = "encoded";

    public static void main(String... args) throws IOException {

        int items = 100000 * 10;

        BiFunction<DataWriter, Integer, byte[]> writerFunction = (writer, element) -> {
            for (int i = 0; i < element; i++) {
                writer.writeInt(i);
            }
            writer.close();
            return writer.toByteArray();
        };

        write(writerFunction, createWriter(RAW), items);
        write(writerFunction, createWriter(ENCODED), items);

    }


    public static byte[] write(BiFunction<DataWriter, Integer, byte[]> write, DataWriter writer, int valueCount) {
        byte[] buffer = write.apply(writer, valueCount);
        System.out.println(String.format("class %s took %s bytes for %s values",
                writer.getClass().getSimpleName(),
                buffer.length,
                valueCount));
        return buffer;
    }


    public static DataWriter createWriter(String type) {
        if (RAW.equals(type))
            return new RawByteWriter();
        else if (ENCODED.equals(type)) {
            return new EncodedByteWriter();
        }
        return null;
    }

    public static DataReader createReader(String type, byte[] buffer) throws IOException {
        if (RAW.equals(type))
            return new RawByteDataReader(buffer);
        else if (ENCODED.equals(type)) {
            return new EncodedByteReader(buffer);
        }
        return null;
    }
}
