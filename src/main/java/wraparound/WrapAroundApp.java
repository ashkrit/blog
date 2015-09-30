package wraparound;


import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.out;
import static wraparound.WrapAround.*;

public class WrapAroundApp {

    public static void main(String... args) throws Exception {

        AtomicInteger i = new AtomicInteger(0);
        loop(() -> i.get() < 5, () -> out.println("Value" + i.incrementAndGet() + " MS" + System.currentTimeMillis()));

        time("println", () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        wrapWithRuntimeException(() -> Thread.sleep(1000));

        withAutoClose(readSelf(), (resource) -> System.out.println("length " + ((InputStream) resource).available()));

    }

    private static InputStream readSelf() {
        return WrapAroundApp.class.getResourceAsStream("/wraparound/WrapAroundApp.class");
    }
}
