package allocation;


public final class HeapAllocatedObject implements ObjectType {

    private final HeapValue[] values;
    private HeapValue cur = null;


    public HeapAllocatedObject(int element) {
        values = new HeapValue[element];
        for (int x = 0; x < element; x++) {
            values[x] = new HeapValue();
        }
    }


    @Override
    public void setInt(int value) {
        cur.id = value;
    }

    @Override
    public void setLong(long value) {
        cur.longValue = value;

    }

    @Override
    public void setByte(byte value) {
        cur.type = value;

    }

    @Override
    public int getInt() {
        return cur.id;
    }

    @Override
    public long getLong() {
        return cur.longValue;
    }

    @Override
    public byte getByte() {
        return cur.type;
    }

    public static final class HeapValue {
        public int id;
        public long longValue;
        public byte type;
    }

    @Override
    public void navigate(int index) {
        cur = values[index];
    }

}
