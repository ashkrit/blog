package bitset;


import java.util.Arrays;
import java.util.Iterator;

public class SortedIntSet implements Iterable<Integer> {

    private boolean[] indexes = new boolean[2];
    private int capacity = indexes.length;

    public void add(int value) {
        ensureCapacity(value + 1);
        indexes[value] = true;
    }

    private void ensureCapacity(int newCapacity) {
        if (capacity < newCapacity) {
            grow(newCapacity);
        }
    }

    private void grow(int newSize) {
        indexes = Arrays.copyOf(indexes, newSize);
        capacity = indexes.length;
    }

    public boolean contains(int value) {
        return value < capacity ? indexes[value] : false;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                for (; index < capacity; index++) {
                    if (indexes[index]) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Integer next() {
                return index++;
            }
        };
    }


}
