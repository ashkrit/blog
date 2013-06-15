import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

public class SeqLock implements ReadWriteLock{

  private AtomicLong counter= new AtomicLong();	
	private Lock write;	
	private Lock read;
	
	
	public SeqLock(boolean singleWriter)
	{
		this.write = singleWriter ? new SeqSingleWriteLock() : new SeqWriteLock();
		this.read = new SeqReadLock();
	}
	
	@Override
	public Lock readLock() {
		return read;
	}

	@Override
	public Lock writeLock() {
		return write;
	}
	
	
	public class SeqWriteLock implements Lock
	{
		ReentrantLock innerLock = new ReentrantLock();
		@Override
		public void lock() {
			/*
			while(true)
			{
				long value = counter.get();
				if(value%2==1) continue;
				if(counter.compareAndSet(value, value+1)) break;
			}
			*/
			innerLock.lock();
			counter.incrementAndGet();
		}

		@Override
		public void unlock() {
			counter.incrementAndGet();
			innerLock.unlock();
		}
		
		@Override
		public void lockInterruptibly() throws InterruptedException {
			throw new UnsupportedOperationException();			
		}

		@Override
		public boolean tryLock() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
			throw new UnsupportedOperationException();
		}

		

		@Override
		public Condition newCondition() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	
	public class SeqSingleWriteLock implements Lock
	{
		
		@Override
		public void lock() {			
			lazyIncrement();
		}

		private void lazyIncrement() {
			long value = counter.get();
			counter.lazySet(value+1);
		}

		@Override
		public void unlock() {
			lazyIncrement();			
		}
		
		@Override
		public void lockInterruptibly() throws InterruptedException {
			throw new UnsupportedOperationException();			
		}

		@Override
		public boolean tryLock() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
			throw new UnsupportedOperationException();
		}

		

		@Override
		public Condition newCondition() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public class SeqReadLock implements Lock
	{

		@Override
		public void lock() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void unlock() {
			throw new UnsupportedOperationException();		
		}
		
		public long tryReadLock()
		{
			while(true)
			{
				long value = counter.get();
				if(value%2==1) continue;
				return value;
			}
		}
		
		public boolean retryReadLock(long value)
		{
			return counter.get()==value;
		}
		
		@Override
		public void lockInterruptibly() throws InterruptedException {
			throw new UnsupportedOperationException();			
		}

		@Override
		public boolean tryLock() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Condition newCondition() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
