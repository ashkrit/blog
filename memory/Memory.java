package playground.memory;

public interface Memory {

  public long alloc(long size);

	public void free(long address);

	public long getLong(long address);

	public void putLong(long address, long value);

	public int getInt(long address);

	public void putInt(long address, int value);

	public byte getByte(long address);

	public void putByte(long address, byte value);
}
