package playground.memory;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class WriteReadTest1 {

  private static int SIZE;
	private static final int WARMUP = 1000;
	private static final int COUNT = 10;

	public static void main(String[] args) {

		SIZE = Integer.parseInt(args[0]);

		Memory memory = Direct.getInstance();

		SomeObject[] someObjects = new SomeObject[SIZE];
		for (int i = 0; i < SIZE; i++)
			someObjects[i] = new SomeObject();

		SomeMemoryObject[] someMemoryObjects = new SomeMemoryObject[SIZE];
		for (int i = 0; i < SIZE; i++)
			someMemoryObjects[i] = new SomeMemoryObject(memory);

		MemoryObjectWithBigAlloc bigAlloc = new MemoryObjectWithBigAlloc(memory, SIZE);

		for (int i = 0; i < WARMUP; i++) {
			write(someObjects);
			write(someMemoryObjects);
			write(bigAlloc);
			read(someObjects);
			read(someMemoryObjects);
			read(bigAlloc);
		}

		double[] heapWriteRes = new double[COUNT];
		double[] heapReadRes = new double[COUNT];
		double[] directWriteRes = new double[COUNT];
		double[] directReadRes = new double[COUNT];
		double[] bigDirectWriteRes = new double[COUNT];
		double[] bigDirectReadRes = new double[COUNT];

		for (int i = 0; i < COUNT; i++) {
			heapWriteRes[i] = write(someObjects);
			directWriteRes[i] = write(someMemoryObjects);
			bigDirectWriteRes[i] = write(bigAlloc);
			
			heapReadRes[i] = read(someObjects);
			directReadRes[i] = read(someMemoryObjects);
			bigDirectReadRes[i] = read(bigAlloc);
			
		}

		System.out.println("Heap Write: " + join(heapWriteRes));
		System.out.println("Direct Write: " + join(directWriteRes));
		System.out.println("BigArrayDirect Write: " + join(bigDirectWriteRes));
		System.out.println("Heap Read: " + join(heapReadRes));
		System.out.println("Direct Read: " + join(directReadRes));
		System.out.println("BigArrayDirect Read: " + join(bigDirectReadRes));

	}

	private static long write(SomeObject[] array) {
		int len = array.length;
		long start = System.nanoTime();
		for (int i = 0; i < len; i++) {
			SomeObject obj = array[i];
			obj.setSomeLong(i * 2);
			obj.setSomeInt(i);
		}
		return System.nanoTime() - start;
	}

	private static long write(SomeMemoryObject[] array) {
		int len = array.length;
		long start = System.nanoTime();
		for (int i = 0; i < len; i++) {
			SomeMemoryObject obj = array[i];
			obj.setSomeLong(i * 2);
			obj.setSomeInt(i);
		}
		return System.nanoTime() - start;
	}

	private static long write(MemoryObjectWithBigAlloc obj) {

		long start = System.nanoTime();
		for (int i = 0; i < SIZE; i++) {
			obj.setIndex(i);
			obj.setSomeLong(i * 2);
			obj.setSomeInt(i);
		}
		return System.nanoTime() - start;
	}
	
	 private static long read(MemoryObjectWithBigAlloc obj) {
        
         long start = System.nanoTime();
         for(int i = 0; i < SIZE; i++) {
        	 obj.setIndex(i);
             obj.getSomeLong();
             obj.getSomeInt();
         }
         return System.nanoTime() - start;
 }

	private static long read(SomeObject[] array) {
		int len = array.length;
		long start = System.nanoTime();
		for (int i = 0; i < len; i++) {
			SomeObject obj = array[i];
			obj.getSomeLong();
			obj.getSomeInt();
		}
		return System.nanoTime() - start;
	}

	private static long read(SomeMemoryObject[] array) {
		int len = array.length;
		long start = System.nanoTime();
		for (int i = 0; i < len; i++) {
			SomeMemoryObject obj = array[i];
			obj.getSomeLong();
			obj.getSomeInt();
		}
		return System.nanoTime() - start;
	}

	private static String join(double[] array) {

		NumberFormat formatter = new DecimalFormat("#,###,###.##");

		StringBuilder sb = new StringBuilder(256);
		for (int i = 0; i < array.length; i++) {
			if (i > 0)
				sb.append(", ");
			double avg = array[i] / (double) SIZE;
			sb.append(formatter.format(avg));
		}
		return sb.toString();
	}
}
