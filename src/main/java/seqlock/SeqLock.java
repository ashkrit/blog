/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package seqlock;

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
