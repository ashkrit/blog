package lazy;


import java.util.concurrent.Callable;

public class DoubleLock<V> implements Lazy<V> {

    private Callable<V> codeBlock;
    private V value;
    private volatile boolean loaded;

    public DoubleLock(Callable<V> codeBlock) {
        this.codeBlock = codeBlock;
    }

    @Override
    public V get() {
        if (!loaded) {
            synchronized (this) {
                if (!loaded) {
                    setValue();
                    loaded = true;
                }
            }
        }
        return value;
    }

    private void setValue() {
        try {
            value = codeBlock.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
