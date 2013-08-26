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


import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DirectByteBufferObject implements ObjectType {

  private static int CURR;
	private final static int int_offset = (CURR = 0);
	private final static int long_offset = (CURR += 4);
	private final static int byte_offset = (CURR += 8);
	private static int SIZE = (CURR += 1);
	
	private int pos;
	private ByteBuffer buffer;
	
	public DirectByteBufferObject(int element) {
		buffer = ByteBuffer.allocateDirect(SIZE * element);
		buffer.order(ByteOrder.nativeOrder());
	}

	@Override
	public void setInt(int value) {		
		buffer.putInt(pos + int_offset, value);
		
	}

	private int identifyIndex(int index) {
		return  (index*SIZE);
	}

	@Override
	public void setLong(long value) {
		buffer.putLong(pos + long_offset, value);

	}

	@Override
	public void setByte(byte value) {
		buffer.put(pos + byte_offset, value);
	}

	@Override
	public int getInt() {
		return buffer.getInt(pos + int_offset);		
	}

	@Override
	public long getLong() {
		return buffer.getLong(pos + long_offset);
	}

	@Override
	public byte getByte() {
		return buffer.get(pos + byte_offset);
	}

	
	@Override
	public void navigate(int index) {
		pos = identifyIndex(index);
	}

}
