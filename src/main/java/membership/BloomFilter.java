package membership;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

public class BloomFilter<T> {

    private final int slotSize;
    private final int noOfHashFunction;
    private final List<BitSet> hashBitSet;
    private final int[] hashSeed;
    private int elementCount;

    public BloomFilter(int noOfHashFunction, int inKB) {

        this.hashBitSet = new ArrayList<>();
        this.hashSeed = new int[noOfHashFunction];
        this.slotSize = inKB * 1024;
        this.noOfHashFunction = noOfHashFunction;
        init(noOfHashFunction, slotSize);

    }

    private void init(int noOfHashFunction, int slotSize) {
        Random rnd = new Random();
        for (int index = 0; index < noOfHashFunction; index++) {
            hashBitSet.add(new BitSet(slotSize));
            hashSeed[index] = rnd.nextInt(Integer.MAX_VALUE);
        }
    }


    public boolean add(T value) {
        if (contains(value)) return false;
        int hashCode = _hash(value);

        for (int index = 0; index < noOfHashFunction; index++) {
            int valueIndex = hashValueIndex(index, hashCode);
            hashBitSet.get(index).set(valueIndex);
        }
        elementCount++;
        return true;
    }

    private int _hash(T value) {
        return value.hashCode();
    }

    public boolean remove(T value) {
        if (contains(value)) return false;
        int hashCode = _hash(value);

        for (int index = 0; index < noOfHashFunction; index++) {
            int valueIndex = hashValueIndex(index, hashCode);
            hashBitSet.get(index).clear(valueIndex);
        }
        elementCount--;
        return true;
    }

    public int elements() {

        return elementCount;
    }

    public boolean contains(T value) {
        int hashCode = _hash(value);
        for (int index = 0; index < noOfHashFunction; index++) {
            int valueIndex = hashValueIndex(index, hashCode);
            if (!hashBitSet.get(index).get(valueIndex)) {
                return false;
            }
        }
        return true;
    }


    private int hashValueIndex(int index, int hashCode) {
        return Math.abs(hashSeed[index] * hashCode) % slotSize;
    }


}
