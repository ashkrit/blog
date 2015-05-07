
package memorymapped.counter;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IPCCounterTest {

    public static void main(String[] args) throws Exception {

        int threads = Integer.parseInt(args[0]);
        final int itr = Integer.parseInt(args[1]) * 1000000;
        System.out.println(String.format("Threads %s , inc %s", threads, itr));
        ExecutorService es = Executors.newFixedThreadPool(threads);
        final File file = new File("counter.long");
        file.createNewFile();
        System.out.println(file.getAbsolutePath());

        for (int x = 0; x < threads; x++) {
            es.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    inc(file, itr);
                    return null;
                }
            });
        }
        es.shutdown();
    }


    private static void inc(File file, int iteration) throws Exception {
        IPCLongCounter counter = new IPCLongCounter(file);
        int cnt = 0;
        long start = System.nanoTime();
        int failCount = 0;
        while (cnt < iteration) {
            if (counter.increment()) {
                cnt++;
            } else {
                failCount++;
            }
        }
        long total = System.nanoTime() - start;
        double tp = (TimeUnit.SECONDS.toNanos(iteration) / total) / 1000000d;
        System.out.println(String.format(" TPS mops %s , Failed CAS %s , time %s ms , Current Value %s", tp, failCount, TimeUnit.NANOSECONDS.toMillis(total), counter.get()));
    }
}
