import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.LockSupport;

public class ParkWaitStrategy implements WaitStrategy {

	private AtomicReferenceArray<Thread> consumerWorker;
	private int consumer;
	public ParkWaitStrategy(int consumer)
	{
		this.consumer =consumer;
		this.consumerWorker = new AtomicReferenceArray<Thread>(consumer);
	}
	
	@Override
	public void block() throws InterruptedException {		
		boolean parked=false;
		for(int index=0;index<consumer;index++)
		{
			
			if(consumerWorker.get(index)==null && consumerWorker.compareAndSet(index, null, Thread.currentThread()))
			{
				parked=true;
				LockSupport.park(this);	
				break;
			}
		}		
		if(!parked)
		{
			//Unable to park because no slots available then park for some time
			LockSupport.parkNanos(this, TimeUnit.MILLISECONDS.toNanos(10));			
		}		
	}

	@Override
	public void release() {
		for(int index=0;index<consumer;index++)
		{
			Thread threadToNotify = consumerWorker.get(index);
			if(threadToNotify!=null)
			{
				consumerWorker.set(index, null);				
				LockSupport.unpark(threadToNotify);
			}
			
		}
	}

}
