import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ListPerformanceTest {

	public static void main(String[] args) {

		int items = 100000 * 10 * 25;
		int warmUp = 5;

		for (int x = 0; x < warmUp; x++) {
			System.gc();
			List<Integer> list = create(args[0]);
			addAndIterate("WarmUp", items, list);
		}

		System.out.println("Start Test Now ");
		int testCount = 10;
		for (int x = 0; x < testCount; x++) {
			System.gc();
			List<Integer> list = create(args[0]);
			addAndIterate("Test", items, list);
		}
	}

	private static long addAndIterate(String tag, int items, List<Integer> list) {
		long start = System.nanoTime();
		for (int x = 0; x < items; x++) {
			list.add(x);
		}
		long addTime = System.nanoTime() - start;

		start = System.nanoTime();
		long sum = 0;
		for (Integer i : list) {
			sum += i;
		}
		long itrTime = System.nanoTime() - start;

		System.out.println(String.format(
				"[%s] [%s] Add Time %s ms , Itr Time %s ms", tag, list
						.getClass().getSimpleName(), TimeUnit.NANOSECONDS
						.toMillis(addTime), TimeUnit.NANOSECONDS
						.toMillis(itrTime)));
		return sum;
	}

	private static List<Integer> create(String type) {
		if ("LL".equals(type)) {
			return new LinkedList<>();
		} else if ("MLL".equals(type)) {
			return new MultiValueLinkList<>(50);
		} else if ("AL".equals(type)) {
			return new ArrayList<>();
		}
		return null;
	}

}
