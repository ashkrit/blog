package initialization;


import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class LockFreeInitialization implements InitializationStrategy {

    private volatile int size;
    private static final long SIZECTL;
    private volatile Object[] arrayValue;

    public LockFreeInitialization(int size) {
        this.size = size;
    }

    @Override
    public Object[] init() {
        Object[] localValue;
        int currentSize;

        while ((localValue = arrayValue) == null || localValue.length == 0) {

            if ((currentSize = size) < 0) {
                Thread.yield(); // Array already init
            } else if (unsafe.compareAndSwapInt(this, SIZECTL, currentSize, -1)) {

                if ((localValue = arrayValue) == null || localValue.length == 0) {
                    Object[] p = new Object[currentSize];
                    localValue = arrayValue = p;
                }
                break;
            }
        }

        return localValue;
    }

    public static Unsafe unsafe;

    static {
        getUnSafe();


        try {
            Class<?> k = LockFreeInitialization.class;
            SIZECTL = unsafe.objectFieldOffset(k.getDeclaredField("size"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static void getUnSafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
        }
    }


}
