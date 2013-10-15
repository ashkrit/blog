public interface WaitStrategy {
	public void block() throws InterruptedException;
	public void release();
}
