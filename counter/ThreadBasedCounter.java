import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class ThreadBasedCounter implements Counter {
	private AtomicIntegerArray atomicArray;
	private AtomicInteger base = new AtomicInteger();
	private int mode;

	public ThreadBasedCounter(int size)
	{
		int newSize=1;
		while(newSize < size)
		{
			newSize = newSize << 1;
		}
		atomicArray = new AtomicIntegerArray(newSize);
		mode = newSize-1;
	}
	
	@Override
	public int inc() {
		int id = (int) Thread.currentThread().getId();
		int index = id & mode;
		int fail = 0;
		while (true) {
			int currVal = atomicArray.get(index);
			if (atomicArray.compareAndSet(index, currVal, currVal + 1))
				break;
			fail++;
			currVal = base.get();
			if (base.compareAndSet(currVal, currVal + 1))
				break;
			fail++;
		}
		return fail;

	}

	@Override
	public int get() {
		int sum = base.get();
		for (int i = 0; i < mode + 1; i++) {
			sum += atomicArray.get(i);
		}
		return sum;
	}


}
