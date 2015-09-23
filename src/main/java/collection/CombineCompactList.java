package collection;


import java.util.ArrayList;
import java.util.List;

public class CombineCompactList<T> {

    private final float growthFactor; // In percentage 50% = .5f
    private final float shrinkFactor; // In percentage 25% = .25f
    private final int initialSize;
    private final List<AutoCompactList<T>> slots = new ArrayList<>();

    public CombineCompactList(float growthFactor, float shrinkFactor, int initialSize) {
        this.growthFactor = growthFactor;
        this.shrinkFactor = shrinkFactor;
        this.initialSize = initialSize;
        appendNewSlot();
    }


    public void add(T element) {
        AutoCompactList<T> slot = slots.get(lastSlotIndex());
        if (slot.remainingCapacity() > 0) {
            slot.add(element);
        } else {
            appendNewSlot();
            add(element);
        }
    }

    private void appendNewSlot() {
        slots.add(new AutoCompactList<>(growthFactor, shrinkFactor, initialSize));
    }

    private int lastSlotIndex() {
        return slots.size() - 1;
    }

    public T remove(long index) {
        long startRange = 0;
        for (AutoCompactList<T> slot : slots) {

            long endRange = startRange + slot.size();
            if (between(index, startRange, endRange)) {

                T item = slot.remove((int) (index - startRange));

                if (slot.size() == 0) {
                    slots.remove(slot);
                }
                return item;
            }

            startRange = endRange;
        }
        return null;
    }

    private boolean between(long index, long startIndex, long endIndex) {
        return index >= startIndex && index < endIndex;
    }

    public long size() {
        return slots.stream().map(slot -> (long) slot.size()).reduce(0L, (acc, v) -> acc + v);
    }

    public long capacity() {
        return slots.stream().map(slot -> (long) slot.capacity()).reduce(0L, (acc, v) -> acc + v);
    }
}
