import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CounterTest {

	public static void main(String... args) throws InterruptedException,
			ExecutionException {
		int incItr = 100000 * 10;
		int noOfThreads = Integer.parseInt(args[0]);
		ExecutorService es = Executors.newFixedThreadPool(noOfThreads);

		Counter[] counters = new Counter[] { new AtomicCounter(),
				new CoreBasedCounter(), new CoreBasedExtraSlotCounter(), new ThreadBasedCounter(noOfThreads) };

		for (Counter counter : counters) {
			List<Future<Long[]>> ftList = new ArrayList<>();

			for (int x = 0; x < noOfThreads; x++) {
				ftList.add(es.submit(new CounterCallable(counter, incItr)));
			}
			List<Long> timeTaken = new ArrayList<>();
			long sum = 0;
			long failCount=0;
			for (Future<Long[]> ft : ftList) {
				Long[] v = ft.get();
				
				timeTaken.add(TimeUnit.NANOSECONDS.toMillis(v[0]));
				sum += TimeUnit.NANOSECONDS.toMillis(v[0]);				
				failCount += v[1];
			}

			System.out
					.println(String
							.format("[%s] No Of Thread: %s , Min:%s ms , Max: %s ms , Avg: %s ms, Fail CAS: %s ",
									counter.getClass().getSimpleName() , noOfThreads, Collections.min(timeTaken),
									Collections.max(timeTaken), sum
											/ noOfThreads, failCount));
		}

		es.shutdownNow();

	}

	private static class CounterCallable implements Callable<Long[]> {

		private Counter counter;
		private int itrCnt;

		public CounterCallable(Counter counter, int itrCnt) {
			this.counter = counter;
			this.itrCnt = itrCnt;
		}

		@Override
		public Long[] call() throws Exception {
			
			long start = System.nanoTime();
			long failCnt=0;
			for (int i = 0; i < itrCnt; i++) {
				failCnt+=counter.inc();
			}
			return new Long[]{System.nanoTime() - start,failCnt};
		}

	}
}
