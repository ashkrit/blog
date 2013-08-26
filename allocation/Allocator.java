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


public class Allocator {
  public enum Type{HEAP,BB,DBB,OFFHEAP}
	public static ObjectType allocate(Type type , int items)
	{
		if(type.equals(Type.HEAP))
		{
			return new HeapAllocatedObject(items);
		}
		else if(type.equals(Type.OFFHEAP))
		{
			return new OffHeapObject(items);
		}
		else if(type.equals(Type.DBB))
		{
			return new DirectByteBufferObject(items);
		}
		else if(type.equals(Type.BB))
		{
			return new ByteBufferObject(items);
		}
		return null;
	}
}
