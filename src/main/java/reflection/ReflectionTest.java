package reflection;

import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


@SuppressWarnings("restriction")
public class ReflectionTest {
	
	public static String functionToCall="getOrderId";
	public static String propertyToCall="orderId";
	public static class Order {
		private int orderId;
		private String orderType;

		public int getOrderId() {
			return orderId;
		}

		public void setOrderId(int orderId) {
			this.orderId = orderId;
		}

		public void setOrderType(String orderType) {
			this.orderType = orderType;
		}
		public String getOrderType() {
			return orderType;
		}
	}

	

	public static void main(String... s) throws Throwable {

		int itrCount = 100000 * 10 * 10;
		
		Order orderObject = new Order();
		orderObject.setOrderId(10);
		orderObject.setOrderType("B");	
		
		executeViaNormalFunction(orderObject, itrCount);		
		executeViaReflection(orderObject,  itrCount);
		executeViaMethodHandle(orderObject,itrCount);
		executeViaCompiledClass(orderObject, itrCount);	
		executeViaCompiledSingleClass(orderObject, itrCount);
		executeViaUnsafe(orderObject,  itrCount);	
	}



	private static void executeViaReflection(Order orderObject,int itrCount) throws Exception {
		Method orderIdMethod = Order.class.getMethod(functionToCall);
		long start;
		start = System.currentTimeMillis();
		for(int cnt = 0;cnt < itrCount;cnt++)
		{
			orderIdMethod.invoke(orderObject);
		}
		System.out.println(String.format("%s Function Call for %s calls took %s(ms)","Reflection",itrCount,(System.currentTimeMillis() - start)));
	}
	
	private static void executeViaMethodHandle(Order orderObject,int itrCount) throws Throwable {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandle m = lookup.findVirtual(Order.class, functionToCall ,  MethodType.methodType(Integer.TYPE));		
		long start;
		start = System.currentTimeMillis();
		for(int cnt = 0;cnt < itrCount;cnt++)
		{
			m.invoke(orderObject);
		}
		System.out.println(String.format("%s Function Call for %s calls took %s(ms)","JDK 7 Methodhandle",itrCount,(System.currentTimeMillis() - start)));
	}
	
	
	private static void executeViaUnsafe(Order orderObject,int itrCount) throws Exception {
		Unsafe u = UNSAFE;
		
		long offset = u.objectFieldOffset(Order.class.getDeclaredField(propertyToCall));
		
		long start;
		start = System.currentTimeMillis();
		for(int cnt = 0;cnt < itrCount;cnt++)
		{
			//u.getObject(orderObject, offset);
			u.getInt(orderObject, offset);
		}
		System.out.println(String.format("%s Function Call for %s calls took %s(ms)","Unsafe",itrCount,(System.currentTimeMillis() - start)));
	}



	private static void executeViaNormalFunction(Order orderObject, int itrCount) {
		long start = System.currentTimeMillis();
		for(int cnt = 0;cnt < itrCount;cnt++)
		{
			orderObject.getOrderId();
		}
		System.out.println(String.format("%s Function Call for %s calls took %s(ms)","Normal",itrCount,(System.currentTimeMillis() - start)));
	}



	private static void executeViaCompiledClass(Order orderObject,int itrCount) throws Exception {
		PropertyGenerator.BaseProperty compliedClass = (PropertyGenerator.BaseProperty) PropertyGenerator.INSTANCE(Order.class);
		long start = System.currentTimeMillis();
		for(int cnt = 0;cnt < itrCount;cnt++)
		{
			compliedClass.getValue(orderObject, "getOrderId");
		}
		System.out.println(String.format("%s Function Call for %s calls took %s(ms)","Compiled",itrCount,(System.currentTimeMillis() - start)));
	}
	
	
	private static void executeViaCompiledSingleClass(Order orderObject,int itrCount) throws Exception {
		PropertyGenerator.BaseProperty compliedClass = (PropertyGenerator.BaseProperty) PropertyGenerator.INSTANCE(Order.class,functionToCall);
		long start = System.currentTimeMillis();
		for(int cnt = 0;cnt < itrCount;cnt++)
		{
			compliedClass.getValue(orderObject);
		}
		System.out.println(String.format("%s Function Call for %s calls took %s(ms)","CompiledSingleProp",itrCount,(System.currentTimeMillis() - start)));
	}

	
	public static final Unsafe UNSAFE;
    static
    {
        try
        {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe)theUnsafe.get(null);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to load unsafe", e);
        }        
    }
    
	
}
