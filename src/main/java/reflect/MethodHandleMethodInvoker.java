package reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;


public class MethodHandleMethodInvoker implements IOrderMethodInvoker {

    private MethodHandle m;

    public MethodHandleMethodInvoker(String methodName) throws Exception {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        m = lookup.findVirtual(Order.class, methodName, MethodType.methodType(Integer.TYPE));
    }

    @Override
    public int integerGetMethod(Order order) throws Throwable {
        return (int) m.invoke(order);
    }
}
