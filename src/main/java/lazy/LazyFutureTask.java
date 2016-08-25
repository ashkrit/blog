package lazy;


import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class LazyFutureTask<V> implements Lazy<V> {

    private final FutureTask<V> futureTask;

    public LazyFutureTask(Callable<V> codeBlock) {
        this.futureTask = new FutureTask<>(codeBlock);
    }

    @Override
    public V get() {
        futureTask.run();
        return getValue();
    }

    private V getValue() {
        try {
            return futureTask.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
