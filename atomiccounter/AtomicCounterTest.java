package atomiccounter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class AtomicCounterTest {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		int cnt=100000 * 10 * 10;
		
		int runs = Integer.parseInt(args[0]);
		int threads = Integer.parseInt(args[1]);
		
		ExecutorService es = Executors.newFixedThreadPool(threads);
		long min,max,avg;
		min=max=avg=0;
		min = Integer.MAX_VALUE;
		long sum=0;
		for(int r = 0; r<runs;r++)
		{
			AtomicInteger inc = new AtomicInteger();			
			List<Future<Long>> ftTask = new ArrayList<Future<Long>>();
			
			long start = System.nanoTime();
			for(int x = 0;x<threads;x++)
			{
				ftTask.add(es.submit( new AtomicLongIncTask(inc,cnt)));
			}
			
			
			for(Future<Long> f : ftTask)
			{
				f.get();
			}
			
			
			long total = System.nanoTime() - start;
			long tp = (TimeUnit.SECONDS.toNanos(cnt) / total);
			
			max = Math.max(max, tp);
			
			min = Math.min(min, tp);
			sum+=tp;
			System.out.println(String.format("Thread %s , Ops/s %,d", Thread.currentThread(),tp));
		}
		
		System.out.println(String.format("No Of Thread %s - Min %,d , Max %,d Avg %,d", threads, min,max,sum/runs));
		
		es.shutdown();
	}
	
	public static class AtomicLongIncTask implements Callable<Long>
	{
		private AtomicInteger inc;
		private int cnt;
		
		public AtomicLongIncTask(AtomicInteger inc, int cnt) {
			super();
			this.inc = inc;
			this.cnt = cnt;
		}

		
		@Override
		public Long call() throws Exception {
			long start = System.nanoTime();
			for(int x=0;x<cnt;x++)
			{
				inc.getAndIncrement();
			}			
			long total = System.nanoTime() - start;
			//long tp = (TimeUnit.SECONDS.toNanos(cnt) / total) / 1000000;
			//System.out.println(String.format("Thread %s , ops/s %s", Thread.currentThread(),tp));
			return total;
		}
		
	}
}
