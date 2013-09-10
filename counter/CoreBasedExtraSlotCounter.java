import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class CoreBasedExtraSlotCounter implements Counter {
	private AtomicIntegerArray atomicArray = new AtomicIntegerArray(Runtime.getRuntime().availableProcessors());
	private AtomicInteger base = new AtomicInteger();
	int mode = Runtime.getRuntime().availableProcessors() - 1;

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
