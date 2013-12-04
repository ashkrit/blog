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
		ObjectType[] types = new ObjectType[]{ Allocator.allocate(Allocator.Type.valueOf(args[0]), element)};
		int ONE_MILLION = 1000000;
		
		for(int x = 0;x<50;x++)
		{
			for(ObjectType t : types)
			{
				long writeStart = System.nanoTime();
				write(t,element);
				long totalWrite = System.nanoTime() - writeStart;
				
				
				long readStart = System.nanoTime();
				read(t,element);
				long totalRead = System.nanoTime() - readStart;
				
				double writeMs = totalWrite/1000000d;
				double readMs = totalRead/1000000d;
				
				System.out.println(String.format("[%s] %s - [Write %s ms , Read %s ms ], Op/Sec(Millions)[ Write %s , Read %s ]",
						x,t.getClass().getName(), writeMs,readMs,
						(TimeUnit.SECONDS.toNanos(element)/totalWrite)/ONE_MILLION,
						(TimeUnit.SECONDS.toNanos(element)/totalRead)/ONE_MILLION));
			}		
		}
	}
	
	
	public static void write(ObjectType t,int items)
	{
		for(int index=0;index<items;index++)
		{
			t.navigate(index);
			t.setByte((byte)index);
			t.setInt(index);
			t.setLong(index * index);
		}
	}
	
	@SuppressWarnings("unused")
	public static void read(ObjectType t,int items)
	{
		int intSum=0;
		int longSum=0;
		for(int index=0;index<items;index++)
		{
			t.navigate(index);
			t.getByte();
			intSum+=t.getInt();
			longSum+=t.getLong();
		}
		
	}
}
