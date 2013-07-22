package playground.memory;



public class SomeMemoryObject {
        
        private final static int someLong_OFFSET = 0;
        private final static int someInt_OFFSET = 8;
        private final static int SIZE = 8 + 4; // one long + one int
        
        private long address;
        private final Memory memory;
        
        public SomeMemoryObject(Memory memory) {
                this.memory = memory;
                this.address = memory.alloc(SIZE);
        }
        
        @Override
        public void finalize() {
                memory.free(address);
        }
        
        public final void setSomeLong(long someLong) {
                memory.putLong(address + someLong_OFFSET, someLong);
        }
        
        public final long getSomeLong() {
                return memory.getLong(address + someLong_OFFSET);
        }
        
        public final void setSomeInt(int someInt) {
                memory.putInt(address + someInt_OFFSET, someInt);
        }
        
        public final int getSomeInt() {
                return memory.getInt(address + someInt_OFFSET);
        }
}
