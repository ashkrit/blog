package lazy;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LazyValueApp {

    public static void main(String... args) {

        Lazy<String> singleLock = new SingleLock<>(() -> "singlelock");
        Lazy<String> doubleLock = new DoubleLock<>(() -> "doublelock");
        Lazy<String> futureTask = new LazyFutureTask<>(() -> "futurelock");
        Lazy<String> scalaLazy = new ScalaLazy<>(() -> "scalalock");

        int noOfThread = 6;
        ExecutorService es = Executors.newFixedThreadPool(noOfThread);

        execute(noOfThread, es, futureTask);
        execute(noOfThread, es, singleLock);
        execute(noOfThread, es, doubleLock);
        execute(noOfThread, es, scalaLazy);


    }

    private static void execute(int noOfThread, ExecutorService es, Lazy<String> lazyValue) {
        for (int threadCount = 0; threadCount < noOfThread; threadCount++) {
            es.submit(() -> {
                long start = System.currentTimeMillis();
                String returnValue = null;
                for (int loadCount = 0; loadCount < 100000 * 10; loadCount++) {
                    returnValue = lazyValue.get();
                }
                long duration = System.currentTimeMillis() - start;
                System.out.println(String.format("%s took %s", returnValue, duration));
            });
        }
    }
}
