public class DirectMethodInvoker implements IOrderMethodInvoker {

	@Override
	public int integerGetMethod(Order object) {
		return object.getOrderId();
	}	
}
