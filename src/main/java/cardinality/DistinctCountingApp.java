package cardinality;

import com.clearspring.analytics.stream.cardinality.HyperLogLog;
import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import com.clearspring.analytics.stream.cardinality.LinearCounting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class DistinctCountingApp {

    public static void main(String...args) throws Exception {

        int distinctElementCount = 11983;
        count(makeSetBased(distinctElementCount) ,"SetBased", distinctElementCount);
        count(makeLCBased(distinctElementCount), "LinearCounting", distinctElementCount);
        //count(makeHyperLogPlusBased(distinctElementCount) ,"HyperLogLogPlus",distinctElementCount);
        count(makeHyperLogBased(distinctElementCount) ,"HyperLog",distinctElementCount);

    }

    private static void count(Supplier<DistinctCount<String>> countSupplier, String type, int distinctElementCount) throws Exception {
        forceGC();
        long before = freeMemory();

        DistinctCount<String> c = countSupplier.get();

        addElements(c);

        forceGC();
        long after = freeMemory();


        float error = (Math.abs(distinctElementCount-c.distinct())/(distinctElementCount*1f)) * 100;
        System.out.println(String.format("Memory Used By %s is  %s KB and distinct count is %s, error %s percentage" ,
                type, ((after - before) / 1024f) , c.distinct() , error));
    }

    private static void addElements(DistinctCount<String> c) throws IOException {
        BufferedReader r = new BufferedReader( new InputStreamReader(DistinctCountingApp.class.getResourceAsStream("/HarryPotterseries.txt")));
        String line;
        while ( ( line = r.readLine()) != null) {
            String v [] = line.split(" ");
           for(String p : v) {
               c.add(p);
           }
        }
    }

    private static Supplier<DistinctCount<String>> makeLCBased(int distinctElementCount) {
        return () -> new DistinctCount<String>() {
            LinearCounting counting = LinearCounting.Builder.onePercentError(distinctElementCount).build();
            @Override
            public void add(String value) {
                counting.offer(value);
            }

            @Override
            public long distinct() {
                return counting.cardinality();
            }
        };
    }

    private static Supplier<DistinctCount<String>> makeHyperLogPlusBased(int distinctElementCount) {
        return () -> new DistinctCount<String>() {

            HyperLogLogPlus counting = new HyperLogLogPlus(6);
            @Override
            public void add(String value) {
                counting.offer(value);
            }

            @Override
            public long distinct() {
                return counting.cardinality();
            }
        };
    }


    private static Supplier<DistinctCount<String>> makeHyperLogBased(int distinctElementCount) {
        return () -> new DistinctCount<String>() {
            HyperLogLog counting = new HyperLogLog(6);
            @Override
            public void add(String value) {
                counting.offer(value);
            }

            @Override
            public long distinct() {
                return counting.cardinality();
            }
        };
    }

    private static Supplier<DistinctCount<String>> makeSetBased(int distinctElementCount) {
        return () -> new DistinctCount<String>() {
            Set<String> counting = new HashSet<>(distinctElementCount);
            @Override
            public void add(String value) {
                counting.add(value);
            }

            @Override
            public long distinct() {
                return counting.size();
            }
        };
    }

    private static void forceGC() {
        for(int x=0;x<10;x++)
            System.gc();
    }


    public static long freeMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }



    interface DistinctCount<T> {
        void add(T value);
        long distinct();
    }

}
