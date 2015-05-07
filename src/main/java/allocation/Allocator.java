package allocation;


public class Allocator {
    public enum Type {HEAP, BB, DBB, OFFHEAP}

    public static ObjectType allocate(Type type, int items) {
        if (type.equals(Type.HEAP)) {
            return new HeapAllocatedObject(items);
        } else if (type.equals(Type.OFFHEAP)) {
            return new OffHeapObject(items);
        } else if (type.equals(Type.DBB)) {
            return new DirectByteBufferObject(items);
        } else if (type.equals(Type.BB)) {
            return new ByteBufferObject(items);
        }
        return null;
    }
}
