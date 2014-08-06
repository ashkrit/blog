

import java.util.Arrays;

public class CompactStringList {

	private char buffer[] = new char[0]; // char buffer for values
	private int[] offset = new int[0]; // stores index of values

	private int keyCount = 0; // no of keys
	private int bufferIndex = 0; // current buffer index

	// add element to list
	public void add(CharSequence value) {
		expandOffset(keyCount + 1);
		expandBuffer(buffer.length + value.length());

		// add values
		for (int i = 0; i < value.length(); i++) {
			buffer[bufferIndex++] = value.charAt(i);
		}

		// store value index
		offset[keyCount++] = bufferIndex;
	}

	public int size() {
		return keyCount;
	}

	private void expandOffset(int newSize) {
		if (newSize - offset.length > 0) {
			int oldCapacity = offset.length;
			int newCapacity = oldCapacity << 1; // Make it double
			if (newCapacity - newSize < 0)
				newCapacity = newSize;
			offset = Arrays.copyOf(offset, newCapacity);
		}
	}

	private void expandBuffer(int newSize) {
		if (newSize > buffer.length) {
			buffer = Arrays.copyOf(buffer, newSize);
		}
	}

	public void forEach(ICharProcedure proc) {
		for (int i = 0; i < keyCount; i++) {
			int startIndex = (i == 0 ? 0 : offset[i - 1]);
			if (!proc.execute(i, startIndex, offset[i], this))
				break;
		}
	}

	public static interface ICharProcedure {
		public boolean execute(int index, int start, int end,
				CompactStringList value);
	}

	public boolean contains(CharSequence value) {
		return indexOf(value) > -1;
	}

	public int indexOf(CharSequence value) {
		MatchProcedure proc = new MatchProcedure();
		proc.valueSearch = value;

		for (int i = 0; i < keyCount; i++) {
			int startIndex = (i == 0 ? 0 : offset[i - 1]);
			if (!proc.execute(i, startIndex, offset[i], this)) {
				return i;
			}
		}
		return -1;
	}

	public static class DebugProcedure implements ICharProcedure {

		@Override
		public boolean execute(int index, int start, int end,
				CompactStringList value) {
			for (int p = start; p < end; p++) {
				System.out.print(value.getAt(p));
			}
			System.out.println();
			return true;
		}

	}

	public class MatchProcedure implements ICharProcedure {
		CharSequence valueSearch;

		@Override
		public boolean execute(int index, int start, int end,
				CompactStringList value) {

			if ((end - start) != valueSearch.length())
				return true;

			int i = 0;
			for (int p = start; p < end; p++) {
				if (valueSearch.charAt(i++) != value.getAt(p))
					return true;
			}
			return false;
		}

	}

	public char getAt(int p) {
		return buffer[p];
	}

	public CharSequence get(int i) {
		int startIndex = (i == 0 ? 0 : offset[i - 1]);
		int endIndex = offset[i];
		return new SubSequence(startIndex, endIndex, buffer);
	}

	public static class SubSequence implements CharSequence {
		int start, end;
		private char buffer[];

		public SubSequence(int start, int end, char[] buffer) {
			super();
			this.start = start;
			this.end = end;
			this.buffer = buffer;
		}

		@Override
		public int length() {
			return end - start;
		}

		@Override
		public char charAt(int index) {
			return buffer[start + index];
		}

		@Override
		public CharSequence subSequence(int start, int end) {
			throw new UnsupportedOperationException();
		}
	}

}
