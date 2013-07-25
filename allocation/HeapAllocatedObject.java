
public class HeapAllocatedObject implements ObjectType {

  private HeapValue[] values;
	private int index;
	public HeapAllocatedObject(int element)
	{
		values = new HeapValue[element];
		for(int x=0;x<element;x++)
		{
			values[x] = new HeapValue();
		}
	}
	
	
	@Override
	public void setInt(int value) {
		values[index].setId(value);
	}

	@Override
	public void setLong(long value) {
		values[index].setLongValue(value);
		
	}

	@Override
	public void setByte(byte value) {
		values[index].setType(value);
		
	}

	@Override
	public int getInt() {
		return values[index].getId();
	}

	@Override
	public long getLong() {
		return values[index].getLongValue();
	}

	@Override
	public byte getByte() {
		return values[index].getType();
	}
	
	private static class HeapValue
	{
		private int id;
		private long longValue;
		private byte type;
		
		public HeapValue() {
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public void setLongValue(long longValue) {
			this.longValue = longValue;
		}
		
		public void setType(byte type) {
			this.type = type;
		}
		
		public int getId() {
			return id;
		}
		
		public long getLongValue() {
			return longValue;
		}
		
		public byte getType() {
			return type;
		}
		
	}

	@Override
	public void navigate(int index) {
		this.index = index;		
	}

}
