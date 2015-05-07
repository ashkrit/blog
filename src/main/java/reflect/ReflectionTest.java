package reflect;

import java.util.concurrent.TimeUnit;


public class ReflectionTest {

    public static String functionToCall = "getOrderId";
    public static int MILLION = 100000 * 10;

    public static void main(String... s) throws Throwable {

        int itrCount = MILLION * 10;
        Order orderObject = new Order(10, "B");
        IOrderMethodInvoker[] invoker = {new DirectMethodInvoker(),
                new ReflectionMethodInvoker(functionToCall),
                new MethodHandleMethodInvoker(functionToCall)};


        for (IOrderMethodInvoker i : invoker) {
            for (int x = 0; x < 10; x++) {
                execute(itrCount, orderObject, i);
            }
        }

    }

    private static int execute(int itrCount, Order orderObject,
                               IOrderMethodInvoker i) throws Throwable {
        long start = System.nanoTime();
        int sum = 0;
        for (int cnt = 0; cnt < itrCount; cnt++) {
            sum += i.integerGetMethod(orderObject);
        }
        long total = System.nanoTime() - start;
        System.out.println(String.format(
                "%s Function Call for %s calls took %s(ms)", i.getClass()
                        .getSimpleName(), itrCount,
                TimeUnit.MILLISECONDS.convert(total, TimeUnit.NANOSECONDS)));
        return sum;
    }
}
