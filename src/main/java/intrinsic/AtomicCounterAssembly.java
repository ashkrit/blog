package intrinsic;

import java.util.concurrent.atomic.AtomicInteger;

/*

java -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -server -cp target/classes intrinsic.AtomicCounterAssembly

 */
public class AtomicCounterAssembly {

    public static void main(String...args) {

        AtomicInteger i = new AtomicInteger();
        int total = 0;
        for(int x=0;x<1000;x++) {
            total = i.incrementAndGet();
        }
        System.out.println(total);

    }
}
