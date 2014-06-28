public class Order {
	private int orderId;
	private String orderType;

	
	public Order(int orderId, String orderType) {
		super();
		this.orderId = orderId;
		this.orderType = orderType;
	}
	
	public int getOrderId() {
		return orderId;
	}

	public String getOrderType() {
		return orderType;
	}
}
