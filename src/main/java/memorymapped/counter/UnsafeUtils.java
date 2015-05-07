package memorymapped.counter;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;

//Taken from java.nio.Bits

class UnsafeUtils {

    public static Unsafe unsafe;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
        }
    }


    static boolean nativeByteOrder = (byteOrder() == ByteOrder.BIG_ENDIAN);
    public static boolean unaligned;
    public static boolean unalignedKnown = false;

    static boolean unaligned() {
        if (unalignedKnown)
            return unaligned;
        String arch = AccessController
                .doPrivileged(new sun.security.action.GetPropertyAction(
                        "os.arch"));
        unaligned = arch.equals("i386") || arch.equals("x86")
                || arch.equals("amd64") || arch.equals("x86_64");
        unalignedKnown = true;
        return unaligned;
    }

    private static ByteOrder byteOrder;
    private static boolean byteOrderKnown = false;

    public static ByteOrder byteOrder() {
        if (!byteOrderKnown) {
            long a = unsafe.allocateMemory(8);
            try {
                unsafe.putLong(a, 0x0102030405060708L);
                byte b = unsafe.getByte(a);
                switch (b) {
                    case 0x01:
                        byteOrder = ByteOrder.BIG_ENDIAN;
                        break;
                    case 0x08:
                        byteOrder = ByteOrder.LITTLE_ENDIAN;
                        break;
                    default:
                        assert false;
                        byteOrder = null;
                }
            } finally {
                unsafe.freeMemory(a);
            }
            byteOrderKnown = true;
        }
        if (byteOrder == null)
            throw new Error("Unknown byte order");
        return byteOrder;
    }


}
