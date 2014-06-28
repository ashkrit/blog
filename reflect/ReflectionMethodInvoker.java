import java.lang.reflect.Method;


public class ReflectionMethodInvoker implements IOrderMethodInvoker {

	private Method orderIdMethod;
	public ReflectionMethodInvoker(String methodName) throws Exception
	{
		orderIdMethod = Order.class.getMethod(methodName);
	}
	
	@Override
	public int integerGetMethod(Order object) throws Exception {
		return (int) orderIdMethod.invoke(object);
	}	
}
