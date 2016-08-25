package lazy;


import java.util.concurrent.Callable;

public class SingleLock<V> implements Lazy<V> {

    private Callable<V> codeBlock;
    private V value;

    public SingleLock(Callable<V> codeBlock) {
        this.codeBlock = codeBlock;
    }

    @Override
    public synchronized V get() {
        if (value == null) {
            setValue();
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
