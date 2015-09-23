package collection;


import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;

public class AutoCompactList<T> {

    private final float growthFactor;
    private final float shrinkFactor;
    private int size = 0;
    private T[] elements;
    private int capacity;

    public AutoCompactList(float growthFactor, float shrinkFactor, int initialSize) {
        this.growthFactor = growthFactor;
        this.shrinkFactor = shrinkFactor;
        this.elements = (T[]) new Object[initialSize];
        this.capacity = elements.length;
    }

    public T element(int index) {
        return elements[index];
    }

    public void add(T element) {
        grow(size + 1);
        elements[size++] = element;
    }

    private void grow(int newSize) {
        if (growthRequired(newSize)) {
            int newCapacity = capacity + (int) (capacity < 2 ? 1 : capacity * growthFactor);
            this.elements = copyOf(elements, newCapacity);
            capacity = elements.length;
        }
    }

    private boolean growthRequired(int newSize) {
        return newSize > capacity;
    }

    public T remove(int index) {
        T oldValue = elements[index];
        shiftElement(index);
        elements[--size] = null;
        shrink();
        return oldValue;
    }

    private void shiftElement(int index) {
        int elementToMove = size - index - 1;
        if (elementToMove > 0)
            arraycopy(elements, index + 1, elements, index, elementToMove);
    }

    private void shrink() {
        float fillPercentage = 1 - (size / (float) capacity);
        if (fillPercentage > shrinkFactor) {
            elements = copyOf(elements, size);
            capacity = elements.length;
        }
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    public int remainingCapacity() {
        return capacity() - size();
    }
}
