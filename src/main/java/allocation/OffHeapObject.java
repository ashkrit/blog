package allocation;


import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class OffHeapObject implements ObjectType {

    private static int CURR;
    private final static int int_offset = (CURR = 0);
    private final static int long_offset = (CURR += 4);
    private final static int byte_offset = (CURR += 8);
    private static int SIZE = (CURR += 1);
    private long address;
    private long pos;

    public OffHeapObject(int element) {
        address = unsafe.allocateMemory(SIZE * element);
    }

    @Override
    public void setInt(int value) {
        unsafe.putInt(pos + int_offset, value);

    }

    private long identifyIndex(int index) {
        return address + (index * SIZE * 1L);
    }

    @Override
    public void setLong(long value) {
        unsafe.putLong(pos + long_offset, value);

    }

    @Override
    public void setByte(byte value) {
        unsafe.putByte(pos + byte_offset, value);
    }

    @Override
    public int getInt() {
        return unsafe.getInt(pos + int_offset);
    }

    @Override
    public long getLong() {
        return unsafe.getLong(pos + long_offset);
    }

    @Override
    public byte getByte() {
        return unsafe.getByte(pos + byte_offset);
    }

    private static Unsafe unsafe;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
        }
    }

    @Override
    public void navigate(int index) {
        pos = identifyIndex(index);
    }

}
