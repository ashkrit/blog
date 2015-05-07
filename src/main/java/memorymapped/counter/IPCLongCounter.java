package memorymapped.counter;

import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Atomic counter based on memory mapped file.
 */
public class IPCLongCounter {

    private MappedByteBuffer mem;
    private long startAddress;

    @SuppressWarnings("resource")
    public IPCLongCounter(File fileName) throws Exception {
        FileChannel fc = new RandomAccessFile(fileName, "rw").getChannel();
        // Map 8 bytes for long value.
        mem = fc.map(FileChannel.MapMode.READ_WRITE, 0, 8);
        startAddress = ((DirectBuffer) mem).address();
    }

    public boolean increment() {
        long orignalValue = readVolatile(startAddress);
        long value = convert(orignalValue);
        return UnsafeUtils.unsafe.compareAndSwapLong(null,
                startAddress, orignalValue, convert(value + 1));
    }

    public long get() {
        long orignalValue = readVolatile(startAddress);
        return convert(orignalValue);
    }

    // Only unaligned is implemented
    private static long readVolatile(long position) {
        if (UnsafeUtils.unaligned()) {
            return UnsafeUtils.unsafe.getLongVolatile(null, position);
        }
        throw new UnsupportedOperationException();
    }

    private static long convert(long a) {
        if (UnsafeUtils.unaligned()) {
            return (UnsafeUtils.nativeByteOrder ? a : Long.reverseBytes(a));
        }
        throw new UnsupportedOperationException();
    }
}
