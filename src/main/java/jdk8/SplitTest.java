package jdk8;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SplitTest {

    private static final int MILLION = 100000 * 10;
    private static final int COUNT = MILLION * 10;

    private static Integer[] values;
    private static List<Integer> alvalues;
    private static Set<Integer> setValues;
    private static List<Integer> linkedListValues;

    public static void main(String... args) {

        System.out.println("Array " + sumValues(() -> arrayStream()) + " sec");
        System.out.println("ArrayList " + sumValues(() -> arrayListStream()) + " sec");
        System.out.println("set " + sumValues(() -> setStream()) + " sec");
        System.out.println("linkedlist " + sumValues(() -> linkedListStream()) + " sec");
    }

    public static long sumValues(Supplier<Stream<Integer>> numberSupplier) {
        int result = 0;
        long fastest = Integer.MAX_VALUE;
        for (int cnt = 0; cnt < 10; cnt++) {
            long start = System.nanoTime();

            result = numberSupplier.get().parallel().reduce(Integer::sum).get();

            long total = System.nanoTime() - start;
            fastest = Math.min(fastest, total / 1_000_000);

        }

        return fastest;
    }

    public static Stream<Integer> arrayStream() {
        if (values == null) {
            values = new Integer[COUNT];
            for (int c = 0; c < COUNT; c++) {
                values[c] = c;
            }
        }
        return Stream.of(values);
    }

    public static Stream<Integer> arrayListStream() {
        if (alvalues == null) {
            alvalues = new ArrayList<>();
            for (int c = 0; c < COUNT; c++) {
                alvalues.add(c);
            }
        }
        return alvalues.stream();
    }

    public static Stream<Integer> setStream() {
        if (setValues == null) {
            setValues = new HashSet<>();
            for (int c = 0; c < COUNT; c++) {
                setValues.add(c);
            }
        }
        return setValues.stream();
    }

    public static Stream<Integer> linkedListStream() {
        if (linkedListValues == null) {
            linkedListValues = new LinkedList<>();
            for (int c = 0; c < COUNT; c++) {
                linkedListValues.add(c);
            }
        }
        return linkedListValues.stream();
    }
}
