import java.nio.ByteBuffer;

public interface BigArrayReaderWriter<T> {
	public void write(T value,ByteBuffer buffer);
	public T read(ByteBuffer buffer);
	public int messageSize();
}
