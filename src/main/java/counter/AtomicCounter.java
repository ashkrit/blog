package counter;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter implements Counter {
    private AtomicInteger atomic = new AtomicInteger();

    @Override
    public int inc() {

        int fail = 0;
        while (true) {
            int c = atomic.get();
            if (atomic.compareAndSet(c, c + 1))
                break;
            fail++;
        }
        return fail;
    }

    @Override
    public int get() {
        return atomic.get();
    }
}
