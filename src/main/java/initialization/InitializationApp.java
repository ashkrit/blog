package initialization;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InitializationApp {

    public static void main(String... args) {


        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ConcurrentMap<Object, Object> v = new ConcurrentHashMap<>();

        InitializationStrategy lockFree = new LockFreeInitialization(10000);


        for (int x = 0; x < 10000; x++) {

            es.submit(() -> {
                        Object[] values = lockFree.init();
                        v.put(values, values);
                    }

            );
        }
        es.shutdown();


        System.out.println(" Size:" + v.size()); // Size Should be 1 if no duplicate allocation is done.

    }
}
