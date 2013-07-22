package playground.memory;



public class MemoryObjectWithBigAlloc {
        
        private final static int someLong_OFFSET = 0;
        private final static int someInt_OFFSET = 8;
        private final static int SIZE = 8 + 4; // one long + one int
        
        private long address;
        private final Memory memory;
        private long addIndex=0;
        public MemoryObjectWithBigAlloc(Memory memory,int noofObjects) {
                this.memory = memory;
                this.address = memory.alloc(SIZE * noofObjects);
        }
        
        @Override
        public void finalize() {
                memory.free(address);
        }
        
        public void setIndex(int index)
        {
          addIndex = address + (index*SIZE);
        }
        
        public final void setSomeLong(long someLong) {
        	memory.putLong(addIndex + someLong_OFFSET, someLong);
        }
        
        public final long getSomeLong() {
        	return memory.getLong(addIndex + someLong_OFFSET);
        }
        
        public final void setSomeInt(int someInt) {
        	memory.putInt(addIndex + someInt_OFFSET, someInt);
        }
        
        public final int getSomeInt() {
        	return memory.getInt(addIndex + someInt_OFFSET);
        }
}
