import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class BigArrayList<T> implements List<T> {

  private RandomAccessFile memoryMappedFile;
	
	private MappedByteBuffer writerBuffer;
	private MappedByteBuffer readerBuffer;
	
	private BigArrayReaderWriter<T> readerWriter;

	private int noOfMessage;
	private int mask;	
	private int bytesInMemory;
	
	private int size = 0;
	private long currentPosition;
	private long startIndex, endIndex;

	
	
	public BigArrayList(BigArrayReaderWriter<T> readerWriter, int noOfMessage) {
		this.noOfMessage = powerOfTwo(noOfMessage);		
		this.mask = noOfMessage - 1;
		this.bytesInMemory = readerWriter.messageSize() * noOfMessage;		
		this.readerWriter = readerWriter;
		initRAF();
		allocateWriteBuffer(0);
		allocateReadBuffer(0);
	}

	
	private void initRAF() {
		try {
			//File f = new File(String.format("bigarraylistFile%s.txt",System.nanoTime()));
			File f = new File("bigarraylistFile.bin");
			f.delete();			
			this.memoryMappedFile = new RandomAccessFile(f, "rw");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void allocateReadBuffer(long pos) throws RuntimeException {
		try {
			this.readerBuffer = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_ONLY, pos,bytesInMemory);
			this.readerBuffer.order(ByteOrder.nativeOrder());
			this.startIndex = pos / readerWriter.messageSize();
			this.endIndex = startIndex + noOfMessage - 1;			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void allocateWriteBuffer(long pos) throws RuntimeException {
		try {
			this.currentPosition = pos;
			this.writerBuffer = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE, currentPosition,bytesInMemory);
			
			this.writerBuffer.order(ByteOrder.nativeOrder());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean add(T e) {
		if (writerBuffer.remaining() < readerWriter.messageSize()) {
			allocateWriteBuffer(currentPosition + bytesInMemory);
		}
		readerWriter.write(e, writerBuffer);
		size++;
		return true;
	}
	
	@Override
	public T set(int index, T e) {
		long oldPos = currentPosition;
		int currPos = writerBuffer.position();
		allocateWriteBuffer(index * readerWriter.messageSize());
		readerWriter.write(e, writerBuffer);
		allocateWriteBuffer(oldPos);
		writerBuffer.position(currPos);
		return e;
	}

	@Override
	public T get(final int index) {
		long pointer = index;
		if (pointer >= startIndex && pointer <= endIndex) {

		} else {
			allocateReadBuffer(pointer * readerWriter.messageSize());
		}
		pointer = index & mask;
		pointer = pointer * readerWriter.messageSize();
		readerBuffer.position((int) pointer);
		T v = readerWriter.read(readerBuffer);
		return v;

	}

	
	@Override
	public boolean contains(Object o) {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public Iterator<T> iterator() {
		return null;
	}

	@Override
	public Object[] toArray() {
		throw new IllegalArgumentException("Not supported");
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public boolean remove(Object o) {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public void clear() {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public void add(int index, T element) {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public T remove(int index) {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public int indexOf(Object o) {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		throw new IllegalArgumentException("Not supported");
	}
	
	 private int powerOfTwo(int x)
	 {
	    return 1 << (32 - Integer.numberOfLeadingZeros(x - 1));
	 }	 
}
