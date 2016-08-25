package lazy;


import java.util.concurrent.Callable;

public class ScalaLazy<V> implements Lazy<V> {

    private Callable<V> codeBlock;
    private volatile boolean bitmap$0;
    private V value;

    public ScalaLazy(Callable<V> codeBlock) {
        this.codeBlock = codeBlock;
    }

    @Override
    public V get() {

        synchronized (this) {

            if (!this.bitmap$0) {
                this.value = getValue();
                this.bitmap$0 = true;
            }
            return this.value;
        }
    }

    private V getValue() {
        try {
            return codeBlock.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
