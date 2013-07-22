package playground.memory;


import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class Direct implements Memory {
        
        private static Unsafe unsafe;
        private static boolean AVAILABLE = false;

        static {
                try {
                        Field field = Unsafe.class.getDeclaredField("theUnsafe");
                        field.setAccessible(true);
                        unsafe = (Unsafe)field.get(null);
                        AVAILABLE = true;
                } catch(Exception e) {
                        // NOOP: throw exception later when allocating memory
                }
    }
        
        public static boolean isAvailable() {
                return AVAILABLE;
        }
        
        private static Direct INSTANCE = null;
        
        public static Memory getInstance() {
                if (INSTANCE == null) {
                        INSTANCE = new Direct();
                }
                return INSTANCE;
        }
        
        private Direct() {
                
        }
        
        @Override
        public long alloc(long size) {
                if (!AVAILABLE) {
                        throw new IllegalStateException("sun.misc.Unsafe is not accessible!");
                }
                return unsafe.allocateMemory(size);
        }
        
        @Override
        public void free(long address) {
                unsafe.freeMemory(address);
        }
        
        @Override
        public final long getLong(long address) {
                return unsafe.getLong(address);
        }
        
        @Override
        public final void putLong(long address, long value) {
                unsafe.putLong(address, value);
        }
        
        @Override
        public final int getInt(long address) {
                return unsafe.getInt(address);
        }
        
        @Override
        public final void putInt(long address, int value) {
                unsafe.putInt(address, value);
        }
        
        @Override
        public final void putByte(long address, byte value) {
                unsafe.putByte(address, value);
        }
        
        @Override
        public final byte getByte(long address) {
                return unsafe.getByte(address);
        }
}       
