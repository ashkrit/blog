import java.util.concurrent.atomic.AtomicIntegerArray;

public class PaddedAtomicCounter implements Counter {
	private int padding=16; // Cacheline is 64 bytes , int is 4 bytes so for int it is 4 * 16 = 64
	private int slots=Runtime.getRuntime().availableProcessors();
	private AtomicIntegerArray atomicArray = new AtomicIntegerArray(slots * padding);
	

	@Override
	public int inc() {
		int id = (int) Thread.currentThread().getId();
		int index = (id % slots) * padding;
		int fail = 0;
		while (true) {
			int currVal = atomicArray.get(index);
			if (atomicArray.compareAndSet(index, currVal, currVal + 1))
				break;
			fail++;
		}
		return fail;

	}

	@Override
	public int get() {
		int sum = 0;
		for (int i = 0; i < slots ; i++) {
			sum += atomicArray.get(i * padding);
		}
		return sum;
	}

	
}
