package membership;


import java.util.HashSet;
import java.util.Random;
import java.util.stream.IntStream;

public class BloomFilterTest {

    public static void main(String... args) {

        BloomFilter<Integer> users = new BloomFilter<>(2, 1);
        HashSet<Integer> actual = new HashSet<>();

        Random rnd = new Random();
        IntStream.range(0, 100000).forEach(i -> {
            int id = rnd.nextInt(1000);
            users.add(id);
            actual.add(id);
        });

        System.out.println("Bloom Filter element count " + users.elements());
        System.out.println("Distinct Element " + actual.size());

        int failCount = 0;
        for (int i : actual) {
            if (!users.contains(i)) {
                failCount++;
            }
        }
        System.out.println("Fail Count " + failCount);

    }
}
