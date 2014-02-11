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


import java.util.concurrent.TimeUnit;

public class TestMemoryAllocator {

  public static void main(String...args)
	{
		int element = Integer.valueOf(args[1]);
		final ObjectType[] types = new ObjectType[]{ Allocator.allocate(Allocator.Type.valueOf(args[0]), element)};
		int ONE_MILLION = 1000000;

		final int len = types.length;
		
		for(int x = 0; x < 100; x++)
		{
			for(int n = 0; n < len; n++)
			{
				final ObjectType t = types[n];

				long writeStart = System.nanoTime();
				int resWrite = write(t,element);
				long totalWrite = System.nanoTime() - writeStart;
				
				
				long readStart = System.nanoTime();
				int resRead = read(t,element);
				long totalRead = System.nanoTime() - readStart;
				
				double writeMs = totalWrite / 1000000d;
				double readMs = totalRead / 1000000d;
				
				System.out.println(String.format("[%2s] %s - [Write %16s ms , Read %16s ms ], Op/Sec(Millions)[ Write %6s , Read %6s ]: %16s %16s",
						x,t.getClass().getName(), writeMs,readMs,
						(TimeUnit.SECONDS.toNanos(element)/totalWrite)/ONE_MILLION,
						(TimeUnit.SECONDS.toNanos(element)/totalRead)/ONE_MILLION,
						resWrite,
						resRead));
			}		
		}
	}
	
	
	public static int write(ObjectType t,int items)
	{
		int index = 0;
		for(; index < items; index++)
		{
			t.navigate(index);
			t.setByte((byte)index);
			t.setInt(index);
			t.setLong(index * index);
		}
		return index;
	}
	
	@SuppressWarnings("unused")
	public static int read(ObjectType t,int items)
	{
		int  byteSum  = 0;
		int  intSum   = 0;
		long longSum = 0l;

		for(int index = 0; index < items; index++)
		{
			t.navigate(index);
			byteSum += t.getByte();
			intSum  += t.getInt();
			longSum += t.getLong();
		}
		return byteSum + intSum + (int)longSum;
	}
}
